package cn.pengshao.psconfig.server.service;

import cn.pengshao.psconfig.server.dal.ConfigsMapper;
import cn.pengshao.psconfig.server.model.Configs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/4/29 23:11
 */
@Service
public class PsConfigService {

    @Autowired
    ConfigsMapper configsMapper;
    // TODO 干啥用？
    Map<String, Long> VERSIONS = new HashMap<>();

    public List<Configs> list(String app, String env, String version, String ns) {
        return configsMapper.list(app, env, version, ns);
    }

    public Configs select(String app, String env, String version, String ns, String configKey) {
        return configsMapper.select(app, env, version, ns, configKey);
    }

    public List<Configs> update(String app, String env, String version, String ns, Map<String, String> params) {
        VERSIONS.put(app + "-" + env + "-" + version + "-" + ns, System.currentTimeMillis());
        params.forEach((k, v) -> insertOrUpdate(new Configs(app, env, version, ns, k, v)));
        return configsMapper.list(app, env, version, ns);
    }

    private void insertOrUpdate(Configs configs) {
        Configs conf = configsMapper.select(configs.getApp(), configs.getEnv(), configs.getVersion(),
                configs.getNs(), configs.getConfigKey());
        if (conf == null) {
            configsMapper.insert(configs);
        } else {
            configsMapper.update(configs);
        }
    }

    public long version(String app, String env, String version, String ns) {
        return VERSIONS.getOrDefault(app + "-" + env + "-" + version + "-" + ns, -1L);
    }

    public List<String> listNs(String app, String env, String version) {
        return configsMapper.listNs(app, env, version);
    }
}
