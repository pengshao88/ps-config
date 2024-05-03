package cn.pengshao.psconfig.client.spring;

import cn.pengshao.psconfig.client.config.PsConfigService;
import org.springframework.core.env.EnumerablePropertySource;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/5/3 8:30
 */
public class PsConfigPropertySource extends EnumerablePropertySource<PsConfigService> {

    public PsConfigPropertySource(String name, PsConfigService source) {
        super(name, source);
    }

    @Override
    public String[] getPropertyNames() {
        return source.getPropertyNames();
    }

    @Override
    public Object getProperty(String name) {
        return source.getProperty(name);
    }
}
