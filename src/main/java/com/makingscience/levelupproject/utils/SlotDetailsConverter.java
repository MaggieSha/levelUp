package com.makingscience.levelupproject.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.makingscience.levelupproject.model.details.slot.SlotDetails;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SlotDetailsConverter implements AttributeConverter<SlotDetails, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public String convertToDatabaseColumn(SlotDetails address) {
        objectMapper.registerModule(new JavaTimeModule());
        try {
            return objectMapper.writeValueAsString(address);
        } catch (JsonProcessingException jpe) {
            return null;
        }
    }

    @Override
    public SlotDetails convertToEntityAttribute(String value) {
        objectMapper.registerModule(new JavaTimeModule());
        try {
            return objectMapper.readValue(value, SlotDetails.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}