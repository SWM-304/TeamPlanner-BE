package com.tbfp.teamplannerbe.domain.chat.repository;

import com.tbfp.teamplannerbe.domain.chat.entity.ChatMessage;

import java.util.List;

public interface ChatRepository {
    String saveChatMessage(ChatMessage chatMessage);
    List<ChatMessage> readRoomWithChatMessageList(Long roomId);

    ChatMessage findAllChatMessageListByChatId(String chatId);

    void saveAllChatMessage(List<ChatMessage> chatMessage);

    ChatMessage saveChatMessageForReadCount(ChatMessage chatMessage);


}

