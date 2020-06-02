package com.bailbots.tg.QuartersBot.parser.unchanged;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "static-keyboard")
@JsonIgnoreProperties(ignoreUnknown = true)
public class StaticKeyboard {

    @JacksonXmlElementWrapper(useWrapping = false, localName = "static-keyrow")
    @JacksonXmlProperty(localName = "static-keyrow")
    private List<StaticKeyrow> KeyRows;

}
