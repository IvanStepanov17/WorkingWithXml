package main;

import model.RAM;
import processor.Processor;
import view.Console;

/** 
 * Реализуем концепцию Model-View-Controller (MVC)
 * @param RAM - модель
 * @param Processor - контроллер
 * @param Console - отображение/взаимодействие
 * @see model.RAM
 * @see processor.Processor
 * @see view.Console
 * @autor Ivan Stepanov
 * @version 1.0
*/

public class Main {
	
	public static void main(String[] args) {
		
		RAM ram = new RAM();
		Processor processor = new Processor(ram);
		Console console = new Console(processor);
		console.startListen();
	}
}
