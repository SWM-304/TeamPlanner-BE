package com.tbfp.teamplannerbe.domain.notification.dto.request;


import com.tbfp.teamplannerbe.domain.notification.domain.Notification;
import com.tbfp.teamplannerbe.domain.notification.dto.response.NotificationResponseDto;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Builder
public class CreateMessageEvent {
    private String content;
    private Long memberId;
    private String recruitmentProfileImage;
    private LocalDateTime createdDate;
    private Integer readCount;


    public static Notification toEntity(CreateMessageEvent createMessageEvent) {
        return Notification.builder()
                .memberId(createMessageEvent.getMemberId())
                .content(createMessageEvent.getContent())
                .createdAt(LocalDateTime.now())
                .recruitmentProfileImage(createMessageEvent.recruitmentProfileImage)
                .readCount(1)
                .build();
    }
}
