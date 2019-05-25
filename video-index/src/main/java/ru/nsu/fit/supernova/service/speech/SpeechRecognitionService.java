package ru.nsu.fit.supernova.service.speech;

import java.net.URL;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.nsu.fit.supernova.service.speech.client.YandexSpeechKitClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpeechRecognitionService {
    private final YandexSpeechKitClient speechKitClient;

    public void initializeAudioFileRecognition(String fileUrl) {
        log.info("SpeechRecognitionService.initializeAudioFileRecognition");
        String operationId = speechKitClient.initializeLongAudioFileRecognition(fileUrl);
        log.info("OperationId = {}", operationId);
    }
}
