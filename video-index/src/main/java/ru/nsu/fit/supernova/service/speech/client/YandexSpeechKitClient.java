package ru.nsu.fit.supernova.service.speech.client;

import java.util.Collections;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.nsu.fit.supernova.service.speech.client.dto.LongAudioFileRecognitionRequest;
import ru.nsu.fit.supernova.service.speech.client.dto.LongAudioFileRecognitionResponse;

import static ru.nsu.fit.supernova.service.speech.client.dto.LongAudioFileRecognitionRequest.ConfigSpecification.DEFAULT;

@Component
@RequiredArgsConstructor
public class YandexSpeechKitClient {
    private final RestTemplate restTemplate;

    @Setter
    @Value("${yandex.speech.kit.service.account.iam.token}")
    private String serviceAccountIamToken;

    @Setter
    @Value("${yandex.cloud.folder.id}")
    private String folderId;

    @Setter
    @Value("${yandex.speech.kit.base.url}")
    private String baseUrl;

    public String initializeLongAudioFileRecognition(String fileUrl) {

        LongAudioFileRecognitionRequest payload = new LongAudioFileRecognitionRequest(
            new LongAudioFileRecognitionRequest.Config(DEFAULT, folderId),
            new LongAudioFileRecognitionRequest.Audio(fileUrl)
        );

        ResponseEntity<LongAudioFileRecognitionResponse> response = restTemplate.exchange(
             baseUrl + "/speech/stt/v2/longRunningRecognize",
            HttpMethod.POST,
            requestEntity(payload),
            LongAudioFileRecognitionResponse.class
        );

        return Optional.ofNullable(response.getBody())
            .map(LongAudioFileRecognitionResponse::getId)
            .orElseThrow(() -> new RuntimeException(
                "Unable to start long audio file recognition operation for file with url " + fileUrl
            ));
    }

    private HttpEntity<LongAudioFileRecognitionRequest> requestEntity(
        LongAudioFileRecognitionRequest payload
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", serviceAccountIamToken));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(payload, headers);
    }
}
