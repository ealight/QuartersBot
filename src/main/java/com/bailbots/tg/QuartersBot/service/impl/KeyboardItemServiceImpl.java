package com.bailbots.tg.QuartersBot.service.impl;

import com.bailbots.tg.QuartersBot.dao.House;
import com.bailbots.tg.QuartersBot.dao.ReserveHouse;
import com.bailbots.tg.QuartersBot.repository.HouseRepository;
import com.bailbots.tg.QuartersBot.repository.ReserveHouseRepository;
import com.bailbots.tg.QuartersBot.service.KeyboardItemService;
import com.bailbots.tg.QuartersBot.service.KeyboardLoaderService;
import com.bailbots.tg.QuartersBot.service.MessageService;
import com.bailbots.tg.QuartersBot.utils.CalendarUtil;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Service
public class KeyboardItemServiceImpl implements KeyboardItemService {
    private final MessageService messageService;
    private final HouseRepository houseRepository;
    private final ReserveHouseRepository reserveHouseRepository;
    private final KeyboardLoaderService keyboardLoaderService;

    public KeyboardItemServiceImpl(MessageService messageService, HouseRepository houseRepository, ReserveHouseRepository reserveHouseRepository, KeyboardLoaderService keyboardLoaderService) {
        this.messageService = messageService;
        this.houseRepository = houseRepository;
        this.reserveHouseRepository = reserveHouseRepository;
        this.keyboardLoaderService = keyboardLoaderService;
    }

    @Override
    public void house(Long telegramId, Long houseId) {

        House house = houseRepository.getById(houseId);

        String image = house.getImages().get(0).getUrl();

        String caption = "Дом #" + house.getId() + "\n\n"
                + "Название: " + house.getName() + "\n"
                + "Описание: " + house.getDescription()  + "\n"
                + "Номер владельца: " + house.getOwnerPhoneNumber();

        InlineKeyboardMarkup keyboard = keyboardLoaderService.getInlineKeyboardFromXML("house/houseItem", houseId);

        messageService.sendPhotoWithInlineKeyboard(caption, image, telegramId, keyboard);
    }

    @Override
    public void magazineHouse(Long telegramId, Long houseId) {
        House house = houseRepository.getById(houseId);

        String image = house.getImages().get(0).getUrl();

        String caption = "Дом #" + house.getId() + "\n\n"
                + "Название: " + house.getName() + "\n"
                + "Описание: " + house.getDescription()  + "\n"
                + "Номер владельца: " + house.getOwnerPhoneNumber();

        InlineKeyboardMarkup keyboard = keyboardLoaderService.getInlineKeyboardFromXML("profile/houseItem", houseId);

        messageService.sendPhotoWithInlineKeyboard(caption, image, telegramId, keyboard);
    }

    @Override
    public void reserveHouse(Long telegramId, Long houseId) {
        ReserveHouse reserveHouse = reserveHouseRepository.getByHouseId(houseId);
        House house = houseRepository.getById(houseId);

        String image = house.getImages().get(0).getUrl();

        String caption = "Дом #" + house.getId() + "\n\n"
                + "Название: " + house.getName() + "\n"
                + "\uD83D\uDD50 Дата брони от: " + CalendarUtil.formatDate(reserveHouse.getDateFrom())
                + " до " + CalendarUtil.formatDate(reserveHouse.getDateTo());

        messageService.sendPhoto(caption, image, telegramId);
    }
}
