package ru.nsu.fit.supernova.service.index;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.supernova.model.Indexing;
import ru.nsu.fit.supernova.model.MultiMediaFile;
import ru.nsu.fit.supernova.model.VideoLemmaTime;
import ru.nsu.fit.supernova.repository.MultiMediaRepository;
import ru.nsu.fit.supernova.repository.VideoLemmaTimeRepository;
import ru.nsu.fit.supernova.service.lemmatizer.Lemmatizer;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoIndexService {

    private final Lemmatizer lemmatizer;
    private final VideoLemmaTimeRepository repository;
    private final MultiMediaRepository multiMediaRepository;

    public List<Indexing.SearchResult> findByTokens(List<String> tokens) {
        List<String> lemmas = lemmatizer.stringLemmas(tokens);
        List<VideoLemmaTime> videoLemmaTimeList = repository.findAllByLemmaInOrderByStartTime(lemmas);
        return convertToApi(videoLemmaTimeList);
    }

    public List<Indexing.SearchResult> findByTokens(List<String> tokens, Long videoId) {
        List<String> lemmas = lemmatizer.stringLemmas(tokens);
        List<VideoLemmaTime> videoLemmaTimeList = repository
            .findAllByLemmaInAndMultiMediaFile_IdOrderByStartTime(lemmas, videoId);
        return convertToApi(videoLemmaTimeList);
    }

    public Indexing.SearchResult getAllWordsByVideoId(long videoId) {
        MultiMediaFile file = multiMediaRepository.findById(videoId).orElseThrow(() -> new RuntimeException("File with such id not found"));
        List<VideoLemmaTime> vlts = repository.findAllByMultiMediaFile_IdOrderByStartTime(videoId);
        List<Indexing.WordInfo> words = vlts.stream()
            .map(vlt -> new Indexing.WordInfo(vlt.getLemma(), vlt.getStartTime()))
            .collect(Collectors.toList());
        return new Indexing.SearchResult(file.getId(), file.getExternalVideoUrl(), words);
    }

    private List<Indexing.SearchResult> convertToApi(List<VideoLemmaTime> videoLemmaTimeList) {
        return videoLemmaTimeList.stream()
            .collect(Collectors.groupingBy(vlt -> vlt.getMultiMediaFile().getId()))
            .entrySet().stream()
            .map(entry -> {
                MultiMediaFile file = entry.getValue().get(0).getMultiMediaFile();
                List<Indexing.WordInfo> wordInfos = entry.getValue().stream()
                    .map(vlt -> new Indexing.WordInfo(vlt.getLemma(), vlt.getStartTime()))
                    .sorted(Comparator.comparing(Indexing.WordInfo::getStartTime))
                    .collect(Collectors.toList());
                return new Indexing.SearchResult(file.getId(), file.getExternalVideoUrl(), wordInfos);
            }).collect(Collectors.toList());
    }
}
