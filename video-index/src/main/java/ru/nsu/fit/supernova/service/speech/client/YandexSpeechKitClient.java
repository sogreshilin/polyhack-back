package ru.nsu.fit.supernova.service.speech.client;

import java.util.Collections;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.nsu.fit.supernova.service.speech.client.dto.AudioFileRecognitionRequest;
import ru.nsu.fit.supernova.service.speech.client.dto.OperationResultsResponse;

import static ru.nsu.fit.supernova.service.speech.client.dto.AudioFileRecognitionRequest.ConfigSpecification.DEFAULT;

@Component
@RequiredArgsConstructor
@Slf4j
public class YandexSpeechKitClient {
    private final RestTemplate restTemplate;

    @Setter
    @Value("${yandex.speech.kit.service.account.iam.token}")
    private String serviceAccountIamToken;

    @Setter
    @Value("${yandex.cloud.folder.id}")
    private String folderId;

    public OperationResultsResponse initializeAudioFileRecognition(String fileUrl) {
        AudioFileRecognitionRequest payload = new AudioFileRecognitionRequest(
            new AudioFileRecognitionRequest.Config(DEFAULT, folderId),
            new AudioFileRecognitionRequest.Audio(fileUrl)
        );

        log.info(payload.toString());

        ResponseEntity<OperationResultsResponse> response = restTemplate.exchange(
            "https://transcribe.api.cloud.yandex.net/speech/stt/v2/longRunningRecognize",
            HttpMethod.POST,
            new HttpEntity<>(payload, httpHeaders()),
            OperationResultsResponse.class
        );

        return Optional.ofNullable(response.getBody())
            .orElseThrow(() -> new RuntimeException(
                "Unable to start audio file recognition operation for file with url " + fileUrl
            ));
    }

    public OperationResultsResponse getOperationResults(String operationId) {
        ResponseEntity<OperationResultsResponse> response = restTemplate.exchange(
            "https://operation.api.cloud.yandex.net/operations/" + operationId,
            HttpMethod.GET,
            new HttpEntity<>(httpHeaders()),
            OperationResultsResponse.class
        );

        return Optional.ofNullable(response.getBody())
            .orElseThrow(() -> new RuntimeException("Cannot get operation results for operation " + operationId));
    }

    private HttpHeaders httpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", serviceAccountIamToken));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
