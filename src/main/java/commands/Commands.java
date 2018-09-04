package commands;

/**
 * Интерфейс для хранения строковых полей, являющихся исполняющими командами.
 * @param DISPLAY - команда на отображение содержимого хранилища на консоль (пока не реализована)
 * @param LIST - команда на отображение наименования всех загруженных хранилищ на консоль (пока не реализована)
 * @param LOADING - команда на загрузку данных из xml-файла в БД
 * @param CREATE - команда на создание хранилища
 * @param USE - команда на выбор существующего хранилища
 * @param SHUTDOWN - команда на завершение работы приложения
 * @param ADD - команда на загрузку данных из BD в xml-файл
 * @see processor.Processor
 * @see view.Console
 * @author Ivan
 * @version 1.0
 */
public interface Commands {
	public final static String DISPLAY = "display";
	public final static String LIST = "list";
	public final static String LOADING = "loading";
	public final static String CREATE = "create";
	public final static String USE = "use";
	public final static String SHUTDOWN = "shutdown";
	public final static String ADD = "add";
	
}