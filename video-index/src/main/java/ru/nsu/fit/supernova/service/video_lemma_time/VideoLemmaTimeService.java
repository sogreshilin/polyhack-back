package ru.nsu.fit.supernova.service.video_lemma_time;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.supernova.model.MultiMediaFile;
import ru.nsu.fit.supernova.model.StatusType;
import ru.nsu.fit.supernova.model.VideoLemmaTime;
import ru.nsu.fit.supernova.model.WordTime;
import ru.nsu.fit.supernova.repository.MultiMediaRepository;
import ru.nsu.fit.supernova.repository.VideoLemmaTimeRepository;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class VideoLemmaTimeService {

    private final VideoLemmaTimeRepository videoLemmaTimeRepository;
    private final MultiMediaRepository multiMediaRepository;

    public void save(List<WordTime> wordTimes, MultiMediaFile multiMediaFile) {
        multiMediaFile.setStatus(StatusType.COMPLETED);
        multiMediaRepository.save(multiMediaFile);
        videoLemmaTimeRepository.saveAll(wordTimes.stream().map(wordTime -> new VideoLemmaTime()
            .setLemma(wordTime.getWord())
            .setStartTime(wordTime.getStartTime())
            .setMultiMediaFile(multiMediaFile)).collect(Collectors.toList()));
    }
}
