package com.bailbots.tg.QuartersBot.bpp;

import com.bailbots.tg.QuartersBot.bpp.annotation.BotController;
import com.bailbots.tg.QuartersBot.bpp.annotation.BotRequestMapping;
import com.bailbots.tg.QuartersBot.bpp.container.BotApiMethodContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class TelegramUpdateHandlerBeanPostProcessor implements BeanPostProcessor, Ordered {
    private BotApiMethodContainer container = BotApiMethodContainer.getInstanse();
    private Map<String, Class> botControllerMap = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(BotController.class)) {
            botControllerMap.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!botControllerMap.containsKey(beanName)) return bean;

        for(Method m : bean.getClass().getMethods()) {
            System.out.println(m);
        }
        Arrays.stream(bean.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(BotRequestMapping.class))
                .forEach((Method method) -> generateController(bean, method));
        return bean;
    }

    private void generateController(Object bean, Method method) {
        BotController botController = bean.getClass().getAnnotation(BotController.class);
        BotRequestMapping botRequestMapping = method.getAnnotation(BotRequestMapping.class);

        String path = (botController.value().length != 0 ? botController.value()[0] : "")
                + (botRequestMapping.value().length != 0 ? botRequestMapping.value()[0] : "");

        container.addBotController(path, new BotApiMethodController(bean, method));
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
