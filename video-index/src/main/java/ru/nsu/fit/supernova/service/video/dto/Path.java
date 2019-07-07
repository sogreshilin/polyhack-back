package ru.nsu.fit.supernova.service.video.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Path {
    private Long id;

    @JsonCreator
    public Path(@JsonProperty("id") Long id) {
        this.id = id;
    }
}
