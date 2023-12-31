package com.tbfp.teamplannerbe.domain.boardLike.controller;


import com.tbfp.teamplannerbe.domain.boardLike.service.BoardLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/board/{boardId}/boardLike")
@RequiredArgsConstructor
@Slf4j
@Tag(name= "BoardLikeContoller", description = "대외활동 공모전 좋아요 API")
public class BoardLikeController {

    private final BoardLikeService boardLikeService;


    @GetMapping("")
    @Operation(summary = "공고 좋아요 API", description = "공고아이디와 멤버아이디로 좋아요 추가")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity<?> addLikeToBoard(@PathVariable Long boardId, Principal principal){

        boardLikeService.createLikesOnBoard(boardId,principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body("좋아요 성공");
    }
    @Operation(summary = "공고 좋아요 삭제 API", description = "좋아요 아이디와 멤버아이디로 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    @DeleteMapping("/{boardLikeId}")
    public ResponseEntity<?> deleteLikeToBoard(@PathVariable Long boardLikeId,Principal principal){
        boardLikeService.deleteLikesOnBoard(boardLikeId,principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body("좋아요 삭제 성공");
    }
    @Operation(summary = "공고 좋아요 상태확인", description = "토큰 Principal로 좋아요 상태확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    @GetMapping("/state")
    public ResponseEntity<?> getLikeState(@PathVariable Long boardId,Principal principal){

        return ResponseEntity.status(HttpStatus.OK).body(
                boardLikeService.getLikeState(boardId,principal.getName())
        );
    }
}
