package com.tbfp.teamplannerbe.crawler;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class CrawlingController {

    private final CrawlingService crawlingService;

    @GetMapping("/crawling")
    public void testCrawling() throws IOException {
        crawlingService.searchXml();
    }
}
