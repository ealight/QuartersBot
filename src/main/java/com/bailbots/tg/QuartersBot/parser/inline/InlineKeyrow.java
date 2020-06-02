package com.bailbots.tg.QuartersBot.parser.inline;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "inline-keyrow")
public class InlineKeyrow {

    @JacksonXmlElementWrapper(useWrapping = false, localName = "inline-button")
    @JacksonXmlProperty(localName = "inline-button")
    private List<InlineButton> buttons;

}
