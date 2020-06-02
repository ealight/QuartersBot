package com.bailbots.tg.QuartersBot.service;

public interface KeyboardItemService {
    void house(Long telegramId, Long houseId);
    void magazineHouse(Long telegramId, Long houseId);
    void reserveHouse(Long telegramId, Long houseId);
}
