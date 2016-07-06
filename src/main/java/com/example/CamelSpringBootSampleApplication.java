package com.example;

import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClient;

import org.apache.camel.spring.boot.FatJarRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(KinesisProperties.class)
public class CamelSpringBootSampleApplication extends FatJarRouter {

    @Autowired
    private KinesisProperties kinesisProperties;

    public static void main(String[] args) {
        SpringApplication.run(CamelSpringBootSampleApplication.class, args);
    }

    @Bean
    public AmazonKinesis eventsClient() {
        AmazonKinesisClient client = new AmazonKinesisClient();
        client.setRegion(RegionUtils.getRegion(kinesisProperties.getRegion()));
        return client;
    }

    @Override
    public void configure() throws Exception {
        from(kinesisProperties.getCamelUrl())
                .bean("transformer", "transform")
                .to("log:out");
    }
}

