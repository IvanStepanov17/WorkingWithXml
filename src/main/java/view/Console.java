package view;

import java.io.IOException;

import processor.Processor;
import reader.SingletonReader;

/**
 * Класс отображения/взаимодействия используется для получения команд от пользователя и передачи их в Процессор {@link processor.Processor}
 * @param rdr - экземпляр класса <b>SingletonReader</b>
 * @see reader.SingletonReader
 * @author Ivan
 * @version 1.0
 */
public class Console {

	public Processor processor;
	public SingletonReader rdr = SingletonReader.getInstance(); 
	
	public Console(Processor processor) {
		this.processor = processor;
	}
/**
 * @param startListen - Считываем данные введённые пользователем и передаём их в Процессор {@link processor.Processor}
 */
	public void startListen() {
			System.out.println("Выберете существующую директорию для хранения xml-файлов (Например C:\\java) ... хотя можете указать и несуществующую, всё равно мы заставим Вас сделать так, как это нужно нам :)");
		while(true) {
			try {
				String commandString = rdr.readLine();
				String result = processor.process(commandString);
				System.out.println(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
