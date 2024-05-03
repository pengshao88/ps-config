package cn.pengshao.psconfig.demo;

import cn.pengshao.psconfig.client.annotation.EnablePsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/5/3 9:05
 */
@EnablePsConfig
@SpringBootApplication
@EnableConfigurationProperties(PsDemoConfig.class)
public class PsDemoApplication {

    @Value("${ps.a}")
    private String a;

    @Autowired
    private PsDemoConfig psDemoConfig;

    @Autowired
    Environment environment;
    public static void main(String[] args) {
        SpringApplication.run(PsDemoApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner() {
        System.out.println(Arrays.toString(environment.getActiveProfiles()));
        return args -> {
            System.out.println(a);
            System.out.println(psDemoConfig.getA());
        };
    }

}
