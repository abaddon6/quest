package com.volvo.jvs.quest.util.collection;

import java.util.Collection;

import org.springframework.stereotype.Service;

@Service
public class CollectionUtilImpl implements CollectionUtil {

	@Override
	public void setOrder(Collection<? extends Orderable> orderableList) {
		int counter = 1;
		for(Orderable order : orderableList) {
			order.setOrderNumber(counter++);
		}		
	}
}
