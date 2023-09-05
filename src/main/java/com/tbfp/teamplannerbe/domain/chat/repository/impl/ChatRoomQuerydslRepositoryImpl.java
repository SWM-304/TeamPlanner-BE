package com.tbfp.teamplannerbe.domain.chat.repository.impl;

import com.tbfp.teamplannerbe.domain.chat.entity.ChatRoom;
import com.tbfp.teamplannerbe.domain.chat.repository.ChatRoomQuerydslRepository;
import com.tbfp.teamplannerbe.domain.common.querydsl.support.Querydsl4RepositorySupport;

public class ChatRoomQuerydslRepositoryImpl extends Querydsl4RepositorySupport implements ChatRoomQuerydslRepository {

    public ChatRoomQuerydslRepositoryImpl() {
        super(ChatRoom.class);
    }
}
