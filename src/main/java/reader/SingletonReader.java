package reader;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Класс используется для чтения команд {@link commands.Commands} и данных, полученных от пользователя. Реализован паттерн Singleton.
 * @see commands.Commands
 * @see view.Console
 * @author Ivan
 * @version 1.0
 */
public class SingletonReader extends BufferedReader{
	
	private static SingletonReader rdr;
	
	private SingletonReader(InputStreamReader inputStreamReader) {
		super(inputStreamReader);
	}

	public static SingletonReader getInstance() {
		if (rdr == null) {
			rdr = new SingletonReader(new InputStreamReader(System.in));
		}
		return rdr;
	}
}
