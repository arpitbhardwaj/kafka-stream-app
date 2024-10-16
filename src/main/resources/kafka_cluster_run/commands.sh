#!/bin/bash

#Setting Up Kafka on Minikube K8s Using Strimzi

#Start minikube
minikube ip

#create a namespace called kafka
kubectl create namespace kafka

# you can check list of namespace on your cluster using below comand
kubectl get ns

# you can switch the ns to default as kafka
kubectl config set-context --current --namespace=kafka

#download and create the strimzi operator which will be used further to create kafka cluster
kubectl create -f 'https://strimzi.io/install/latest?namespace=kafka' -n kafka

#minikube dashboard
minikube dashboard

#get minikube ip
minikube ip

#Apply the kafka cluster configuration to your k8s cluster (Add minikube ip in advertisedHost)
kubectl apply -f /Users/arbhard2/IdeaProjects/personal/kafka-stream-app/src/main/resources/kafka_cluster_run/simple-kafka-cluster-with-nodeport.yaml -n kafka
#if only zookeeper pods comes then change the kafka version in yaml and reapply the yaml



#Deploy Kafka Streams Word Count Application

#Build Jar
mvn clean package

#Build the Docker image (Dockerfile should be present in the project)
docker build -t arpitb/word-count-app:latest .

#Push the image to a Docker registry (e.g. Docker Hub)
docker push arpitb/word-count-app:latest

#Apply the topic configuration:
kubectl apply -f /Users/arbhard2/IdeaProjects/personal/kafka-stream-app/src/main/resources/kafka_cluster_run/word-count-input-topic.yaml
kubectl apply -f /Users/arbhard2/IdeaProjects/personal/kafka-stream-app/src/main/resources/kafka_cluster_run/word-count-output-topic.yaml

#Apply the kafka stream app deployment
kubectl apply -f /Users/arbhard2/IdeaProjects/personal/kafka-stream-app/src/main/resources/kafka_cluster_run/word-count-app-deployment.yaml

#Method 1 (Use Node Port)
#Apply the Kafka external service configuration
kubectl apply -f /Users/arbhard2/IdeaProjects/personal/kafka-stream-app/src/main/resources/kafka_cluster_run/kafka-external-service.yaml

#Launch Consumer
bin/kafka-console-consumer.sh --bootstrap-server 192.168.49.2:30092 --topic output-topic --from-beginning

#Launch Producer
bin/kafka-console-producer.sh --broker-list 192.168.49.2:30092 --topic word-count-input

#Method 2 (Use Port Forwarding)
#Expose Kafka cluster outside of Minikube to local machine
kubectl port-forward svc/ab-kafka-cluster-kafka-bootstrap 9092:9092 -n kafka

#Launch Consumer
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic output-topic --from-beginning

#Launch Producer
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic word-count-input

#Produce data in the producer terminal
hello kafka streams
kafka streams is working
arpit is working on kafka streams

#Common Commands

#To list Kafka CRD resources in the kafka namespace.
kubectl get kafka -n kafka

#To list all pods in the kafka namespace.
kubectl get po -n kafka

#To list all services in the kafka namespace
kubectl get svc -n kafka



#Debugging Commands

# output pod logs
kubectl logs -f kafka-streams-app-ddc9bc55f-spsr4

#restart the Kafka Streams application
kubectl rollout restart deployment/word-count-app

#test DNS resolution for the Kafka service:
kubectl run -it --rm --image=busybox dns-test -- nslookup ab-kafka-cluster-kafka-brokers.kafka.svc.cluster.local
#output
Name: ab-kafka-cluster-kafka-brokers.kafka.svc.cluster.local
Address: 10.244.0.8

#test DNS resolution for a specific Kafka broker pod
kubectl run -it --rm --image=busybox dns-test -- nslookup ab-kafka-cluster-kafka-0.ab-kafka-cluster-kafka-brokers.kafka.svc
#output
** server can't find ab-kafka-cluster-kafka-0.ab-kafka-cluster-kafka-brokers.kafka.svc: NXDOMAIN