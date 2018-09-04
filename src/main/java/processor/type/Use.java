package processor.type;

import model.RAM;

/**
 * Команда на выбор хранилища
 * @see model.RAM
 * @see type.MapStorage
 * @author Ivan
 * @version 1.0
 */
public class Use {

	/**
	 * @param ram - модель.
	 * @param commandWords - массив строк, полученный от пользователя.
	 * @return - подтверждаем/опровергаем выполнение команды на выбор хранилища.
	 */
	public static String process(RAM ram, String[] commandWords) {
		boolean isSelected = ram.use(commandWords[1]);
		if (isSelected) {
			return "Выбранное хранилище: " + commandWords[1];
		} else {
			return "Хранилище с именем " + commandWords[1] + " не найдено";
		}
	}

}
