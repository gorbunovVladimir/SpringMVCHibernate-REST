package com.gorbunov.spring.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class HostDAOImpl implements HostDAO {
	private static final Logger logger = LoggerFactory.getLogger(HostDAOImpl.class);

	private SessionFactory sessionFactory;
	private static final Random random = new Random();
	public void setSessionFactory(SessionFactory sf){
		this.sessionFactory = sf;
	}
	//Проверка на существования базы данных/соответствия ожиданиям прав пользователя
	@Override
	public boolean isDatabaseExist(String schemaName) {
		try{
		 Session session = this.sessionFactory.getCurrentSession();
		 @SuppressWarnings("unchecked")
		 List<String> bds = session.createSQLQuery("SHOW DATABASES LIKE '"+schemaName+"'").list();
		 if (bds.contains(schemaName.toLowerCase())){ 
			 logger.info("Переданная база данных корректна "+schemaName);
		     return true;
		 }
		 else logger.info("Переданная база данных не существует или права пользователя не соответствуют ожиданиям"+schemaName);
		}
		catch (Exception e){logger.info("Exception: "+e.getMessage());}
		return false;
	}
	//Проверка на существования таблицы/соответствия ожиданиям прав пользователя
	@Override
	public boolean isTableExist(String tableName, String schemaName) {
		Session session = this.sessionFactory.getCurrentSession();
		try{
		 @SuppressWarnings("unchecked")
		 List<String> tables = session.createSQLQuery("SHOW TABLES FROM "+schemaName+" LIKE '"+tableName+"'").list();
		 if (tables.contains(tableName.toLowerCase())) { 
			logger.info("Переданная таблица корректна "+tableName);
		    return true;
		 }
		 else logger.info("Переданная таблица не существует или права пользователя не соответствуют ожиданиям "+tableName);
			
		}
		catch (Exception e){logger.info("Exception: "+e.getMessage());}
		return false;
	}

    @Override
	public List<String> gerPrimaryKeys(String schemaName, String tableName) {
		Session session = this.sessionFactory.getCurrentSession();
		String key=null;
		
		List<String> primaryKeys=new ArrayList<String>();
		String partOfLogs="";
		try{
		 @SuppressWarnings("unchecked")	
		 List<Object> primaryKeysObject = session.createSQLQuery("SELECT k.column_name FROM information_schema.table_constraints t JOIN information_schema.key_column_usage k USING(constraint_name,table_schema,table_name) WHERE t.constraint_type='PRIMARY KEY' AND t.table_schema='"+schemaName+"' AND t.table_name='"+tableName+"'").list();		
		 for (int i=0;i<primaryKeysObject.size();i++){
			if (primaryKeysObject.get(i) instanceof String) {
				key=((String)primaryKeysObject.get(i)).trim();
				primaryKeys.add(key);
				partOfLogs+=String.format(", %s", key);
		    }
		 }
		 logger.info(String.format("Primary keys %s в таблице %s базы данных  %s", partOfLogs.replaceFirst(", ", ""),tableName,schemaName));
         }
		catch (Exception e){logger.info("Exception: "+e.getMessage());}
		return primaryKeys;
	}
	
    @Override
	public String getDdlCreateTable(String schemaName, String tableName) {
    	Session session = this.sessionFactory.getCurrentSession();
    	String ddlCreateTable="";
		try{
			List<Object[]> crTable=session.createSQLQuery("SHOW CREATE TABLE "+schemaName+"."+tableName).list();
			ddlCreateTable=(String)(crTable.get(0)[1]);
			logger.info(String.format("DDL на создание таблицы %s", ddlCreateTable));
		}	
		catch (Exception e){logger.info("Exception: "+e.getMessage());}
		return ddlCreateTable;
	}
	
    @Override
    //берем случайную выборку значений полей
	public List<String> getRandomValueOfFields(String schemaName, String tableName) {
    	Session session = this.sessionFactory.getCurrentSession();
    	List<String> valuesOfRandomSelect=new ArrayList<String>();
    	try{ 
    		 //кол-во записей в таблице
			 int count=Integer.parseInt(session.createSQLQuery("SELECT COUNT(*) FROM "+schemaName+"."+tableName).list().get(0).toString()); 
	         //берем произвольную запись
			 int random1=random.nextInt(count);
	         
			 List<Object[]> valuesOfRandomSelectObject = session.createSQLQuery("SELECT * FROM "+schemaName+"."+tableName+" LIMIT "+random1+", 1").list();
			 
			 for (int j=0;j<valuesOfRandomSelectObject.get(0).length;j++){
				 /*if (valuesOfRandomSelectObject.get(0)[j] instanceof String) {
					    String temp=((String)valuesOfRandomSelectObject.get(0)[j]).trim();
		 				valuesOfRandomSelect.add(temp);
		 				//
		 				logger.info("Random Value: "+temp);
		 		 }*/
				 String temp=valuesOfRandomSelectObject.get(0)[j].toString().trim();
	 			 valuesOfRandomSelect.add(temp);
	 			 logger.info("Random Value: "+temp);
			 }
		}	
		catch (Exception e){logger.info("Exception: "+e.getMessage());}
		return valuesOfRandomSelect;
	}
       
    
 // Получение Map<String, String>, где Key - имя поля, Value - ordinal Position (в переданной таблице базы данных)
    @Override
	public Map<String, Integer> getFieldsAndOrdinalPosition(String schemaName, String tableName) {
		Session session = this.sessionFactory.getCurrentSession();
		Map<String, Integer> fieldsAndOrdinalPosition = new HashMap<String, Integer>();
		try{
		 @SuppressWarnings("unchecked")
		 List<Object> fields = session.createSQLQuery("SELECT COLUMN_NAME FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = '"+schemaName+"' AND TABLE_NAME = '"+tableName+"' ORDER BY ORDINAL_POSITION").list();		
		 for (int i=0;i<fields.size();i++){
			if (fields.get(i) instanceof String) {
				String column=((String)fields.get(i)).trim();

                List<Object> ordinalPosition = session.createSQLQuery("select ordinal_position from information_schema.columns where table_schema='"+schemaName+"' and table_name='"+tableName+"' and column_name='"+column+"'").list();
			    Integer ordinalPos=Integer.parseInt((ordinalPosition.get(0)).toString().trim());;
			    fieldsAndOrdinalPosition.put(column, ordinalPos);	
			    logger.info("Field "+column+" OrdPos"+ordinalPos);
			}
		 }
		for (Map.Entry<String, Integer> pair : fieldsAndOrdinalPosition.entrySet())        
			logger.info(String.format("Поле %s с ordinal position %d присутствует в таблице %s базы данных %s",pair.getKey(),pair.getValue(),tableName,schemaName));
        }
		catch (Exception e){logger.info("Exception: "+e.getMessage());}
		return fieldsAndOrdinalPosition;
	}
}
