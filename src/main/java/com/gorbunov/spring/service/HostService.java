package com.gorbunov.spring.service;

public interface HostService {
	public String getCreate(String schemaName, String tableName);
    public String getSelect(String schemaName, String tableName);
    public String getUpdate(String schemaName, String tableName);
}
