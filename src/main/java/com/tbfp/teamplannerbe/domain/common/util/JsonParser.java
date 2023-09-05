package com.tbfp.teamplannerbe.domain.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tbfp.teamplannerbe.domain.chat.dto.request.ChatRoomRequestDto.ChattingReqeust;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JsonParser {

    private final ObjectMapper objectMapper;

    public String toJson(ChattingReqeust chattingRequest) {
        try {
            return objectMapper.writeValueAsString(chattingRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public ChattingReqeust toChattingRequest(String chatting) {
        try {
            return objectMapper.readValue(chatting, ChattingReqeust.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}