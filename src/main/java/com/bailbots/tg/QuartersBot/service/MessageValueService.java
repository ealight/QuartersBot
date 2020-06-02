package com.bailbots.tg.QuartersBot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageValueService {
    void getValueFromMessage(Update update, Long telegramId);
    void turnGetMessageValueForUser(Long telegramId, String action);
    boolean isGetValueTurn(Long telegramId);
}
