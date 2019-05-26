package ru.nsu.fit.supernova.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.supernova.model.VideoLemmaTime;

@Repository
public interface VideoLemmaTimeRepository extends JpaRepository<VideoLemmaTime, Long> {

    List<VideoLemmaTime> findAllByLemmaInOrderByStartTime(List<String> lemmas);

    List<VideoLemmaTime> findAllByLemmaInAndMultiMediaFile_IdOrderByStartTime(List<String> lemmas, Long fileId);
}
