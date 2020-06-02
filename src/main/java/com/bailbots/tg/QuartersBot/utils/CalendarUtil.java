package com.bailbots.tg.QuartersBot.utils;

import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.IntStream.range;

public class CalendarUtil {

    public static String formatDate(Date date) {
        return new SimpleDateFormat("dd.MM.yyyy").format(date);
    }

    public static Date getDate(Integer year, Integer month, Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    public static Integer getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    public static Integer getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static Calendar getMonthByNumber(Integer month) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month);
        return calendar;
    }

    public static String getYearString(Calendar calendar) {
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    public static String getMonthName(Calendar calendar, Locale locale) {
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG_STANDALONE, locale);
        return month.substring(0, 1).toUpperCase() + month.substring(1);
    }

    public static List<String> getDaysName(Locale locale) {
        List<String> days = new ArrayList<>();
        Calendar calendarName = Calendar.getInstance();
        String name;

        calendarName.set(Calendar.DAY_OF_WEEK, calendarName.getFirstDayOfWeek());
        for (int i = 3; i < 10; i++) {
            name = calendarName.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, locale);
            days.add(name.substring(0, 1).toUpperCase() + name.substring(1));
            calendarName.set(Calendar.DAY_OF_WEEK, i);
        }

        return days;
    }

    public static List<List<String>> getMonthMatrix(Calendar calendar) {
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

        int firstDayInWeek = (calendar.get(Calendar.DAY_OF_WEEK)) - 1 == 0 ? 7 : calendar.get(Calendar.DAY_OF_WEEK) - 1;
        //Ternary operator for fixed problem with day sunday because Calendar.DAY_OF_WEEK gives as 0 instead of 7.

        List<List<String>> monthMatrix = range(firstDayInWeek - 1, daysInMonth + firstDayInWeek - 1)
                .boxed()
                .collect(Collectors.groupingBy(index -> index / 7))
                .values()
                .stream()
                .map(week -> week.stream()
                        .map(day -> String.valueOf(day = day - firstDayInWeek + 2))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());


        monthMatrix.stream()
                .filter(week -> week.size() < 7 && Integer.parseInt(Collections.max(week)) < 28)
                .map(day -> day.addAll(0, Stream.iterate(" ", n -> " ")
                        .limit(7 - day.size())
                        .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        monthMatrix.stream()
                .filter(week -> week.size() < 7 && Integer.parseInt(Collections.max(week)) > 27)
                .map(day -> day.addAll(Stream.iterate(" ", n -> " ")
                        .limit(7 - day.size())
                        .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return monthMatrix;
    }
}
