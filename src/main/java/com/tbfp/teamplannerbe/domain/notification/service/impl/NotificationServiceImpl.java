package com.tbfp.teamplannerbe.domain.notification.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tbfp.teamplannerbe.config.helper.UserHelper;
import com.tbfp.teamplannerbe.config.security.SecurityUtils;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import com.tbfp.teamplannerbe.domain.notification.domain.Notification;
import com.tbfp.teamplannerbe.domain.notification.dto.request.CreateMessageEvent;
import com.tbfp.teamplannerbe.domain.notification.dto.response.NotificationListResponseDto;
import com.tbfp.teamplannerbe.domain.notification.dto.response.NotificationResponseDto;
import com.tbfp.teamplannerbe.domain.notification.repository.DynamoNotificationRepository;
import com.tbfp.teamplannerbe.domain.notification.repository.NotificationRepository;
import com.tbfp.teamplannerbe.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    // 기본 타임아웃 설정
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final RedisOperations<String, NotificationResponseDto> eventRedisOperations;
    private static final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper;
    private final NotificationRepository notificationRepository;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final MemberRepository memberRepository;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    //실질적으로 알림을 저장하고 redis 채널에 메시지를 publish하는 역할
    public void send(final CreateMessageEvent createMessageEvent) {

        Notification notification = CreateMessageEvent.toEntity(createMessageEvent);
        notificationRepository.save(notification);
        final String id = String.valueOf(notification.getMemberId());
        eventRedisOperations.convertAndSend(getChannelName(id), notification);
    }

    @Override
    @Transactional
    public List<NotificationListResponseDto> getNotificationList(String username) {

        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new ApplicationException(ApplicationErrorType.UNAUTHORIZED));

        List<Notification> notificationListEntity = notificationRepository.findAllByMemberId(member.getId());

        updateReadCount(notificationListEntity);
        notificationRepository.saveAll(notificationListEntity);
        List<NotificationListResponseDto> notificationListDto = notificationListEntity.stream().
                map(NotificationListResponseDto::from)
                .collect(Collectors.toList());
        return notificationListDto;
    }

    /**
     * 클라이언트가 구독을 위해 호출하는 메서드.
     *
     * @param memberId - 구독하는 클라이언트의 사용자 아이디.
     * @return SseEmitter - 서버에서 보낸 이벤트 Emitter
     */
    public SseEmitter subscribe(final Long memberId) throws IOException{
        final String id = String.valueOf(memberId);
        final SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        //초반 연결용 메시지!!
        emitter.send(SseEmitter.event()
                .id(id)
                .name("sse"));
        emitters.add(emitter);
        // MessageListener 익명함수 사용해서 onMessage 구현, Redis에서 새로운 알림이 발생하면 자동적으로 onMessage가 호출
        // 즉 알림을 serialize하고 해당 Client에게 알림을 전송한다.
        final MessageListener messageListener = (message, pattern) -> {
            final NotificationResponseDto notificationResponse = serialize(message);
            sendToClient(emitter, id, notificationResponse);
        };
        this.redisMessageListenerContainer.addMessageListener(messageListener, ChannelTopic.of(getChannelName(id)));
        checkEmitterStatus(emitter, messageListener);
        return emitter;
    }

    /**
     * 알림 읽음처리를 해주는 로직
     */

    private static void updateReadCount(List<Notification> notificationListEntity) {

        notificationListEntity.forEach(notification -> notification.updateNotificationReadCount(notification.getReadCount()));
    }




    private NotificationResponseDto serialize(final Message message) {
        try {
            Notification notification = this.objectMapper.readValue(message.getBody(), Notification.class);
            return NotificationResponseDto.from(notification);
        } catch (IOException e) {
            throw new ApplicationException(ApplicationErrorType.BAD_REQUEST);
        }
    }

    // 클라이언트에게 메시지를 전달하는 부분
    private void sendToClient(final SseEmitter emitter, final String id, final Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException e) {
            emitters.remove(emitter);
            log.error("SSE 연결이 올바르지 않습니다. 해당 memberID={}", id);
        }
    }

    private void checkEmitterStatus(final SseEmitter emitter, final MessageListener messageListener) {
        emitter.onCompletion(() -> {
            log.info("끊어버린다1");
            emitters.remove(emitter);
            this.redisMessageListenerContainer.removeMessageListener(messageListener);
        });
        emitter.onTimeout(() -> {
            log.info("끊어버린다2");
            emitters.remove(emitter);
            this.redisMessageListenerContainer.removeMessageListener(messageListener);
        });
    }

    private void removeEmitter(SseEmitter emitter) {
        emitters.remove(emitter);
    }
    private String getChannelName(final String memberId) {
        return "topics:" + memberId;
    }
}
