package com.volvo.jvs.quest.utils.datetime;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.volvo.jvs.quest.service.configuration.ConfigurationService;

@Service
public class DateTimeUtilImpl implements DateTimeUtil{

	@Autowired
	private ConfigurationService configurationService;
	
	@Override
	public Date getCurrentDate() {
		return Calendar.getInstance(TimeZone.getTimeZone(
				getConfigurationService().getServerTimeZone())).getTime();
	}
	
	protected ConfigurationService getConfigurationService() {
		return configurationService;
	}
}
