package com.tbfp.teamplannerbe.domain.chat.repository;

import com.tbfp.teamplannerbe.domain.chat.dto.redis.RedisChatRoom;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RedisChatRoomRepository extends CrudRepository<RedisChatRoom, String> {

    List<RedisChatRoom> findByChatroomNo(Integer chatRoomNo);

    Optional<RedisChatRoom> findByChatroomNoAndEmail(Integer chatRoomNo, String username);
}