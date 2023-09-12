package com.tbfp.teamplannerbe.domain.chat.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingResponse {

    private String chatId;
    private Long senderId;
    private String content;
    private String createdDate;
    private String createdTime;
    private Integer readCount;
}
