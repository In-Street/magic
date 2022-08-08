package com.magic.interview.service.jackson.customDeserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-05 15:53
 **/
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Deserial extends JsonDeserializer<String> implements ContextualDeserializer {

    private Integer len;

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String text = p.getText();
        String currentName = p.getCurrentName();
        return text.substring(0, len);
    }


    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        int value = property.getAnnotation(T1.class).cutLen();
        log.info(">>>>called createContextual");
        len = value;
        //return new Deserial(value);
        return this;
    }
}
