package ru.nsu.fit.supernova.service.lemmatizer;

import lombok.Value;

class Request {

    @Value
    static final class TextEntry {
        String text;
    }

}
