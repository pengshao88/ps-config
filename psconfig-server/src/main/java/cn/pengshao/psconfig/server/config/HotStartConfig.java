package cn.pengshao.psconfig.server.config;

import cn.pengshao.common.http.HttpInvoker;
import cn.pengshao.common.http.OkHttpInvoker;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Description: TODO 改成请求 /hello 接口
 *
 * @Author: yezp
 * @date 2024/5/7 23:12
 */
@Configuration
public class HotStartConfig {

    @Value("${psconfig.app:psrpc}")
    private String app;
    @Value("${psconfig.env:dev}")
    private String env;
    @Value("${psconfig.version:v1_0_0}")
    private String version;
    @Value("${server.port}")
    private int serverPort;

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            String configServer = "http://localhost:" + serverPort;
            String listNsPath = configServer + "/listNs?app=" + app + "&env=" + env + "&version=" + version;
            List<String> nsList = HttpInvoker.httpGet(listNsPath, new TypeReference<>() {
            });
            System.out.println("application run start nsList:" + nsList);
        };
    }

}
