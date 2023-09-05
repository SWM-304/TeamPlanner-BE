package com.tbfp.teamplannerbe.domain.chat.repository;


import com.tbfp.teamplannerbe.domain.chat.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
}
