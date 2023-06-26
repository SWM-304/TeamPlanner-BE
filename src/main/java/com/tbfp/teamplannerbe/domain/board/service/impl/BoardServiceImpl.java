package com.tbfp.teamplannerbe.domain.board.service.impl;

import com.google.gson.Gson;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.board.repository.BoardRepository;
import com.tbfp.teamplannerbe.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;


    /**
     *
     * @param board
     * 크롤링 데이터 없으면 save 있으면 overWrite
     * @return findBoard.getId()
     */
    @Transactional
    public Long upsert(Board board) {
        // Check if the board already exists
        Board findBoard = Optional.ofNullable(boardRepository.findByactivitykey(board.getActivity_key())).orElse(new Board());
        findBoard.overwrite(board);
        if (findBoard.getId() == null) {
            log.info("\tinsert board");
            boardRepository.save(findBoard);
        } else {
            log.info("\toverWrite board");

        }
        return findBoard.getId();
    }


}




