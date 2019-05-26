package ru.nsu.fit.supernova.service.video;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class VideoConvertingService {

    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;

    private final YCService ycService;
    private final MultiMediaRepository multiMediaRepository;

    public MultiMediaFile convertFromUrl(String fileUrl) {
        Optional<MultiMediaFile> optionalMultiMediaFile = multiMediaRepository.findByExternalVideoUrl(fileUrl);
        try {
            if (optionalMultiMediaFile.isPresent()) {
                return optionalMultiMediaFile.get();
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

                MultiMediaFile multiMediaFile = multiMediaRepository.save(new MultiMediaFile()
                    .setExternalVideoUrl(fileUrl)
                    .setStatus(StatusType.CREATED));
                multiMediaFile.setInternalAudioUrl(ycService.uploadFile(multiMediaFile.getId(), targetStream).toString());
                return multiMediaRepository.save(multiMediaFile);
            }
        } catch (IOException | EncoderException e) {
            return multiMediaRepository.save(new MultiMediaFile()
                .setExternalVideoUrl(fileUrl)
                .setStatus(StatusType.FAILED)
                .setMessage(e.getMessage()));
        }
    }
}
