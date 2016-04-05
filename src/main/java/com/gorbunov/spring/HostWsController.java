package com.gorbunov.spring;

import com.gorbunov.spring.service.HostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value = "/ws", produces = MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"  )

public class HostWsController {

	private HostService hostService;
	
	@Autowired(required=true)
	@Qualifier(value="hostService")
	public void setPersonService(HostService hs){
		this.hostService = hs;
	}
	/*
	http://localhost:8080/SpringMVCHibernate-REST/ws/getCreate/{schemaName}/{tableName}
	http://localhost:8080/SpringMVCHibernate-REST/ws/getSelect/{schemaName}/{tableName}
	http://localhost:8080/SpringMVCHibernate-REST/ws/getUpdate/{schemaName}/{tableName}
	*/
	
	@RequestMapping(value = "/getCreate/{schemaName}/{tableName}", method = RequestMethod.GET)
	@ResponseBody 
	public String getCreate(@PathVariable("schemaName") String schemaName, @PathVariable("tableName") String tableName)
	{   
		return this.hostService.getCreate(schemaName, tableName);
	}
	
	@RequestMapping(value = "/getSelect/{schemaName}/{tableName}", method = RequestMethod.GET)
	@ResponseBody 
	public String getSelect(@PathVariable("schemaName") String schemaName, @PathVariable("tableName") String tableName)
	{   
		return this.hostService.getSelect(schemaName, tableName);
	}
	
	@RequestMapping(value = "/getUpdate/{schemaName}/{tableName}", method = RequestMethod.GET)
	@ResponseBody 
	public String getUpdate(@PathVariable("schemaName") String schemaName, @PathVariable("tableName") String tableName)
	{   
        return this.hostService.getUpdate(schemaName, tableName);
	}

	

	
}
