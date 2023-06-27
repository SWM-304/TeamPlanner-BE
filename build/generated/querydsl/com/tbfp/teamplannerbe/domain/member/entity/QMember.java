package com.tbfp.teamplannerbe.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -888249860L;

    public static final QMember member = new QMember("member1");

    public final com.tbfp.teamplannerbe.domain.common.base.QBaseTimeEntity _super = new com.tbfp.teamplannerbe.domain.common.base.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath loginId = createString("loginId");

    public final EnumPath<com.tbfp.teamplannerbe.domain.auth.MemberRole> memberRole = createEnum("memberRole", com.tbfp.teamplannerbe.domain.auth.MemberRole.class);

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final StringPath providerId = createString("providerId");

    public final EnumPath<com.tbfp.teamplannerbe.domain.auth.ProviderType> providerType = createEnum("providerType", com.tbfp.teamplannerbe.domain.auth.ProviderType.class);

    public final BooleanPath state = createBoolean("state");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

