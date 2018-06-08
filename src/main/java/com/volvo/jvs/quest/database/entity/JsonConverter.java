package com.volvo.jvs.quest.database.entity;

import java.io.IOException;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;

public class JsonConverter<T> implements AttributeConverter<T, String> {

	private final TypeToken<T> type = new TypeToken<T>(getClass()) {
		private static final long serialVersionUID = 7744655508667942906L;
	};
    
	@Override
	public String convertToDatabaseColumn(T object) {
		if (object == null) {
            return null;
        } else {
        	ObjectMapper objectMapper = new ObjectMapper();
        	try {
				return objectMapper.writeValueAsString(object);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e.getMessage());
			}
        }
	}

	@Override
	public T convertToEntityAttribute(String objectAsString) {		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			if(objectAsString == null) {
				return null;
			}
			return objectMapper.readValue(objectAsString, objectMapper.constructType(type.getType()));
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}