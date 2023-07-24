package com.tbfp.teamplannerbe.domain.recruitmentComment.controller;

import com.tbfp.teamplannerbe.domain.recruitmentComment.service.RecruitmentCommentService;
import com.tbfp.teamplannerbe.domain.recruitmentComment.dto.RecruitmentCommentRequestDto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruitment/{recruitmentId}/comment")
public class RecruitmentCommentController {

    private final RecruitmentCommentService recruitmentCommentService;
    // c
    @PostMapping("")
    public ResponseEntity createComment(Principal principal, @RequestBody CreateRequestDto createRequestDto) {
        return ResponseEntity.ok(
                recruitmentCommentService.createComment(principal.getName(), createRequestDto)
        );
    }


    // r
//    @GetMapping("/{commentId}")
//    public ResponseEntity readComment(Principal principal, @PathVariable )
    // u
    @PutMapping("/{recruitmentCommentId}")
    public ResponseEntity updateComment(Principal principal, @PathVariable Long recruitmentCommentId, @RequestBody UpdateRequestDto updateRequestDto) {
        return ResponseEntity.ok(
                recruitmentCommentService.updateComment(principal.getName(), recruitmentCommentId, updateRequestDto)
        );
    }
//
    // d
    @DeleteMapping("/{recruitmentCommentId}")
    public ResponseEntity deleteComment(Principal principal, @PathVariable Long recruitmentCommentId) {
        return ResponseEntity.ok(
                recruitmentCommentService.deleteComment(principal.getName(), recruitmentCommentId)
        );
    }

    // 대댓글 달기
    @PostMapping("/{parentRecruitmentCommentId}/comment")
    public ResponseEntity createCommentToComment(Principal principal, @PathVariable Long parentRecruitmentCommentId , @RequestBody CreateCoCommentRequestDto createCoCommentRequestDto) {
        return ResponseEntity.ok(
                recruitmentCommentService.createCommentToComment(principal.getName(), parentRecruitmentCommentId, createCoCommentRequestDto)
        );
    }
}
