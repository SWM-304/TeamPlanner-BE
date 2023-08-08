package com.tbfp.teamplannerbe.domain.team.controller;

import com.tbfp.teamplannerbe.domain.profile.dto.ProfileRequestDto;
import com.tbfp.teamplannerbe.domain.profile.dto.ProfileResponseDto;
import com.tbfp.teamplannerbe.domain.profile.service.ProfileService;
import com.tbfp.teamplannerbe.domain.team.dto.CommonResponseDto;
import com.tbfp.teamplannerbe.domain.team.dto.TeamRequestDto;
import com.tbfp.teamplannerbe.domain.team.dto.TeamRequestDto.CreatTeamRequestDto;
import com.tbfp.teamplannerbe.domain.team.dto.TeamResponseDto;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/team")
public class TeamController {

    private final TeamService teamService;
    private final ProfileService profileService;

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
    public ResponseEntity<ProfileResponseDto.CreateEvaluationResponseDto> createEvaluation(@Valid @RequestBody ProfileRequestDto.CreateEvaluationRequestDto createEvaluationRequestDto, @PathVariable Long teamId, @PathVariable Long subjectMemberId, Principal principal){
        return ResponseEntity.ok(profileService.createEvaluation(createEvaluationRequestDto,teamId,subjectMemberId,principal.getName()));
    }

    @PutMapping("/evaluation/{teamId}/{subjectMemberId}")
    @Operation(summary = "평가 수정", description = "활동이 끝난 팀의 멤버 평가를 수정한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity<ProfileResponseDto.UpdateEvaluationResponseDto> updateEvaluation(@Valid @RequestBody ProfileRequestDto.UpdateEvaluationRequestDto updateEvaluationRequestDto,@PathVariable Long teamId, @PathVariable Long subjectMemberId, Principal principal){
        return ResponseEntity.ok(profileService.updateEvaluation(updateEvaluationRequestDto,teamId,subjectMemberId,principal.getName()));
    }

    @DeleteMapping("/evaluation/{teamId}/{subjectMemberId}")
    @Operation(summary = "평가 수정", description = "활동이 끝난 팀의 멤버 평가를 수정한다")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity<?> deleteEvaluation(ProfileRequestDto.DeleteEvaluationRequestDto deleteEvaluationRequestDto, @PathVariable Long teamId, @PathVariable Long subjectMemberId, Principal principal){
        profileService.deleteEvaluation(deleteEvaluationRequestDto,teamId,subjectMemberId,principal.getName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/evaluation")
    @Operation(summary = "내가 한 평가", description = "사용자가 준 모든 평가들을 가져온다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity<List<ProfileResponseDto.EvaluationResponseDto>> getAllEvaluations(Principal principal) {
        return ResponseEntity.ok(profileService.getAllEvaluations(principal.getName()));
    }

    @GetMapping("/my-team")
    public ResponseEntity<List<TeamResponseDto.GetMyTeamResponseDto>> getMyTeam(Principal principal){
        return ResponseEntity.ok(teamService.getMyTeam(principal.getName()));
    }
}
