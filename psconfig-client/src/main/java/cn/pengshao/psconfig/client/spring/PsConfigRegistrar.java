package cn.pengshao.psconfig.client.spring;

import cn.pengshao.psconfig.client.value.SpringValueProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.Optional;

/**
 * Description: register ps config bean.
 *
 * @Author: yezp
 * @date 2024/5/3 8:50
 */
@Slf4j
public class PsConfigRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registerBean(registry, PropertySourcesProcessor.class);
        registerBean(registry, SpringValueProcessor.class);
    }

    private static void registerBean(BeanDefinitionRegistry registry, Class<?> clazz) {
        Optional<String> first = Arrays.stream(registry.getBeanDefinitionNames())
                .filter(x -> clazz.getName().equals(x)).findFirst();
        if (first.isPresent()) {
            log.info("class:{} already registered", clazz.getName());
            return;
        }

        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(clazz).getBeanDefinition();
        registry.registerBeanDefinition(clazz.getName(), beanDefinition);
    }
}
