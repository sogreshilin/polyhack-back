package ru.nsu.fit.supernova.controller;

import java.util.List;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;
import ru.nsu.fit.supernova.model.Indexing.SearchResult;
import ru.nsu.fit.supernova.service.index.VideoIndexService;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class SearchController {

    private final VideoIndexService indexService;

    @GetMapping("/search")
    List<SearchResult> search(
        @RequestParam(name = "query") String query,
        @RequestParam(name = "id", required = false) Long videoId
    ) {
        query = UriUtils.decode(query, "UTF-8");
        List<String> tokens = Lists.newArrayList(query.split(" "));
        if (videoId == null) {
            return indexService.findByTokens(tokens);
        } else {
            return indexService.findByTokens(tokens, videoId);
        }
    }

    @GetMapping("/video/{videoId}/details")
    SearchResult getAllWordsBy(@PathVariable long videoId) {
        return indexService.getAllWordsByVideoId(videoId);
    }
}
