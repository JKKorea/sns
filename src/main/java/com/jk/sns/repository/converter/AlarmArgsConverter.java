package com.jk.sns.repository.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jk.sns.model.AlarmArgs;
import javax.persistence.AttributeConverter;

public class AlarmArgsConverter implements AttributeConverter<AlarmArgs, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(AlarmArgs information) {
        try {
            return objectMapper.writeValueAsString(information);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public AlarmArgs convertToEntityAttribute(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, AlarmArgs.class);
        } catch (Exception e) {
            return null;
        }
    }
}

