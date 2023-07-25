package com.tbfp.teamplannerbe.domain.image.controller;

import com.tbfp.teamplannerbe.domain.image.dto.ImageResponseDto;
import com.tbfp.teamplannerbe.domain.image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
@Slf4j
@Tag(name= "ProfileController", description = "프로필 API")
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/pre-signed-url")
    @Operation(summary = "preSigned url 발급", description = "이미지를 업로드 할 pre-signed url을 발급한다. extension:이미지 확장자명 ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity<ImageResponseDto.GetPreSignedUrlResponseDto> getPreSignedUrl(Principal principal, @RequestParam String extension){
        return ResponseEntity.ok(imageService.getPreSignedUrl(principal.getName(),extension));
    }

    @DeleteMapping("/image")
    @Operation(summary = "image를 삭제한다", description = "filename의 이름을 가지는 이미지를 삭제한다. ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity<?> deleteProfileImage(Principal principal, @RequestParam String filename){
        imageService.deleteProfileImage(principal.getName(),filename);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
