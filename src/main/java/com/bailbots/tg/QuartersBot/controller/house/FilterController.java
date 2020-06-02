package com.bailbots.tg.QuartersBot.controller.house;

import com.bailbots.tg.QuartersBot.bpp.annotation.BotController;
import com.bailbots.tg.QuartersBot.bpp.annotation.BotRequestMapping;
import com.bailbots.tg.QuartersBot.dao.User;
import com.bailbots.tg.QuartersBot.repository.HouseRepository;
import com.bailbots.tg.QuartersBot.service.KeyboardLoaderService;
import com.bailbots.tg.QuartersBot.service.MessageService;
import com.bailbots.tg.QuartersBot.service.MessageValueService;
import com.bailbots.tg.QuartersBot.service.UserSessionService;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@BotController
public class FilterController {
    private final MessageService messageService;
    private final MessageValueService messageValueService;
    private final KeyboardLoaderService keyboardLoaderService;
    private final UserSessionService userSessionService;
    private final HouseRepository houseRepository;

    public FilterController(MessageService messageService, MessageValueService messageValueService, KeyboardLoaderService keyboardLoaderService, UserSessionService userSessionService, HouseRepository houseRepository) {
        this.messageService = messageService;
        this.messageValueService = messageValueService;
        this.keyboardLoaderService = keyboardLoaderService;
        this.userSessionService = userSessionService;
        this.houseRepository = houseRepository;
    }
    @BotRequestMapping("Количество мест")
    public void seatsNumber(Update update) {
        Long telegramId = update.getMessage().getChatId();

        messageValueService.turnGetMessageValueForUser(telegramId, "houseSeatsNumber");

        messageService.sendMessage("Хорошо, введите сообщением ниже минимальное количество мест в доме:", telegramId);
    }

    @BotRequestMapping("Цена")
    public void price(Update update) {
        Long telegramId = update.getMessage().getChatId();

        messageService.sendInlineKeyboard("Выберите какую цену вы хотите установить максимальную, или минимальную",
                keyboardLoaderService.getInlineKeyboardFromXML("house/housePriceFilter"), telegramId);
    }

    @BotRequestMapping("Больше фильтров")
    public void moreFilters(Update update) {
        Long telegramId = update.getMessage().getChatId();

        messageService.sendInlineKeyboard("Нажмите на фильтр, что-бы выбрать",
                keyboardLoaderService.getInlineKeyboardFromXML("house/houseMoreFilters"), telegramId);
    }

    @BotRequestMapping("⬅ К выбору дома")
    public void toGetHouse(Update update) {
        Long telegramId = update.getMessage().getChatId();

        messageService.sendStaticKeyboard("Возвращаемся к домам...",
                keyboardLoaderService.getStaticKeyboardFromXML("house/choseHouse"), telegramId);
    }

    @BotRequestMapping("Показать дома ➡")
    public void houses(Update update) {
        Long telegramId = update.getMessage().getChatId();

        User user = userSessionService.getUserFromSession(telegramId);

        int minSeatsNumber = user.getHouseFilter().getMinSeatsNumber();
        int minPrice = user.getHouseFilter().getMinPrice();
        int maxPrice = user.getHouseFilter().getMaxPrice();
        boolean swimmingPool = user.getHouseFilter().isSwimmingPool();
        boolean bath = user.getHouseFilter().isBath();

        String filtersText = "\n\nМинимальное количетсво мест: " + minSeatsNumber
                + "\nМинимальная цена: " + minPrice
                + "\nМаксимальная цена: " + maxPrice
                + "\nБасейн: " + (swimmingPool ? "Да ✅" : "Нет ❎")
                + "\nБаня: " + (bath ? "Да ✅" : "Нет ❎");

        if(houseRepository.countByFilters(minSeatsNumber, minPrice, maxPrice, swimmingPool, bath).equals(0L)) {
               messageService.sendMessage("Мы не можем найти дома с такими фильтрами: " + filtersText, telegramId);
               return;
        }

        messageService.sendInlineKeyboard("Фильтры:" + filtersText,
                keyboardLoaderService.getInlineListFromXMLWithRepository("house/filtersHouseList", 0L, 0, minSeatsNumber, minPrice, maxPrice, swimmingPool, bath), telegramId);
    }

    @BotRequestMapping("MaxPrice")
    public void maxPrice(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long telegramId = callbackQuery.getMessage().getChatId();

        messageValueService.turnGetMessageValueForUser(telegramId, "houseMaxPrice");

        messageService.sendMessage("Хорошо, введите сообщением ниже максимальную цену:", telegramId);
    }

    @BotRequestMapping("MinPrice")
    public void minPrice(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long telegramId = callbackQuery.getMessage().getChatId();

        messageValueService.turnGetMessageValueForUser(telegramId, "houseMinPrice");

        messageService.sendMessage("Хорошо, введите сообщением ниже минимальную цену:", telegramId);
    }

    @BotRequestMapping("SwimmingPool")
    public void swimmingPool(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long telegramId = callbackQuery.getMessage().getChatId();

        User user = userSessionService.getUserFromSession(telegramId);

        boolean swimmingPool = !user.getHouseFilter().isSwimmingPool();

        user.getHouseFilter().setSwimmingPool(swimmingPool);

        String text = swimmingPool ? "✅ Басейн включен в фильтры" : "❎ Басейн выключен из фильтров";

        messageService.sendMessage(text, telegramId);
    }

    @BotRequestMapping("Bath")
    public void bath(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long telegramId = callbackQuery.getMessage().getChatId();

        User user = userSessionService.getUserFromSession(telegramId);

        boolean bath = !user.getHouseFilter().isBath();

        user.getHouseFilter().setBath(bath);

        String text = bath ? "✅ Баня включена в фильтры" : "❎ Баня выключена из фильтров";

        messageService.sendMessage(text, telegramId);
    }
}
