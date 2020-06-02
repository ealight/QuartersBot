package com.bailbots.tg.QuartersBot.controller;

import com.bailbots.tg.QuartersBot.bpp.annotation.BotController;
import com.bailbots.tg.QuartersBot.bpp.annotation.BotRequestMapping;
import com.bailbots.tg.QuartersBot.dao.User;
import com.bailbots.tg.QuartersBot.repository.HouseRepository;
import com.bailbots.tg.QuartersBot.service.KeyboardLoaderService;
import com.bailbots.tg.QuartersBot.service.MessageService;
import com.bailbots.tg.QuartersBot.service.UserSessionService;
import org.telegram.telegrambots.meta.api.objects.Update;

@BotController
public class ProfileController {
    private static final Long NULL_LONG = 0L;
    private static final Integer NULL_INT = 0;

    private final MessageService messageService;
    private final KeyboardLoaderService keyboardLoaderService;
    private final UserSessionService userSessionService;
    private final HouseRepository houseRepository;

    public ProfileController(MessageService messageService, KeyboardLoaderService keyboardLoaderService, UserSessionService userSessionService, HouseRepository houseRepository) {
        this.messageService = messageService;
        this.keyboardLoaderService = keyboardLoaderService;
        this.userSessionService = userSessionService;
        this.houseRepository = houseRepository;
    }

    @BotRequestMapping("Уведомления")
    public void notifications(Update update) {
        Long telegramId = update.getMessage().getChatId();

        messageService.sendInlineKeyboard("Выберите хотите ли вы включить или выключить уведомления",
                keyboardLoaderService.getInlineKeyboardFromXML("profile/notifications"), telegramId);
    }

    @BotRequestMapping("Мои брони")
    public void reserveHouses(Update update) {
        Long telegramId = update.getMessage().getChatId();

        if(houseRepository.countByTelegramId(telegramId, "reserve_house").equals(NULL_LONG)) {
            messageService.sendMessage("Ваш список забронированных домов пусть", telegramId);
            return;
        }

        messageService.sendInlineKeyboard("Ваши забронированные дома:",
                keyboardLoaderService.getInlineListFromXMLWithRepository("profile/reserveHousesList", NULL_LONG, NULL_INT,
                        telegramId, "reserve_house"), telegramId);
    }

    @BotRequestMapping("Мой журнал")
    public void magazine(Update update) {
        Long telegramId = update.getMessage().getChatId();

        if(houseRepository.countByTelegramId(telegramId, "user_magazine").equals(NULL_LONG)) {
            messageService.sendMessage("Ваш журнал понравившихся домов пуст", telegramId);
            return;
        }

        messageService.sendInlineKeyboard("Ваш журнал:",
                keyboardLoaderService.getInlineListFromXMLWithRepository("profile/magazineList", NULL_LONG, NULL_INT,
                        telegramId, "user_magazine"), telegramId);
    }
}
