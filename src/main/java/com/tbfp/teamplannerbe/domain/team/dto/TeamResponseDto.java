package com.tbfp.teamplannerbe.domain.team.dto;

import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.team.entity.MemberTeam;
import com.tbfp.teamplannerbe.domain.team.entity.Team;
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
    public static class getMyTeamResponseDto {
        private Long id;
        private String teamName;
        private Long teamSize;
        private Long teamCapacity;
        private String teamLeader;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Long recruitmentId;
        private Long boardId;
        @Builder.Default
        private List<Long> memberIds = new ArrayList<>();


        public static getMyTeamResponseDto toDto(Team team) {
            return builder()
                    .id(team.getId())
                    .teamName(team.getTeamName())
                    .teamSize(team.getTeamSize())
                    .teamCapacity(team.getTeamCapacity())
                    .teamLeader(team.getTeamLeader())
                    .startDate(team.getStartDate())
                    .endDate(team.getEndDate())
                    .recruitmentId(team.getRecruitment().getId())
                    .boardId(team.getRecruitment().getBoard().getId())
                    .memberIds(getMemberIdsFromMemberTeams(team))
                    .build();
        }

        public static List<Long> getMemberIdsFromMemberTeams(Team team){
            return team.getMemberTeams()
                    .stream().map(MemberTeam::getMember).collect(Collectors.toList())
                    .stream().map(Member::getId).collect(Collectors.toList());
        }
    }
}
