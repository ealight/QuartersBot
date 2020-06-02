package com.bailbots.tg.QuartersBot.service.impl;

import com.bailbots.tg.QuartersBot.dao.User;
import com.bailbots.tg.QuartersBot.service.*;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Component
public class MessageValueServiceImpl implements MessageValueService {
    private final MessageService messageService;
    private final UserSessionService userSessionService;

    private Map<Long, String> telegramIdToAction = new HashMap<>();

    public MessageValueServiceImpl(MessageService messageService, UserSessionService userSessionService) {
        this.messageService = messageService;
        this.userSessionService = userSessionService;
    }


    @Override
    public void getValueFromMessage(Update update, Long telegramId) {

        switch(telegramIdToAction.get(telegramId)) {

            case "houseSeatsNumber": {
                int minSeatsNumber;

                try {
                    minSeatsNumber = Integer.parseInt(update.getMessage().getText());
                }
                catch (NumberFormatException e) {
                    messageService.sendMessage("❌ Введите количество мест в чат цифрой", telegramId);
                    return;
                }

                User user = userSessionService.getUserFromSession(telegramId);

                user.getHouseFilter().setMinSeatsNumber(minSeatsNumber);

                String text = "Хорошо, мы подберем вам дома с количеством мест больше " + minSeatsNumber + "."
                        + " Желаете еще поставить какие-то фильтры?";

                messageService.sendMessage(text, telegramId);

                telegramIdToAction.remove(telegramId);
                break;
            }

            case "houseMinPrice": {
                int price;

                try {
                    price = Integer.parseInt(update.getMessage().getText());
                }
                catch (NumberFormatException e) {
                    messageService.sendMessage("❌ Введите минимальную цену в чат цифрой", telegramId);
                    return;
                }

                User user = userSessionService.getUserFromSession(telegramId);

                user.getHouseFilter().setMinPrice(price);

                String text = "Хорошо, мы подберем вам дома с минимальной ценой " + price + "."
                        + " Желаете еще поставить какие-то фильтры?";

                messageService.sendMessage(text, telegramId);

                telegramIdToAction.remove(telegramId);
                break;
            }

            case "houseMaxPrice": {
                int price;

                try {
                    price = Integer.parseInt(update.getMessage().getText());
                }
                catch (NumberFormatException e) {
                    messageService.sendMessage("❌ Введите максимальную цену в чат цифрой", telegramId);
                    return;
                }

                User user = userSessionService.getUserFromSession(telegramId);

                user.getHouseFilter().setMaxPrice(price);

                String text = "Хорошо, мы подберем вам дома с максимальной ценой " + price + "."
                        + " Желаете еще поставить какие-то фильтры?";

                messageService.sendMessage(text, telegramId);

                telegramIdToAction.remove(telegramId);
                break;
            }

        }

    }

    @Override
    public void turnGetMessageValueForUser(Long telegramId, String action) {
        telegramIdToAction.put(telegramId, action);
    }

    @Override
    public boolean isGetValueTurn(Long telegramId) {
        return telegramIdToAction.containsKey(telegramId);
    }


}
