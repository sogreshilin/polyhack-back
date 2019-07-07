package ru.nsu.fit.supernova.service.video;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import ru.nsu.fit.supernova.model.MultiMediaFile;
import ru.nsu.fit.supernova.model.StatusType;
import ru.nsu.fit.supernova.repository.MultiMediaRepository;
import ru.nsu.fit.supernova.service.yc.YCService;
import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoConvertingService {

    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;

    private final YCService ycService;
    private final MultiMediaRepository multiMediaRepository;

    public MultiMediaFile convertFile(Long id) {
        log.info("Starting VideoConvertingService convertFile with id ({})", id);
        Optional<MultiMediaFile> optionalMultiMediaFile = multiMediaRepository.findById(id);
        MultiMediaFile multiMediaFile = optionalMultiMediaFile.get();
        try {
            if (multiMediaFile.getInternalAudioUrl() != null) {
                log.info("File was already processed. Audio url: {}", multiMediaFile.getInternalAudioUrl());
                return multiMediaFile;
            } else {
                log.info("Downloading video from {}", multiMediaFile.getExternalVideoUrl());
                File source = File.createTempFile("input", ".tmp");

                FileUtils.copyURLToFile(
                    new URL(multiMediaFile.getExternalVideoUrl()),
                    source,
                    CONNECT_TIMEOUT,
                    READ_TIMEOUT);
                log.info("Video downloaded successfully");
                log.info("Converting audio stream to OGG format");
                File target = File.createTempFile("output", ".tmp");

                AudioAttributes audio = new AudioAttributes();
                audio.setCodec("libopus");
                audio.setBitRate(192000);
                audio.setChannels(2);
                audio.setSamplingRate(24000);

                EncodingAttributes attrs = new EncodingAttributes();
                attrs.setFormat("ogg");
                attrs.setAudioAttributes(audio);

                Encoder encoder = new Encoder();
                encoder.encode(new MultimediaObject(source), target, attrs);
                InputStream targetStream = FileUtils.openInputStream(target);

                log.info("Uploading audio to Yandex Cloud");
                String audioUrl = ycService.uploadFile(multiMediaFile.getId(), targetStream).toString();
                multiMediaFile.setInternalAudioUrl(audioUrl);
                log.info("Upload success. Audio url: {}", audioUrl);
                return multiMediaRepository.save(multiMediaFile);
            }
        } catch (IOException | EncoderException e) {
            log.error("Error while file processing", e);
            return multiMediaRepository.save(multiMediaFile.setStatus(StatusType.FAILED).setMessage(e.getMessage()));
        }
    }
}
