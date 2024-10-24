package com.ab.wordcount;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Arrays;
import java.util.Properties;

import static com.ab.wordcount.Constants.*;

public class WordCountApp {
    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, WORD_COUNT_APP_ID);
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "ab-kafka-cluster-kafka-bootstrap:9092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> wordCountInput = builder.stream(WORD_COUNT_INPUT);

        KTable<String, Long> wordCounts = wordCountInput
                .mapValues(value -> value.toLowerCase())
                .flatMapValues(value -> Arrays.asList(value.split(" ")))
                .selectKey((key, value) -> value)
                .groupByKey()
                .count();

        wordCounts.toStream().to(WORD_COUNT_OUTPUT);

        KafkaStreams streams = new KafkaStreams(builder.build(), config);
        streams.start();
        System.out.println(streams);

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}