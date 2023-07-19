package com.tbfp.teamplannerbe.domain.recruitmentApply.controller;


import com.tbfp.teamplannerbe.domain.recruitmentApply.dto.RecruitmentApplyRequestDto.*;
import com.tbfp.teamplannerbe.domain.recruitmentApply.service.RecruitmentApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/recruitment/{recruitmentId}/apply")
@RequiredArgsConstructor
public class RecruitmentApplyController {
    private final RecruitmentApplyService recruitmentApplyService;

    // apply
    @PostMapping
    public ResponseEntity apply(Principal principal, @PathVariable Long recruitmentId, @RequestBody CreateApplyRequest createApplyRequest) {
        return ResponseEntity.ok(
                recruitmentApplyService.apply(principal.getName(), recruitmentId, createApplyRequest)
        );
    }

    // cancel apply
    @DeleteMapping
    public ResponseEntity cancelApply(Principal principal, @PathVariable Long recruitmentId) {
        recruitmentApplyService.cancelApply(principal.getName(), recruitmentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
}
