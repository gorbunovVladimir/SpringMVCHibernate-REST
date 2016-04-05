package com.gorbunov.spring.service;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gorbunov.spring.dao.HostDAO;


@Service
public class HostServiceImpl implements HostService {

	private HostDAO hostDAO;
	
	public void setHostDAO(HostDAO hostDAO) {
		this.hostDAO = hostDAO;
	}
	
	@Transactional
	@Override
	public String getCreate(String schemaName, String tableName) {
		if (this.hostDAO.isDatabaseExist (schemaName) && this.hostDAO.isTableExist (tableName,schemaName)){
			return this.hostDAO.getDdlCreateTable(schemaName, tableName);
        }
		return "Переданная таблица или база данных не существует или права пользователя не соответствуют ожиданиям";
	}
	
	@Transactional
	@Override
	public String getSelect(String schemaName, String tableName) {
		if (this.hostDAO.isDatabaseExist (schemaName) && this.hostDAO.isTableExist (tableName,schemaName)){
			List<String> primaryKeys=this.hostDAO.gerPrimaryKeys(schemaName, tableName);
			Map<String, Integer> fieldsAndOrdinalPosition=this.hostDAO.getFieldsAndOrdinalPosition (schemaName, tableName);
			String keysAndRandomValue="";
			for (int i=0;i<primaryKeys.size();i++){
				keysAndRandomValue+=String.format(", %s=%s", primaryKeys.get(i),this.hostDAO.getRandomValueOfFields(schemaName, tableName).get(fieldsAndOrdinalPosition.get(primaryKeys.get(i))-1));
			}
			String partOfResponce="";
			for (Map.Entry<String, Integer> pair : fieldsAndOrdinalPosition.entrySet()){ 
				partOfResponce+=String.format(", %s", pair.getKey());
            }
			//select id, phoneNumber, name from ContactDB.Person where id=7
			return String.format("select %s from %s.%s where %s",partOfResponce.replaceFirst(", ", ""),schemaName,tableName,keysAndRandomValue.replaceFirst(", ", ""));
        }
		return "Переданная таблица или база данных не существует или права пользователя не соответствуют ожиданиям";
	}
	@Transactional
	@Override
	public String getUpdate(String schemaName, String tableName) {
		if (this.hostDAO.isDatabaseExist (schemaName) && this.hostDAO.isTableExist (tableName,schemaName)){
			List<String> primaryKeys=this.hostDAO.gerPrimaryKeys(schemaName, tableName);
			Map<String, Integer> fieldsAndOrdinalPosition=this.hostDAO.getFieldsAndOrdinalPosition (schemaName, tableName);
			String keysAndRandomValue="";
			String partOfResponce="";
			for (int i=0;i<primaryKeys.size();i++){
				keysAndRandomValue+=String.format(", %s=%s", primaryKeys.get(i),this.hostDAO.getRandomValueOfFields(schemaName, tableName).get(fieldsAndOrdinalPosition.get(primaryKeys.get(i))-1));
			}
			keysAndRandomValue=keysAndRandomValue.replaceFirst(", ", "");
			for (Map.Entry<String, Integer> pair : fieldsAndOrdinalPosition.entrySet()){
				if (!primaryKeys.contains(pair.getKey()))
			       partOfResponce+=String.format(", %s=%s", pair.getKey(),this.hostDAO.getRandomValueOfFields(schemaName, tableName).get(fieldsAndOrdinalPosition.get(pair.getKey())-1));
		    }
			//update ContactDB.Person set id=2, phoneNumber='hiRJi', name='bjrRL' where id=2
            return String.format("update %s.%s set %s%s where %s",schemaName,tableName,keysAndRandomValue,partOfResponce,keysAndRandomValue);
        }
		return "Переданная таблица или база данных не существует или права пользователя не соответствуют ожиданиям";
	}

}
