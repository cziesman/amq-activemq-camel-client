package com.redhat.activemq.camel;

import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class JmsConfig {

    private static final Logger LOG = LoggerFactory.getLogger(JmsConfig.class);

    @Value("${broker.scheme}")
    private String brokerScheme;

    @Value("${broker.host}")
    private String brokerHost;

    @Value("${broker.port}")
    private String brokerPort;

    @Value("${broker.username}")
    private String brokerUsername;

    @Value("${broker.password}")
    private String brokerPassword;

    @Value("${broker.maxConnections}")
    private Integer brokerMaxConnections;

    @Value("${trustStorePath}")
    private String trustStorePath;

    @Value("${trustStorePassword}")
    private String trustStorePassword;

    @Value("${verifyHostName}")
    private String verifyHostName;

    @Bean
    public JmsComponent jmsComponent(CachingConnectionFactory cachingConnectionFactory) {

        JmsComponent jms = new JmsComponent();
        jms.setConnectionFactory(cachingConnectionFactory);

        return jms;
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() throws Exception {

        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setTargetConnectionFactory(connectionFactory());
        cachingConnectionFactory.afterPropertiesSet();

        return cachingConnectionFactory;
    }

    protected ActiveMQSslConnectionFactory connectionFactory() throws Exception {

        ActiveMQSslConnectionFactory factory = new ActiveMQSslConnectionFactory();
        factory.setBrokerURL(remoteUri());
        factory.setTrustStorePassword(trustStorePassword);
        factory.setTrustStore(trustStorePath);
        factory.setUserName(brokerUsername);
        factory.setPassword(brokerPassword);

        return factory;
    }

    private String remoteUri() {

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(brokerScheme)
                .host(brokerHost)
                .port(brokerPort)
                .queryParam("verifyHostName", verifyHostName)
                .build();

        LOG.debug(uriComponents.toUriString());

        return String.format("failover:(%s)", uriComponents.toUriString());
    }
}
