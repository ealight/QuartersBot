package com.bailbots.tg.QuartersBot.controller;

import com.bailbots.tg.QuartersBot.bpp.annotation.BotController;
import com.bailbots.tg.QuartersBot.bpp.annotation.BotRequestMapping;
import com.bailbots.tg.QuartersBot.service.CalendarService;
import com.bailbots.tg.QuartersBot.service.MessageService;
import com.bailbots.tg.QuartersBot.service.ReserveHouseService;
import com.bailbots.tg.QuartersBot.utils.CalendarUtil;
import com.bailbots.tg.QuartersBot.utils.callback.CallbackParser;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.SimpleDateFormat;
import java.util.Date;

@BotController("Calendar")
public class CalendarController {
    private final MessageService messageService;
    private final CalendarService calendarService;
    private final ReserveHouseService reserveHouseService;

    public CalendarController(MessageService messageService, CalendarService calendarService, ReserveHouseService reserveHouseService) {
        this.messageService = messageService;
        this.calendarService = calendarService;
        this.reserveHouseService = reserveHouseService;
    }

    @BotRequestMapping("choseDateFrom")
    public void choseDateFrom(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long telegramId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String callbackData = callbackQuery.getData();

        CallbackParser parser = CallbackParser.parseCallback(callbackData, "id", "year", "month", "day");

        if (reserveHouseService.existByTelegramId(telegramId, parser.getLongByName("id"))) {
            return;
        }

        Date date = CalendarUtil.getDate(parser.getIntByName("year"), parser.getIntByName("month"), parser.getIntByName("day"));

        String text = "\uD83D\uDCC6 Дата приезда " + CalendarUtil.formatDate(date) + " выбрана" +
                "\nТеперь выберите дату отьезда ⬆";

        Integer month = CalendarUtil.getCurrentDay() >= 28 ? 1 : parser.getIntByName("month");

        messageService.editInlineKeyboard(messageId, telegramId,
                calendarService.getInlineCalendar(month, parser.getLongByName("id"), "choseDateTo",
                        parser.getIntByName("year"), parser.getIntByName("month"), parser.getIntByName("day")));

        messageService.sendMessage(text, telegramId);
    }

    @BotRequestMapping("choseDateTo")
    public void choseDateTo(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long telegramId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String callbackData = callbackQuery.getData();

        CallbackParser parser = CallbackParser.parseCallback(callbackData,
                "id", "yearTo", "monthTo", "dayTo", "yearFrom", "monthFrom", "dayFrom");

        Date dateFrom = CalendarUtil.getDate(
                parser.getIntByName("yearFrom"), parser.getIntByName("monthFrom"), parser.getIntByName("dayFrom"));
        Date dateTo = CalendarUtil.getDate(
                parser.getIntByName("yearTo"), parser.getIntByName("monthTo"), parser.getIntByName("dayTo"));

        if(reserveHouseService.dateFromMoreThenDateTo(telegramId, dateFrom, dateTo)) {
            return;
        }

        if(reserveHouseService.selectedPastDate(messageId, telegramId, parser, dateFrom, dateTo)) {
            return;
        }

        if(reserveHouseService.houseAlreadyReserved(messageId, telegramId, parser, dateFrom, dateTo)) {
            return;
        }

        reserveHouseService.reserve(telegramId, parser.getLongByName("id"), dateFrom, dateTo);

        messageService.editInlineKeyboard(messageId, telegramId,
                calendarService.getInlineCalendar(parser.getIntByName("monthTo"), parser.getLongByName("id"), "choseDateFrom",
                        parser.getIntByName("yearFrom"), parser.getIntByName("monthFrom"), parser.getIntByName("dayFrom")));
    }

    @BotRequestMapping("ToFirstMonth")
    public void toFirstMonth(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String callbackData = callbackQuery.getData();

        CallbackParser parser = CallbackParser.parseCallback(callbackData,
                "id", "callback", "month", "dateFromYear", "dateFromMonth", "dateFromDay");

        if(parser.getIntByName("month") == 0) {
            return;
        }

        Long telegramId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        messageService.editInlineKeyboard(messageId, telegramId,
                calendarService.getInlineCalendar(0, parser.getLongByName("id"), parser.getStringByName("callback"),
                        parser.getIntByName("dateFromYear"), parser.getIntByName("dateFromMonth"), parser.getIntByName("dateFromDay")));
    }


    @BotRequestMapping("Next")
    public void next(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String callbackData = callbackQuery.getData();

        Long telegramId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        CallbackParser parser = CallbackParser.parseCallback(callbackData,
                "id", "callback", "month", "dateFromYear", "dateFromMonth", "dateFromDay");

        messageService.editInlineKeyboard(messageId, telegramId,
                calendarService.getInlineCalendar(parser.getIntByName("month") + 1, parser.getLongByName("id"), parser.getStringByName("callback"),
                        parser.getIntByName("dateFromYear"), parser.getIntByName("dateFromMonth"), parser.getIntByName("dateFromDay")));
    }

    @BotRequestMapping("Previous")
    public void previous(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String callbackData = callbackQuery.getData();

        CallbackParser parser = CallbackParser.parseCallback(callbackData,
                "id", "callback", "month", "dateFromYear", "dateFromMonth", "dateFromDay");

        if (parser.getIntByName("month") == 0) {
            return;
        }

        Long telegramId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        messageService.editInlineKeyboard(messageId, telegramId,
                calendarService.getInlineCalendar(parser.getIntByName("month") - 1, parser.getLongByName("id"), parser.getStringByName("callback"),
                        parser.getIntByName("dateFromYear"), parser.getIntByName("dateFromMonth"), parser.getIntByName("dateFromDay")));
    }

}
