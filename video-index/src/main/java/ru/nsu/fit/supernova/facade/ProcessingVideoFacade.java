package ru.nsu.fit.supernova.facade;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.nsu.fit.supernova.model.MultiMediaFile;
import ru.nsu.fit.supernova.model.StatusType;
import ru.nsu.fit.supernova.model.WordTime;
import ru.nsu.fit.supernova.repository.MultiMediaRepository;
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
    private final MultiMediaRepository multiMediaRepository;

    public void process(String url) {
        MultiMediaFile multiMediaFile = videoConvertingService.convertFromUrl(url);
        if (multiMediaFile.getStatus() == StatusType.FAILED || multiMediaFile.getStatus() == StatusType.CREATED) {
            log.info(String.format("Started audio processing. Url: %s, Id: %d", url, multiMediaFile.getId()));
            multiMediaRepository.save(multiMediaFile.setStatus(StatusType.PROCESSING));
            List<WordTime> extractedWords = speechRecognitionService.recognizeAudioFile(multiMediaFile.getInternalAudioUrl());
            List<WordTime> normalizedWords = lemmatizer.lemmas(extractedWords);
            videoLemmaTimeService.save(normalizedWords, multiMediaFile);
            log.info(String.format("Finished audio processing. Url: %s, Id: %d", url, multiMediaFile.getId()));
        }
    }
}
