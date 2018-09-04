package processor.type;

import model.RAM;

public class Shutdown {
	public static String process(RAM ram){
		return ram.shutdown();
	}
}