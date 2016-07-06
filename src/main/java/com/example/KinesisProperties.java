package com.example;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kinesis")
class KinesisProperties {
    private String region;
    private String stream;
    private String client;

    public String getCamelUrl() {
        return "aws-kinesis://" + stream + "?amazonKinesisClient=#" + client;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(final String stream) {
        this.stream = stream;
    }

    public String getClient() {
        return client;
    }

    public void setClient(final String client) {
        this.client = client;
    }
}
