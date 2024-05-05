package cn.pengshao.psconfig.client.respository;

import cn.pengshao.psconfig.client.config.ConfigMeta;
import cn.pengshao.psconfig.client.config.Configs;
import cn.pengshao.psconfig.client.utils.HttpInvoker;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/5/5 22:17
 */
public class PsRepositoryImpl implements PsRepository {

    private ConfigMeta meta;
    Map<String, Long> versionMap = new HashMap<>();
    Map<String, Map<String, String>> configMap = new HashMap<>();
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    List<PsRepositoryChangeListener> listeners = new ArrayList<>();

    public PsRepositoryImpl(ConfigMeta meta) {
        this.meta = meta;
        executor.scheduleWithFixedDelay(this::heatBeat, 1000, 5000, TimeUnit.MILLISECONDS);
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
        System.out.println("[PSCONFIG] list all configs from ps config server.");

        List<Configs> configs = HttpInvoker.httpGet(listPath, new TypeReference<List<Configs>>() {
        });
        Map<String, String> resultMap = new HashMap<>();
        configs.forEach(c -> resultMap.put(c.getConfigKey(), c.getConfigValue()));
        return resultMap;
    }

    private void heatBeat() {
        String versionPath = meta.versionPath();
        Long version = HttpInvoker.httpGet(versionPath, Long.class);
        String key = meta.genKey();
        Long oldVersion = versionMap.getOrDefault(key, -1L);
        System.out.println("[PSCONFIG] current=" + version + ", old=" + oldVersion);
        if (version > oldVersion) {
            System.out.println("[PSCONFIG] current=" + version + ", old=" + oldVersion + " need update new configs.");
            Map<String, String> config = findAll();
            configMap.put(key, config);
            versionMap.put(key, version);
            listeners.forEach(listener -> listener.onChange(new PsRepositoryChangeListener.ChangeEvent(meta, config)));
        }
    }
}
