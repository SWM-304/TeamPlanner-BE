package com.tbfp.teamplannerbe.domain.member.entity;

<<<<<<< HEAD
import com.tbfp.teamplannerbe.domain.common.base.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
=======
import com.tbfp.teamplannerbe.domain.auth.MemberRole;
import com.tbfp.teamplannerbe.domain.auth.ProviderType;
import com.tbfp.teamplannerbe.domain.common.base.BaseTimeEntity;
import lombok.*;
>>>>>>> bd5328b2e80d02b1cf4e1f010f02da6719f7142e

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
<<<<<<< HEAD
=======
@AllArgsConstructor
@Builder
@ToString
>>>>>>> bd5328b2e80d02b1cf4e1f010f02da6719f7142e
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;


<<<<<<< HEAD
    @Column(length = 20)
    private String loginId;

    @Column(length = 20)
=======
    @Column(length = 300)
    private String loginId;

    @Column(length = 300)
>>>>>>> bd5328b2e80d02b1cf4e1f010f02da6719f7142e
    private String password;

    private String email;

    private String phone;

    private Boolean state;

<<<<<<< HEAD
=======
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;
    private String providerId;
>>>>>>> bd5328b2e80d02b1cf4e1f010f02da6719f7142e
}
