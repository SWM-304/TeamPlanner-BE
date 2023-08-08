package com.tbfp.teamplannerbe.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tbfp.teamplannerbe.domain.auth.MemberRole;
import com.tbfp.teamplannerbe.domain.auth.ProviderType;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.comment.entity.Comment;
import com.tbfp.teamplannerbe.domain.common.base.BaseTimeEntity;
import com.tbfp.teamplannerbe.domain.profile.entity.BasicProfile;
import com.tbfp.teamplannerbe.domain.team.entity.MemberTeam;
import lombok.*;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(length = 300)
    private String username;

    @Column(length = 300)
    private String password;

    @Column(length = 300)
    private String nickname;

    private String email;

    private String phone;

    private Boolean state;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    private String providerId;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Comment> applies = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Board> boardList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy ="member", cascade = CascadeType.ALL)
    private List<MemberTeam> memberTeams=new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "member")
    @JoinColumn(name = "BASIC_PROFILE_ID")
    private BasicProfile basicProfile;
}




