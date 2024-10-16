#!/bin/bash

# create input topic with two partitions
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 2 --topic word-count-input

# create output topic
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 2 --topic word-count-output

# list all topics that we have in Kafka (so we can observe the internal topics)
bin/kafka-topics.sh --bootstrap-server localhost:9092 --list

# launch Kafka consumer
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
    --topic word-count-output \
    --from-beginning \
    --formatter kafka.tools.DefaultMessageFormatter \
    --property print.key=true \
    --property print.value=true \
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
    --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer

# launch the streams application from intellij

# launch Kafka producer
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic word-count-input

# Produce data in the producer terminal
hello kafka streams
kafka streams is working
arpit is working on kafka streams

# package your application as a fat jar
mvn clean package

# run your fat jar
java -jar word-count-app.jar
