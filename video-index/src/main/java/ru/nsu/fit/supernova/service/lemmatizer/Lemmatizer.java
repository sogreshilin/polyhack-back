package ru.nsu.fit.supernova.service.lemmatizer;

import java.util.List;

import ru.nsu.fit.supernova.model.WordTime;

public interface Lemmatizer {

    List<WordTime> lemmas(List<WordTime> wordTime);

    List<String> stringLemmas(List<String> strings);

}
