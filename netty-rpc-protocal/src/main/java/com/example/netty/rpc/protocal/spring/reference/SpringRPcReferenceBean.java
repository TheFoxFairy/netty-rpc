package com.example.netty.rpc.protocal.spring.reference;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class SpringRPcReferenceBean implements FactoryBean<Object> {

    private String serviceAddress;

    private int servicePort;

    private Class<?> interfaceClass;

    @Override
    public Object getObject() throws Exception {
        /*
         * loader：一个classloader对象，定义了由哪个classloader对象对生成的代理类进行加载
         * interfaces：一个interface对象数组，表示我们将要给我们的代理对象提供一组什么样的接口，如果我们提供了这样一个接口对象数组，那么也就是声明了代理类实现了这些接口，代理类就可以调用接口中声明的所有方法。
         * invocationHandler：一个InvocationHandler对象，表示的是当动态代理对象调用方法的时候会关联到哪一个InvocationHandler对象上，并最终由其调用。
         */
       return Proxy.newProxyInstance(interfaceClass.getClassLoader(),new Class<?>[]{interfaceClass},new RpcInvokerProxy(serviceAddress,servicePort));

    }

    @Override
    public Class<?> getObjectType() {
        return this.interfaceClass;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }
}
