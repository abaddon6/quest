package com.volvo.jvs.quest.util.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Service;

@Service
public class RestUtilImpl implements RestUtil{

	@Override
	public <T> Resource<T> getResource(T object) {
		if(object != null) {
			return new Resource<>(object);
		}
		return null;
	}
	
	@Override
	public <T> List<Resource<T>> getResourceList(List<T> objectList) {
		List<Resource<T>> result = null;
		if(objectList != null) {
			result = new ArrayList<>();
			for(T object : objectList) {
				result.add(getResource(object));
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Resource<T> getEmptyResource(Class<T> clazz) {
		return (Resource<T>) new Resource<>("");
	}
}
