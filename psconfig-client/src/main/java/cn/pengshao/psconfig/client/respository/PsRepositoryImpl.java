package cn.pengshao.psconfig.client.respository;

import cn.pengshao.common.http.OkHttpInvoker;
import cn.pengshao.psconfig.client.config.ConfigMeta;
import cn.pengshao.psconfig.client.config.Configs;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/5/5 22:17
 */
@Slf4j
public class PsRepositoryImpl implements PsRepository {

    private final ConfigMeta meta;
    Map<String, Long> versionMap = new HashMap<>();
    Map<String, Map<String, String>> configMap = new HashMap<>();
    List<PsRepositoryChangeListener> listeners = new ArrayList<>();

    OkHttpInvoker httpInvoker;

    public PsRepositoryImpl(ConfigMeta meta) {
        this.meta = meta;
        httpInvoker = new OkHttpInvoker(12);
        init();

        Thread updateThread = new Thread(() -> {
            while (true) {
                heatBeat();
            }
        });
        updateThread.setDaemon(true);
        updateThread.start();
    }

    @Override
    public void addChangeListener(PsRepositoryChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public Map<String, String> getConfig() {
        String key = meta.genKey();
        if (configMap.containsKey(key)) {
            return configMap.get(key);
        }
        Map<String, String> config = findAll();
        configMap.put(key, config);
        return config;
    }

    private Map<String, String> findAll() {
        String listPath = meta.listPath();
        log.info("[PSCONFIG] list all configs from ps config server.");

        List<Configs> configs = httpInvoker.httpGet(listPath, new TypeReference<List<Configs>>() {
        });
        Map<String, String> resultMap = new HashMap<>();
        configs.forEach(c -> resultMap.put(c.getConfigKey(), c.getConfigValue()));
        return resultMap;
    }

    private void init() {
        String versionPath = meta.versionPath();
        Long version = httpInvoker.httpGet(versionPath, Long.class);
        String key = meta.genKey();
        log.info("[PSCONFIG] init get version key=" + key + " current=" + version);

        Map<String, String> config = findAll();
        log.info("[PSCONFIG] init get findAll key=" + key + " config.size=" + config.keySet().size());
        configMap.put(key, config);
        versionMap.put(key, version);
        listeners.forEach(listener -> listener.onChange(new PsRepositoryChangeListener.ChangeEvent(meta, config)));
    }

    private void heatBeat() {
        String key = meta.genKey();
        try {
            log.info("[PSCONFIG] key{} heatBeat ...", key);
            Long oldVersion = versionMap.getOrDefault(key, -1L);
            String versionPath = meta.pollVersionPath() + "&timestamp=" + oldVersion;
            Long version = httpInvoker.httpGet(versionPath, Long.class);

            log.info("[PSCONFIG] key=" + key + " current=" + version + ", old=" + oldVersion);
            if (version > oldVersion) {
                log.info("[PSCONFIG] key=" + key + " current=" + version + ", old=" + oldVersion + " need update new configs.");
                Map<String, String> config = findAll();
                configMap.put(key, config);
                versionMap.put(key, version);
                listeners.forEach(listener -> listener.onChange(new PsRepositoryChangeListener.ChangeEvent(meta, config)));
            }
        } catch (Exception e) {
            log.info("heatBeat key{} timeout ignore exception.", key);
        }

    }
}
