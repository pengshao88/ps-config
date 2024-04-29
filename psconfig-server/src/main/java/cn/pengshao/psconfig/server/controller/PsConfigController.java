package cn.pengshao.psconfig.server.controller;

import cn.pengshao.psconfig.server.model.Configs;
import cn.pengshao.psconfig.server.service.PsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/4/29 23:12
 */
@RestController
public class PsConfigController {

    @Autowired
    PsConfigService psConfigService;

    @GetMapping("/list")
    public List<Configs> list(@RequestParam("app") String app,
                              @RequestParam("env") String env,
                              @RequestParam("version") String version,
                              @RequestParam("ns") String ns) {
        return psConfigService.list(app, env, version, ns);
    }

    @RequestMapping("/update")
    public List<Configs> update(@RequestParam("app") String app,
                       @RequestParam("env") String env,
                       @RequestParam("version") String version,
                       @RequestParam("ns") String ns,
                       @RequestBody Map<String, String> params) {
        return psConfigService.update(app, env, version, ns, params);
    }

    @GetMapping("/version")
    public long version(@RequestParam("app") String app,
                        @RequestParam("env") String env,
                        @RequestParam("version") String version,
                        @RequestParam("ns") String ns) {
        return psConfigService.version(app, env, version, ns);
    }
}
