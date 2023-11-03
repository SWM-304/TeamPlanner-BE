package com.tbfp.teamplannerbe.domain.chat.service;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.tbfp.teamplannerbe.domain.chat.dto.response.ChatRoomResponseDto;
import com.tbfp.teamplannerbe.domain.chat.dto.response.ChattingReadCountResponseDto;
import com.tbfp.teamplannerbe.domain.chat.dto.response.ChattingRoomCheckResponseDto;
import com.tbfp.teamplannerbe.domain.chat.dto.response.ChattingRoomDetailResponse;

import java.util.List;
import java.util.Map;

public interface ChatRoomService {
    List<ChatRoomResponseDto.ChatRoomListDto> getRoomList(String nickname);
//    ChattingRoomDetailResponse getMyRoom(String nickname, Long chattingRoomId);
    ChattingRoomDetailResponse getMyRoom(String nickname, Long chattingRoomId);

    Long createRoom(String nickname, String targetNickname);

    ChattingRoomCheckResponseDto chatRoomCheck(String nickname,String targetNickname);

    ChattingReadCountResponseDto readCountDecrease(String chatId);
}
