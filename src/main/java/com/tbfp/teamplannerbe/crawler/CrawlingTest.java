package com.tbfp.teamplannerbe.crawler;

import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.board.service.BoardService;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CrawlingTest {


    private final BoardService boardService;

    SimpleDateFormat format = new SimpleDateFormat("yy.M.d");

    // 현재 날짜와 시간 가져오기
    LocalDateTime now = LocalDateTime.now();

    // 형식 지정
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");

    // 날짜 형식 변환
    String currentDate = now.format(formatter);

    @Scheduled(fixedDelay = 7 * 24 * 60 * 60 * 1000) // 1 week
    public void searchXml() throws IOException {
        WebDriverManager.chromedriver().setup();
        int maxNumber = maxNumberXml();
        String baseUrl = "https://linkareer.com/sitemap/activities/%s.xml";



        for(int i=maxNumber;i>maxNumber-5;i--){  //이거 나중에 75정도까지만 탐색하기

            String url = String.format(baseUrl, maxNumber);
            Document doc = Jsoup.connect(url).get();
            Elements locElements = doc.select("loc");
            for (Element locElement : locElements) {
                String link = locElement.text();
                if (link.startsWith("https")) {
                    searchActivity(link); // 해당하는 활동을 탐색
                }
            }
        }

    }

    private void searchActivity(String link) throws IOException {


            //크롬 설정을 담은 객체 생성
            ChromeOptions options = new ChromeOptions();
            //브라우저가 눈에 보이지 않고 내부적으로 돈다.
            //설정하지 않을 시 실제 크롬 창이 생성되고, 어떤 순서로 진행되는지 확인할 수 있다.
            options.addArguments("headless");
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920x1080");
//            options.addArguments("--disable-websocket");

            //위에서 설정한 옵션은 담은 드라이버 객체 생성
            //옵션을 설정하지 않았을 때에는 생략 가능하다.
            //WebDriver객체가 곧 하나의 브라우저 창이라 생각한다.

            WebDriver driver = new ChromeDriver(options);

            //이동을 원하는 url
            String url = link; //링크로 바꿔주기

            //WebDriver을 해당 url로 이동한다.
            driver.get(url);

            //브라우저 이동시 생기는 로드시간을 기다린다.
            //HTTP응답속도보다 자바의 컴파일 속도가 더 빠르기 때문에 임의적으로 1초를 대기한다.
            try {Thread.sleep(1000);} catch (InterruptedException e) {}

            //class="nav" 인 모든 태그를 가진 WebElement리스트를 받아온다.
            //WebElement는 html의 태그를 가지는 클래스이다.
            List<WebElement> elements = driver.findElements(By.cssSelector(".nav-menu-list button[data-active='true']"));


        for (WebElement element : elements) {

                if(element.getText().equals("공모전")){
                    System.out.println("공모전 글 입니다");
                    Document doc = Jsoup.connect(url).get();

                    Element 접수기간 = doc.select("h3.jss76").get(3);
                    String[] split = 접수기간.text().split("~");
                    String startDateString=split[0];
                    String endDateString=split[1];


                    try {
                        Date startDate = format.parse(startDateString);
                        Date endDate = format.parse(endDateString);
                        Date currentDateObj = format.parse(currentDate);

                        if (currentDateObj.compareTo(startDate) >= 0 && currentDateObj.compareTo(endDate) <= 0) {
                            System.out.println("현재 날짜는 접수 기간에 포함됩니다.");

                            String activityName = doc.selectFirst("h1.TrickyH1Tag__StyledWrapper-sc-501a6070-0.eHFQjd").text();
                            Element activityImg = doc.selectFirst("figure.jss60 > img.jss61");


                            String companyType = doc.select("h3.jss76").get(0).text();
                            String target = doc.select("h3.jss76").get(1).text();
                            String prizeScale = doc.select("h3.jss76").get(2).text();

                            String activityUrl = doc.select("h3.jss76").get(4).text();
                            String activityBenefits = doc.select("h3.jss76").get(5).text();
                            String activity_field = doc.select("h3.jss76").get(6).text();
                            Element 추가혜택 = doc.select("h3.jss76").get(7);

                            System.out.println("activityKey : "+"https://linkareer.com/activity/13900");
                            System.out.println("제목 : "+activityName);
                            System.out.println("이미지 : "+activityImg.attr("src"));
                            System.out.println(companyType);
                            System.out.println(target);
                            System.out.println(prizeScale);
                            System.out.println(접수기간.text());
                            System.out.println(activityUrl);
                            System.out.println(activityBenefits);
                            System.out.println(activity_field);
                            System.out.println(추가혜택.text());

                            String activitiyKey=link;


                            //상세내용
                            String activitiyDetail = doc.select("div.jss143").html();
                            System.out.println(activitiyDetail);

                            Board board = Board.builder()
                                    .activityKey(activitiyKey)
                                    .activityName(activityName)
                                    .activityUrl(activityUrl)
                                    .activityImg(activityImg.attr("src"))
                                    .activityDetail(activitiyDetail)
                                    .category("공모전")
                                    .companyType(companyType)
                                    .target(target)
                                    .activityArea("")
                                    .recruitmentPeriod(startDateString+"~"+endDateString)
                                    .recruitmentCount("")
                                    .meetingTime("")
                                    .activityBenefits(activityBenefits)
                                    .interestArea("")
                                    .homepage("")
                                    .activityField(activity_field)
                                    .prizeScale(prizeScale)
                                    .competitionCategory("")
                                    .preferredSkills("")
                                    .activityPeriod("")
                                    .build();
                            boardService.upsert(board);
                        } else {
                            System.out.println("현재 날짜는 접수 기간에 포함되지 않습니다.");
                            break;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(element.getText().equals("대외활동")){
                    System.out.println("대외활동글 입니다");

                    Document doc = Jsoup.connect(url).get();

                    Element recruitmentPeriod = doc.select("h3.jss76").get(2);
                    String[] split = recruitmentPeriod.text().split("~");
                    String startDateString=split[0];
                    String endDateString=split[1];


                    try {
                        Date startDate = format.parse(startDateString);
                        Date endDate = format.parse(endDateString);
                        Date currentDateObj = format.parse(currentDate);

                        if (currentDateObj.compareTo(startDate) >= 0 && currentDateObj.compareTo(endDate) <= 0) {
                            System.out.println("현재 날짜는 접수 기간에 포함됩니다.");

                            String activityName = doc.selectFirst("h1.TrickyH1Tag__StyledWrapper-sc-501a6070-0.eHFQjd").text();
                            Element activityImg = doc.selectFirst("figure.jss60 > img.jss61");


                            String companyType = doc.select("h3.jss76").get(0).text(); //기업형태
                            String target = doc.select("h3.jss76").get(1).text(); //참여대상
                                                                                            //접수기간  recruitmentPeriod

                            String activityPeriod = doc.select("h3.jss76").get(3).text(); //활동기간
                            String recruitmentCount = doc.select("h3.jss76").get(4).text(); //모집인원
                            String activityArea = doc.select("h3.jss76").get(5).text();//활동지역
                            String preferredSkills = doc.select("h3.jss76").get(6).text();//우대역량
                            String activityUrl = doc.select("h3.jss76").get(7).text();//홈페이지
                            String activityBenefits = doc.select("h3.jss76").get(8).text();//활동혜택
                            String interestArea = doc.select("h3.jss76").get(9).text();//관심분야


                            String activitiyKey=link;


                            //상세내용
                            String activitiyDetail = doc.select("div.jss147").html();
                            System.out.println(activitiyDetail);

                            Board board = Board.builder()
                                    .activityKey(activitiyKey)
                                    .activityName(activityName)
                                    .activityUrl(activityUrl)
                                    .activityImg(activityImg.attr("src"))
                                    .activityDetail(activitiyDetail)
                                    .category("대외활동")
                                    .companyType(companyType)
                                    .target(target)
                                    .activityArea(activityArea)
                                    .recruitmentPeriod(startDateString+"~"+endDateString)
                                    .recruitmentCount(recruitmentCount)
                                    .meetingTime("")
                                    .activityBenefits(activityBenefits)
                                    .interestArea(interestArea)
                                    .homepage("")
                                    .activityField("")
                                    .prizeScale("")
                                    .competitionCategory("")
                                    .preferredSkills(preferredSkills)
                                    .activityPeriod(activityPeriod)
                                    .build();
                            boardService.upsert(board);
                        } else {
                            System.out.println("현재 날짜는 접수 기간에 포함되지 않습니다.");
                            break;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(element.getText().equals("동아리")){
                    System.out.println("동아리글 입니다");

                    Document doc = Jsoup.connect(url).get();

                    Element recruitmentPeriod = doc.select("h3.jss76").get(3);
                    String[] split = recruitmentPeriod.text().split("~");
                    String startDateString=split[0];
                    String endDateString=split[1];


                    try {
                        Date startDate = format.parse(startDateString);
                        Date endDate = format.parse(endDateString);
                        Date currentDateObj = format.parse(currentDate);

                        if (currentDateObj.compareTo(startDate) >= 0 && currentDateObj.compareTo(endDate) <= 0) {
                            System.out.println("현재 날짜는 접수 기간에 포함됩니다.");

                            String activityName = doc.selectFirst("h1.TrickyH1Tag__StyledWrapper-sc-501a6070-0.eHFQjd").text();
                            Element activityImg = doc.selectFirst("figure.jss60 > img.jss61");


                            String companyType = doc.select("h3.jss76").get(0).text(); //기업형태
                            String target = doc.select("h3.jss76").get(1).text(); //참여대상
                            //접수기간  recruitmentPeriod

                            String activitiyArea = doc.select("h3.jss76").get(2).text(); //활동지역
                            String recruitmentCount = doc.select("h3.jss76").get(4).text(); //모집인원
                            String meetingTime = doc.select("h3.jss76").get(5).text();//모임시간
                            String activityUrl = doc.select("h3.jss76").get(6).text();//홈페이지
                            String activityBenefits = doc.select("h3.jss76").get(7).text();//활동혜택
                            String interestArea = doc.select("h3.jss76").get(8).text();//관심분야
                            String activityField = doc.select("h3.jss76").get(9).text();//활동분야

                            String activitiyKey=link;


                            //상세내용
                            String activitiyDetail = doc.select("div.jss147").html();
                            System.out.println(activitiyDetail);

                            Board board = Board.builder()
                                    .activityKey(activitiyKey)
                                    .activityName(activityName)
                                    .activityUrl(activityUrl)
                                    .activityImg(activityImg.attr("src"))
                                    .activityDetail(activitiyDetail)
                                    .category("동아리")
                                    .companyType(companyType)
                                    .target(target)
                                    .activityArea(activitiyArea)
                                    .recruitmentPeriod(startDateString+"~"+endDateString)
                                    .recruitmentCount(recruitmentCount)
                                    .meetingTime(meetingTime)
                                    .activityBenefits(activityBenefits)
                                    .interestArea(interestArea)
                                    .homepage("")
                                    .activityField("")
                                    .prizeScale("")
                                    .competitionCategory("")
                                    .preferredSkills("")
                                    .activityPeriod(activityField)
                                    .build();
                            boardService.upsert(board);
                        } else {
                            System.out.println("현재 날짜는 접수 기간에 포함되지 않습니다.");
                            break;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

        try {
            //드라이버가 null이 아니라면
            if(driver != null) {
                //드라이버 연결 종료
                driver.close(); //드라이버 연결 해제

                //프로세스 종료
                driver.quit();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        // h1 요소의 내용 출력


    }

    /**
     *
     * @return
     * 80
     */
    public int maxNumberXml(){
        int maxNumber = 0;
        try {
            Document doc = Jsoup.connect("https://linkareer.com/robots.txt").get();
            String robotsTxt = doc.text();
            // Find the sitemap URLs

            String[] split = robotsTxt.split("Sitemap:");
            maxNumber=split.length-5;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maxNumber;
    }
}
