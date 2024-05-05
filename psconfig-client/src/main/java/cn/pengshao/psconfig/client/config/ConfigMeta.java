package cn.pengshao.psconfig.client.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: config meta
 *
 * @Author: yezp
 * @date 2024/5/5 21:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigMeta {

    String app;
    String env;
    String version;
    String ns;
    String configServer;

    public String genKey() {
        return app + "-" + env + "-" + version + "-" + ns;
    }

    public String listPath() {
        return path("list");
    }

    public String versionPath() {
        return path("version");
    }

    public String path(String context) {
        return this.configServer + "/" + context + "?app="
                + app + "&env=" + env + "&version=" + version + "&ns=" + ns;
    }

}
