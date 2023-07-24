package com.tbfp.teamplannerbe.domain.recruitmentLike.controller;

import com.tbfp.teamplannerbe.domain.recruitmentLike.service.RecruitmentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
