package cn.pengshao.psconfig.client.config;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/5/3 8:32
 */
public interface PsConfigService {

    String[] getPropertyNames();

    String getProperty(String name);

}
