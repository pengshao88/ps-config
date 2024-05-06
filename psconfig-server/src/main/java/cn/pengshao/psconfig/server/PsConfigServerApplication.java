package cn.pengshao.psconfig.server;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Description:config center server
 *
 * @Author: yezp
 * @date 2024/4/29 23:08
 */
@SpringBootApplication
public class PsConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PsConfigServerApplication.class, args);
    }

//    @Bean
//    ApplicationRunner applicationRunner() {
//
//    }

}
