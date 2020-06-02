package com.bailbots.tg.QuartersBot.parser.unchanged;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "static-keyrow")
public class StaticKeyrow {

    @JacksonXmlElementWrapper(useWrapping = false, localName = "static-button")
    @JacksonXmlProperty(localName = "static-button")
    private List<StaticButton> buttons;


}
