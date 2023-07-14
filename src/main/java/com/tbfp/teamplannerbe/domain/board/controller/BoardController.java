package com.tbfp.teamplannerbe.domain.board.controller;


import com.tbfp.teamplannerbe.domain.board.dto.BoardRequestDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardRequestDto.createBoardResquestDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardRequestDto.updateBoardReqeustDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto.BoardDetailResponseDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto.BoardSimpleListResponseDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardSearchCondition;
import com.tbfp.teamplannerbe.domain.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
@Slf4j
@Tag(name= "BoardContoller", description = "대외활동 공모전 API")
public class BoardController {

    private final BoardService boardService;

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

    @PostMapping("")
    @Operation(summary = "공고 생성 API", description = "사용자가 직접 공고생성을 할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity<?> createBaord(@RequestBody createBoardResquestDto createBoardResquestDto,Principal principal){


        return ResponseEntity.status(HttpStatus.OK).body(boardService.createBoard(createBoardResquestDto,principal.getName()));

    }
    /**
     *
     *
     * 동적쿼리를 이용하여 pagination 및 search 기능 구현
     * 예) localhost:8080/api/v1/board/list
     * @return
     */
    @GetMapping("")
    @Operation(summary = "공고리스트 출력 및 페이징 and 동적검색기능", description = "localhost:8080/api/v1/board/list2?category=공모전&page=0&size=2&sortBy=boardId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity<?> boardList(BoardSearchCondition boardSearchCondition,
                                 Pageable pageable){

        return ResponseEntity.status(HttpStatus.OK).body(boardService.searchPageSimple(boardSearchCondition,pageable).map(BoardSimpleListResponseDto::toDTO));
    }

    @PutMapping("/{boardId}")
    @Operation(summary = "공고수정", description = "공고 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity<?> updateBoard(@PathVariable Long boardId,@RequestBody updateBoardReqeustDto updateDto,Principal principal){

        boardService.updateBoard(boardId,updateDto,principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body("수정이 완료 되었습니다");
    }


    @DeleteMapping("/{boardId}")
    @Operation(summary = "공고삭제", description = "UserId 와 boardId로 해당하는 공고글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId,Principal principal){

        boardService.deleteBoard(boardId,principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body("삭제가 완료 되었습니다");
    }



}




