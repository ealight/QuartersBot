package com.bailbots.tg.QuartersBot.utils.callback;

import com.google.common.primitives.Primitives;
import org.apache.commons.beanutils.ConvertUtils;

import java.util.*;

public class CallbackUtil {
    public static final String SEPARATOR = "&";
    public static final String NONE_CALLBACK = "none";

    public static String getController(String callbackData) { return callbackData.split(SEPARATOR)[0].trim(); }

    public static String getMappingValue(String callbackData) { return callbackData.split(SEPARATOR)[1].trim(); }

    public static List<Object> parametersParser(String callbackData) {
        List<Class> argsType = new ArrayList<>();
        List<Object> args = new ArrayList<>();

        List<String> types = Arrays.asList(callbackData.split(SEPARATOR)[5].replaceAll("[^A-Za-z-,._]", "").trim().split(","));
        List<String> values = Arrays.asList(callbackData.split(SEPARATOR)[6].replaceAll("[^A-Za-z-,0-9._]", "").trim().split(","));
        Set<Class<?>> javaLangTypes = new HashSet<>();

        javaLangTypes.add(String.class);
        javaLangTypes.addAll(Primitives.allWrapperTypes());

        types.stream().forEach(type -> {
            Primitives.allPrimitiveTypes().stream()
                    .filter(primitiveType -> primitiveType.getName().equals(type))
                    .findFirst().ifPresent(argsType::add);

            javaLangTypes.stream()
                    .filter(primitiveType -> primitiveType.getName().equals(type.replace("class", "")))
                    .findFirst().ifPresent(argsType::add);
        });

        for (int i = 0; i < values.size(); i++) {
            args.add(ConvertUtils.convert(values.get(i), argsType.get(i)));
        }
        return args;
    }

}
