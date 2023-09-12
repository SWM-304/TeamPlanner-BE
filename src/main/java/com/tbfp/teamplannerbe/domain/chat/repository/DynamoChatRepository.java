package com.tbfp.teamplannerbe.domain.chat.repository;

import com.tbfp.teamplannerbe.domain.chat.dto.response.ChattingResponse;
import com.tbfp.teamplannerbe.domain.chat.entity.ChatMessage;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface DynamoChatRepository extends CrudRepository<ChatMessage, String> {
    List<ChatMessage> findAllByRoomId(Long roomId);

    List<ChatMessage> findAll();
}
