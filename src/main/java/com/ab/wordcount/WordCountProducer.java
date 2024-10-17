package com.ab.wordcount;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

import static com.ab.wordcount.Constants.*;

public class WordCountProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        producer.send(new ProducerRecord<>(WORD_COUNT_INPUT, "hello kafka streams"));
        producer.send(new ProducerRecord<>(WORD_COUNT_INPUT, "kafka streams is working"));
        producer.send(new ProducerRecord<>(WORD_COUNT_INPUT, "arpit is working on kafka streams"));

        producer.close();
    }
}