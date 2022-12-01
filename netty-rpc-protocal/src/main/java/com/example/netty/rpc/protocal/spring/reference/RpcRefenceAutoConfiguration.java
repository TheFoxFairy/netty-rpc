package com.example.netty.rpc.protocal.spring.reference;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class RpcRefenceAutoConfiguration  implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public SpringRpcReferencePostProcessor postProcessor(){
        RpcClientProperties rpcClientProperties = new RpcClientProperties();
        rpcClientProperties.setServicePort(Integer.parseInt(this.environment.getProperty("client.servicePort")));
        rpcClientProperties.setServiceAddress(this.environment.getProperty("client.serviceAddress"));
        return new SpringRpcReferencePostProcessor(rpcClientProperties);
    }
}
