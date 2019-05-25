package ru.nsu.fit.supernova.configuration;

import com.amazonaws.services.s3.AmazonS3;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.nsu.fit.supernova.service.factory.S3ClientFactory;

@Configuration
@Getter
public class YCConfiguration {

    @Value("${yc.s3.access.key:#{null}}")
    private String accessKey;

    @Value("${yc.s3.secret.key:#{null}}")
    private String secretKey;

    @Value("${yc.s3.path:#{null}}")
    private String endpoint;

    @Bean
    public AmazonS3 amazonS3() {
        final String accessKeyValue = getAccessKey();
        final String secretKeyValue = getSecretKey();
        final String endpointValue = getEndpoint();

        return S3ClientFactory.create(accessKeyValue, secretKeyValue, endpointValue);
    }
}
