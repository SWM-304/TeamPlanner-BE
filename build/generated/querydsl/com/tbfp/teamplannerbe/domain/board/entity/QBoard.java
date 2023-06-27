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

    public final StringPath activitiy_Detail = createString("activitiy_Detail");

    public final StringPath activitiy_Name = createString("activitiy_Name");

    public final StringPath activity_Area = createString("activity_Area");

    public final StringPath activity_Benefits = createString("activity_Benefits");

    public final StringPath activity_Field = createString("activity_Field");

    public final StringPath activity_Img = createString("activity_Img");

    public final StringPath activity_Key = createString("activity_Key");

    public final StringPath activity_Period = createString("activity_Period");

    public final StringPath activity_Url = createString("activity_Url");

    public final StringPath category = createString("category");

    public final StringPath company_Type = createString("company_Type");

    public final StringPath competition_Category = createString("competition_Category");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath homepage = createString("homepage");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath interest_Area = createString("interest_Area");

    public final StringPath meeting_Time = createString("meeting_Time");

    public final StringPath preferred_Skills = createString("preferred_Skills");

    public final StringPath prize_Scale = createString("prize_Scale");

    public final StringPath recruitment_Count = createString("recruitment_Count");

    public final StringPath recruitment_Period = createString("recruitment_Period");

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

