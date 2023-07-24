package com.tbfp.teamplannerbe.domain.recruitmentApply.repository;


import com.tbfp.teamplannerbe.domain.recruitmentApply.entity.RecruitmentApply;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RecruitmentApplyRepository extends JpaRepository<RecruitmentApply, Long> {

    // fetch recruitment
    // fetch board
//    @Query(
//            "select ra from RecruitmentApply ra " +
//                    "left join fetch ra.recruitment.board " +
//                    "where ra.applicant.id = :memberId"
//    )
//    List<RecruitmentApply> findAllByApplicantFetchRecruitmentFetchBoard(@Param("memberId") Long memberId);

    Optional<RecruitmentApply> findRecruitmentApplyByRecruitment_IdAndApplicant_Username(Long recruitmentId, String username);

    Optional<RecruitmentApply> findRecruitmentApplyByRecruitment_IdAndApplicant_Id(Long recruitmentId,Long applicantId);
}
