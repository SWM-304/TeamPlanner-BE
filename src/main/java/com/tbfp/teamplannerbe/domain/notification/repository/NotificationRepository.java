package com.tbfp.teamplannerbe.domain.notification.repository;

import com.tbfp.teamplannerbe.domain.notification.domain.Notification;
import com.tbfp.teamplannerbe.domain.notification.dto.request.CreateMessageEvent;

import java.util.List;

public interface NotificationRepository {
    void save(Notification notification);
    List<Notification> findAllByMemberId(Long memberId);
}
