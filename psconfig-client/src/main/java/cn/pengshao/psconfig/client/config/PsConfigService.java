package cn.pengshao.psconfig.client.config;

import cn.pengshao.psconfig.client.respository.PsRepository;
import cn.pengshao.psconfig.client.respository.PsRepositoryChangeListener;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/5/3 8:32
 */
public interface PsConfigService extends PsRepositoryChangeListener {

    static PsConfigService getDefault(ApplicationContext applicationContext, ConfigMeta configMeta) {
        PsRepository repository = PsRepository.getDefault(configMeta);
        // 通过 repository 获取配置
        Map<String, String> config = repository.getConfig();
        PsConfigServiceImpl configService = new PsConfigServiceImpl(applicationContext, config);
        repository.addChangeListener(configService);
        return configService;
    }

    String[] getPropertyNames();

    String getProperty(String name);

}
