package processor;

import java.io.File;

import commands.Commands;
import model.RAM;
import processor.type.Add;
import processor.type.Create;
import processor.type.Loading;
import processor.type.Use;
import processor.type.Shutdown;;

/**
 * Класс используется для корректности коамнды полученной от пользователя и запуска соответствующего метода для его исполнения.
 * @param directoryPath - используется для хранения пути, указанного пользователем при старте прииложения
 * @param expansion - используется для хранения расширения файлов.
 * @param counter - счётчик некорректно указанных пользователем команд на выбор каталога местоположения хранилищ.
 * @see commands.Commands
 * @author Ivan
 * @version 1.0
 */
public class Processor {
	
	public RAM ram;
	String directoryPath = "";
	String expansion = ".xml";
	int counter = 0;
	
	public Processor(RAM ram) {
		this.ram = ram;
	}
	/**
	 * Просим пользователя указать каталог или создаём его за него после 5 попыток. Данный блок будет выполняться только 
	 * пока не задан каталог.
	 * @param commandString - данные введённые пользователем в консоль {@link view.Console}
	 * @return - путь к каталогу для хранилищ.
	 */
	public String process(String commandString) {
		if (directoryPath.isEmpty()){
			File isdir = new File(commandString);
			if (counter >= 5){
				File newFolder = new File("IamVeryStubbornPerson").getAbsoluteFile();
				if (!newFolder.exists())
					newFolder.mkdir();
				System.out.println("Вы нас убедили, вы действительно очень настырный человек :) В качестве каталога для xml-файлов выбран " + newFolder);
				directoryPath = newFolder.getAbsolutePath();
			} else if (!isdir.isDirectory()){
				System.out.println("Указанного каталога не существует, пожалуйста попробуйте ещё раз.");
				counter++;
			}else{
				directoryPath = commandString;
				System.out.println("Ожидание ввода команды");
			}
		}
		/**
		 * Разбиваем полученную от пользователя строку на массив строк и после проверки отправляем команду на выполнение.
		 */
		String[] commandWords = commandString.trim().split("\\s+");
		if (commandWords.length != 0) {
			for (String s : commandWords) {
				System.out.println(s);
			}
			
			String result = "";
			switch (commandWords[0]) {
		
			case Commands.USE:
				if (commandWords.length > 1) {
					result = Use.process(ram, commandWords);
				} else {
					result = "Имя хранилища для использования не указано";
				}
				break;
				
			case Commands.CREATE:
				if (commandWords.length > 1) {
					result = Create.process(ram, commandWords);
				} else {
					result = "Имя хранилища для создания не указано";
				}
				break;
				
			case Commands.ADD:
				result = Add.process(ram, this.directoryPath, this.expansion);					
				break;
				
			case Commands.LOADING:
				result = Loading.process(ram, directoryPath, expansion);
				break;
				
			case Commands.SHUTDOWN:
				result = Shutdown.process(ram);
				if (result.equals("y")){
					System.out.println("Good bye!");
					System.exit(0);
				}
			}
			return result;
		} else {
			return "Вы не указали ни одной команды";
		}
	}

}
