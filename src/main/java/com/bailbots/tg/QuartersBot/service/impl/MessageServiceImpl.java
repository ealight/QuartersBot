package com.bailbots.tg.QuartersBot.service.impl;

import com.bailbots.tg.QuartersBot.service.MessageService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Service
public class MessageServiceImpl implements MessageService {
    private final TelegramLongPollingBot telegramLongPollingBot;

    public MessageServiceImpl(TelegramLongPollingBot telegramLongPollingBot) {
        this.telegramLongPollingBot = telegramLongPollingBot;
    }

    @SneakyThrows
    @Override
    public void sendMessage(String text, Long telegramId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(telegramId);
        sendMessage.setText(text);

        telegramLongPollingBot.execute(sendMessage);
    }

    @SneakyThrows
    @Override
    public void sendPhoto(String caption, String imageURL, Long telegramId) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(telegramId);
        sendPhoto.setPhoto(imageURL);
        sendPhoto.setCaption(caption);

        telegramLongPollingBot.execute(sendPhoto);
    }

    @SneakyThrows
    @Override
    public void sendStaticKeyboard(String text, ReplyKeyboardMarkup replyKeyboardMarkup, Long telegramId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(telegramId);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setText(text);

        telegramLongPollingBot.execute(sendMessage);
    }

    @SneakyThrows
    @Override
    public void sendInlineKeyboard(String text, InlineKeyboardMarkup inlineKeyboardMarkup, Long telegramId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(telegramId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.setText(text);

        telegramLongPollingBot.execute(sendMessage);
    }

    @SneakyThrows
    @Override
    public void editInlineKeyboard(Integer messageId, Long telegramId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(telegramId);
        editMessageReplyMarkup.setMessageId(messageId);

        editMessageReplyMarkup.setReplyMarkup(inlineKeyboardMarkup);

        telegramLongPollingBot.execute(editMessageReplyMarkup);
    }

    @SneakyThrows
    @Override
    public void sendPhotoWithInlineKeyboard(String caption, String imageURL, Long telegramId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(telegramId);
        sendPhoto.setPhoto(imageURL);
        sendPhoto.setCaption(caption);
        sendPhoto.setReplyMarkup(inlineKeyboardMarkup);

        telegramLongPollingBot.execute(sendPhoto);
    }


}
