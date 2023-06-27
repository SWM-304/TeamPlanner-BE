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
public class ContestCrawler {

    private final BoardService boardService;
    /**
     * 공모전에 관련된 json 파일을 읽어오는 코드
     */

    //    @Scheduled(fixedDelay = 10 * 60 * 1000) // 10min
    public void runCrawler_with_Contest() {
        String url = "https://raw.githubusercontent.com/SWM-304/TeamPlanner-Crawler/main/crawler/Contest.json";

        try {
            URL urlObj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

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
                    String prize_scale = summary_information.getString("시상규모");
                    String activity_period = summary_information.getString("접수기간");
                    String activity_url = summary_information.getString("홈페이지");
                    String meetingTime = summary_information.getString("활동혜택");
                    String activity_field = summary_information.getString("공모분야");
                    String activity_benefits = summary_information.getString("추가혜택");

                    Board board = Board.builder()
                            .activity_Key(key)
                            .activitiy_Name(title)
                            .activity_Url(activity_url)
                            .activity_Img(activity_img)
                            .activitiy_Detail(activitiy_detail)
                            .category("공모전")
                            .company_Type(company_Type)
                            .target(target)
                            .activity_Area("")
                            .recruitment_Period("")
                            .recruitment_Count("")
                            .meeting_Time(meetingTime)
                            .activity_Benefits(activity_benefits)
                            .interest_Area("")
                            .homepage("")
                            .activity_Field(activity_field)
                            .prize_Scale(prize_scale)
                            .competition_Category("")
                            .preferred_Skills("")
                            .activity_Period(activity_period)
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
