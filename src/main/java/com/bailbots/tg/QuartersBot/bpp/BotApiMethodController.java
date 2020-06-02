package com.bailbots.tg.QuartersBot.bpp;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;
import java.util.Optional;

public class BotApiMethodController {
    private Object bean;
    private Method method;

    public BotApiMethodController(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    public boolean successUpdatePredicate(Update update) {
        return Optional.of(update).map(Update::getMessage).map(Message::hasText).orElse(false) ||
                Optional.of(update).map(Update::getCallbackQuery).map(CallbackQuery::getMessage).map(Message::hasText).orElse(false) ||
                Optional.of(update).map(Update::getCallbackQuery).map(CallbackQuery::getMessage).map(Message::hasPhoto).orElse(false);
    }

    @SneakyThrows
    public void process(Update update) {
        if(!successUpdatePredicate(update)) return;

        method.invoke(bean, update);
    }

}
