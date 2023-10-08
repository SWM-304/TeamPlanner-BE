package com.tbfp.teamplannerbe.domain.notification.dto.response;

import com.tbfp.teamplannerbe.domain.notification.domain.Notification;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Builder
public class NotificationListResponseDto {
    private String content;
    private Long memberId;
    private String recruitmentProfileImage;
    private LocalDateTime createdDate;

    public static NotificationListResponseDto from(Notification notification) {
        return NotificationListResponseDto.builder()
                .content(notification.getContent())
                .recruitmentProfileImage(notification.getRecruitmentProfileImage())
                .createdDate(notification.getCreatedAt())
                .memberId(notification.getMemberId())
                .build();

    }
}
