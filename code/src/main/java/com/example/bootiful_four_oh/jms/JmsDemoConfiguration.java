package com.example.bootiful_four_oh.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsClient;

@Configuration
class JmsDemoConfiguration implements ApplicationRunner {

    static final String Q = "messages";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final JmsClient jms;

    JmsDemoConfiguration(JmsClient jms) {
        this.jms = jms;
    }

    @JmsListener(destination = Q)
    void receiveMessage(String message) {
        this.log.debug("got the message: {}", message);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.jms.destination(Q).send("see ya later");
    }
}
