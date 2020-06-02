package com.bailbots.tg.QuartersBot.controller;

import com.bailbots.tg.QuartersBot.bpp.annotation.BotController;
import com.bailbots.tg.QuartersBot.bpp.annotation.BotRequestMapping;
import com.bailbots.tg.QuartersBot.service.KeyboardLoaderService;
import com.bailbots.tg.QuartersBot.service.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

@BotController
public class HouseController {
    private final MessageService messageService;
    private final KeyboardLoaderService keyboardLoaderService;

    public HouseController(MessageService messageService, KeyboardLoaderService keyboardLoaderService) {
        this.messageService = messageService;
        this.keyboardLoaderService = keyboardLoaderService;
    }

    @BotRequestMapping("Посмотреть все дома")
    public void allHouses(Update update) {
        Long telegramId = update.getMessage().getChatId();

        messageService.sendInlineKeyboard("Все дома:",
                keyboardLoaderService.getInlineListFromXML("house/allHouseList", 0), telegramId);
    }

    @BotRequestMapping("К фильтрам ➡")
    public void choseHouse(Update update) {
        Long telegramId = update.getMessage().getChatId();

        messageService.sendStaticKeyboard("Хорошо, теперь вы можете выбрать фильтры для поиска:",
                keyboardLoaderService.getStaticKeyboardFromXML("house/filters"), telegramId);
    }

}
