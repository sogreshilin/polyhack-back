package ru.nsu.fit.supernova.service.lemmatizer;

import java.util.List;

public interface Lemmatizer {

    List<String> lemmas(String text);

}
