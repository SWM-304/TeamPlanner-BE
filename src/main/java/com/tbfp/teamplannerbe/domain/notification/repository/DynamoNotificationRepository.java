package com.tbfp.teamplannerbe.domain.notification.repository;

import com.tbfp.teamplannerbe.domain.notification.domain.Notification;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface DynamoNotificationRepository extends CrudRepository<Notification,Long> {

    List<Notification> findAllByMemberId(Long memberId);
}


