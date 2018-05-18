package com.volvo.jvs.quest.util.datetime;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DateTimeUtilImpl implements DateTimeUtil{

	@Value("${server.time-zone}")
	private String serverTimeZone;
	
	@Override
	public Date getCurrentDate() {
		return Calendar.getInstance(TimeZone.getTimeZone(
				getServerTimeZone())).getTime();
	}

	protected String getServerTimeZone() {
		return serverTimeZone;
	}
}
