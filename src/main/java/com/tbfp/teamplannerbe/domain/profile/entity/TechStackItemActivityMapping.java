package com.tbfp.teamplannerbe.domain.profile.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechStackItemActivityMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TECH_STACK_ITEM_ACTIVITY_MAPPING_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TECH_STACK_ITEM_ID")
    private TechStackItem techStackItem;

    @ManyToOne
    @JoinColumn(name = "ACTIVITY_ID")
    private Activity activity;
}
