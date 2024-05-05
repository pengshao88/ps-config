package cn.pengshao.psconfig.client.config;

import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/5/3 8:37
 */
public class PsConfigServiceImpl implements PsConfigService {

    Map<String, String> config;
    ApplicationContext applicationContext;

    public PsConfigServiceImpl(ApplicationContext applicationContext, Map<String, String> config) {
        this.applicationContext = applicationContext;
        this.config = config;
    }

    @Override
    public String[] getPropertyNames() {
        return config.keySet().toArray(new String[0]);
    }

    @Override
    public String getProperty(String name) {
        return config.get(name);
    }

    @Override
    public void onChange(ChangeEvent event) {
        // 修改数据源 PsConfigPropertySource
        this.config = event.config();
        if (!config.isEmpty()) {
            // 发布事件，修改 ConfigurationProperties 配置
            System.out.println("[PSCONFIG] fire an EnvironmentChangeEvent with keys:" + config.keySet());
            applicationContext.publishEvent(new EnvironmentChangeEvent(config.keySet()));
        }
    }
}
