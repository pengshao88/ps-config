package cn.pengshao.psconfig.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/5/2 23:04
 */
@Data
@ConfigurationProperties(prefix = "ps")
public class PsDemoConfig {

    private String a;
    private String b;
    private String c;

}
