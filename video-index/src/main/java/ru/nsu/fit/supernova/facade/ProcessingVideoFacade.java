package ru.nsu.fit.supernova.facade;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.nsu.fit.supernova.model.MultiMediaFile;
import ru.nsu.fit.supernova.model.StatusType;
import ru.nsu.fit.supernova.model.WordTime;
import ru.nsu.fit.supernova.service.lemmatizer.Lemmatizer;
import ru.nsu.fit.supernova.service.speech.SpeechRecognitionService;
import ru.nsu.fit.supernova.service.video.VideoConvertingService;
import ru.nsu.fit.supernova.service.video_lemma_time.VideoLemmaTimeService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessingVideoFacade {

    private final VideoConvertingService videoConvertingService;
    private final Lemmatizer lemmatizer;
    private final SpeechRecognitionService speechRecognitionService;
    private final VideoLemmaTimeService videoLemmaTimeService;

    public void process(String url) {
        MultiMediaFile multiMediaFile = videoConvertingService.convertFromUrl(url);
        if (multiMediaFile.getStatus() == StatusType.FAILED || multiMediaFile.getStatus() == StatusType.CREATED) {
            List<WordTime> extractedWords = speechRecognitionService.recognizeAudioFile(multiMediaFile.getInternalAudioUrl());
            List<WordTime> normalizedWords = lemmatizer.lemmas(extractedWords);
            videoLemmaTimeService.save(normalizedWords, multiMediaFile);
        }
    }
}
