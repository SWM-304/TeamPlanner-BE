package com.tbfp.teamplannerbe.domain.chat.controller;

import com.tbfp.teamplannerbe.domain.chat.dto.request.ChatRoomRequestDto.ChattingReqeust;
import com.tbfp.teamplannerbe.domain.chat.service.ChattingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final ChattingService chattingService;
    @MessageMapping("/chattings/rooms/{chattingRoomId}")
    public void send(@DestinationVariable Long chattingRoomId, ChattingReqeust chattingRequest) {
        log.info("Message [{}] send by member: {} to chatting room: {}", chattingRequest.getContent(), chattingRequest.getSenderId(), chattingRoomId);
        chattingService.sendMessage(chattingRoomId, chattingRequest);
    }

//    public void send(@DestinationVariable Long chattingRoomId, ChattingRequest chattingRequest) {
//        chattingService.sendMessage(chattingRoomId, chattingRequest);
//        log.info("Message [{}] send by member: {} to chatting room: {}", chattingRequest.getContent(), chattingRequest.getSenderId(), chattingRoomId);
//    }
}
