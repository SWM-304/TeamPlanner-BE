package com.tbfp.teamplannerbe.domain.notification.service;

import com.tbfp.teamplannerbe.domain.notification.dto.request.CreateMessageEvent;
import com.tbfp.teamplannerbe.domain.notification.dto.response.NotificationListResponseDto;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

public interface NotificationService {

    SseEmitter subscribe(Long userId) throws IOException;

    void send(final CreateMessageEvent createMessageEvent);

    List<NotificationListResponseDto> getNotificationList(String username);

}
