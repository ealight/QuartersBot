package com.bailbots.tg.QuartersBot.repository;

public interface UserMagazineRepository {
    void saveForTelegramId(Long telegramId, Long houseId);
    void removeById(Long houseId);
    boolean houseIdAlreadyExistForTelegramId(Long houseId, Long telegramId);
}
