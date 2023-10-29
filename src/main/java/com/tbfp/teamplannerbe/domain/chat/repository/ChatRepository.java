package com.tbfp.teamplannerbe.domain.chat.repository;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.tbfp.teamplannerbe.domain.chat.dto.response.ChatRoomResponseDto;
import com.tbfp.teamplannerbe.domain.chat.entity.ChatMessage;

import java.util.List;
import java.util.Map;

public interface ChatRepository {
    String saveChatMessage(ChatMessage chatMessage);
    List<ChatMessage> readRoomWithChatMessageList(Long roomId);

    ChatMessage findAllChatMessageListByChatId(String chatId);

    void saveAllChatMessage(List<ChatMessage> chatMessage);

    ChatMessage saveChatMessageForReadCount(ChatMessage chatMessage);

    List<ChatMessage> findAllChatListByRoomId(Integer roomId);


}

