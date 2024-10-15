#!/bin/bash


#Apply the kafka cluster configuration to your Kubernetes cluster:
#This will create a Kafka cluster named my-cluster with one Kafka broker replica and one Zookeeper replica.
kubectl apply -f /Users/arbhard2/IdeaProjects/personal/kafka-stream-app/src/main/resources/kafka_cluster_run/kafka-cluster.yaml -n kafka

#To verify that the Kafka cluster is successfully deployed, you can check the status of the Kafka resources:
kubectl get kafka -n kafka

#Check the status of the Kafka and Zookeeper pods
kubectl get pods -n kafka