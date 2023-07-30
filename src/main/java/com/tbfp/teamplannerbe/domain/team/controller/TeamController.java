package com.tbfp.teamplannerbe.domain.team.controller;

import com.tbfp.teamplannerbe.domain.profile.dto.ProfileResponseDto;
import com.tbfp.teamplannerbe.domain.team.dto.CommonResponseDto;
import com.tbfp.teamplannerbe.domain.team.dto.TeamRequestDto;
import com.tbfp.teamplannerbe.domain.team.dto.TeamRequestDto.CreatTeamRequestDto;
import com.tbfp.teamplannerbe.domain.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/team")
public class TeamController {

    private final TeamService teamService;

    //team create
    @PostMapping("")
    public ResponseEntity<?> createTeam(Principal principal, @RequestBody CreatTeamRequestDto creatTeamRequestDto) {


        return ResponseEntity.ok().body(teamService.createTeam(principal.getName(),creatTeamRequestDto));
    }

    @DeleteMapping("/{teamId}/member/{memberId}")
    public ResponseEntity<?> deleteTeamMember(Principal principal,@PathVariable Long teamId,@PathVariable Long memberId){
        teamService.deleteTeamMember(principal.getName(),teamId,memberId);
        return new ResponseEntity<>(
                new CommonResponseDto<>(1,"팀 Member 삭제완료",null), HttpStatus.OK);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<?> deleteTeam(Principal principal,@PathVariable Long teamId){
        teamService.deleteTeam(principal.getName(),teamId);
        return new ResponseEntity<>(
                new CommonResponseDto<>(1,"팀 삭제완료",null), HttpStatus.OK);
    }

    @PostMapping("/evaluation/{teamId}/{subjectMemberId}")
    @Operation(summary = "평가 제출", description = "활동이 끝난 팀의 멤버를 평가한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity<ProfileResponseDto.SubmitEvaluationResponseDto> submitEvaluation(@Valid @RequestBody TeamRequestDto.SubmitEvaluationRequestDto submitEvaluationRequestDto,@PathVariable Long teamId, @PathVariable Long subjectMemberId, Principal principal){
        return ResponseEntity.ok(teamService.submitEvaluation(submitEvaluationRequestDto,teamId,subjectMemberId,principal.getName()));
    }
}
