package com.tbfp.teamplannerbe.domain.member.entity;

import com.tbfp.teamplannerbe.domain.auth.MemberRole;
import com.tbfp.teamplannerbe.domain.auth.ProviderType;
import com.tbfp.teamplannerbe.domain.common.base.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(length = 300)
    private String loginId;

    @Column(length = 300)
    private String password;

    private String email;

    private String phone;

    private Boolean state;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    private String providerId;

}
