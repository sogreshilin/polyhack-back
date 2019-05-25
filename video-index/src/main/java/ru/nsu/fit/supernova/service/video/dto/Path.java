package ru.nsu.fit.supernova.service.video.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Path {
    private String url;

    @JsonCreator
    public Path(@JsonProperty("url") String url) {
        this.url = url;
    }
}
