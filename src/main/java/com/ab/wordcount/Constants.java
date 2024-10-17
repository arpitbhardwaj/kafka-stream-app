package com.ab.wordcount;

public class Constants {

    //for port forwarding method
    //public static final String BOOTSTRAP_SERVERS = "localhost:9092";
    //for nodeport method
    public static final String BOOTSTRAP_SERVERS = "192.168.49.2:30092";

    public static final String WORD_COUNT_INPUT = "word-count-input";
    public static final String WORD_COUNT_OUTPUT = "word-count-output";
    public static final String WORD_COUNT_APP_ID = "word-count-app";
    public static final String WORD_COUNT_CONSUMER_GROUP = "word-count-consumer-group";
}
