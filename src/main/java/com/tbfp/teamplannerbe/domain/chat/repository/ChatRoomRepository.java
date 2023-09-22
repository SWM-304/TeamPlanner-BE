package com.tbfp.teamplannerbe.domain.chat.repository;

import com.tbfp.teamplannerbe.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // member nickname 으로 챗룸리스트 가져오고싶은건디
//    List<ChatRoom> findByChatRoomMemberList
}
