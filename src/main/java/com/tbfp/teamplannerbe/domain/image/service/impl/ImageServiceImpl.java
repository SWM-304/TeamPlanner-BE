package com.tbfp.teamplannerbe.domain.image.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.image.dto.ImageResponseDto;
import com.tbfp.teamplannerbe.domain.image.service.ImageService;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import com.tbfp.teamplannerbe.domain.profile.repository.BasicProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {
    private final AmazonS3 amazonS3;

    @Value("${amazon.aws.bucket}")
    private String bucketName;

    private final MemberRepository memberRepository;

    private final BasicProfileRepository basicProfileRepository;

    @Override
    @Transactional
    public ImageResponseDto.GetPreSignedUrlResponseDto getPreSignedUrl(String username, String extension){
        String url = generatePreSignedUrl(bucketName, username + "." + extension, HttpMethod.PUT);
        return ImageResponseDto.GetPreSignedUrlResponseDto.builder()
                .preSignedUrl(url)
                .build();
    }

    public String generatePreSignedUrl(String bucketName, String filePath, HttpMethod httpMethod){
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE,10);
        return amazonS3.generatePresignedUrl(bucketName, filePath, calendar.getTime(),httpMethod).toString();
    }

    @Override
    @Transactional
    public void deleteProfileImage(String username, String filename){
        Optional<Member> member = memberRepository.findMemberByUsername(username);
        if(!member.isPresent()) throw new ApplicationException(ApplicationErrorType.USER_NOT_FOUND);

        Long memberId = member.get().getId();

        System.out.println(filename+memberId);

        if(!basicProfileRepository.findByProfileImageAndMemberId(filename,memberId).isPresent()){
            throw new ApplicationException(ApplicationErrorType.IMAGE_DELETION_UNAUTHORIZED);
        }

        try {
            amazonS3.deleteObject(bucketName, filename);
        } catch (AmazonServiceException e){
            throw new ApplicationException(ApplicationErrorType.IMAGE_DOESNT_EXIST);
        }
    }
}
