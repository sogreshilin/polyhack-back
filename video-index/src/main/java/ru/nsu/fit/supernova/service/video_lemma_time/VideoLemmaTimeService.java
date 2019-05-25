package ru.nsu.fit.supernova.service.video_lemma_time;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.supernova.model.MultiMediaFile;
import ru.nsu.fit.supernova.model.StatusType;
import ru.nsu.fit.supernova.model.VideoLemmaTime;
import ru.nsu.fit.supernova.model.WordTime;
import ru.nsu.fit.supernova.repository.VideoLemmaTimeRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoLemmaTimeService {

    private final VideoLemmaTimeRepository videoLemmaTimeRepository;

    public void save(List<WordTime> wordTimes, MultiMediaFile multiMediaFile) {
        videoLemmaTimeRepository.saveAll(wordTimes.stream().map(wordTime -> new VideoLemmaTime()
            .setLemma(wordTime.getWord())
            .setStartTime(wordTime.getStartTime())
            .setMultiMediaFile(multiMediaFile.setStatus(StatusType.COMPLETED))).collect(Collectors.toList()));
    }
}
