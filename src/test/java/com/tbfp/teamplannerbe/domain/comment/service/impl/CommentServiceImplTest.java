package com.tbfp.teamplannerbe.domain.comment.service.impl;

import com.tbfp.teamplannerbe.common.BaseControllerTest;
import com.tbfp.teamplannerbe.domain.board.dto.BoardRequestDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto;
import com.tbfp.teamplannerbe.domain.board.service.BoardService;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.updatedCommentResponseDto;
import com.tbfp.teamplannerbe.domain.comment.entity.Comment;
import com.tbfp.teamplannerbe.domain.comment.repository.CommentRepository;
import com.tbfp.teamplannerbe.domain.comment.service.CommentService;
import com.tbfp.teamplannerbe.domain.member.Education;
import com.tbfp.teamplannerbe.domain.member.Gender;
import com.tbfp.teamplannerbe.domain.member.Job;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Optional;


class CommentServiceImplTest extends BaseControllerTest {

    @Autowired
    MemberService memberService;
    @Autowired
    BoardService boardService;
    @Autowired
    CommentService commentService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CommentRepository commentRepository;

    @Test
    public void 댓글쓰기(){
        셋업멤버();
        Long boardId = 공모전생성();


        Long commentId = commentService.sendComment(CommentRequestDto.CommentSendRequestDto.builder()
                .boardId(boardId)
                .content("내용테스트입니다")
                .memberId("test")
                .build());
        Comment comment = commentRepository.findByIdAndBoardId(commentId, boardId);
        Assertions.assertThat(comment.getContent()).isEqualTo("내용테스트입니다");
    }
    @Test
    public void 댓글삭제(){
        셋업멤버();
        Long boardId = 공모전생성();


        Long commentId = commentService.sendComment(CommentRequestDto.CommentSendRequestDto.builder()
                .boardId(boardId)
                .content("내용테스트입니다")
                .memberId("test")
                .build());

        commentRepository.deleteById(commentId);
        Comment comment = commentRepository.findByIdAndBoardId(commentId, boardId);
        Assertions.assertThat(comment).isNull();
    }

    @Test
    public void 대댓글쓰기(){
        셋업멤버();
        Long boardId = 공모전생성();


        Long commentId = commentService.sendComment(CommentRequestDto.CommentSendRequestDto.builder()
                .boardId(boardId)
                .content("내용테스트입니다")
                .memberId("test")
                .build());

        Long childCommentId = commentService.sendBigComment(CommentRequestDto.bigCommentSendRequestDto.builder()
                .boardId(boardId)
                .parentCommentId(commentId)
                .memberId("test")
                .content("대댓글입니다")
                .isConfidential(true)
                .build());
        Optional<Comment> findChildComment = commentRepository.findById(childCommentId);

        Assertions.assertThat(findChildComment.get().getContent()).isEqualTo("대댓글입니다");
    }

    @Test
    public void 댓글수정(){
        셋업멤버();
        Long boardId = 공모전생성();


        Long commentId = commentService.sendComment(CommentRequestDto.CommentSendRequestDto.builder()
                .boardId(boardId)
                .content("내용테스트입니다")
                .memberId("test")
                .build());

        updatedCommentResponseDto updateComment = commentService.updateComment(CommentRequestDto.CommentUpdateRequestDto.builder()
                .commentId(commentId)
                .boardId(boardId)
                .content("댓글수정입니다")
                .build());

        Assertions.assertThat(updateComment.getContent()).isEqualTo("댓글수정입니다");
    }


    public void 셋업멤버() {

        memberService.registerMember(MemberRequestDto.SignUpRequestDto.builder()
                .username("test")
                .password("1234")
                .email("swkwon25@gmail.com")
                .phone("010-3170-8048")
                .profileIntro("나는 훌륭한 일꾼입니다")
                .profileImage("dd.image")
                .job(Job.valueOf("COLLEGE"))
                .education(Education.valueOf("COLLEGE"))
                .educationGrade(4)
                .birth(LocalDate.parse("2010-10-11"))
                .address("경기도 용인시 처인구 우리집")
                .gender(Gender.valueOf("MALE"))
                .kakaoId("kakaoId")
                .contactEmail("swkwon25@gmail.com")
                .isPublic(Long.valueOf("1023"))
                .usernameChecked(true)
                .emailChecked(true)
                .build());
    }
    public Long 공모전생성(){

        BoardRequestDto.createBoardResquestDto build = BoardRequestDto.createBoardResquestDto.builder()
                .activitiyName("test1")
                .activityUrl("test")
                .activityImg("test")
                .activitiyDetail("test")
                .category("test")
                .companyType("test")
                .target("test")
                .activityArea("test")
                .recruitmentPeriod("test")
                .recruitmentCount("test")
                .meetingTime("test")
                .homepage("test")
                .activityBenefits("test")
                .interestArea("test")
                .activityField("test")
                .prizeScale("test")
                .competitionCategory("test")
                .preferredSkills("test")
                .activityPeriod("test")
                .build();
        BoardResponseDto.savedBoardIdResponseDto board = boardService.createBoard(build, "test");

        return board.getBoardId();

    }

}