package ru.nsu.fit.supernova.service.factory;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.retry.PredefinedBackoffStrategies.ExponentialBackoffStrategy;
import com.amazonaws.retry.PredefinedRetryPolicies;
import com.amazonaws.retry.RetryPolicy;
import com.amazonaws.retry.RetryPolicy.BackoffStrategy;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class S3ClientFactory {
    private static final Regions DEFAULT_REGION = Regions.US_EAST_1;
    private static final boolean HONOR_MAX_ERROR_RETRY_IN_CLIENT_CONFIG = false;
    private static final int BACK_OFF_BASE_DELAY = 100;
    private static final int BACK_OFF_MAX_TIME = Integer.MAX_VALUE;


    private S3ClientFactory() {
        throw new UnsupportedOperationException();
    }

    public static AmazonS3 create(final String accessKey, final String secretKey, final String endpoint) {
        final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        final AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
        final EndpointConfiguration endpointConfig = new EndpointConfiguration(endpoint, DEFAULT_REGION.getName());
        final ClientConfiguration clientConfiguration = createClientConfiguration();

        return AmazonS3ClientBuilder
            .standard()
            .withClientConfiguration(clientConfiguration)
            .withDualstackEnabled(false)
            .withCredentials(credentialsProvider)
            .withEndpointConfiguration(endpointConfig)
            .build();
    }


    private static ClientConfiguration createClientConfiguration() {
        final ClientConfiguration clientConfiguration = new ClientConfiguration();
        final RetryPolicy retryPolicy = createRetryPolicy();

        clientConfiguration.setRetryPolicy(retryPolicy);

        return clientConfiguration;
    }

    private static RetryPolicy createRetryPolicy() {
        final BackoffStrategy backoffStrategy = createBackOffStrategy();

        return new RetryPolicy(
            PredefinedRetryPolicies.DEFAULT_RETRY_CONDITION,
            backoffStrategy,
            PredefinedRetryPolicies.DEFAULT_MAX_ERROR_RETRY,
            HONOR_MAX_ERROR_RETRY_IN_CLIENT_CONFIG
        );
    }

    private static ExponentialBackoffStrategy createBackOffStrategy() {
        return new ExponentialBackoffStrategy(BACK_OFF_BASE_DELAY, BACK_OFF_MAX_TIME);
    }
}
