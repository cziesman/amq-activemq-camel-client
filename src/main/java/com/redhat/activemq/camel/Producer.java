package com.redhat.activemq.camel;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    private static final Logger LOG = LoggerFactory.getLogger(Producer.class);

    @Produce("{{from.endpoint}}")
    private ProducerTemplate producer;

    public void sendMessage(String payload) {

        producer.sendBody(payload);
    }

}
