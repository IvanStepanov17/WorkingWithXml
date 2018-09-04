package processor.type;

import model.RAM;

/**
 * Команда на выгрузку данных из БД в xml-файл.
 * @see model.RAM
 * @see type.MapStorage
 * @author Ivan
 * @version 1.0
 */
public class Add {
	
	/**
	 * @param ram - модель.
	 * @param path - каталог для чтения.
	 * @param expansion - расширение файла.
	 * @return - подтверждаем/опровергаем выполнение команды на выгрузку данных из БД в xml-файл.
	 */
	public static String process(RAM ram, String path, String expansion) {
		boolean isAdded = ram.add(path, expansion);
		if (isAdded) {
			return "Данные успешно выгружены в xml-файл";
		} else {
			return "Данные в xml-файл не выгружены";
		}
	}
}
