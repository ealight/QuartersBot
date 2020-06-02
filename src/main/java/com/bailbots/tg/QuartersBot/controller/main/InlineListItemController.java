package com.bailbots.tg.QuartersBot.controller.main;

import com.bailbots.tg.QuartersBot.bpp.annotation.BotController;
import com.bailbots.tg.QuartersBot.bpp.annotation.BotRequestMapping;
import com.bailbots.tg.QuartersBot.service.KeyboardItemService;
import com.bailbots.tg.QuartersBot.utils.callback.CallbackParser;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@BotController("InlineListItem")
public class InlineListItemController {
    private final KeyboardItemService keyboardItemService;

    public InlineListItemController(KeyboardItemService keyboardItemService) {
        this.keyboardItemService = keyboardItemService;
    }

    @SneakyThrows
    @BotRequestMapping("GetItem")
    public void choseItem(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long telegramId = callbackQuery.getMessage().getChatId();

        String callbackData = callbackQuery.getData();

        CallbackParser parser = CallbackParser.parseCallback(callbackData,"itemResponseMethod", "id");

        keyboardItemService.getClass().getDeclaredMethod(parser.getStringByName("itemResponseMethod"), Long.class, Long.class)
                .invoke(keyboardItemService, telegramId, parser.getLongByName("id"));
    }

}
