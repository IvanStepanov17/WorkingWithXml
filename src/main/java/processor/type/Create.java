package processor.type;

import model.RAM;

/**
 * Команда на создание хранилища.
 * @see model.RAM
 * @see type.MapStorage
 * @author Ivan
 * @version 1.0
 */
public class Create {
	
	/**
	 * @param ram - модель.
	 * @param commandWords - массив строк, полученный от пользователя.
	 * @return - подтверждаем/опровергаем выполнение команды на создание хранилища.
	 */
	public static String process(RAM ram, String[] commandWords) {
		boolean isCreated = ram.create(commandWords[1]);
		if (isCreated) {
			return "Хранилище " + commandWords[1] + " создано";
		} else {
			return "Хранилище " + commandWords[1] + " уже существует";
		}
	}

}
