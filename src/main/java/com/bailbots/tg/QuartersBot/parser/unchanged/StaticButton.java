package com.bailbots.tg.QuartersBot.parser.unchanged;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "static-button")
public class StaticButton {

    @JacksonXmlText
    private String text;

}
