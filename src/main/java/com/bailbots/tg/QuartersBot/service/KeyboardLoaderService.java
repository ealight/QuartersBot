package com.bailbots.tg.QuartersBot.service;

import com.bailbots.tg.QuartersBot.parser.inline.InlineKeyboard;
import org.springframework.boot.autoconfigure.data.RepositoryType;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;


public interface KeyboardLoaderService {
    ReplyKeyboardMarkup getStaticKeyboardFromXML(String filename);
    InlineKeyboardMarkup getInlineKeyboardFromXML(String filename, Long ...itemId);
    InlineKeyboardMarkup getInlineListFromXML(String filename, Integer page);
    InlineKeyboardMarkup getInlineListFromXMLWithRepository(String filename, Long inlineListId, Integer page, Object ...args);
}
