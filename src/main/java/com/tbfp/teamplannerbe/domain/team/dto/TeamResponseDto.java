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
    public static class GetMyTeamResponseDto {
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
        private List<MemberInfoDto> memberInfos = new ArrayList<>();


        public static GetMyTeamResponseDto toDto(Team team) {
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
                    .memberInfos(getMemberInfoFromMemberTeam(team))
                    .build();
        }

        public static List<MemberInfoDto> getMemberInfoFromMemberTeam(Team team){
//            List<MemberTeam> memberTeams = team.getMemberTeams();
//            System.out.println(memberTeams.size());
//            for(int i=0;i<memberTeams.size();i++){
//                System.out.println(memberTeams.get(0).getMember().getId());
//            }
            List<Member> members = team.getMemberTeams()
                    .stream().map(MemberTeam::getMember).collect(Collectors.toList());
            return members.stream().map(MemberInfoDto::toDto).collect(Collectors.toList());
        }

        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor
        @Builder
        public static class MemberInfoDto{
            private Long memberId;
            private String nickname;
            private String profileImage;

            public static MemberInfoDto toDto(Member member){
                return MemberInfoDto.builder()
                        .memberId(member.getId())
                        .nickname(member.getNickname())
                        .profileImage(member.getBasicProfile().getProfileImage())
                        .build();
            }
        }

    }
}
