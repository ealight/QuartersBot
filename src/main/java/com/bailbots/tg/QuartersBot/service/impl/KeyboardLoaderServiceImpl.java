package com.bailbots.tg.QuartersBot.service.impl;

import com.bailbots.tg.QuartersBot.dao.InlineList;
import com.bailbots.tg.QuartersBot.parser.inline.InlineButton;
import com.bailbots.tg.QuartersBot.parser.inline.InlineDbKeyrow;
import com.bailbots.tg.QuartersBot.parser.inline.InlineKeyboard;
import com.bailbots.tg.QuartersBot.parser.inline.InlineKeyrow;
import com.bailbots.tg.QuartersBot.parser.unchanged.StaticButton;
import com.bailbots.tg.QuartersBot.parser.unchanged.StaticKeyboard;
import com.bailbots.tg.QuartersBot.parser.unchanged.StaticKeyrow;
import com.bailbots.tg.QuartersBot.repository.InlineListRepository;
import com.bailbots.tg.QuartersBot.repository.KeyboardObjectRepository;
import com.bailbots.tg.QuartersBot.service.KeyboardLoaderService;
import com.bailbots.tg.QuartersBot.utils.callback.CallbackBuilder;
import com.bailbots.tg.QuartersBot.utils.callback.CallbackUtil;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

@Service
public class KeyboardLoaderServiceImpl implements KeyboardLoaderService {
    private static final String STATIC_KEYBOARD_PATH = "src/main/resources/keyboard/static/";
    private static final String INLINE_KEYBOARD_PATH = "src/main/resources/keyboard/inline/";
    private static final String KEYBOARD_DAO_PATH = "com.bailbots.tg.QuartersBot.dao.";
    private static final String KEYBOARD_REPOSITORY_PATH = "com.bailbots.tg.QuartersBot.repository.";

    private final KeyboardObjectRepository keyboardObjectRepository;
    private final InlineListRepository inlineListRepository;
    private final ApplicationContext applicationContext;

    public KeyboardLoaderServiceImpl(KeyboardObjectRepository keyboardObjectRepository, InlineListRepository inlineListRepository, ApplicationContext applicationContext) {
        this.keyboardObjectRepository = keyboardObjectRepository;
        this.inlineListRepository = inlineListRepository;
        this.applicationContext = applicationContext;
    }

    @Override
    @SneakyThrows
    public ReplyKeyboardMarkup getStaticKeyboardFromXML(String filename) {
        XmlMapper xmlMapper = new XmlMapper();

        StaticKeyboard keyboard = xmlMapper.readValue(
                new File(STATIC_KEYBOARD_PATH + filename + ".xml"), StaticKeyboard.class);

        ReplyKeyboardMarkup result = new ReplyKeyboardMarkup();
        result.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        for (StaticKeyrow keyrow : keyboard.getKeyRows()) {
            KeyboardRow keyboardRow = new KeyboardRow();

            for (StaticButton button : keyrow.getButtons()) {
                keyboardRow.add(new KeyboardButton(button.getText()));
            }
            keyboardRows.add(keyboardRow);
        }

        result.setKeyboard(keyboardRows);
        return result;
    }

    @Override
    @SneakyThrows
    public InlineKeyboardMarkup getInlineKeyboardFromXML(String filename, Long... itemId) {
        XmlMapper mapper = new XmlMapper();

        InlineKeyboard inlineKeyboard = mapper.readValue(
                new File(INLINE_KEYBOARD_PATH + filename + ".xml"), InlineKeyboard.class);

        InlineKeyboardMarkup result = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        if (inlineKeyboard.getKeyrows() != null) {

            for (InlineKeyrow keyrow : inlineKeyboard.getKeyrows()) {
                List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

                for (InlineButton button : keyrow.getButtons()) {
                    CallbackBuilder callback = CallbackBuilder.createCallback();

                    String id = itemId.length == 0 ? "" : itemId[0].toString();
                    String controller = null == inlineKeyboard.getController() ? "" : inlineKeyboard.getController();

                    callback.appendAll(controller, button.getCallback(), id);
                    keyboardRow.add(new InlineKeyboardButton(button.getText())
                            .setCallbackData(callback.toString()));
                }

                keyboardRows.add(keyboardRow);
            }

        }
        result.setKeyboard(keyboardRows);
        return result;
    }

    @Override
    @SneakyThrows
    public InlineKeyboardMarkup getInlineListFromXML(String filename, Integer page) {
        XmlMapper mapper = new XmlMapper();

        InlineKeyboard inlineKeyboard = mapper.readValue(
                new File(INLINE_KEYBOARD_PATH + filename + ".xml"), InlineKeyboard.class);

        InlineKeyboardMarkup result = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        Long maxPagesList = 0L;

        if (inlineKeyboard.getDbKeyrows() != null) {

            for (InlineDbKeyrow keyrow : inlineKeyboard.getDbKeyrows()) {
                int maxItemsOnPage = Integer.parseInt(keyrow.getMaxItemsOnPage());
                String entity = keyrow.getEntity();

                Class<?> entityClass = Class.forName(KEYBOARD_DAO_PATH + entity);

                Method nameMethod = entityClass.getMethod(keyrow.getMethodForName());
                Method callbackMethod = entityClass.getMethod(keyrow.getMethodForCallback());

                for (Object object : keyboardObjectRepository.getObjectListByEntity(entityClass, page, maxItemsOnPage)) {
                    CallbackBuilder callback = CallbackBuilder.createCallback();

                    callback.appendAll("InlineListItem", "GetItem", keyrow.getItemResponseMethod(), callbackMethod.invoke(object));

                    keyboardRows.add(Collections.singletonList(
                            new InlineKeyboardButton(nameMethod.invoke(object).toString())
                                    .setCallbackData(callback.toString())));
                }

                Long number = keyboardObjectRepository.getPageNumbersOfObjectListByEntity(entityClass);

                maxPagesList = pagesCount(number, maxItemsOnPage);
            }
        }

        if (inlineKeyboard.getKeyrows() != null) {

            for (InlineKeyrow keyrow : inlineKeyboard.getKeyrows()) {
                List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

                for (InlineButton button : keyrow.getButtons()) {
                    if (page != -1) {
                        if (button.isPageable()) {
                            button.setText(button.getText() + " " + (page + 1) + "/" + maxPagesList);
                        }

                        CallbackBuilder callback = CallbackBuilder.createCallback();

                        if (!button.getCallback().equals(CallbackUtil.NONE_CALLBACK)) {
                            callback.appendAll(inlineKeyboard.getController(), button.getCallback(), page, maxPagesList, filename);
                            button.setCallback(callback.toString());
                        } else {
                            callback.appendAll(inlineKeyboard.getController(), button.getCallback());
                            button.setCallback(callback.toString());
                        }
                    }
                    keyboardRow.add(new InlineKeyboardButton(button.getText())
                            .setCallbackData(button.getCallback()));
                }

                keyboardRows.add(keyboardRow);
            }

        }
        result.setKeyboard(keyboardRows);
        return result;
    }


    @Override
    @SneakyThrows
    public InlineKeyboardMarkup getInlineListFromXMLWithRepository(String filename, Long inlineListId, Integer page, Object... args) {
        XmlMapper mapper = new XmlMapper();

        InlineKeyboard inlineKeyboard = mapper.readValue(
                new File(INLINE_KEYBOARD_PATH + filename + ".xml"), InlineKeyboard.class);

        InlineKeyboardMarkup result = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        Long maxPagesList = 0L;

        List<Class> parametersList = new ArrayList<>();
        Object[] arguments = new Object[args.length + 2];

        if (inlineKeyboard.getDbKeyrows() != null) {

            for (InlineDbKeyrow keyrow : inlineKeyboard.getDbKeyrows()) {
                int maxItemsOnPage = Integer.parseInt(keyrow.getMaxItemsOnPage());

                String entity = keyrow.getEntity();

                Class<?> clazz = Class.forName(KEYBOARD_DAO_PATH + entity);
                Class<?> repository = Class.forName(KEYBOARD_REPOSITORY_PATH + keyrow.getRepository());

                Method methodParameters = Arrays.stream(repository.getMethods())
                        .filter(method -> method.getName().equals(keyrow.getRepoMethod()))
                        .findFirst()
                        .get();

                String beanName = keyrow.getRepository().substring(0, 1).toLowerCase() + keyrow.getRepository().substring(1);

                Method repoMethod = repository.getMethod(keyrow.getRepoMethod(), methodParameters.getParameterTypes());

                parametersList.addAll(Arrays.asList(methodParameters.getParameterTypes()));

                Method nameMethod = clazz.getMethod(keyrow.getMethodForName());
                Method callbackMethod = clazz.getMethod(keyrow.getMethodForCallback());

                for (int i = 0; i < args.length; i++) {
                    arguments[i] = args[i];
                }

                arguments[args.length] = page;
                arguments[args.length + 1] = maxItemsOnPage;

                List<Object> objectList = (List<Object>) repoMethod.invoke(applicationContext.getBean(beanName), arguments);

                for (Object object : objectList) {
                    CallbackBuilder callback = CallbackBuilder.createCallback();
                    callback.appendAll("InlineListItem", "GetItem", keyrow.getItemResponseMethod(), callbackMethod.invoke(object));
                    keyboardRows.add(Collections.singletonList(
                            new InlineKeyboardButton(nameMethod.invoke(object).toString())
                                    .setCallbackData(callback.toString())));
                }

                String countMethodName = "count" + keyrow.getRepoMethod().substring(keyrow.getRepoMethod().indexOf("By"));

                Class[] countParameters = Arrays.asList(repoMethod.getParameterTypes()).subList(0, args.length).toArray(Class[]::new);

                Method countRepoMethod = repository.getMethod(countMethodName, countParameters);

                Long number = (Long) countRepoMethod.invoke(applicationContext.getBean(beanName), args);

                maxPagesList = pagesCount(number, maxItemsOnPage);
            }
        }

        if (inlineKeyboard.getKeyrows() != null) {

            for (InlineKeyrow keyrow : inlineKeyboard.getKeyrows()) {
                List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

                for (InlineButton button : keyrow.getButtons()) {

                    CallbackBuilder callback = CallbackBuilder.createCallback();

                    if (page != -1) {
                        if (button.isPageable()) {
                            button.setText(button.getText() + " " + (page + 1) + "/" + maxPagesList);
                        }

                        if (!button.getCallback().equals(CallbackUtil.NONE_CALLBACK)) {
                            InlineList inlineList;

                            if (inlineListId != 0) {
                                inlineList = inlineListRepository.getById(inlineListId);
                            }
                            else {
                                CallbackBuilder largeCallback = CallbackBuilder.createCallback();

                                largeCallback.appendAll(inlineKeyboard.getController(), button.getCallback(), page, maxPagesList, filename, parametersList, Arrays.toString(args));
                                inlineList = inlineListRepository.save(InlineList.builder().largeCallback(largeCallback.toString()).build());
                            }

                            callback.appendAll(inlineKeyboard.getController(), button.getCallback(), page, maxPagesList, filename, inlineList.getId());
                            button.setCallback(callback.toString());
                        } else {
                            callback.appendAll(inlineKeyboard.getController(), button.getCallback());
                            button.setCallback(callback.toString());
                        }
                    }
                    keyboardRow.add(new InlineKeyboardButton(button.getText())
                            .setCallbackData(button.getCallback()));
                }

                keyboardRows.add(keyboardRow);
            }

        }
        result.setKeyboard(keyboardRows);
        return result;
    }

    private Long pagesCount(Long number, int maxItemsOnPage) {
        int pageIncrementer = number % maxItemsOnPage == 0 ? 0 : 1;
        return (number / maxItemsOnPage + pageIncrementer);
    }
}
