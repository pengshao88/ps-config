package cn.pengshao.psconfig.client.spring;

import cn.pengshao.psconfig.client.config.PsConfigService;
import cn.pengshao.psconfig.client.config.PsConfigServiceImpl;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * ps config property source processor
 *
 * @Author: yezp
 * @date 2024/5/3 8:22
 */
@Data
public class PropertySourcesProcessor implements BeanFactoryPostProcessor, EnvironmentAware, PriorityOrdered {

    public static final String PS_CONFIG_PROPERTY_SOURCE_NAME = "PsPropertySource";

    private Environment environment;
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
        if (env.getPropertySources().contains(PS_CONFIG_PROPERTY_SOURCE_NAME)) {
            return;
        }

        // 获取配置 TODO from config-server
        Map<String, String> configs = new HashMap<>(3);
        configs.put("ps.a", "a300");
        configs.put("ps.b", "b400");
        configs.put("ps.c", "c500");
        PsConfigService psConfigService = new PsConfigServiceImpl(configs);
        CompositePropertySource compositePropertySource = new CompositePropertySource(PS_CONFIG_PROPERTY_SOURCE_NAME);
        // TODO 替换成 keyName = ns
        compositePropertySource.addPropertySource(new PsConfigPropertySource("mock_config", psConfigService));
        env.getPropertySources().addFirst(compositePropertySource);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
