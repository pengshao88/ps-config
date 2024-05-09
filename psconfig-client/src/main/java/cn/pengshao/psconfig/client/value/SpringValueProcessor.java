package cn.pengshao.psconfig.client.value;

import cn.pengshao.common.utils.FieldUtils;
import cn.pengshao.psconfig.client.util.PlaceholderHelper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * Description:process spring value
 * 1、扫描所有的 Spring Value，保存起来
 * 2、当配置中心有更新时，更新到 Spring Value
 * @EventListener 与 ApplicationListener 等价
 *
 * @Author: yezp
 * @date 2024/5/8 22:18
 */
@Slf4j
public class SpringValueProcessor implements BeanPostProcessor, BeanFactoryAware, ApplicationListener<EnvironmentChangeEvent> {

    static final PlaceholderHelper HELPER = PlaceholderHelper.getInstance();
    static final MultiValueMap<String, SpringValue> VALUE_HOLDER = new LinkedMultiValueMap<>();
    @Setter
    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        FieldUtils.findAnnotatedField(bean.getClass(), Value.class).forEach(field -> {
            log.info("[PSCONFIG] >> find spring value:{}", field);
            Value value = field.getAnnotation(Value.class);
            // 解析 placeholder
            HELPER.extractPlaceholderKeys(value.value()).forEach(key -> {
                log.info("[PSCONFIG] >> find spring value key:{}", key);
                SpringValue springValue = new SpringValue(bean, beanName, key, value.value(), field);
                VALUE_HOLDER.add(key, springValue);
            });
        });

        return bean;
    }

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        log.info("[PSCONFIG] >> update spring value for keys:{}", event.getKeys());
        event.getKeys().forEach(key -> {
            List<SpringValue> springValues = VALUE_HOLDER.get(key);
            if (springValues == null || springValues.isEmpty()) {
                return;
            }

            springValues.forEach(springValue -> {
                log.info("[PSCONFIG] >> update spring value:{} for key:{}", springValue, key);
                try {
                    Object value = HELPER.resolvePropertyValue((ConfigurableBeanFactory) beanFactory,
                            springValue.getBeanName(), springValue.getPlaceholder());
                    log.info("[PSCONFIG] >> update value:{} for holder:{}", value, springValue.getPlaceholder());
                    springValue.getField().setAccessible(true);
                    springValue.getField().set(springValue.getBean(), value);
                } catch (Exception ex) {
                    log.error("[PSCONFIG] >> update spring value error", ex);
                }
            });
        });
    }

}
