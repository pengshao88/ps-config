package cn.pengshao.psconfig.server.config;

import cn.pengshao.common.http.OkHttpInvoker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: TODO 改成请求 /hello 接口
 *
 * @Author: yezp
 * @date 2024/5/7 23:12
 */
@Slf4j
@Configuration
public class HotStartConfig {

    @Value("${server.port}")
    private int serverPort;

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            String configServer = "http://localhost:" + serverPort;
            String hostStartPath = configServer + "/hotStart";
            OkHttpInvoker okHttpInvoker = new OkHttpInvoker(1000);
            String response = okHttpInvoker.get(hostStartPath);
            log.warn("application run hot start:" + response);
        };
    }

}
