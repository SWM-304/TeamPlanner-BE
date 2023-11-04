package com.tbfp.teamplannerbe.domain.member.nickname;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class RandomNicknameGenerator {
    public String generateRandomNickname() {
        Random random = new Random();
        List<Adjective> adjectiveList = Arrays.asList(Adjective.values());
        List<Noun> nounList = Arrays.asList(Noun.values());

        Adjective randomAdjective = adjectiveList.get(random.nextInt(adjectiveList.size()));
        Noun randomNoun = nounList.get(random.nextInt(nounList.size()));
        return randomAdjective + " " + randomNoun;
    }
}
