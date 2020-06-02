package com.bailbots.tg.QuartersBot.service.impl;

import com.bailbots.tg.QuartersBot.dao.ReserveHouse;
import com.bailbots.tg.QuartersBot.repository.ReserveHouseRepository;
import com.bailbots.tg.QuartersBot.service.CalendarService;
import com.bailbots.tg.QuartersBot.service.ReserveHouseService;
import com.bailbots.tg.QuartersBot.utils.CalendarUtil;
import com.bailbots.tg.QuartersBot.utils.callback.CallbackParser;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReserveHouseServiceImpl implements ReserveHouseService {
    private final MessageServiceImpl messageService;
    private final ReserveHouseRepository reserveHouseRepository;
    private final CalendarService calendarService;

    public ReserveHouseServiceImpl(MessageServiceImpl messageService, ReserveHouseRepository reserveHouseRepository, CalendarService calendarService) {
        this.messageService = messageService;
        this.reserveHouseRepository = reserveHouseRepository;
        this.calendarService = calendarService;
    }

    @Override
    public boolean houseAlreadyReserved(Integer messageId, Long telegramId, CallbackParser parser, Date dateFrom, Date dateTo) {
        Long houseId = parser.getLongByName("id");

        if (null != reserveHouseRepository.getByHouseIdAndDateToAfterAndDateFromBefore(houseId, dateFrom, dateTo)) {
            ReserveHouse reserveHouse = reserveHouseRepository.getByHouseIdAndDateToAfterAndDateFromBefore(houseId, dateFrom, dateTo);
            sendHouseAlreadyReserveMessage(reserveHouse, messageId, telegramId, parser);
            return true;
        }

        if (null != reserveHouseRepository.getByHouseIdAndDateFromBeforeAndDateToAfter(houseId, dateFrom, dateTo)) {
            ReserveHouse reserveHouse = reserveHouseRepository.getByHouseIdAndDateFromBeforeAndDateToAfter(houseId, dateFrom, dateTo);
            sendHouseAlreadyReserveMessage(reserveHouse, messageId, telegramId, parser);
            return true;
        }

        return false;
    }

    @Override
    public boolean existByTelegramId(Long telegramId, Long houseId) {
        if (reserveHouseRepository.existsByTelegramIdAndHouseId(telegramId, houseId)) {
            messageService.sendMessage("❌ Вы уже бронировали этот дом", telegramId);
            return true;
        }
        return false;
    }

    @Override
    public boolean dateFromMoreThenDateTo(Long telegramId, Date dateFrom, Date dateTo) {
        if (dateFrom.compareTo(dateTo) > 0) {
            String text = "❌ Вы неверно выбрали дату"
                    + "\n\uD83D\uDCC6 Выбранная дата с: " + CalendarUtil.formatDate(dateFrom) + " по " + CalendarUtil.formatDate(dateTo);

            messageService.sendMessage(text, telegramId);
            return true;
        }
        return false;
    }

    @Override
    public boolean selectedPastDate(Integer messageId, Long telegramId, CallbackParser parser,Date dateFrom, Date dateTo) {
        Date currentDate = CalendarUtil.getCurrentDate();

        if (currentDate.compareTo(dateTo) > 0 || currentDate.compareTo(dateFrom) > 0) {
            String text = "❌ Вы не можете забронировать дом в прошлом"
                    + "\n\uD83D\uDCC6 Выбранная дата с: " + CalendarUtil.formatDate(dateFrom) + " по " + CalendarUtil.formatDate(dateTo);

            messageService.sendMessage(text, telegramId);

            messageService.editInlineKeyboard(messageId, telegramId,
                    calendarService.getInlineCalendar(parser.getIntByName("monthTo"), parser.getLongByName("id"), "choseDateFrom"));
            return true;
        }
        return false;
    }

    @Override
    public ReserveHouse reserve(Long telegramId, Long houseId, Date dateFrom, Date dateTo) {
        String text = "Вы успешно создали заказ на бронь дома №" + houseId + " ✔"
                + "\n\uD83D\uDCC5 Дата: " + CalendarUtil.formatDate(dateFrom) + " - " + CalendarUtil.formatDate(dateTo)
                + "\nКогда владелец подтвердит вашу бронь, я вам отпишу :)";

        messageService.sendMessage(text, telegramId);

        return reserveHouseRepository.save(ReserveHouse.builder()
                .telegramId(telegramId)
                .houseId(houseId)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build());
    }

    private void sendHouseAlreadyReserveMessage(ReserveHouse reserveHouse, Integer messageId, Long telegramId, CallbackParser parser) {
        Date dateFrom = reserveHouse.getDateFrom();
        Date dateTo = reserveHouse.getDateTo();

        String text = "❌ Извините, этот дом забронирован на это время"
                + "\n\uD83D\uDCC5 Дата с: " + CalendarUtil.formatDate(dateFrom) + " по " + CalendarUtil.formatDate(dateTo);

        messageService.sendMessage(text, telegramId);

        messageService.editInlineKeyboard(messageId, telegramId,
                calendarService.getInlineCalendar(parser.getIntByName("monthTo"), parser.getLongByName("id"), "choseDateFrom"));
    }
}
