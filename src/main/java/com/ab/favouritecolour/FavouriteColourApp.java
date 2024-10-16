package com.ab.favouritecolour;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Arrays;
import java.util.Properties;

public class FavouriteColourApp {
    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "favourite-colour-app");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> favColourInput = builder.stream("favourite-colour-input");

        KStream<String, String> userAndColourStream = favColourInput
                .filter((key, value) -> value.contains(","))
                .selectKey((key, value) -> value.split(",")[0].toLowerCase())
                .filter((key, value) -> Arrays.asList("red", "green", "blue").contains(value));

        userAndColourStream.to("user-and-colours-temp-topic");

        KTable<String,String> userAndColourTable = builder.table("user-and-colours-temp-topic");

        KTable<String,Long> favouriteColours = userAndColourTable
                .groupBy((user, colour) -> new KeyValue<>(colour, colour))
                .count();

        favouriteColours.toStream().to("favourite-colour-output");

        KafkaStreams streams = new KafkaStreams(builder.build(), config);
        streams.cleanUp(); //only for dev
        streams.start();
        System.out.println(streams);

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}