package com.tbfp.teamplannerbe.domain.recruitmentLike.controller;

import com.tbfp.teamplannerbe.domain.recruitmentLike.service.RecruitmentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/recruitment/{recruitmentId}/like")
@RequiredArgsConstructor
public class RecruitmentLikeController {
    private final RecruitmentLikeService recruitmentLikeService;



    @PostMapping("")
    public ResponseEntity recruitmentLike(@PathVariable Long recruitmentId, Principal principal) {
        return ResponseEntity.ok(
                recruitmentLikeService.like(recruitmentId, principal.getName())
        );
    }
    @DeleteMapping("")
    public ResponseEntity deleteRecruitmentLike(@PathVariable Long recruitmentId,Principal principal){

        recruitmentLikeService.cancel(recruitmentId,principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body("좋아요 삭제 성공");
    }

}
