package com.tbfp.teamplannerbe.domain.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = 1121622254L;

    public static final QBoard board = new QBoard("board");

    public final com.tbfp.teamplannerbe.domain.common.base.QBaseTimeEntity _super = new com.tbfp.teamplannerbe.domain.common.base.QBaseTimeEntity(this);

    public final StringPath activitiy_detail = createString("activitiy_detail");

    public final StringPath activitiy_name = createString("activitiy_name");

    public final StringPath activity_area = createString("activity_area");

    public final StringPath activity_benefits = createString("activity_benefits");

    public final StringPath activity_field = createString("activity_field");

    public final StringPath activity_img = createString("activity_img");

    public final StringPath activity_key = createString("activity_key");

    public final StringPath activity_period = createString("activity_period");

    public final StringPath activity_url = createString("activity_url");

    public final StringPath category = createString("category");

    public final StringPath company_Type = createString("company_Type");

    public final StringPath competition_category = createString("competition_category");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath homepage = createString("homepage");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath interest_area = createString("interest_area");

    public final StringPath meetingTime = createString("meetingTime");

    public final StringPath preferred_skills = createString("preferred_skills");

    public final StringPath prize_scale = createString("prize_scale");

    public final StringPath recruitment_count = createString("recruitment_count");

    public final StringPath recruitment_period = createString("recruitment_period");

    public final StringPath target = createString("target");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QBoard(String variable) {
        super(Board.class, forVariable(variable));
    }

    public QBoard(Path<? extends Board> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoard(PathMetadata metadata) {
        super(Board.class, metadata);
    }

}

