package dbloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * В данном классе производится считывание данных из файла настроек для дальнейшей загрузки драйвера, создания соединения и
 * выполнения запроса на выборку данных из БД. Вызываются методы на создание xml-файла и на запись в него информации из БД.
 * @see dbloader.Dom
 * @author Ivan
 * @version 1.0
 */
public class DBloader {
	
	private static final Logger logger = LogManager.getLogger(DBloader.class);

	/**
	 * @param absolutePath - абсолютный путь для создания xml-файла
	 * @return подтверждаем/опровергаем выполнение команды на выгрузку данных из БД в xml-файл.
	 */
	public boolean load(String absolutePath) {

		Connection connection = null;
		Dom dom = new Dom();
		File file = new File(absolutePath);
		boolean flag = false;
		
		/*
		 * Получаем данные из config.properties
		 */
		FileInputStream fis = null;
		Properties property = new Properties();

		String host = null;
		String login = null;
		String password = null;
		String driver = null;

		try {
			fis = new FileInputStream("src/main/resources/config.properties");
			property.load(fis);

			host = property.getProperty("database");
			login = property.getProperty("dbuser");
			password = property.getProperty("dbpassword");
			driver = property.getProperty("driver");
			
			logger.info("Данные файла настроек получены");
		} catch (IOException e) {
			logger.error("ОШИБКА: Файл свойств отсуствует!", e);
		} finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}

		try {
			logger.info("Загружаем драйвер и создаем соединение");
			Class.forName(driver);
			connection = DriverManager.getConnection(host, login, password);
			Statement statement = null;
			statement = connection.createStatement();
			logger.info("Выполняем запрос на выбоку данных из БД");
			ResultSet result = statement.executeQuery(
					"SELECT * FROM structure");
			logger.info("Создаем xml-файл в который будет выгружена информация с БД");
			dom.setFile(file);
			try
	        {
	            boolean created = file.createNewFile();
	            if(created)
	                System.out.println("Файл создан");
	        }
	        catch(IOException ex){
	            System.out.println(ex.getMessage());
	        }  
			// костыль
			logger.info("Считываем элементы, полученные из БД и передаём их для записи в xml-файл");
			result.next();
				dom.setCrutch(result.getString("DepCode"), result.getString("DepJob"), result.getString("Description"));
			while (result.next()) {
				dom.setXML(result.getString("DepCode"), result.getString("DepJob"), result.getString("Description"));
			}
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
