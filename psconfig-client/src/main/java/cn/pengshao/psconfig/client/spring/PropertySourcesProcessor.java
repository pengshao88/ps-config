package cn.pengshao.psconfig.client.spring;

import cn.pengshao.common.http.HttpInvoker;
import cn.pengshao.psconfig.client.config.ConfigMeta;
import cn.pengshao.psconfig.client.config.PsConfigService;
import com.alibaba.fastjson.TypeReference;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * ps config property source processor
 *
 * @Author: yezp
 * @date 2024/5/3 8:22
 */
@Data
public class PropertySourcesProcessor implements BeanFactoryPostProcessor, ApplicationContextAware, EnvironmentAware, PriorityOrdered {

    public static final String PS_CONFIG_PROPERTY_SOURCE_NAME = "PsPropertySource";

    private Environment environment;
    private ApplicationContext applicationContext;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
        if (env.getPropertySources().contains(PS_CONFIG_PROPERTY_SOURCE_NAME)) {
            return;
        }

        // 从config-server 获取配置
        String app = env.getProperty("psconfig.app", "psrpc");
        String configEnv = env.getProperty("psconfig.env", "dev");
        String version = env.getProperty("psconfig.version", "v1_0_0");
        String configServer = env.getProperty("psconfig.configServer", "http://localhost:9129");
        String listNsPath = configServer + "/listNs?app=" + app + "&env=" + configEnv + "&version=" + version;
        List<String> nsList = HttpInvoker.httpGet(listNsPath, new TypeReference<>() {
        });

        // 加载多个 property source
        CompositePropertySource compositePropertySource = new CompositePropertySource(PS_CONFIG_PROPERTY_SOURCE_NAME);
        for (String ns : nsList) {
            ConfigMeta configMeta = new ConfigMeta(app, configEnv, version, ns, configServer);
            PsConfigService psConfigService = PsConfigService.getDefault(applicationContext, configMeta);
            compositePropertySource.addPropertySource(new PsConfigPropertySource(ns, psConfigService));
        }
        env.getPropertySources().addFirst(compositePropertySource);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
