package com.volvo.jvs.quest.util.rest;

import java.util.List;

import org.springframework.hateoas.Resource;

public interface RestUtil {

	public <T> Resource<T> getResource(T object);
	
	public <T> List<Resource<T>> getResourceList(List<T> objectList);
	
	public <T> Resource<T> getEmptyResource(Class<T> clazz);
}
