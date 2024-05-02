package cn.pengshao.psconfig.client;

import java.lang.annotation.*;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/5/2 22:53
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface EnablePsConfig {
}
