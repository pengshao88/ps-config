package cn.pengshao.psconfig.client.config;

import java.util.Map;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/5/3 8:37
 */
public class PsConfigServiceImpl implements PsConfigService {

    Map<String, String> configs;

    public PsConfigServiceImpl(Map<String, String> configs) {
        this.configs = configs;
    }

    @Override
    public String[] getPropertyNames() {
        return configs.keySet().toArray(new String[0]);
    }

    @Override
    public String getProperty(String name) {
        return configs.get(name);
    }
}
