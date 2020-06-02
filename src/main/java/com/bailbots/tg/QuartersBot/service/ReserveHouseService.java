package com.bailbots.tg.QuartersBot.service;

import com.bailbots.tg.QuartersBot.dao.ReserveHouse;
import com.bailbots.tg.QuartersBot.utils.callback.CallbackParser;

import java.util.Date;

public interface ReserveHouseService {
    boolean houseAlreadyReserved(Integer messageId, Long telegramId, CallbackParser parser, Date dateFrom, Date dateTo);
    boolean existByTelegramId(Long telegramId, Long houseId);
    boolean dateFromMoreThenDateTo(Long telegramId, Date dateFrom, Date dateTo);
    boolean selectedPastDate(Integer messageId, Long telegramId, CallbackParser parser, Date dateFrom, Date dateTo);
    ReserveHouse reserve(Long telegramId, Long houseId, Date dateFrom, Date dateTo);
}
