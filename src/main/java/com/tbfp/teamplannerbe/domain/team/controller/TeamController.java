package com.tbfp.teamplannerbe.domain.team.controller;

import com.tbfp.teamplannerbe.domain.team.dto.CommonResponseDto;
import com.tbfp.teamplannerbe.domain.team.dto.TeamReqeustDto.CreatTeamRequestDto;
import com.tbfp.teamplannerbe.domain.team.dto.TeamResponseDto;
import com.tbfp.teamplannerbe.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/my-team")
    public ResponseEntity<TeamResponseDto.getMyTeamResponseDto> getMyTeam(Principal principal){
        return ResponseEntity.ok(teamService.getMyTeam(principal.getName()));
    }
}
