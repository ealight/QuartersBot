package com.bailbots.tg.QuartersBot.controller.main;

import com.bailbots.tg.QuartersBot.bpp.annotation.BotController;
import com.bailbots.tg.QuartersBot.bpp.annotation.BotRequestMapping;
import com.bailbots.tg.QuartersBot.repository.InlineListRepository;
import com.bailbots.tg.QuartersBot.service.KeyboardLoaderService;
import com.bailbots.tg.QuartersBot.service.MessageService;
import com.bailbots.tg.QuartersBot.utils.callback.CallbackParser;
import com.bailbots.tg.QuartersBot.utils.callback.CallbackUtil;
import com.bailbots.tg.QuartersBot.utils.Pageable;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@BotController("InlineListRepository")
public class InlineListRepositoryController {
    private final InlineListRepository inlineListRepository;
    private final MessageService messageService;
    private final KeyboardLoaderService keyboardLoaderService;

    public InlineListRepositoryController(InlineListRepository inlineListRepository, MessageService messageService, KeyboardLoaderService keyboardLoaderService) {
        this.inlineListRepository = inlineListRepository;
        this.messageService = messageService;
        this.keyboardLoaderService = keyboardLoaderService;
    }

    @BotRequestMapping("Next")
    public void next(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long telegramId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        CallbackParser parser = CallbackParser.parseCallback(callbackQuery.getData(), "page", "maxPages", "keyboardName", "inlineListId");

        String callbackData = inlineListRepository.getById(parser.getLongByName("inlineListId")).getLargeCallback();

        List parameters = CallbackUtil.parametersParser(callbackData);

        messageService.editInlineKeyboard(messageId, telegramId,
                keyboardLoaderService.getInlineListFromXMLWithRepository(
                        parser.getStringByName("keyboardName"), parser.getLongByName("inlineListId"),
                        Pageable.getNextPage(parser.getIntByName("maxPages"), parser.getIntByName("page")), parameters.toArray()));
    }

    @BotRequestMapping("Previous")
    public void previous(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long telegramId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        CallbackParser parser = CallbackParser.parseCallback(callbackQuery.getData(), "page", "maxPages", "keyboardName", "inlineListId");

        String callbackData = inlineListRepository.getById(parser.getLongByName("inlineListId")).getLargeCallback();

        List parameters = CallbackUtil.parametersParser(callbackData);

        messageService.editInlineKeyboard(messageId, telegramId,
                keyboardLoaderService.getInlineListFromXMLWithRepository(
                        parser.getStringByName("keyboardName"), parser.getLongByName("inlineListId"),
                        Pageable.getPreviousPage(parser.getIntByName("maxPages"), parser.getIntByName("page")), parameters.toArray()));
    }
}
