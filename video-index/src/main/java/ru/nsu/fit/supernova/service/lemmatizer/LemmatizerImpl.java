package ru.nsu.fit.supernova.service.lemmatizer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LemmatizerImpl implements Lemmatizer {


    @Value("${service.texterra.token}")
    private String API_KEY;

    @Value("${service.texterra.baseUrl}")
    private String BASE_URL;


    private final static String endpoint = "http://api.ispras.ru/texterra/v1/nlp?targetType=lemma&apikey=53690b89aea841305c8e2221f21a685d45eb8fd4";

    @Override
    public List<String> lemmas(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        List<Request.TextEntry> requestBody = Lists.newArrayList(new Request.TextEntry(text));
        HttpEntity requestEntity = new HttpEntity<>(requestBody, headers);

        String url = String.format("%s?targetType=lemma&apikey=%s", BASE_URL, API_KEY);
        System.out.println(url);

        ResponseEntity<List<Response.ResponseEntry>> response =
                new RestTemplate().exchange(
                        url,
                        HttpMethod.POST,
                        requestEntity,
                        new ParameterizedTypeReference<List<Response.ResponseEntry>>() {
                        }
                );

        return Optional.ofNullable(response.getBody())
                .map(lemmasList -> lemmasList.get(0))
                .map(lemmaDetails -> lemmaDetails.getAnnotations().getLemma().stream().map(Response.Lemma::getValue).collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);
    }

}
