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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     *
     * 몇페이지를 가지고 올 것이고 스케줄링을 몇 일 주기로 돌릴 것인지 설정
     */
    @Scheduled(fixedDelay = 7 * 24 * 60 * 60 * 1000) // 1 week
    public void searchXml() throws IOException {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);

        int maxNumber = maxNumberXml();
        String baseUrl = "https://linkareer.com/sitemap/activities/%s.xml";

        for (int i = maxNumber; i > maxNumber - 5; i--) {
            String url = String.format(baseUrl, maxNumber);
            Document doc = Jsoup.connect(url).get();
            Elements locElements = doc.select("loc");
            for (Element locElement : locElements) {
                String link = locElement.text();
                if (link.startsWith("https")) {
                    searchActivity(link);
                }
            }
        }
    }

    /**
     * xml 에서 최신 xmlNumber은 추출하는해옴
     */

    public int maxNumberXml(){
        int maxActivityNum = -1;
        try {
            Document doc = Jsoup.connect("https://linkareer.com/sitemap/sitemapindex.xml").get();
            String robotsTxt = doc.text();
            // Find the sitemap URLs
            System.out.println(robotsTxt);

            String[] text = robotsTxt.split(" ");


            String pattern = "https://linkareer.com/sitemap/activities/(\\d+)\\.xml";


            for (String url : text) {
                Matcher matcher = Pattern.compile(pattern).matcher(url);
                if (matcher.find()) {
                    int activityNum = Integer.parseInt(matcher.group(1));
                    if (activityNum > maxActivityNum) {
                        maxActivityNum = activityNum;
                    }
                }
            }

            if (maxActivityNum != -1) {
                System.out.println("가장 큰 활동 번호: " + maxActivityNum);
            } else {
                System.out.println("활동 URL을 찾을 수 없습니다.");
            }


            
        } catch (IOException e) {
            e.printStackTrace();
        }

        return maxActivityNum;

    }

    /**
     *
     * 공모전 대외외활동 동아리 카테고리 별 크롤링 시작
     */

    private void searchActivity(String link) throws IOException {
        ChromeOptions options = configureChromeOptions();
        WebDriver driver = new ChromeDriver(options);

        driver.get(link);
        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        List<WebElement> elements = driver.findElements(By.cssSelector(".nav-menu-list [data-active='true']"));
        for (WebElement element : elements) {
            if (element.getText().equals("공모전")) {
                processContestActivity(driver, link);
            } else if (element.getText().equals("대외활동")) {
                processExternalActivity(driver, link);
            } else if (element.getText().equals("동아리")) {
                processClubActivity(driver, link);
            }
        }

        driver.quit();
    }

    /**
     *
     * 크롤링 설정
     */
    private ChromeOptions configureChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless", "--remote-allow-origins=*", "--no-sandbox", "--disable-dev-shm-usage",
                "--disable-gpu", "--window-size=1920x1080", "--single-process", "--disable-websocket");
        return options;
    }

    /**
     *
     * 공모전크롤링하는 영역
     */

    private void processContestActivity(WebDriver driver, String link) {

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
                log.info("현재 날짜는 접수 기간에 포함됩니다.");
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
               log.info("현재 날짜는 접수 기간에 포함되지 않습니다.");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    /**
     *
     * 대외활동크롤링하는 영역
     */

    private void processExternalActivity(WebDriver driver, String link) {
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
                log.info("현재 날짜는 접수 기간에 포함됩니다.");


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
                log.info("현재 날짜는 접수 기간에 포함되지 않습니다.");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * 동아리크롤링하는 영역
     */

    private void processClubActivity(WebDriver driver, String link) {
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

            String[] split = recruitmentPeriod.split("~");
            String startDateString=split[0];
            String endDateString=split[1];

            try {
                Date startDate = format.parse(startDateString);
                Date endDate = format.parse(endDateString);
                Date currentDateObj = format.parse(currentDate);

                if (currentDateObj.compareTo(startDate) >= 0 && currentDateObj.compareTo(endDate) <= 0) {
                    log.info("현재 날짜는 접수 기간에 포함됩니다.");

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
                    log.info("현재 날짜는 접수 기간에 포함되지 않습니다.");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
}