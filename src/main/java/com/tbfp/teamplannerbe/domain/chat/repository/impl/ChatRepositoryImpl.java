package com.tbfp.teamplannerbe.domain.chat.repository.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.tbfp.teamplannerbe.domain.chat.dto.response.ChattingResponse;
import com.tbfp.teamplannerbe.domain.chat.entity.ChatMessage;
import com.tbfp.teamplannerbe.domain.chat.repository.ChatRepository;
import com.tbfp.teamplannerbe.domain.chat.repository.DynamoChatRepository;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@Primary
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository {


    private static final String dot = ".";
    private static final String TIME_DELIMITER = ":";


    private final DynamoChatRepository dynamoChatRepository;
//    private final DynamoDBMapper dynamoDBMapper;
    @Autowired
    private AmazonDynamoDB amazonDynamoDB; // Initialize your AmazonDynamoDB client



    @Override
    public ChatMessage saveChatMessageForReadCount(ChatMessage chatMessage) {
        return dynamoChatRepository.save(chatMessage);
    }

    @Override
    public List<ChattingResponse> findAllChatListByRoomId(Long roomId,Map<String, AttributeValue> exclusiveStartKey) {
        List<ChattingResponse> result;
        try {
            Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
            expressionAttributeValues.put(":x", new AttributeValue().withN(roomId.toString()));

            QueryRequest queryRequest = new QueryRequest()
                    .withTableName("chatting")
                    .withIndexName("roomId-createdAt-index")
                    .withKeyConditionExpression("roomId = :x")
                    .withExpressionAttributeValues(expressionAttributeValues)
                    .withScanIndexForward(false) // 내림차순 정렬
                    .withLimit(10);

            queryRequest.setExclusiveStartKey(exclusiveStartKey);
            QueryResult queryResult = amazonDynamoDB.query(queryRequest);

            // query dynomdb결과값을 entity값으로 변환
            List<ChatMessage> entityResult = queryResult.getItems().stream().map(
                    ChatMessage::new
            ).collect(Collectors.toList());
            Map<String, AttributeValue> lastEvaluatedKey = queryResult.getLastEvaluatedKey();

            result = entityResult.stream()
                    .sorted(Comparator.comparing(ChatMessage::getCreatedAt))
                    .map(message -> ChattingResponse.builder()
                            .senderId(message.getSenderId())
                            .content(message.getMessage())
                            .createdDate(toCreatedDate(message.getCreatedAt()))
                            .createdTime(toCreatedTime(message.getCreatedAt()))
                            .chatId(message.getId())
                            .readCount(message.getReadCount())
                            .lastEvaluatedKey(lastEvaluatedKey)
                            .build())
                    .collect(Collectors.toList());


        } catch (Exception e) {
            System.err.println("err: " + e);
            throw e;
        }
        return result;
    }

    @Override
    public String saveChatMessage(ChatMessage chatMessage) {
        return dynamoChatRepository.save(chatMessage).getId();
    }

    // 전체 챗 다 읽어와서 채팅읽음처리
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

    private String toCreatedTime(LocalDateTime createdAt) {
        return createdAt.getYear() + dot + createdAt.getMonthValue() + dot + createdAt.getDayOfMonth() + dot;
    }

    private String toCreatedDate(LocalDateTime createdAt) {
        return createdAt.getHour() + TIME_DELIMITER + createdAt.getMinute();
    }
}
