package com.tbfp.teamplannerbe.domain.profile.controller;

import com.tbfp.teamplannerbe.domain.profile.dto.ProfileRequestDto;
import com.tbfp.teamplannerbe.domain.profile.dto.ProfileResponseDto;
import com.tbfp.teamplannerbe.domain.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Slf4j
@Tag(name= "ProfileController", description = "프로필 API")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{nickname}")
    @Operation(summary = "프로필 조희", description = "프로필을 조회한다(다른 사용자의 프로필 조회도 포함)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity<ProfileResponseDto.ShowProfileResponseDto> showProfile(@PathVariable String nickname){
        return ResponseEntity.ok(profileService.showProfile(nickname));
    }

    @GetMapping("")
    @Operation(summary = "사용자 프로필", description = "사용자가 프로필 수정을 원하면 우선 현재 프로필 정보를 보여준다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity<ProfileResponseDto.GetProfileResponseDto> getProfile(Principal principal){
        return ResponseEntity.ok(profileService.getProfile(principal.getName()));
    }

    @PostMapping("")
    @Operation(summary = "프로필 생성", description = "프로필 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity<ProfileResponseDto.CreateProfileResponseDto> createProfile(@Valid @RequestBody ProfileRequestDto.CreateProfileRequestDto createProfileRequestDto, Principal principal){
        return ResponseEntity.ok(profileService.createProfile(createProfileRequestDto,principal.getName()));
    }

    @PutMapping("")
    @Operation(summary = "프로필 수정", description = "프로필 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity<ProfileResponseDto.UpdateProfileResponseDto> editProfile(@Valid @RequestBody ProfileRequestDto.UpdateProfileRequestDto updateProfileRequestDto, Principal principal){
        return ResponseEntity.ok(profileService.updateProfile(updateProfileRequestDto,principal.getName()));
    }

    @DeleteMapping("")
    @Operation(summary = "프로필 삭제", description = "프로필 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity<?> deleteProfile(Principal principal){
        profileService.deleteProfile(principal.getName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

