package com.gorbunov.spring.dao;

import java.util.List;
import java.util.Map;

public interface HostDAO {
	public boolean isDatabaseExist (String schemaName);
	public boolean isTableExist (String tableName, String schemaName);
    public List<String> gerPrimaryKeys (String schemaName, String tableName);
    public Map<String, Integer> getFieldsAndOrdinalPosition(String schemaName, String tableName);
    public List<String> getRandomValueOfFields(String schemaName, String tableName);
    public String getDdlCreateTable(String schemaName, String tableName);
}