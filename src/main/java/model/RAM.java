package model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import reader.SingletonReader;
import type.MapStorage;

/** 
 * Класс <b>модели</b> используется для реализации логики приложения.
 * @param Map <String, MapStorage> - Данная коллекция служит для хранения названия хранилища в виде ключа и значения в виде коллекции, представляющей из себя таблицу <Натуральный ключ, значение>
 * @param currentStorage - текущее хранилище над которым совершаются операции 
 * @autor Ivan Stepanov
 * @version 1.0
 */
public class RAM {

	public Map<String, MapStorage> map;
	public MapStorage currentStorage = null;
	public SingletonReader rdr = SingletonReader.getInstance(); 

	public RAM() {
		map = new HashMap<>();
	}
	/**
	 * @param create - Метод создаёт новое хранилище <b>MapStorage</b>, при условии отсутствия в коллекции {@link MapStorage} хранилища с идентичным именем.
	 * @param name - в качестве входного параметра принимаем строку, указанную пользователем.
	 */
	public boolean create(String name) {
		if (map.containsKey(name)) {
			return false;
		} else {
			map.put(name, new MapStorage(name));
			return true;
		}
	}
	/**
	 * @param use - Метод используется для выбора хранилища из коллекции <b>map</b> данного класса {@link MapStorage}
	 * @param name - в качестве входного параметра принимаем строку, указанную пользователем. 
	 */
	public boolean use(String name) {
		MapStorage mapStorage = map.get(name);
		if (mapStorage != null) {
			currentStorage = mapStorage;
			return true;
		} else {
			return false;
		}
	}
	/**
	 * @param add - Метод передаёт абсолютный путь xml-файла для выгрузки в него информации из БД, в случае если текущее хранилище <b>currentStorage</b> выбрано.
	 * @see MapStorage.loadBDtoXML
	 * @param path - принимаем путь к файлу.
	 * @param expansion - принимаем расширение файла.
	 */
	public boolean add(String path, String expansion) {
		if (currentStorage != null) {
			String absolutePath = path + File.separator + currentStorage.name + expansion;
			return currentStorage.loadBDtoXML(absolutePath);
		} else {
			return false;
		}
	}
	/**
	 * @param loading - Метод передаёт абсолютный путь xml-файла для выгрузки из него информации в БД, в случае если текущее хранилище <b>currentStorage</b> выбрано.
	 * @see MapStorage.loadXMLtoBD
	 * @param path - принимаем путь к файлу.
	 * @param expansion - принимаем расширение файла.
	 */
	public boolean loading(String path, String expansion) {
		if (currentStorage != null) {
			String absolutePath = path + File.separator + currentStorage.name + expansion;
			return currentStorage.loadXMLtoBD(absolutePath);
		} else {
			return false;
		}
	}
	/**
	 * @param shtudown - Метод используется для завершения работы приложения.
	 * @return возвращаем выбор пользователя, усли <b>y</b>, значит закрываем поток на чтение и передаём команду на закрытие в Процессор
	 */
	public String shutdown() {
		String choice = "";
		while(true){
			System.out.println("Вы действительно хотите выйти? y//n");
			try {
				choice = rdr.readLine();
				if (choice.equals("y")){
					rdr.close();
					break;
				} else if (choice.equals("n")){
					break;
				} else
					System.out.println("Введите y для сохранения, или n для отказа");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		return choice;
	}

}
