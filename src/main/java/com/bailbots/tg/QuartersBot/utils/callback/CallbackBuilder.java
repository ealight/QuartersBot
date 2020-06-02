package com.bailbots.tg.QuartersBot.utils.callback;

public class CallbackBuilder {
    private String callback = "";

    public static CallbackBuilder createCallback() {
        return new CallbackBuilder();
    }

    public void append(Object unit) {
        callback = callback.concat(unit.toString()).concat(CallbackUtil.SEPARATOR);
    }

    public void appendAll(Object... units) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < units.length; i++) {
            result.append(units[i].toString()).append(CallbackUtil.SEPARATOR);
        }

        callback = callback.concat(result.toString());
    }

    public String toString() {
        return callback;
    }
}
