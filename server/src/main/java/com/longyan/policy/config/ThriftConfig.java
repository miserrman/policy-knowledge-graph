package com.longyan.policy.config;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThriftConfig {

    @Value("${thrift.host}")
    private String host;

    @Value("${thrift.port}")
    private String port;

    private Integer TIMEOUT = 30000;

    @Bean
    public TTransport getTransport() {
        TTransport transport = new TSocket(host, Integer.valueOf(port), TIMEOUT);
        return transport;
    }

}
