package ru.nsu.fit.supernova.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.supernova.model.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    Optional<Video> findByUrl(String url);
}
