package ru.nsu.fit.supernova.service.speech.client.dto;

import lombok.Value;

@Value
public class LongAudioFileRecognitionRequest {
    private Config config;
    private Audio audio;

    @Value
    public static class Audio {
        private final String uri;
    }

    @Value
    public static class Config {
        private ConfigSpecification specification;
        private String folderId;
    }

    @Value
    public static class ConfigSpecification {
        public static final ConfigSpecification DEFAULT =
            new ConfigSpecification("ru-RU", "false", "OGG_OPUS");

        private String languageCode;
        private String profanityFilter;
        private String audioEncoding;
    }
}
