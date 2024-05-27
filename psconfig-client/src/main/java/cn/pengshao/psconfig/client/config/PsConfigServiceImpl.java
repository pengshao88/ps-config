package cn.pengshao.psconfig.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/5/3 8:37
 */
@Slf4j
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
        Set<String> keys = calcChangedKeys(this.config, event.config());
        if (keys.isEmpty()) {
            log.info("[PSCONFIG] calcChangedKeys return empty, ignore update.");
            return;
        }

        // 修改数据源 PsConfigPropertySource
        // ConfigurationPropertiesRebinder spring cloud 自动刷新
        this.config = event.config();
        // 发布事件，修改 ConfigurationProperties/@Value 配置
        System.out.println("[PSCONFIG] fire an EnvironmentChangeEvent with keys:" + keys);
        applicationContext.publishEvent(new EnvironmentChangeEvent(keys));
    }

    /**
     * 找出有变化的 keys
     * TODO 测试下 返回配置为空时
     *
     * @param oldConfigs 旧配置
     * @param newConfigs 新配置
     * @return keys
     */
    private Set<String> calcChangedKeys(Map<String, String> oldConfigs, Map<String, String> newConfigs) {
        if (oldConfigs.isEmpty()) {
            return newConfigs.keySet();
        }
        if (newConfigs.isEmpty()) {
            return oldConfigs.keySet();
        }

        Set<String> changedKeys = newConfigs.keySet().stream()
                .filter(key -> !newConfigs.get(key).equals(oldConfigs.get(key))).collect(Collectors.toSet());
        oldConfigs.keySet().stream().filter(key -> !newConfigs.containsKey(key)).forEach(changedKeys::add);
        return changedKeys;
    }
}
