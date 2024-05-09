package cn.pengshao.psconfig.client.value;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * Description: spring value
 * store @Value info
 *
 * @Author: yezp
 * @date 2024/5/9 22:22
 */
@Data
@AllArgsConstructor
public class SpringValue {

    private Object bean;
    private String beanName;
    private String key;
    private String placeholder;
    private Field field;

}
