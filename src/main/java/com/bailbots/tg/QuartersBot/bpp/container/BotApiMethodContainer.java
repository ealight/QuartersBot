package com.bailbots.tg.QuartersBot.bpp.container;

import com.bailbots.tg.QuartersBot.bpp.BotApiMethodController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class BotApiMethodContainer {
    private static final Logger LOGGER = LogManager.getLogger(BotApiMethodContainer.class);

    private Map<String, BotApiMethodController> controllerMap;

    public static BotApiMethodContainer getInstanse() {
        return Holder.INST;
    }

    public void addBotController(String path, BotApiMethodController controller) {
        if(controllerMap.containsKey(path)) throw new RuntimeException("path " + path + " already add");
        LOGGER.trace("add telegram bot controller for path: " +  path);
        controllerMap.put(path, controller);
    }

    public Map<String, BotApiMethodController> getControllerMap() {
        return controllerMap;
    }

    public BotApiMethodController getBotApiMethodController(String path) {
        return controllerMap.get(path);
    }

    private BotApiMethodContainer() {
        controllerMap = new HashMap<>();
    }

    private static class Holder{
        final static BotApiMethodContainer INST = new BotApiMethodContainer();
    }
}
