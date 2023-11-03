package com.tbfp.teamplannerbe.domain.chat.repository.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.tbfp.teamplannerbe.domain.chat.dto.response.ChattingResponse;
import com.tbfp.teamplannerbe.domain.chat.entity.ChatMessage;
import com.tbfp.teamplannerbe.domain.chat.repository.ChatRepository;
import com.tbfp.teamplannerbe.domain.chat.repository.DynamoChatRepository;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Primary
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository {


    private final DynamoChatRepository dynamoChatRepository;
    private final DynamoDBMapper dynamoDBMapper;


    @Override
    public ChatMessage saveChatMessageForReadCount(ChatMessage chatMessage) {
        return dynamoChatRepository.save(chatMessage);
    }

    @Override
    public List<ChatMessage> findAllChatListByRoomId(Integer roomId) {
        List<ChatMessage> result = dynamoChatRepository.findAllByRoomId(Long.valueOf(roomId));
        return result;
    }

    @Override
    public String saveChatMessage(ChatMessage chatMessage) {
        return dynamoChatRepository.save(chatMessage).getId();
    }

    @Override
    public List<ChatMessage> readRoomWithChatMessageList(Long roomId) {
        List<ChatMessage> result = dynamoChatRepository.findAllByRoomId(roomId);
        return result;
    }
//    public List<ChatMessage> readRoomWithChatMessageList(Long roomId, Map<String, AttributeValue> exclusiveStartKey, int limit) {
//        Map<String, AttributeValue> eav = new HashMap<>();
//        eav.put(":roomId", new AttributeValue().withN(roomId.toString()));
//
//        DynamoDBQueryExpression<ChatMessage> queryExpression = new DynamoDBQueryExpression<ChatMessage>()
//                .withKeyConditionExpression("roomId = :roomId")
//                .withExpressionAttributeValues(eav)
//                .withLimit(limit);
//
//        if (exclusiveStartKey != null) {
//            queryExpression.setExclusiveStartKey(exclusiveStartKey);
//        }
//        return dynamoDBMapper.query(ChatMessage.class, queryExpression);
//    }

    @Override
    public ChatMessage findAllChatMessageListByChatId(String chatId) {
        ChatMessage chatMessage = dynamoChatRepository.findById(chatId).orElseThrow(() -> new ApplicationException(ApplicationErrorType.CHAT_NOT_FOUND));
        return chatMessage;
    }


    @Override
    public void saveAllChatMessage(List<ChatMessage> chatMessage) {
        dynamoChatRepository.saveAll(chatMessage);
    }
}
