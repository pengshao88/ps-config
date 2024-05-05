package cn.pengshao.psconfig.client.respository;

import cn.pengshao.psconfig.client.config.ConfigMeta;

import java.util.Map;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/5/5 22:07
 */
public interface PsRepositoryChangeListener {

    void onChange(ChangeEvent event);

    record ChangeEvent(ConfigMeta configMeta, Map<String, String> config) {
    }

}
