package com.tbfp.teamplannerbe.crawler;

import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
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
import org.openqa.selenium.JavascriptExecutor;
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
    LocalDateTime now = LocalDateTime.now(  );

    // 형식 지정
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");

    // 날짜 형식 변환
    String currentDate = now.format(formatter);


    private final String CHROME_DRIVER_PATH = "/Users/minwookim/Downloads/chromedriver-mac-arm64/chromedriver";


    @Scheduled(fixedDelay = 7 * 24 * 60 * 60 * 1000) // 1 week
    public void searchXml() throws IOException {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);

//        WebDriverManager.chromedriver().setup();
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
            options.addArguments("--single-process");
            options.addArguments("--disable-websocket");

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
            List<WebElement> elements = driver.findElements(By.cssSelector(".nav-menu-list [data-active='true']"));

        for (WebElement element : elements) {

                if(element.getText().equals("공모전")){


                    String activityName="";
                    String companyType="";
                    String target="";
                    String prizeScale="";
                    String activityUrl="";
                    String activityBenefits="";
                    String activityField="";
                    String recruitmentPeriod="";
                    String activityImg="";

                    System.out.println("공모전 글 입니다");

                    // 이미지 ,타이틀 , 각종 상세정보를 가져오는 태그들
                    List<WebElement> activityContainers = driver.findElements(By.cssSelector("div[class^='ActivityInformationFieldContainer']"));
                    List<WebElement> title = driver.findElements(By.cssSelector("main[class^='main-container']"));
                    WebElement activityInfo = driver.findElement(By.className("activity-info"));
                    List<WebElement> imageTags = activityInfo.findElements(By.tagName("img"));
                    activityImg=imageTags.get(0).getAttribute("src");


                    for (WebElement webElement : title) {
                        String text = webElement.getText();
                        String[] lines = text.split("\n");
                        activityName=lines[0];
                    }
                    // 찾은 요소들을 iterator로 순회하며 정보를 가져옵니다.
                    for (WebElement container : activityContainers) {
                        // 필요한 정보를 찾아 출력하거나 다른 작업을 수행합니다.
                        String text = container.getText();
                        String[] lines = text.split("\n");

                        for (int i = 0; i < lines.length-1; i += 2) {
                            String field = lines[i];
                            String value = lines[i + 1];

                            switch (field) {
                                case "기업형태":
                                    companyType = value;
                                    break;
                                case "참여대상":
                                    target = value;
                                    break;
                                case "시상규모":
                                    prizeScale = value;
                                    break;
                                case "접수기간":
                                    recruitmentPeriod = value;
                                    break;
                                case "홈페이지":
                                    activityUrl = value;
                                    break;
                                case "활동혜택":
                                    activityBenefits = value;
                                    break;
                                case "공모분야":
                                    activityField = value;
                                    break;
                            }
                        }
                    }




                    String[] split = recruitmentPeriod.split("~");
                    String startDateString=split[0];
                    String endDateString=split[1];


                    try {
                        Date startDate = format.parse(startDateString);
                        Date endDate = format.parse(endDateString);
                        Date currentDateObj = format.parse(currentDate);

                        if (currentDateObj.compareTo(startDate) >= 0 && currentDateObj.compareTo(endDate) <= 0) {
                            System.out.println("현재 날짜는 접수 기간에 포함됩니다.");
                            String activitiyKey=link;

                            WebElement activityDetailElement = driver.findElement(By.xpath("//*[@id=\"DETAIL\"]/div[1]/div"));;//상세내용

                            // detailluterHTML 가져오기
                            String activitiyDetail = activityDetailElement.getAttribute("outerHTML");

                            Board board = Board.builder()
                                    .activityKey(activitiyKey)
                                    .activityName(activityName)
                                    .activityUrl(activityUrl)
                                    .activityImg(activityImg)
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
                                    .activityField(activityField)
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

                    String activityName="";
                    String companyType="";
                    String target="";
                    String activityPeriod="";
                    String recruitmentCount="";
                    String activityArea="";
                    String preferredSkills="";
                    String activityUrl="";
                    String activityBenefits="";
                    String interestArea="";
                    String activityField="";
                    String recruitmentPeriod="";
                    String activityImg="";


                    // 이미지 ,타이틀 , 각종 상세정보를 가져오는 태그들
                    List<WebElement> activityContainers = driver.findElements(By.cssSelector("div[class^='ActivityInformationFieldContainer']"));
                    List<WebElement> title = driver.findElements(By.cssSelector("main[class^='main-container']"));
                    WebElement activityInfo = driver.findElement(By.className("activity-info"));
                    List<WebElement> imageTags = activityInfo.findElements(By.tagName("img"));
                    activityImg=imageTags.get(0).getAttribute("src");

                    for (WebElement webElement : title) {
                        String text = webElement.getText();
                        String[] lines = text.split("\n");
                        activityName=lines[0];
                    }
                    // 찾은 요소들을 iterator로 순회하며 정보를 가져옵니다.
                    for (WebElement container : activityContainers) {
                        // 필요한 정보를 찾아 출력하거나 다른 작업을 수행합니다.
                        String text = container.getText();
                        String[] lines = text.split("\n");

                        for (int i = 0; i < lines.length-1; i += 2) {
                            String field = lines[i];
                            String value = lines[i + 1];

                            switch (field) {
                                case "기업형태":
                                    companyType = value;
                                    break;
                                case "참여대상":
                                    target = value;
                                    break;
                                case "접수기간":
                                    recruitmentPeriod = value;
                                    break;
                                case "활동기간":
                                    activityPeriod = value;
                                    break;
                                case "모집인원":
                                    recruitmentCount = value;
                                    break;
                                case "활동지역":
                                    activityArea = value;
                                    break;
                                case "공모분야":
                                    activityField = value;
                                    break;
                                case "홈페이지":
                                    activityUrl = value;
                                    break;
                                case "관심분야":
                                    interestArea = value;
                                    break;
                                case "활동분야":
                                    activityField = value;
                                    break;
                                case "활동혜택":
                                    activityBenefits = value;
                                    break;
                                case "우대역량":
                                    preferredSkills = value;
                                    break;
                            }
                        }
                    }

                    String[] split = recruitmentPeriod.split("~");
                    String startDateString=split[0];
                    String endDateString=split[1];

                    try {
                        Date startDate = format.parse(startDateString);
                        Date endDate = format.parse(endDateString);
                        Date currentDateObj = format.parse(currentDate);

                        if (currentDateObj.compareTo(startDate) >= 0 && currentDateObj.compareTo(endDate) <= 0) {
                            System.out.println("현재 날짜는 접수 기간에 포함됩니다.");


                            String activityKey=link;

//                            // 상세내용 jsoup으로 못가져오게 막아놔서  동적크롤링으로 변환
//                            String activitiyDetail = doc.select(".ActivityDetailTabContent__StyledWrapper-sc-b973f285-0.juIUdj > div").html();
                            WebElement activityDetailElement = driver.findElement(By.xpath("//*[@id=\"DETAIL\"]/div[1]/div"));;//상세내용


                            // detailluterHTML 가져오기
                            String activitiyDetail = activityDetailElement.getAttribute("outerHTML");

                            System.out.println(activityKey);
                            Board board = Board.builder()
                                    .activityKey(activityKey)
                                    .activityName(activityName)
                                    .activityUrl(activityUrl)
                                    .activityImg(activityImg)
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
                                    .homepage(activityUrl)
                                    .activityField(activityField)
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

                    String activityName="";
                    String activityImg="";
                    String companyType="";
                    String target="";
                    String activityArea="";
                    String recruitmentCount="";
                    String meetingTime="";
                    String activityUrl="";
                    String activityBenefits="";
                    String interestArea="";
                    String activityField="";
                    String recruitmentPeriod="";


                    // 이미지 ,타이틀 , 각종 상세정보를 가져오는 태그들
                    List<WebElement> activityContainers = driver.findElements(By.cssSelector("div[class^='ActivityInformationFieldContainer']"));
                    List<WebElement> title = driver.findElements(By.cssSelector("main[class^='main-container']"));
                    WebElement activityInfo = driver.findElement(By.className("activity-info"));
                    List<WebElement> imageTags = activityInfo.findElements(By.tagName("img"));
                    activityImg=imageTags.get(0).getAttribute("src");

                    for (WebElement webElement : title) {
                        String text = webElement.getText();
                        String[] lines = text.split("\n");
                        activityName=lines[0];
                    }
                    // 찾은 요소들을 iterator로 순회하며 정보를 가져옵니다.
                    for (WebElement container : activityContainers) {
                        // 필요한 정보를 찾아 출력하거나 다른 작업을 수행합니다.
                        String text = container.getText();
                        String[] lines = text.split("\n");

                        for (int i = 0; i < lines.length-1; i += 2) {
                            String field = lines[i];
                            String value = lines[i + 1];

                            switch (field) {
                                case "기업형태":
                                    companyType = value;
                                    break;
                                case "참여대상":
                                    target = value;
                                    break;
                                case "활동지역":
                                    activityArea = value;
                                    break;
                                case "접수기간":
                                    recruitmentPeriod = value;
                                    break;
                                case "모집인원":
                                    recruitmentCount = value;
                                    break;
                                case "모임시간":
                                    meetingTime = value;
                                    break;
                                case "홈페이지":
                                    activityUrl = value;
                                    break;
                                case "활동혜택":
                                    activityBenefits = value;
                                    break;
                                case "관심분야":
                                    interestArea = value;
                                    break;
                                case "활동분야":
                                    activityField = value;
                                    break;

                            }
                        }
                    }

//                    WebElement recruitmentPeriod = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[1]/div/main/div/main/div[1]/div/ul/li/div[2]/div[1]/div[4]/h3"));

                    String[] split = recruitmentPeriod.split("~");
                    String startDateString=split[0];
                    String endDateString=split[1];

                    try {
                        Date startDate = format.parse(startDateString);
                        Date endDate = format.parse(endDateString);
                        Date currentDateObj = format.parse(currentDate);

                        if (currentDateObj.compareTo(startDate) >= 0 && currentDateObj.compareTo(endDate) <= 0) {
                            System.out.println("현재 날짜는 접수 기간에 포함됩니다.");

                            String activitiyKey=link;

                            WebElement activityDetailElement = driver.findElement(By.xpath("//*[@id=\"DETAIL\"]/div[1]/div"));;//상세내용

                            String activitiyDetail = activityDetailElement.getAttribute("outerHTML");

                            Board board = Board.builder()
                                    .activityKey(activitiyKey)
                                    .activityName(activityName)
                                    .activityUrl(activityUrl)
                                    .activityImg(activityImg)
                                    .activityDetail(activitiyDetail)
                                    .category("동아리")
                                    .companyType(companyType)
                                    .target(target)
                                    .activityArea(activityArea)
                                    .recruitmentPeriod(startDateString+"~"+endDateString)
                                    .recruitmentCount(recruitmentCount)
                                    .meetingTime(meetingTime)
                                    .activityBenefits(activityBenefits)
                                    .interestArea(interestArea)
                                    .homepage("")
                                    .activityField(activityField)
                                    .prizeScale("")
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


    public int maxNumberXml(){
        int maxNumber = 0;
        try {
            Document doc = Jsoup.connect("https://linkareer.com/robots.txt").get();
            String robotsTxt = doc.text();
            // Find the sitemap URLs

            String[] split = robotsTxt.split("Sitemap:");
            maxNumber=split.length-6;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(maxNumber);
        return maxNumber;
    }
}
