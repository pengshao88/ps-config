package cn.pengshao.psconfig.client.respository;

import cn.pengshao.psconfig.client.config.ConfigMeta;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/5/5 22:11
 */
public interface PsRepository {

    static PsRepository getDefault(ConfigMeta meta) {
        return new PsRepositoryImpl(meta);
    }

    Map<String, String> getConfig();

    void addChangeListener(PsRepositoryChangeListener listener);

}
