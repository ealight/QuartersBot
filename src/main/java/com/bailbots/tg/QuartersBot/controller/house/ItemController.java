package com.bailbots.tg.QuartersBot.controller.house;

import com.bailbots.tg.QuartersBot.bpp.annotation.BotController;
import com.bailbots.tg.QuartersBot.bpp.annotation.BotRequestMapping;
import com.bailbots.tg.QuartersBot.dao.House;
import com.bailbots.tg.QuartersBot.dao.HouseImageData;
import com.bailbots.tg.QuartersBot.repository.HouseRepository;
import com.bailbots.tg.QuartersBot.repository.UserMagazineRepository;
import com.bailbots.tg.QuartersBot.service.CalendarService;
import com.bailbots.tg.QuartersBot.service.MessageService;
import com.bailbots.tg.QuartersBot.utils.CalendarUtil;
import com.bailbots.tg.QuartersBot.utils.callback.CallbackUtil;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@BotController("House")
public class ItemController {
    private final HouseRepository houseRepository;
    private final MessageService messageService;
    private final UserMagazineRepository userMagazineRepository;
    private final CalendarService calendarService;

    public ItemController(HouseRepository houseRepository, MessageService messageService, UserMagazineRepository userMagazineRepository, CalendarService calendarService) {
        this.houseRepository = houseRepository;
        this.messageService = messageService;
        this.userMagazineRepository = userMagazineRepository;
        this.calendarService = calendarService;
    }

    @BotRequestMapping("Reserve")
    public void reserve(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long id = Long.parseLong(callbackQuery.getData().split(CallbackUtil.SEPARATOR)[2].trim());

        Long telegramId = callbackQuery.getMessage().getChatId();

        Integer month = CalendarUtil.getCurrentDay() >= 28 ? 1 : 0;

        messageService.sendInlineKeyboard("\uD83D\uDCCC Отличный выбор! Выберите дату приезда: ",
                calendarService.getInlineCalendar(month, id, "choseDateFrom"), telegramId);
    }


    @BotRequestMapping("MorePhoto")
    public void morePhoto(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long telegramId = callbackQuery.getMessage().getChatId();

        Long houseId = Long.parseLong(callbackQuery.getData().split(CallbackUtil.SEPARATOR)[2].trim());

        House house = houseRepository.getById(houseId);

        for(HouseImageData image : house.getImages()) {
            messageService.sendPhoto("", image.getUrl(), telegramId);
        }
    }

    @BotRequestMapping("Detail")
    public void detail(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long id = Long.parseLong(callbackQuery.getData().split(CallbackUtil.SEPARATOR)[2].trim());

        House house = houseRepository.getById(id);
        Long telegramId = callbackQuery.getMessage().getChatId();

        boolean swimmingPool = house.isSwimmingPool();
        boolean bath = house.isBath();

        String detailInformation = "Дом #" + house.getId()
                + "\n\nПодробное описание:\n"
                + house.getDetailInfo()
                + "\nБасейн: " + (swimmingPool ? "Есть ✅" : "Нету ❎")
                + "\nБаня: " + (bath ? "Есть ✅" : "Нету ❎");

        messageService.sendMessage(detailInformation, telegramId);
    }

    @BotRequestMapping("OwnerRequirements")
    public void ownerRequirements(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long id = Long.parseLong(callbackQuery.getData().split(CallbackUtil.SEPARATOR)[2].trim());

        House house = houseRepository.getById(id);
        Long telegramId = callbackQuery.getMessage().getChatId();

        String detailInformation = "Дом #" + house.getId()
                + "\n\n"
                + "Требования владельца: \n" + house.getOwnerRequirements();

        messageService.sendMessage(detailInformation, telegramId);
    }

    @BotRequestMapping("AdditionalService")
    public void additionalService(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long id = Long.parseLong(callbackQuery.getData().split(CallbackUtil.SEPARATOR)[2].trim());

        House house = houseRepository.getById(id);
        Long telegramId = callbackQuery.getMessage().getChatId();

        String detailInformation = "Дом #" + house.getId()
                + "\n\nДополнительные услуги:\n"
                + house.getAdditionalService();

        messageService.sendMessage(detailInformation, telegramId);
    }

    @BotRequestMapping("ToMagazine")
    public void toMagazine(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long houseId = Long.parseLong(callbackQuery.getData().split(CallbackUtil.SEPARATOR)[2].trim());

        Long telegramId = callbackQuery.getMessage().getChatId();

        if(userMagazineRepository.houseIdAlreadyExistForTelegramId(houseId, telegramId)) {
            messageService.sendMessage("❌ Вы уже добавляли этом дом в свой журнал", telegramId);
            return;
        }

        userMagazineRepository.saveForTelegramId(telegramId, houseId);

        messageService.sendMessage("Дом #" + houseId + " успешно добавлен в ваш журнал", telegramId);
    }

    @BotRequestMapping("Delete")
    public void delete(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long houseId = Long.parseLong(callbackQuery.getData().split(CallbackUtil.SEPARATOR)[2].trim());

        Long telegramId = callbackQuery.getMessage().getChatId();

        userMagazineRepository.removeById(houseId);

        messageService.sendMessage("Дом #" + houseId + " успешно удален из вашего журнала", telegramId);
    }
}
