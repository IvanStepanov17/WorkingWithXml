package dbloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import type.NaturalKey;

/**
 * В данном классе производится считывание данных из файла настроек для дальнейшей загрузки драйвера, создания соединения и
 * выполнения запроса на синхронизацию данных БД с xml-файлом. 
 * @see dbloader.Dom
 * @author Ivan
 * @version 1.0
 */
public class XMLloader {
	
	private static final Logger logger = Logger.getLogger(XMLloader.class);
	
	/**
	 * @param absolutePath - абсолютный путь для создания xml-файла
	 * @return подтверждаем/опровергаем выполнение команды на синхронизацию данных БД с xml-файлом.
	 */
	public boolean load(String absolutePath) {

		Connection connection = null;
		Dom dom = new Dom();
		File file = new File(absolutePath);
		boolean flag = false;
		HashMap<NaturalKey, String> map;
		
		/*
		 * Получаем данные из config.properties
		 */
		FileInputStream fis = null;
		Properties property = new Properties();

		String host = null;
		String login = null;
		String password = null;
		String driver = null;
		
		logger.info("Данные файла настроек получены");
		try {
			fis = new FileInputStream("src/main/resources/config.properties");
			property.load(fis);

			host = property.getProperty("database");
			login = property.getProperty("dbuser");
			password = property.getProperty("dbpassword");
			driver = property.getProperty("driver");

		} catch (IOException e) {
			logger.error("ОШИБКА: Файл свойств отсуствует!", e);
		} finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		dom.setFile(file);
		logger.info("получаем HashMap с ключом в виде натурального ключа БД (поля depcode и depjob) и значением (поле description) в виде строки.");
		map = dom.getValuesXML();
		
		try {
			logger.info("Загружаем драйвер и создаем соединение");
			Class.forName(driver);
			connection = DriverManager.getConnection(host, login, password);
			logger.info("Автокоммит отключен");
			connection.setAutoCommit(false);
			Statement statement = null;
			statement = connection.createStatement();
			logger.info("Cоздаем временную таблицу в БД из HashMap для осуществления запросов на вставку, удаление и обновление.");
			statement.executeUpdate(
					"CREATE TABLE tmp_structure " +
					" (id SERIAL not NULL, " +
					" tmp_depcode character varying(20) NOT NULL, " +
					" tmp_depjob character varying(100) NOT NULL, " +
					" tmp_description character varying(255) NOT NULL, " +
					" CONSTRAINT tmp_structure_pkey PRIMARY KEY (id)," +
					" CONSTRAINT tmp_structure_depcode_depjob_key UNIQUE (tmp_depcode, tmp_depjob))");
			for (Map.Entry<NaturalKey, String> pair : map.entrySet()) {
				statement.executeUpdate(
					"INSERT INTO tmp_structure " +
					" (tmp_depcode, tmp_depjob, tmp_description) " + "VALUES ('" +
					 pair.getKey().getDepCode() + "', '" + pair.getKey().getDepJob() + "', '" + pair.getValue() + "')");
			}
			/* 
			 * Запрос на вставку. Если натуральные ключи из основной и временной таблиц не совпадают, то 
			 * вставляем значения из временной таблицы в основную таблицу БД.
			 */
			logger.info("Запрос на вставку.");
			statement.executeUpdate(
					" INSERT INTO structure(depcode, depjob, description) " +
					" SELECT tmp_depcode, tmp_depjob, tmp_description " +
					" FROM tmp_structure " +
					" WHERE NOT EXISTS " +
					" (SELECT * FROM structure WHERE depcode = tmp_depcode AND depjob = tmp_depjob)");
			
			/*
			 * Запрос на обновление. Если натуральные ключи таблиц совпадают, а поле description различны, то значение этого поля в основной 
			 * таблице обновляются значениями из временной таблицы 
			 */
			logger.info("Запрос на обновление.");
			statement.executeUpdate(
					" UPDATE structure " +
					" SET description = " +
							"( " +
							" SELECT tmp_description " +
							" FROM tmp_structure " + 
							" WHERE tmp_depcode = depcode AND tmp_depjob = depjob " +
							") " + 
					" WHERE EXISTS " +
							"( " +
							" SELECT tmp_description " +
							" FROM tmp_structure " +
							" WHERE tmp_depcode = depcode AND tmp_depjob = depjob AND tmp_description <> description " +
							") ");
			/*
			 * Запрс на удаление. Если натуральные ключи из временной таблицы не совпадают с ключами из основной таблицы, 
			 * тогда данные поля удаляются из основной таблицы
			 */
			logger.info("Запрос на удаление.");
			statement.executeUpdate(
					" DELETE FROM tmp_structure " +
					" WHERE NOT EXISTS " +
							"( " +
							" SELECT * " +
							" FROM structure " +
							" WHERE depcode = tmp_depcode AND depjob = tmp_depjob" +
							") ");

			logger.info("Удаление временной таблицы.");
			statement.executeUpdate(
					" DROP TABLE tmp_structure ");
			
			logger.info("Транзацкция завершена.");
			connection.commit();
			
			// Реализация запроса (непроверенная) всех трёх запросов при помощи MERGE. Оказалось, что он не поддерживается postgreSQL
//			statement.executeUpdate(
//				"MERGE INTO structure " +
//				" USING tmp_structure " +
//				" ON (structure.depcode = tmp_structure.tmp_depcode AND structure.depjob = tmp_structure.tmp_depjob" +
//				" WHEN MATCHED THEN UPDATE SET structure.description = tmp_structure.tmp_description" +
//				" WHEN NOT MATCHED THEN INSERT VALUES (tmp_structure.tmp_depcode, tmp_structure.tmp_depjob, tmp_structure.tmp_description" +
//				" WHEN NOT MATCHED BY SOURCE THEN DELETE");
//			connection.commit();
			
			flag = true;
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
		}
		return flag;
	}
}