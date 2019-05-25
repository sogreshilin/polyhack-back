package ru.nsu.fit.supernova.model;

import java.math.BigDecimal;

import lombok.Value;

@Value
public class WordTime {
    private String word;
    private BigDecimal startTime;
}
