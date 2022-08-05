package com.magic.interview.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-05 15:53
 **/
public class Deserial extends JsonDeserializer<String> implements ContextualDeserializer {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String text = p.getText();
        String currentName = p.getCurrentName();
        return null;
    }


    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        int value = property.getAnnotation(T1.class).value();
        return ctxt.findRootValueDeserializer(property.getType());
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt, String intoValue) throws IOException {
        String text = p.getText();
        return super.deserialize(p, ctxt, intoValue);
    }
}
