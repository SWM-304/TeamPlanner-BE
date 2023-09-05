package com.tbfp.teamplannerbe.domain.chat.service;

import com.tbfp.teamplannerbe.domain.chat.dto.request.ChatRoomRequestDto.ChattingReqeust;

public interface ChattingService {
    void sendMessage(Long chattingRoomId, ChattingReqeust chattingRequest);
}
