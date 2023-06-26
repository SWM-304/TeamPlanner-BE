package com.tbfp.teamplannerbe.domain.board.controller;


import com.tbfp.teamplannerbe.crawler.ClubCrawler;
import com.tbfp.teamplannerbe.crawler.ContestCrawler;
import com.tbfp.teamplannerbe.crawler.Outside_ActivityCrawler;

import com.tbfp.teamplannerbe.domain.board.service.BoardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
@Slf4j
@Tag(name= "BoardContoller", description = "대외활동 공모전 API")
public class BoardController {

    private final BoardService boardService;

    private final Outside_ActivityCrawler outsideActivityCrawler;
    @GetMapping("")
    public void test(){
        outsideActivityCrawler.runCrawler_with_outsideActivity();
    }


}
