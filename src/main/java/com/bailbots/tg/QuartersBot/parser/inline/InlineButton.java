package com.bailbots.tg.QuartersBot.parser.inline;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "inline-button")
public class InlineButton {

    @JacksonXmlText
    private String text;

    @JacksonXmlProperty(isAttribute = true)
    private boolean pageable;

    @JacksonXmlProperty(isAttribute = true)
    private String callback;

}
