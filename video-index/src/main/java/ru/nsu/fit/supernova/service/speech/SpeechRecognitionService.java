package ru.nsu.fit.supernova.service.speech;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.nsu.fit.supernova.model.WordTime;
import ru.nsu.fit.supernova.service.speech.client.YandexSpeechKitClient;
import ru.nsu.fit.supernova.service.speech.client.dto.OperationResultsResponse;
import ru.nsu.fit.supernova.service.speech.client.dto.OperationResultsResponse.Chunk;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpeechRecognitionService {
    private static final long TIMEOUT = 10_000;
    private final YandexSpeechKitClient speechKitClient;

    public List<WordTime> recognizeAudioFile(String fileUrl) {
        OperationResultsResponse results = speechKitClient.initializeAudioFileRecognition(fileUrl);

        try {
            while (!results.getDone()) {
                results = speechKitClient.getOperationResults(results.getId());
                Thread.sleep(TIMEOUT);
            }
        } catch (InterruptedException e) {
            log.info("Thread was interrupted", e);
        }

        List<OperationResultsResponse.Chunk> chunks = results.getResponse().getChunks().stream()
            .filter(c -> c.getChannelTag() == 1).collect(Collectors.toList());

        return chunks.stream().map(Chunk::getAlternatives)
            .flatMap(Collection::stream)
            .map(OperationResultsResponse.Alternative::getWords)
            .flatMap(Collection::stream)
            .sorted(Comparator.comparing(OperationResultsResponse.Word::getStartTime))
            .map(word -> new WordTime(word.getWord(), word.getStartTime()))
            .collect(Collectors.toList());
    }
}
