package ru.nsu.fit.supernova.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.supernova.model.Video;
import ru.nsu.fit.supernova.repository.VideoRepository;
import ru.nsu.fit.supernova.service.yc.YCService;
import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoConvertingService {

    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;

    private final YCService ycService;
    private final VideoRepository videoRepository;

    public URL convertFromUrl(String fileUrl) {
        Optional<Video> optionalVideo = videoRepository.findByUrl(fileUrl);

        try {
            if (optionalVideo.isPresent()) {
                return new URL("https://storage.yandexcloud.net/polyhack/" + optionalVideo.get().getId() + ".ogg");
            } else {
                File source = File.createTempFile("input", ".tmp");

                FileUtils.copyURLToFile(
                    new URL(fileUrl),
                    source,
                    CONNECT_TIMEOUT,
                    READ_TIMEOUT);

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

                Video video = videoRepository.save(new Video().setUrl(fileUrl));
                return ycService.uploadFile(video.getId(), targetStream);
            }
        } catch (IOException | EncoderException e) {
            throw new RuntimeException(e);
        }
    }
}
