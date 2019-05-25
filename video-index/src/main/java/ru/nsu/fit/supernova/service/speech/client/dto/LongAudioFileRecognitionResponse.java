package ru.nsu.fit.supernova.service.speech.client.dto;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class LongAudioFileRecognitionResponse {
    private String id;

    @JsonCreator
    public LongAudioFileRecognitionResponse(@JsonProperty("id") String id) {
        this.id = id;
    }
}
