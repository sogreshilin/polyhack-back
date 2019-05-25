package ru.nsu.fit.supernova.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.supernova.model.MultiMediaFile;

@Repository
public interface MultiMediaRepository extends JpaRepository<MultiMediaFile, Long> {
    Optional<MultiMediaFile> findByExternalVideoUrl(String url);
}
