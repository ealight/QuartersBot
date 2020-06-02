package com.bailbots.tg.QuartersBot.service.impl;

import com.bailbots.tg.QuartersBot.repository.ReserveHouseRepository;
import com.bailbots.tg.QuartersBot.service.CalendarService;
import com.bailbots.tg.QuartersBot.utils.CalendarUtil;
import com.bailbots.tg.QuartersBot.utils.callback.CallbackBuilder;
import com.bailbots.tg.QuartersBot.utils.callback.CallbackUtil;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CalendarServiceImpl implements CalendarService {
    private static final String EMPTY_DAY = " ";
    private static final String UNAVAILABLE_DAY = "-";
    private static final String NON_EXISTENT_DAY = "1337";


    private final ReserveHouseRepository reserveHouseRepository;

    public CalendarServiceImpl(ReserveHouseRepository reserveHouseRepository) {
        this.reserveHouseRepository = reserveHouseRepository;
    }


    @Override
    public InlineKeyboardMarkup getInlineCalendar(Integer month, Long forItemId, String requestCallback, Integer... args) {
        Locale locale = new Locale("ru");

        Calendar calendar = CalendarUtil.getMonthByNumber(month);
        Date currentDate = CalendarUtil.getCurrentDate();

        String year = CalendarUtil.getYearString(calendar);
        String monthName = CalendarUtil.getMonthName(calendar, locale);

        InlineKeyboardMarkup result = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> calendarRows = new ArrayList<>();

        calendarRows.add(Collections.singletonList(new InlineKeyboardButton(monthName + " " + year).setCallbackData(CallbackUtil.NONE_CALLBACK)));

        calendarRows.add(CalendarUtil.getDaysName(locale).stream()
                .map(day -> new InlineKeyboardButton(day).setCallbackData(CallbackUtil.NONE_CALLBACK))
                .collect(Collectors.toList()));

        for (List<String> week : CalendarUtil.getMonthMatrix(calendar)) {
            List<InlineKeyboardButton> calendarRow = new ArrayList<>();

            for (String day : week) {
                boolean emptyDay = day.equals(EMPTY_DAY);

                String emptyDayEquateToMinusOne = (emptyDay ? NON_EXISTENT_DAY : day);

                Date dayDate = CalendarUtil.getDate(
                        CalendarUtil.getCurrentYear(), month, Integer.parseInt(emptyDayEquateToMinusOne));

                int dayReserve = reserveHouseRepository.existByDateBetweenReserveDates(forItemId, dayDate);

                String dayName = (dayReserve == 1 || currentDate.compareTo(dayDate) > 0 && !emptyDay ? UNAVAILABLE_DAY : day);
                String controller = (emptyDay || dayName.equals(UNAVAILABLE_DAY) ? CallbackUtil.NONE_CALLBACK : "Calendar");

                CallbackBuilder callback = CallbackBuilder.createCallback();
                callback.appendAll(controller, requestCallback, forItemId, year, month, day);

                if (args.length != 0) {
                    callback.appendAll(args[0], args[1], args[2]);
                }

                calendarRow.add(new InlineKeyboardButton(dayName).setCallbackData(callback.toString()));
            }

            calendarRows.add(calendarRow);
        }

        CallbackBuilder navigationCallback = CallbackBuilder.createCallback();
        navigationCallback.appendAll(forItemId, requestCallback, month);

        if (args.length != 0) {
            navigationCallback.appendAll(args[0], args[1], args[2]);
        } else {
            navigationCallback.appendAll(0, 0, 0);
        }

        String currentMonth = (month == 0 ? "Текущий" : "К текущему");

        calendarRows.add(Arrays.asList(
                new InlineKeyboardButton("<").setCallbackData("Calendar&Previous&" + navigationCallback),
                new InlineKeyboardButton(currentMonth).setCallbackData("Calendar&ToFirstMonth&" + navigationCallback),
                new InlineKeyboardButton(">").setCallbackData("Calendar&Next&" + navigationCallback)));

        result.setKeyboard(calendarRows);
        return result;
    }
}
