package com.example.netty.rpc.protocal.spring.reference;

import com.example.netty.rpc.protocal.annotation.MyRemoteReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpringRpcReferencePostProcessor implements BeanFactoryPostProcessor, ApplicationContextAware, BeanClassLoaderAware {

    private ClassLoader classLoader;

    private ApplicationContext applicationContext;

    private RpcClientProperties rpcClientProperties;

    private final Map<String,BeanDefinition> rpcBeanDefinitionMap = new ConcurrentHashMap<>();

    public SpringRpcReferencePostProcessor(RpcClientProperties rpcClientProperties) {

        this.rpcClientProperties = rpcClientProperties;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 获取所有bean的名称循环
        for(String beanName : beanFactory.getBeanDefinitionNames()){
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            String beanClassName = beanDefinition.getBeanClassName();
            if(beanClassName!=null){
                // 如果加载的类中有属性加了MyRemoteReference注解的话，就生成代理类
                Class<?> clazz = ClassUtils.resolveClassName(beanClassName, this.classLoader);

                // 遍历Clazz中的对象中的所有的属性，把class中的属性作为参数传递给parseRpcReference方法
                ReflectionUtils.doWithFields(clazz,this::parseRpcReference);
            }

        }
        // 注入到容器里面
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry)beanFactory;
        this.rpcBeanDefinitionMap.forEach((beaName,beanDefinition) -> {
            if(applicationContext.containsBean(beaName)){
                return;
            }
            registry.registerBeanDefinition(beaName,beanDefinition);
        });
    }

    private void parseRpcReference(Field field) {
        MyRemoteReference myRemoteReference = AnnotationUtils.getAnnotation(field, MyRemoteReference.class);
        if(myRemoteReference !=null){
            // 如果有这个注解，就要生成代理类
            BeanDefinitionBuilder beanDefinitionBuilder =
                    BeanDefinitionBuilder.genericBeanDefinition(SpringRPcReferenceBean.class);
            beanDefinitionBuilder.addPropertyValue("interfaceClass",field.getType());
            beanDefinitionBuilder.addPropertyValue("serviceAddress",rpcClientProperties.getServiceAddress());
            beanDefinitionBuilder.addPropertyValue("servicePort",rpcClientProperties.getServicePort());
            BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
            rpcBeanDefinitionMap.put(field.getName(),beanDefinition);
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
