package com.tbfp.teamplannerbe.domain.notification.dto.response;

import com.tbfp.teamplannerbe.domain.notification.domain.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDto {

    private String id;
    private String content;
    private LocalDateTime createdAt;
    private String recruitmentProfileImage;

    public static NotificationResponseDto from(Notification notification) {

        return NotificationResponseDto.builder()
                .content(notification.getContent())
                .id(notification.getId())
                .createdAt(notification.getCreatedAt())
                .recruitmentProfileImage(notification.getRecruitmentProfileImage())
                .build();
    }
}