package ru.nsu.fit.supernova.service.lemmatizer;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

class Response {

    @Value
    static final class Lemma {
        Integer start;
        Integer end;
        String value;

        @JsonCreator
        public Lemma(@JsonProperty("start") Integer start,
                     @JsonProperty("end") Integer end,
                     @JsonProperty("value") String value) {
            this.start = start;
            this.end = end;
            this.value = value;
        }
    }

    @Value
    static final class Annotations {
        List<Lemma> lemma;

        @JsonCreator
        public Annotations(@JsonProperty("lemma") List<Lemma> lemma) {
            this.lemma = lemma;
        }
    }

    @Value
    static final class ResponseEntry {
        String text;
        Annotations annotations;

        @JsonCreator
        public ResponseEntry(@JsonProperty("text") String text, @JsonProperty("annotations") Annotations annotations) {
            this.text = text;
            this.annotations = annotations;
        }
    }

}
