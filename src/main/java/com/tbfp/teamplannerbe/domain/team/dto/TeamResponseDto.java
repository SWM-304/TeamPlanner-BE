package com.tbfp.teamplannerbe.domain.team.dto;

import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto;
import com.tbfp.teamplannerbe.domain.team.entity.Team;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
        List<Team> teams;
    }
}
