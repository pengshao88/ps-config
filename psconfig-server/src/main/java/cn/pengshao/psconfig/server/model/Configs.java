package cn.pengshao.psconfig.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/4/29 23:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configs {

    private String app;
    private String env;
    private String version;
    private String ns;
    private String configKey;
    private String configValue;

}
