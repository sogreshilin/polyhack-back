package ru.nsu.fit.supernova.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.supernova.service.speech.SpeechRecognitionService;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final SpeechRecognitionService service;

    @PostMapping
    public ResponseEntity<String> triggerMeetUpSync() {
        service.initializeAudioFileRecognition("https://storage.yandexcloud.net/polyhack/philosophy.ogg");
        return new ResponseEntity<>("Recognition started", HttpStatus.OK);
    }
}
