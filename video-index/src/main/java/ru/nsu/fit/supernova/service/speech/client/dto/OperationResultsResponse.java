package ru.nsu.fit.supernova.service.speech.client.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class OperationResultsResponse {
    private String id;
    private Boolean done;
    private Response response;

    @Value
    public static class Word {
        private BigDecimal startTime;
        private BigDecimal endTime;
        private String word;
        private Double confidence;

        @JsonCreator
        public Word(
            @Valid @NotNull @JsonProperty("startTime") String startTime,
            @Valid @NotNull @JsonProperty("endTime") String endTime,
            @Valid @NotNull @JsonProperty("word") String word,
            @JsonProperty("confidence") Double confidence
        ) {
            this.startTime = new BigDecimal(startTime.substring(0, startTime.length() - 1));
            this.endTime = new BigDecimal(endTime.substring(0, endTime.length() - 1));
            this.word = word;
            this.confidence = confidence;
        }
    }

    @Value
    public static class Alternative {
        private List<Word> words;
        private String text;
        private Double confidence;

        @JsonCreator
        public Alternative(
            @JsonProperty("words") List<Word> words,
            @JsonProperty("text") String text,
            @JsonProperty("confidence") Double confidence
        ) {
            this.words = words;
            this.text = text;
            this.confidence = confidence;
        }
    }

    @Value
    public static class Chunk {
        private List<Alternative> alternatives;
        private Integer channelTag;

        @JsonCreator
        public Chunk(
            @JsonProperty("alternatives") List<Alternative> alternatives,
            @JsonProperty("channelTag") Integer channelTag
        ) {
            this.alternatives = alternatives;
            this.channelTag = channelTag;
        }
    }

    @Value
    public static class Response {
        private List<Chunk> chunks;

        @JsonCreator
        public Response(@JsonProperty("chunk") List<Chunk> chunks) {
            this.chunks = chunks;
        }
    }

    @JsonCreator
    public OperationResultsResponse(
        @JsonProperty("id") String id,
        @JsonProperty("done") Boolean done,
        @JsonProperty("response") Response response
    ) {
        this.id = id;
        this.done = done;
        this.response = response;
    }
}
