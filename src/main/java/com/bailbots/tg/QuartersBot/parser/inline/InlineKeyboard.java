package com.bailbots.tg.QuartersBot.parser.inline;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "inline-keyboard")
public class InlineKeyboard {

    @JacksonXmlElementWrapper(useWrapping = false, localName = "inline-db-keyrow")
    @JacksonXmlProperty(localName = "inline-db-keyrow")
    private List<InlineDbKeyrow> dbKeyrows;

    @JacksonXmlElementWrapper(useWrapping = false, localName = "inline-keyrow")
    @JacksonXmlProperty(localName = "inline-keyrow")
    private List<InlineKeyrow> keyrows;

    @JacksonXmlProperty(isAttribute = true)
    private String controller;

    @Getter(AccessLevel.NONE)
    @JacksonXmlProperty(isAttribute = true)
    private String noNamespaceSchemaLocation;
}
