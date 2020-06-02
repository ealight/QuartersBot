package com.bailbots.tg.QuartersBot.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface CalendarService {
    InlineKeyboardMarkup getInlineCalendar(Integer month, Long forItemId, String requestCallback, Integer... args);
}
