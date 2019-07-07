package ru.nsu.fit.supernova.controller;

import java.util.List;
import java.util.concurrent.Executor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.supernova.facade.ProcessingVideoFacade;
import ru.nsu.fit.supernova.service.video.dto.Path;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/process")
@RequiredArgsConstructor
@Slf4j
public class VideoConvertingController {

    private final ProcessingVideoFacade processingVideoFacade;
    private final Executor executor;

    @PostMapping
    public ResponseEntity convert(@RequestBody List<Path> paths) {
        paths.forEach(this::convert);
        return ResponseEntity.ok("Processing started");
    }

    private void convert(Path path) {
        executor.execute(() -> processingVideoFacade.process(path.getId()));
    }
}
