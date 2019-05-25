package ru.nsu.fit.supernova.service.lemmatizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.nsu.fit.supernova.model.WordTime;


@Service
@Transactional
@Slf4j
public class LemmatizerImpl implements Lemmatizer {


    @Value("${service.texterra.token}")
    private String API_KEY;

    @Value("${service.texterra.baseUrl}")
    private String BASE_URL;

    private final Set<String> russianStopWords;

    public LemmatizerImpl() throws FileNotFoundException, URISyntaxException {
        URI stopWordsUri = this.getClass().getClassLoader().getResource("nlp/stopwords.txt").toURI();
        this.russianStopWords = new BufferedReader(new FileReader(new File(stopWordsUri))).lines().collect(Collectors.toSet());
    }

    @Override
    public List<WordTime> lemmas(List<WordTime> wordTime) {
        String text = wordTime.stream().map(WordTime::getWord).collect(Collectors.joining(" "));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        List<Request.TextEntry> requestBody = Lists.newArrayList(new Request.TextEntry(text));
        HttpEntity requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<List<Response.ResponseEntry>> response =
                new RestTemplate().exchange(
                        String.format("%s?targetType=lemma&apikey=%s", BASE_URL, API_KEY),
                        HttpMethod.POST,
                        requestEntity,
                        new ParameterizedTypeReference<List<Response.ResponseEntry>>() {
                        }
                );

        return Optional.ofNullable(response.getBody())
                .map(lemmasList -> lemmasList.get(0))
                .map(lemmaDetails -> {
                    List<Response.Lemma> lemmas = lemmaDetails.getAnnotations().getLemma();
                    return IntStream.range(0, lemmas.size())
                            .mapToObj(index -> new WordTime(lemmas.get(index).getValue(), wordTime.get(index).getStartTime()))
                            .filter(wt -> !this.russianStopWords.contains(wt.getWord()))
                            .collect(Collectors.toList());
                })
                .orElseGet(Collections::emptyList);
    }

}
