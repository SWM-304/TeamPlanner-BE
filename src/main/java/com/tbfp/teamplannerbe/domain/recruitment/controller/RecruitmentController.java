package com.tbfp.teamplannerbe.domain.recruitment.controller;

import com.tbfp.teamplannerbe.domain.recruitment.condition.RecruitmentSearchCondition;
import com.tbfp.teamplannerbe.domain.recruitment.dto.RecruitmentRequestDto.RecruitmentCreateRequestDto;
import com.tbfp.teamplannerbe.domain.recruitment.dto.RecruitmentRequestDto.RecruitmentUpdateRequestDto;
import com.tbfp.teamplannerbe.domain.recruitment.service.RecruitmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruitment")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    /**
     * read all List of recruitment with search condition
     * @param recruitmentSearchCondition search condition
     * @param pageable page
     * @return page
     */
    @GetMapping("")
    @Operation(summary = "전체 모집글 조회", description = "전체 모집글 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity getListWithCondition(RecruitmentSearchCondition recruitmentSearchCondition, Pageable pageable) {
        return ResponseEntity.ok().body(
            recruitmentService.getListWithCondition(recruitmentSearchCondition, pageable)
        );
    }

    /**
     * create recruitment
     * @param recruitmentCreateRequestDto create param
     * @return created id
     */
    @PostMapping("")
    @Operation(summary = "모집글 생성", description = "모집글 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity createRecruitment(Principal principal, @RequestBody RecruitmentCreateRequestDto recruitmentCreateRequestDto) {
        return ResponseEntity.ok().body(
                recruitmentService.createRecruitment(principal.getName(), recruitmentCreateRequestDto)
        );
    }

//    /**
//     * read one recruitment
//     * @param recruitmentId target id
//     * add view count
//     * @return recruitment info
//     */
//    @GetMapping("/{recruitmentId}")
//    @Operation(summary = "모집글 조회", description = "모집글 조회")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "성공"),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
//            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
//    })
//    public ResponseEntity getOne(@PathVariable Long recruitmentId) {
//        return ResponseEntity.ok().body(
//                recruitmentService.getOne(recruitmentId)
//        );
//    }
     /**
     * read one recruitment with comments
     * @param recruitmentId target id
     * add view count
     * @return recruitment info
     */
    @GetMapping("/{recruitmentId}")
    @Operation(summary = "모집글 조회", description = "모집글 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity getOne(Principal principal, @PathVariable Long recruitmentId) {
        return ResponseEntity.ok().body(
                recruitmentService.getOneWithComment(principal, recruitmentId)
        );
    }


    /**
     * update one recruitment
     * @param recruitmentId target id
     * @param recruitmentUpdateRequestDto update params
     * @return ""
     */
    @PutMapping("/{recruitmentId}")
    @Operation(summary = "모집글 수정", description = "모집글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity updateOne(@PathVariable Long recruitmentId, @RequestBody RecruitmentUpdateRequestDto recruitmentUpdateRequestDto, Principal principal) {
        return ResponseEntity.ok(
          recruitmentService.updateOne(principal.getName(), recruitmentId, recruitmentUpdateRequestDto)
        );
    }

    /**
     * delete one recruitment
     * @param recruitmentId target id
     * @return ""
     */
    @DeleteMapping("/{recruitmentId}")
    @Operation(summary = "모집글 삭제", description = "모집글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })public ResponseEntity deleteOne(@PathVariable Long recruitmentId, Principal principal) {
        return ResponseEntity.ok(
                recruitmentService.deleteOne(principal.getName(), recruitmentId)
        );
    }
}
