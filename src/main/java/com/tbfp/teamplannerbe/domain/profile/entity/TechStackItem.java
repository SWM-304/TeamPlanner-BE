package com.tbfp.teamplannerbe.domain.profile.entity;

import com.tbfp.teamplannerbe.domain.profile.TechCategory;
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
public class TechStackItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TECH_STACK_ITEM_ID")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private TechCategory techCategory;

    @Column(columnDefinition = "bit default 0")
    private Boolean userGenerated;

}
