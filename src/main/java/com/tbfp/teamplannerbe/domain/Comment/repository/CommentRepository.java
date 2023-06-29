package com.tbfp.teamplannerbe.domain.Comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tbfp.teamplannerbe.domain.Comment.entity.Comment;
import com.tbfp.teamplannerbe.domain.common.querydsl.support.Querydsl4RepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.tbfp.teamplannerbe.domain.Comment.entity.QComment.comment;


@Repository
public class CommentRepository extends Querydsl4RepositorySupport {


    JPAQueryFactory jpaQueryFactory;

    public CommentRepository(JPAQueryFactory jpaQueryFactory) {
        super(Comment.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }


    public Comment save(Comment comment) {
        getEntityManager().persist(comment);
        return comment;
    }


    public Optional<Comment> findBycommentId(Long commentId){
        return Optional.ofNullable(
                select(comment)
                        .from(comment)
                        .where(commentId != null ? comment.id.eq(commentId) : comment.id.isNull())
                        .fetchOne()
        );
    }

    /**
     * jpaqueryfactory를 사용해서 delete update 처리
     */

//    public Long deleteComment(Long commentId) {
//        return jpaQueryFactory
//                .delete(comment)
//                .where(commentId!=null ? comment.id.eq(commentId) : comment.id.isNull())
//                .execute();
//    }
//    public Long stateFalseComment(Long commentId){
//        return jpaQueryFactory
//                .update(comment)
//                .set(comment.state,false)
//                .where(commentId!=null ? comment.parentComment.id.eq(commentId) : comment.id.isNull())
//                .execute();
//
//    }
}
