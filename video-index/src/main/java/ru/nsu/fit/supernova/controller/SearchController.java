package ru.nsu.fit.supernova.controller;

import java.util.List;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.supernova.model.Indexing.SearchResult;
import ru.nsu.fit.supernova.service.index.VideoIndexService;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final VideoIndexService indexService;

    @GetMapping
    List<SearchResult> findForQuery(
        @RequestParam(name = "query") String query,
        @RequestParam(name = "id", required = false) Long videoId
    ) {
        List<String> tokens = Lists.newArrayList(query.split(" "));
        if (videoId == null) {
            return indexService.findByTokens(tokens);
        } else {
            return indexService.findByTokens(tokens, videoId);
        }
    }
}
