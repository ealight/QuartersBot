package com.bailbots.tg.QuartersBot.bpp;

import com.bailbots.tg.QuartersBot.bpp.container.BotApiMethodContainer;
import com.bailbots.tg.QuartersBot.utils.callback.CallbackUtil;
import org.telegram.telegrambots.meta.api.objects.Update;

public class BotSelectHandle {
    private static BotApiMethodContainer container = BotApiMethodContainer.getInstanse();

    public static void processByUpdate(Update update) {
        String path;
        BotApiMethodController controller = null;

        if (update.hasMessage() && update.getMessage().hasText()) {
            path = update.getMessage().getText();//.split("")[0].trim();
            controller = container.getControllerMap().get(path);
            if (controller == null) controller = container.getControllerMap().get("");
        }
        else if (update.hasCallbackQuery()) {
            if (!update.getCallbackQuery().getData().equals(CallbackUtil.NONE_CALLBACK)) {
                String botControllerValue = CallbackUtil.getController(update.getCallbackQuery().getData());
                String requestMappingValue = CallbackUtil.getMappingValue(update.getCallbackQuery().getData());

                path = botControllerValue + requestMappingValue;

                controller = container.getControllerMap().get(path);
            }
        }

        if(controller == null) {
            return;
        }

        controller.process(update);
    }
}
