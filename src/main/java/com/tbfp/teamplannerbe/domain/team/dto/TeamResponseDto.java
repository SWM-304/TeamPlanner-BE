package com.tbfp.teamplannerbe.domain.team.dto;

import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.team.entity.MemberTeam;
import com.tbfp.teamplannerbe.domain.team.entity.Team;
import com.tbfp.teamplannerbe.domain.team.entity.TeamStateEnum;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TeamResponseDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class createdTeamResponseDto{
        private Long teamId;
        private String teamName;
        private String teamLeader;
        private Long currentSize;
        private Long capacity;
        private TeamStateEnum teamStateEnum;
        private LocalDateTime startDate;
        private LocalDateTime endDate;

        public static TeamResponseDto.createdTeamResponseDto toDto(Team team) {
            createdTeamResponseDto result = createdTeamResponseDto.builder()
                    .teamLeader(team.getTeamLeader())
                    .capacity(team.getTeamCapacity())
                    .teamName(team.getTeamName())
                    .currentSize(team.getTeamSize())
                    .startDate(team.getStartDate())
                    .endDate(team.getEndDate())
                    .teamId(team.getId())
                    .teamStateEnum(team.getTeamStateEnum())
                    .build();
            return result;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class deleteTeamMemberResponseDto {
        String deleteMessage;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class GetMyTeamResponseDto {
        private Long id;
        private String leaderProfileImage;
        private String activityName;
        private String teamName;
        private Long teamSize;
        private Long teamCapacity;
        private String teamLeader;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Long recruitmentId;
        private Long boardId;
        private TeamStateEnum teamStateEnum;
        @Builder.Default
        private List<MemberResponseDto.MemberInfoDto> memberInfos = new ArrayList<>();


        public static GetMyTeamResponseDto toDto(Team team) {
            return builder()
                    .id(team.getId())
                    .leaderProfileImage(team.getRecruitment().getAuthor().getBasicProfile().getProfileImage())
                    .teamName(team.getTeamName())
                    .teamSize(team.getTeamSize())
                    .teamCapacity(team.getTeamCapacity())
                    .teamLeader(team.getTeamLeader())
                    .startDate(team.getStartDate())
                    .endDate(team.getEndDate())
                    .recruitmentId(team.getRecruitment().getId())
                    .activityName(team.getRecruitment().getBoard().getActivityName())
                    .boardId(team.getRecruitment().getBoard().getId())
                    .memberInfos(getMemberInfoFromMemberTeam(team))
                    .teamStateEnum(team.getTeamStateEnum())
                    .build();
        }

        public static List<MemberResponseDto.MemberInfoDto> getMemberInfoFromMemberTeam(Team team){
            List<Member> members = team.getMemberTeams()
                    .stream().map(MemberTeam::getMember).collect(Collectors.toList());
            return members.stream().map(MemberResponseDto.MemberInfoDto::toDto).collect(Collectors.toList());
        }
    }
}
