package processor.type;

import model.RAM;

/**
 * Команда на выгрузку данных из xml-файла в БД.
 * @see model.RAM
 * @see type.MapStorage
 * @author Ivan
 * @version 1.0
 */

public class Loading {
	
	/**
	 * @param ram - модель.
	 * @param path - каталог для сохранения.
	 * @param expansion - расширение файла.
	 * @return - подтверждаем/опровергаем выполнение команды на выгрузку данных из xml-файла в БД.
	 */
	public static String process(RAM ram, String path, String expansion) {
		boolean isAdded = ram.loading(path, expansion);
		if (isAdded) {
			return "База данных синхронизирована с выбранным хранилищем";
		} else {
			return "Синхронизация БД с хранилищем не удалась";
		}
	}
}
