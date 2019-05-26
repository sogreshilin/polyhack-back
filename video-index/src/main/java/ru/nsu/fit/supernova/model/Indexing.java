package ru.nsu.fit.supernova.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.Value;

public class Indexing {

    @Value
    public static class WordInfo {
        String word;
        BigDecimal startTime;
    }

    @Value
    public static class SearchResult {
        Long id;
        String url;
        List<WordInfo> words;
    }
}
