package com.bailbots.tg.QuartersBot.controller.main;

import com.bailbots.tg.QuartersBot.bpp.annotation.BotController;
import com.bailbots.tg.QuartersBot.bpp.annotation.BotRequestMapping;
import com.bailbots.tg.QuartersBot.service.KeyboardLoaderService;
import com.bailbots.tg.QuartersBot.service.MessageService;
import com.bailbots.tg.QuartersBot.utils.callback.CallbackParser;
import com.bailbots.tg.QuartersBot.utils.Pageable;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@BotController("InlineList")
public class InlineListController {
    private final MessageService messageService;
    private final KeyboardLoaderService keyboardLoaderService;

    public InlineListController(MessageService messageService, KeyboardLoaderService keyboardLoaderService) {
        this.messageService = messageService;
        this.keyboardLoaderService = keyboardLoaderService;
    }

    @BotRequestMapping("Next")
    public void next(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long telegramId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        CallbackParser parser = CallbackParser.parseCallback(callbackQuery.getData(), "page", "maxPage", "keyboardName");

        messageService.editInlineKeyboard(messageId, telegramId,
                keyboardLoaderService.getInlineListFromXML(parser.getStringByName("keyboardName"),
                        Pageable.getNextPage(parser.getIntByName("maxPage"), parser.getIntByName("page"))));
    }

    @BotRequestMapping("Previous")
    public void previous(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long telegramId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        CallbackParser parser = CallbackParser.parseCallback(callbackQuery.getData(),"page", "maxPage", "keyboardName");

        messageService.editInlineKeyboard(messageId, telegramId,
                keyboardLoaderService.getInlineListFromXML(parser.getStringByName("keyboardName"),
                        Pageable.getPreviousPage(parser.getIntByName("maxPage"), parser.getIntByName("page"))));
    }
}
