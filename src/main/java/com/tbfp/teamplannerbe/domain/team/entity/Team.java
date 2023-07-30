package com.tbfp.teamplannerbe.domain.team.entity;

import com.tbfp.teamplannerbe.domain.common.base.BaseTimeEntity;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import com.tbfp.teamplannerbe.domain.team.dto.TeamRequestDto.CreatTeamRequestDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "team")
public class Team extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "team_size")
    private Long teamSize;

    @Column(name = "team_capacity")
    private Long teamCapacity;

    @Column(name = "team_leader")
    private String teamLeader;


    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "RECRUITMENT_ID")
    private Recruitment recruitment;

    @Builder.Default
    @OneToMany(mappedBy ="team", cascade = CascadeType.ALL)
    private List<MemberTeam> memberTeams=new ArrayList<>();


    public static Team initialCreateTeam (CreatTeamRequestDto creatTeamRequestDto,
                                  Recruitment recruitment,
                                  String username){
        // 초기 팀 생성
        Team team = Team.builder()
                .teamName(creatTeamRequestDto.getTeamName())
                .recruitment(recruitment)
                .teamSize(1L)
                .teamCapacity(creatTeamRequestDto.getMaxTeamSize())
                .startDate(LocalDateTime.parse(creatTeamRequestDto.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .endDate(LocalDateTime.parse(creatTeamRequestDto.getEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .teamLeader(username)
                .build();


        return team;
    }
    public void addTeamMembers(List<Member> members,String teamLeaderName) {
        // team 맥시멈에 가득찼는지 확인
        if (teamSize + members.size() > teamCapacity) {
            throw new ApplicationException(TEAM_CAPACITY_EXCEEDED);
        }

        // 체크리스트 한 members들을 순회하면서 check 및 memberTeam 에 저장
        members.forEach(member -> {
            // Check if the member is already part of the team
            if (memberTeams.stream().anyMatch(memberTeam -> memberTeam.getMember().equals(member))) {
                throw new ApplicationException(ALREADY_TEAM_ACCEPT);
            }
            MemberTeam memberTeam = MemberTeam.builder()
                    .member(member)
                    .team(this)
                    .role(MemberTeamStateEnum.MEMBER)
                    .build();
            memberTeams.add(memberTeam);
        });

        // team 테이블의 사이즈 업데이트
        teamSize += members.size();

    }



    public void decreaseTeamSize(){
        this.teamSize--;
    }
    public void updateTeam(CreatTeamRequestDto creatTeamRequestDto) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime formattedStartDate = LocalDateTime.parse(creatTeamRequestDto.getStartDate(), formatter);
        LocalDateTime formattedEndDate = LocalDateTime.parse(creatTeamRequestDto.getEndDate(), formatter);

        this.teamName=creatTeamRequestDto.getTeamName();
        this.teamCapacity=creatTeamRequestDto.getMaxTeamSize();
        this.startDate=formattedStartDate;
        this.endDate=formattedEndDate;

    }
    public void checkTeamCapacity(Team team,CreatTeamRequestDto creatTeamRequestDto){
        // 기존의 팀이 있었음
        Long selectedUserPlusTeamSize=teamSize+creatTeamRequestDto.getSelectedUserIds().stream().count();
        if(team!=null) {
            if (creatTeamRequestDto.getMaxTeamSize() < selectedUserPlusTeamSize) {
                throw new ApplicationException(TEAM_CAPACITY_EXCEEDED);
            }
        }

    }


    public void saveTeamLeaderToTeamMember(Member member) {

        MemberTeam memberTeam = MemberTeam.builder()
                .member(member)
                .team(this)
                .role(MemberTeamStateEnum.LEADER)
                .build();
        memberTeams.add(memberTeam);
    }
}
