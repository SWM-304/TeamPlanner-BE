package com.tbfp.teamplannerbe.domain.comment.service.impl;

import com.tbfp.teamplannerbe.common.BaseControllerTest;
import com.tbfp.teamplannerbe.domain.board.dto.BoardRequestDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto;
import com.tbfp.teamplannerbe.domain.board.service.BoardService;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.CreatedCommentResponseDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.CreatedchildCommentResponseDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.UpdatedCommentResponseDto;
import com.tbfp.teamplannerbe.domain.comment.entity.Comment;
import com.tbfp.teamplannerbe.domain.comment.repository.CommentRepository;
import com.tbfp.teamplannerbe.domain.comment.service.CommentService;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.Education;
import com.tbfp.teamplannerbe.domain.member.Gender;
import com.tbfp.teamplannerbe.domain.member.Job;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

import static com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType.UNAUTHORIZED;


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


        CreatedCommentResponseDto createdCommentResponseDto = commentService.sendComment(CommentRequestDto.CreateCommentRequestDto.builder()
                .boardId(boardId)
                .content("내용테스트입니다")
                .isConfidential(Boolean.TRUE)
                .memberId("test")
                .build());
        Comment comment = commentRepository.findByIdAndStateIsTrue(createdCommentResponseDto.getCommentId());
        Assertions.assertThat(comment.getContent()).isEqualTo("내용테스트입니다");
    }
    @Test
    public void 댓글삭제(){
        셋업멤버();
        Long boardId = 공모전생성();


        CreatedCommentResponseDto createdCommentResponseDto = commentService.sendComment(CommentRequestDto.CreateCommentRequestDto.builder()
                .boardId(boardId)
                .content("내용테스트입니다")
                .isConfidential(Boolean.TRUE)
                .memberId("test")
                .build());

        commentRepository.deleteById(createdCommentResponseDto.getCommentId());
        Comment comment = commentRepository.findByIdAndStateIsTrue(createdCommentResponseDto.getCommentId());
        Assertions.assertThat(comment).isNull();
    }

    @Test
    public void 대댓글쓰기(){
        셋업멤버();
        Long boardId = 공모전생성();
        CreatedCommentResponseDto createdCommentResponseDto = commentService.sendComment(CommentRequestDto.CreateCommentRequestDto.builder()
                .boardId(boardId)
                .content("내용테스트입니다")
                .isConfidential(Boolean.TRUE)
                .memberId("test")
                .build());

        CreatedchildCommentResponseDto createdchildCommentResponseDto = commentService.sendBigComment(
                CommentRequestDto.CommentToCommentCreateRequestDto.builder()
                        .isConfidential(Boolean.valueOf("true"))
                        .boardId(boardId)
                        .parentCommentId(createdCommentResponseDto.getCommentId())
                        .content("대댓글입니다")
                        .build(), "test"
        );
        Optional<Comment> findChildComment = commentRepository.findById(createdchildCommentResponseDto.getCommentId());

        Assertions.assertThat(findChildComment.get().getContent()).isEqualTo("대댓글입니다");
    }

    /**
     * 익명댓글인데 다른사람이 댓글쓰기를 요청할 경우 예외처리
     */
    @Test
    public void 대댓글쓰기유효성검사() throws Exception{
        셋업멤버();
        셋업멤버2();
        Long boardId = 공모전생성();



        CreatedCommentResponseDto createdCommentResponseDto = commentService.sendComment(CommentRequestDto.CreateCommentRequestDto.builder()
                .boardId(boardId)
                .content("내용테스트입니다")
                .isConfidential(Boolean.TRUE)
                .memberId("test")
                .build());


        try {
            CreatedchildCommentResponseDto createdchildCommentResponseDto = commentService.sendBigComment(
                    CommentRequestDto.CommentToCommentCreateRequestDto.builder()
                            .isConfidential(Boolean.valueOf("true"))
                            .boardId(boardId)
                            .parentCommentId(createdCommentResponseDto.getCommentId())
                            .content("대댓글입니다")
                            .build(), "test2"
            );
        } catch (ApplicationException e) {
            Assertions.assertThat(e.getErrorType()).isEqualTo(UNAUTHORIZED);
        }


    }

    @Test
    public void 댓글수정(){
        셋업멤버();
        Long boardId = 공모전생성();


        CreatedCommentResponseDto createdCommentResponseDto = commentService.sendComment(CommentRequestDto.CreateCommentRequestDto.builder()
                .boardId(boardId)
                .content("내용테스트입니다")
                .memberId("test")
                .isConfidential(Boolean.TRUE)
                .build());

        UpdatedCommentResponseDto updateComment = commentService.updateComment(CommentRequestDto.UpdateCommentRequestDto.builder()
                .commentId(createdCommentResponseDto.getCommentId())
                .boardId(boardId)
                .content("댓글수정입니다")
                .build());

        Assertions.assertThat(updateComment.getContent()).isEqualTo("댓글수정입니다");
    }


    public void 셋업멤버() {

        memberService.registerMember(MemberRequestDto.SignUpRequestDto.builder()
                .username("test")
                .password("1234")
                .email("asdas@gmail.com")
                .phone("010-0000-0000")
                .profileIntro("나는 훌륭한 일꾼입니다")
                .profileImage("dd.image")
                .job(Job.valueOf("COLLEGE"))
                .education(Education.valueOf("COLLEGE"))
                .admissionDate(YearMonth.parse("2017-03"))
                .graduationDate(YearMonth.parse(("2022-02")))
                .birth(LocalDate.parse("2010-10-11"))
                .address("경기도 시흥 아몰라 우리집")
                .gender(Gender.valueOf("MALE"))
                .kakaoId("kakaoId")
                .contactEmail("asdas@gmail.com")
                .isPublic(Long.valueOf("1023"))
                .usernameChecked(true)
                .emailChecked(true)
                .build());
    }

    public void 셋업멤버2() {

        memberService.registerMember(MemberRequestDto.SignUpRequestDto.builder()
                .username("test2")
                .password("12342")
                .email("asdas@gmail.com2")
                .phone("010-0000-00002")
                .profileIntro("나는 훌륭한 일꾼입니다")
                .profileImage("dd.image")
                .job(Job.valueOf("COLLEGE"))
                .education(Education.valueOf("COLLEGE"))
                .admissionDate(YearMonth.parse("2017-03"))
                .graduationDate(YearMonth.parse(("2022-02")))
                .birth(LocalDate.parse("2010-10-11"))
                .address("경기도 시흥 아몰라 우리집")
                .gender(Gender.valueOf("MALE"))
                .kakaoId("kakaoId")
                .contactEmail("asdas@gmail.com")
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