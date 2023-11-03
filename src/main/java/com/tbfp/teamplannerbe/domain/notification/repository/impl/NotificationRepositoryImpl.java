package com.tbfp.teamplannerbe.domain.notification.repository.impl;

import com.tbfp.teamplannerbe.domain.notification.domain.Notification;
import com.tbfp.teamplannerbe.domain.notification.dto.response.NotificationListResponseDto;
import com.tbfp.teamplannerbe.domain.notification.repository.DynamoNotificationRepository;
import com.tbfp.teamplannerbe.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;


@Repository
@Primary
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {
    private final DynamoNotificationRepository dynamoNotificationRepository;

    @Override
    public void save(Notification notification) {
        dynamoNotificationRepository.save(notification);
    }

    @Override
    public void saveAll(List<Notification> notificationList) {
        dynamoNotificationRepository.saveAll(notificationList);
    }

    @Override
    public List<Notification> findAllByMemberId(Long memberId) {

        List<Notification> notificationList = dynamoNotificationRepository.findAllByMemberId(memberId);
        return notificationList;
    }
}