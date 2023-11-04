package com.tbfp.teamplannerbe.config.dynamodb;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

@Configuration
@EnableDynamoDBRepositories(basePackages = {"com.tbfp.teamplannerbe.domain.chat.repository","com.tbfp.teamplannerbe.domain.notification.repository"}) // 패키지 경로를 수정
public class DynamoDBConfig {

    @Value("${amazon.aws.endpoint}")
    private String dynamodbEndpoint;

    @Value("${amazon.aws.region}")
    private String awsRegion;

    @Value("${amazon.aws.accessKey}")
    private String dynamodbAccessKey;

    @Value("${amazon.aws.secretKey}")
    private String dynamodbSecretKey;
//
//
//    @Bean
//    public DynamoDBMapper dynamoDBMapper() {
//        return new DynamoDBMapper(buildAmazonDynamoDB());
//    }
//
    private AmazonDynamoDB buildAmazonDynamoDB() {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(dynamodbEndpoint,awsRegion))
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(dynamodbAccessKey,dynamodbSecretKey)))
                .build();
    }

    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(dynamodbAccessKey, dynamodbSecretKey);
    }

    public AWSCredentialsProvider amazonAWSCredentialsProvider() {
        return new AWSStaticCredentialsProvider(amazonAWSCredentials());
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard().withCredentials(amazonAWSCredentialsProvider())
                .withRegion(awsRegion).build();
    }


    /**
     * LocalDateTime converter
     */
    public static class LocalDateTimeConverter implements DynamoDBTypeConverter<Date, LocalDateTime> {

        private static final ZoneId KST_ZONE = ZoneId.of("Asia/Seoul");
        @Override
        public  Date convert(LocalDateTime source) {
            return Date.from(source.atZone(KST_ZONE).toInstant());
        }

        @Override
        public LocalDateTime unconvert(Date source) {
            return source.toInstant().atZone(TimeZone.getDefault().toZoneId()).toLocalDateTime();
        }
    }
}