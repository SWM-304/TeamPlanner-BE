package com.tbfp.teamplannerbe.domain.profile.service.impl;

import com.tbfp.teamplannerbe.domain.profile.service.ProfileService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProfileServiceImplTest {

    @Autowired
    ProfileService profileService;

    @Autowired
    ProfileServiceImpl profileServiceImpl;

    static Stream<Arguments> addressPairs() {
        return Stream.of(
                Arguments.of("경기도 삼일시 이사구 오팔동", "삼일시 이사구",0.46667),
                Arguments.of("삼일시 이사구", "삼일 이사",0.71429),
                Arguments.of("서울특별시 강남구 역삼동", "서울시 역삼동",0.53846),
                Arguments.of("서울시 강남구 역삼동","서울시 강남구 역삼",0.90909)
        );
    }

    @ParameterizedTest
    @MethodSource("addressPairs")
    void getMatchWordCount_Test(String addressStr1, String addressStr2, Double matchWordCountAnswer){
        double matchWordCount = profileServiceImpl.getMatchWordCount(addressStr1,addressStr2);
        double epsilon = 1e-4;
        assertEquals(matchWordCount,matchWordCountAnswer,epsilon);
    }
}