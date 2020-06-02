package com.bailbots.tg.QuartersBot.utils.callback;

import java.util.HashMap;
import java.util.Map;

public class CallbackParser {
    private static final Integer SYSTEM_PLACE = 2;

    private static Map<String,String> arguments = new HashMap<>();

    public static CallbackParser parseCallback(String callbackData, String... argNames) {
        for (int i = 0; i < argNames.length; i++) {
            arguments.put(argNames[i], callbackData.split(CallbackUtil.SEPARATOR)[i + SYSTEM_PLACE].trim());
        }
        return new CallbackParser();
    }

    public String getStringByName(String name) {
        return getArgByName(name);
    }

    public Long getLongByName(String name) {
        return Long.parseLong(getArgByName(name));
    }

    public Integer getIntByName(String name) {
        return Integer.parseInt(getArgByName(name));
    }

    private String getArgByName(String name) {
        return arguments.get(name);
    }
}
