package ru.nsu.fit.supernova.facade;

import java.net.URL;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.supernova.service.VideoConvertingService;
import ru.nsu.fit.supernova.service.lemmatizer.Lemmatizer;
import ru.nsu.fit.supernova.service.speech.SpeechRecognitionService;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProcessingVideoFacade {

    private final VideoConvertingService videoConvertingService;
    private final Lemmatizer lemmatizer;
    private final SpeechRecognitionService speechRecognitionService;

    public void process(String url) {
        URL s3Url = videoConvertingService.convertFromUrl(url);
        speechRecognitionService.recognizeAudioFile(s3Url.toString());
    }
}