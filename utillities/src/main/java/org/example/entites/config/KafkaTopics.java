    package org.example.entites.config;

    public enum KafkaTopics {

        USER_TOPIC("user-event");

        final String topic;

        KafkaTopics(String topic) {
            this.topic = topic;
        }

        public String getTopic() {
            return topic;
        }
    }
