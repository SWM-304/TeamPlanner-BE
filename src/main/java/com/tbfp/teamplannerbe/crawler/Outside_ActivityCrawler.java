package com.tbfp.teamplannerbe.crawler;

import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

@Slf4j
@Component
@RequiredArgsConstructor
public class Outside_ActivityCrawler {

    private final BoardService boardService;
    /**
     * 대외활동에 관련된 json 파일을 읽어오는 코드
     */
    @Value("${github.access_token}")
    private String token;

    //    @Scheduled(fixedDelay = 10 * 60 * 1000) // 10min
    public void runCrawler_with_outsideActivity() {
        String url = "https://raw.githubusercontent.com/SWM-304/TeamPlanner-BE/develop/crawler/outside_activities.json?token=GHSAT0AAAAAACD6DGX7BW4ZLZMJDRTUGL5EZEZJ62A";

        try {
            URL urlObj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "token " + token);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                reader.close();

                String responseBody = content.toString();
                JSONObject jsonRoot = new JSONObject(responseBody);

                Iterator<String> keys = jsonRoot.keys();

                while(keys.hasNext()){
                    String key=keys.next();
                    JSONObject jsonObject = jsonRoot.getJSONObject(key);
                    String activity_img = jsonObject.getString("이미지");
                    String title = jsonObject.getString("제목 ");
                    String activitiy_detail = jsonObject.getString("상세내용");

                    JSONObject summary_information = jsonObject.getJSONObject("요약정보");
                    String company_Type = summary_information.getString("기업형태");
                    String target = summary_information.getString("참여대상");
                    String recruitment_period = summary_information.getString("접수기간");
                    String activity_period = summary_information.getString("활동기간");
                    String recruitment_count = summary_information.getString("모집인원");
                    String activity_area = summary_information.getString("활동지역");
                    String preferred_skills = summary_information.getString("우대역량");
                    String activity_url = summary_information.getString("홈페이지");
                    String activity_benefits = summary_information.getString("활동혜택");
                    String interest_area = summary_information.getString("관심분야");
                    String activity_field = summary_information.getString("활동분야");

                    Board board = Board.builder()
                            .activity_key(key)
                            .activitiy_name(title)
                            .activity_url(activity_url)
                            .activity_img(activity_img)
                            .activitiy_detail(activitiy_detail)
                            .category("대외활동")
                            .company_Type(company_Type)
                            .target(target)
                            .activity_area(activity_area)
                            .recruitment_period(recruitment_period)
                            .recruitment_count(recruitment_count)
                            .meetingTime("")
                            .activity_benefits(activity_benefits)
                            .interest_area(interest_area)
                            .activity_field(activity_field)
                            .prize_scale("")
                            .competition_category("")
                            .preferred_skills(preferred_skills)
                            .activity_period(activity_period)
                            .build();
                    boardService.upsert(board);



                }




            } else {
                System.out.println("Request failed with response code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
