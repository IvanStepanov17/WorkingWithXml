package type;

import java.util.HashMap;

import dbloader.DBloader;
import dbloader.XMLloader;

/**
 * Класс "хранилище". Используется для хранения данных БД в виде коллекций.
 * @param hashMap - хранит таблицу БД в виде объекта <b>NaturalKey</b>, являющегося первичным ключом и значения в виде строки.
 * @see NaturalKey
 * @param name - имя хранилища, задаваемое при создании. В дальнейшем используется в виде ключа объекта <b>map</b> {@link model.RAM} по которому можно найти/выбрать хранилище.
 * @see model.RAM
 * @author Ivan
 * @version 1.0
 */
public class MapStorage {
	
	public HashMap<NaturalKey, String> hashMap;
	public String name;
	public XMLloader xmlloader;
	public DBloader dbloader;
	
	public MapStorage(String name) {
		this.name = name;
		hashMap = new HashMap<>();
	}
/**
 * @param loadBDtoXML - метод для записи данных из БД в xml-файл
 * @param absolutePath - принимаем абсолютный путь для сохранения xml-файла
 * @return - возвращает подтверждение/опровержение выполнения операции.
 */
	public boolean loadBDtoXML(String absolutePath) {
		dbloader = new DBloader();
		if (dbloader.load(absolutePath)) {
			return true;
		} else {
			return false;
		}
	}
/**
 * @param loadXMLtoBD - метод для записи данных из xml-файла в БД
 * @param absolutePath - принимаем абсолютный путь для чтения xml-файла
 * @return - возвращает подтверждение/опровержение выполнения операции.
 */
	public boolean loadXMLtoBD(String absolutePath) {
		xmlloader = new XMLloader();
		if (xmlloader.load(absolutePath)) {
			return true;
		} else {
			return false;
		}
	}
}