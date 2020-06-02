package com.bailbots.tg.QuartersBot.controller;

import com.bailbots.tg.QuartersBot.bpp.annotation.BotController;
import com.bailbots.tg.QuartersBot.bpp.annotation.BotRequestMapping;
import com.bailbots.tg.QuartersBot.service.CalendarService;
import com.bailbots.tg.QuartersBot.service.KeyboardLoaderService;
import com.bailbots.tg.QuartersBot.service.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

@BotController
public class StartController {
    private final MessageService messageService;
    private final KeyboardLoaderService keyboardLoaderService;

    public StartController(MessageService messageService, KeyboardLoaderService keyboardLoaderService) {
        this.messageService = messageService;
        this.keyboardLoaderService = keyboardLoaderService;
    }

    @BotRequestMapping("/start")
    public void start(Update update) {
        Long telegramId = update.getMessage().getChatId();

        String text = "Привет, наш бот поможет выбрать вам дом, просто отвечайте на вопросы. "
                + "Что Вы хотите сделать, посмотреть информацию о Славске или выбрать дом?";

        messageService.sendStaticKeyboard(text,
                keyboardLoaderService.getStaticKeyboardFromXML("start"), telegramId);
    }

    @BotRequestMapping("⬅ На главную")
    public void toStart(Update update) {
        Long telegramId = update.getMessage().getChatId();

        messageService.sendStaticKeyboard("Возвращаемся на главную...",
                keyboardLoaderService.getStaticKeyboardFromXML("start"), telegramId);
    }

    @BotRequestMapping("Выбрать дом")
    public void choseHouse(Update update) {
        Long telegramId = update.getMessage().getChatId();

        messageService.sendStaticKeyboard("Хорошо, теперь вы можете выбрать фильтры для поиска:",
                keyboardLoaderService.getStaticKeyboardFromXML("house/choseHouse"), telegramId);
    }

    @BotRequestMapping("Мой профиль")
    public void profile(Update update) {
        Long telegramId = update.getMessage().getChatId();

        messageService.sendStaticKeyboard("Переходим в профиль...",
                keyboardLoaderService.getStaticKeyboardFromXML("profile"), telegramId);
    }

    @BotRequestMapping("Информация о Славске")
    public void info(Update update) {
        Long telegramId = update.getMessage().getChatId();
    }
}
