package ru.nsu.fit.supernova.service.yc;

import java.io.InputStream;
import java.net.URL;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class YCServiceImpl implements YCService {

    private final AmazonS3 client;

    @Override
    public URL uploadFile(long fileId,
                          InputStream fileStream) {
        final String bucketName = "polyhack";

        client.putObject(bucketName, fileId + ".ogg", fileStream, new ObjectMetadata());
        return client.getUrl(bucketName, fileId + ".ogg");
    }
}

