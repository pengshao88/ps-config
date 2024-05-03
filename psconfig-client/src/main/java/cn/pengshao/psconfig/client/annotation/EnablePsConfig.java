package cn.pengshao.psconfig.client.annotation;

import cn.pengshao.psconfig.client.spring.PsConfigRegistrar;
import org.springframework.context.annotation.Import;

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
@Import(PsConfigRegistrar.class)
public @interface EnablePsConfig {
}
