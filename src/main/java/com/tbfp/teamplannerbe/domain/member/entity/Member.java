package com.tbfp.teamplannerbe.domain.member.entity;

import com.tbfp.teamplannerbe.domain.common.base.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;


    @Column(length = 20)
    private String loginId;

    @Column(length = 20)
    private String password;

    private String email;

    private String phone;

    private Boolean state;

}
