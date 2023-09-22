package com.tbfp.teamplannerbe.domain.chat.service;

public interface RedisChatRoomService {

    // for redis chatroom
    void connectChatRoom(Integer chatRoomNo, String username);

    void disconnectChatRoom(Integer chatRoomNo, String username);

    boolean isAllConnected(Integer chatRoomNo);

    boolean isConnected(Integer chatRoomNo);

    void updateCountAllZero(Integer chatNo, String username);

    void updateMessage(String username, Integer chatRoomNo);

}
