package cn.pengshao.psconfig.demo.controller;

import cn.pengshao.psconfig.demo.config.PsDemoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/5/5 22:44
 */
@RestController
public class PsDemoController {

    @Value("${ps.a}.${ps.c}")
    private String a;

    @Value("${ps.b}")
    private String b;

    @Autowired
    private PsDemoConfig psDemoConfig;

    @GetMapping("/demo")
    public String demo() {
        return "ps.a = " + a + "\n"
                + "ps.b = " + b + "\n"
                + "demo.a = " + psDemoConfig.getA() + "\n"
                + "demo.b = " + psDemoConfig.getB() + "\n";
    }

}
