package com.tbfp.teamplannerbe.domain.board.controller;


import com.tbfp.teamplannerbe.crawler.ClubCrawler;
import com.tbfp.teamplannerbe.crawler.ContestCrawler;
import com.tbfp.teamplannerbe.crawler.Outside_ActivityCrawler;

import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardSearchCondition;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
@Slf4j
@Tag(name= "BoardContoller", description = "대외활동 공모전 API")
public class BoardController {

    private final BoardService boardService;

    private final Outside_ActivityCrawler outsideActivityCrawler;
    private final ContestCrawler contestCrawler;

    /**
     * 임시로 healthcheck 를 통해서 크롤링한데이터를 읽어오고 db에 넣는작업
     */

    @GetMapping("/healthcheck")
    @Operation(summary = "크롤링데이터 삽입 API", description = "스케줄러 대신 임시로 수동으로 크롤링데이터 DB삽입")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public void healthCheck(){
        contestCrawler.runCrawler_with_Contest();
    }

    /**
     *
     * 공모전 및 대외활동에 대한 상세조회
     * @boardId 1,2,3,4
     */

    @GetMapping("/{boardId}")
    @Operation(summary = "공고 세부페이지 API", description = "boardId를 통해 공고에 대한 세부사항을 확인할 수 있다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity<?> boardDetail(@PathVariable Long boardId){

        return ResponseEntity.status(HttpStatus.OK).body(boardService.getBoardDetail(boardId));
    }


    /**
     *
     *
     * 동적쿼리를 이용하여 pagination 및 search 기능 구현
     * 예) localhost:8080/api/v1/board/list
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "공고리스트 출력 및 페이징 and 동적검색기능", description = "localhost:8080/api/v1/board/list2?category=공모전&page=0&size=2&sortBy=boardId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public Page<BoardResponseDto.BoardSimpleListResponseDto> boardList(BoardSearchCondition boardSearchCondition,
                                 Pageable pageable){

        return ResponseEntity.status(HttpStatus.OK).body(boardService.searchPageSimple(boardSearchCondition,pageable)).getBody();
    }

}
