package com.tbfp.teamplannerbe.domain.notification.contoller;

import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.notification.dto.request.CreateMessageEvent;
import com.tbfp.teamplannerbe.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
            @PathVariable Long id) throws IOException {
        log.info("subscribe 했습니다");
        return notificationService.subscribe(id);
    }
    // 모집신청했을 때 같이 쏴주는 api
    @PostMapping("/send-data")
    public void sendData(@RequestBody CreateMessageEvent createMessageEvent) {
        log.info("sendDataTest");
        notificationService.send(createMessageEvent);
    }
    @GetMapping("")
    public ResponseEntity<?> getNotificationList(Principal principal) {
        log.info("getNotificationList 호출");
        if(principal==null){
            throw new ApplicationException(ApplicationErrorType.UNAUTHORIZED);
        }
        return ResponseEntity.ok().body(
                notificationService.getNotificationList(principal.getName())
        );
    }
    @PutMapping("/")
    public ResponseEntity<?> readNotificationList(Principal principal) {
        log.info("readNotificationList 호출");
        if(principal==null){
            throw new ApplicationException(ApplicationErrorType.UNAUTHORIZED);
        }
        return ResponseEntity.ok().body(
                notificationService.readNotificationList(principal.getName())
        );
    }
}
