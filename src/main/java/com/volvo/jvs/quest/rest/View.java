package com.volvo.jvs.quest.rest;

public class View { 
	public interface ListView {}
	public interface DetailedView extends ListView {} 
	public interface ReportView extends ListView {} 
	public interface ManagementView extends DetailedView{}
}
