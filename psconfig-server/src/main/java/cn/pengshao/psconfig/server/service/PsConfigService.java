package cn.pengshao.psconfig.server.service;

import cn.pengshao.psconfig.server.dal.ConfigsMapper;
import cn.pengshao.psconfig.server.model.Configs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/4/29 23:11
 */
@Slf4j
@Service
public class PsConfigService {

    @Autowired
    ConfigsMapper configsMapper;
    Map<String, Long> VERSIONS = new HashMap<>();
    MultiValueMap<String, DeferredResult<Long>> POLL_VERSION = new LinkedMultiValueMap<>();

    public List<Configs> list(String app, String env, String version, String ns) {
        return configsMapper.list(app, env, version, ns);
    }

    public Configs select(String app, String env, String version, String ns, String configKey) {
        return configsMapper.select(app, env, version, ns, configKey);
    }

    public List<Configs> update(String app, String env, String version, String ns, Map<String, String> params) {
        String key = app + "-" + env + "-" + version + "-" + ns;
        Long value = System.currentTimeMillis();
        VERSIONS.put(key, value);
        params.forEach((k, v) -> insertOrUpdate(new Configs(app, env, version, ns, k, v)));

        touchDeferredResult(key, value);
        return configsMapper.list(app, env, version, ns);
    }

    private void touchDeferredResult(String nsKey, Long value) {
        log.info("touch {}:{}", nsKey, value);
        List<DeferredResult<Long>> deferredResults = POLL_VERSION.get(nsKey);
        if (deferredResults == null || deferredResults.isEmpty()) {
            log.warn("deferredResults is null {}:{}.", nsKey, value);
            return;
        }

        deferredResults.forEach(deferredResult -> {
            // 触发 pollVersion() 方法的 finally
            log.info("setResult {}:{}", nsKey, value);
            deferredResult.setResult(value);
        });
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

    public DeferredResult<Long> pollVersion(String app, String env, String version, String ns, long timestamp) {
        String key = app + "-" + env + "-" + version + "-" + ns;
        log.info("poll version {} in defer.", key);
        DeferredResult<Long> deferredResult = new DeferredResult<>();
        deferredResult.onCompletion(() -> {
            log.debug("{} onCompletion.", key);
            POLL_VERSION.remove(key);
        });

        deferredResult.onTimeout(() -> {
            log.debug("{} onTimeout.", key);
            POLL_VERSION.remove(key);
        });
        POLL_VERSION.add(key, deferredResult);
        log.debug("return defer for {}", key);

        long value = version(app, env, version, ns);
        if (timestamp != value) {
            // 避免更新时，刚好请求过期
            touchDeferredResult(key, value);
        }
        return deferredResult;
    }
}
