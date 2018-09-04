package dbloader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import type.NaturalKey;

/**
 * Класс реализующий запись данных из xml-файла в БД и наоборот.
 * @see type.NaturalKey
 * @see type.MapStorage
 * @see dbloader.DBloader
 * @see dbloader.XMLloader
 * @author Ivan
 * @version 1.0
 */
public class Dom {
	
	private static final Logger logger = Logger.getLogger(Dom.class);

	NaturalKey natKey;
	File file;
	HashMap<NaturalKey, String> map;

	/**
	 * Метод записывает значения БД в xml-файл
	 * @param code - получаем значения поля depcode из таблицы БД
	 * @param job - получаем значения поля depjob из таблицы БД
	 * @param descr - получаем значение поля description из таблицы БД

	 */
	public void setXML(String code, String job, String descr) {
		// Создается построитель документа

		try {
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			// Создается дерево DOM документа из файла
			Document document = documentBuilder.parse(file);

			Node root = document.getDocumentElement();

			Element element = document.createElement("Element");
			root.appendChild(element);

			Element depCode = document.createElement("DepCode");
			depCode.setTextContent(code);
			element.appendChild(depCode);

			Element depJob = document.createElement("DepJob");
			depJob.setTextContent(job);
			element.appendChild(depJob);

			Element description = document.createElement("Description");
			description.setTextContent(descr);
			element.appendChild(description);

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(new DOMSource(document), new StreamResult(file));

		} catch (ParserConfigurationException | TransformerConfigurationException e) {
			logger.error(e);;
		} catch (TransformerException e) {
			logger.error(e);;
		} catch (SAXException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}

	}

	/**
	 * Метод проверяет xml-файл на корректность (уникальность) натральных ключей и записывает их в БД
	 * @return возвращаем коллекцию с ключом в виде натурального ключа БД (поля depcode и depjob) 
	 * и значением (поле description) в виде строки.
	 */
	public HashMap<NaturalKey, String> getValuesXML() {
		map = new HashMap<NaturalKey, String>();
		String tempDepCode = null;
		String tempDepJob = null;
		String tempDescription = null;
		try {
			// Создается построитель документа
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			// Создается дерево DOM документа из файла
			Document document = documentBuilder.parse(file);

			// Получаем корневой элемент
			Node root = document.getDocumentElement();

			// Просматриваем все подэлементы корневого - т.е. Element
			NodeList elements = root.getChildNodes();
			for (int i = 0; i < elements.getLength(); i++) {
				Node element = elements.item(i);
				// Если нода не текст, то это элемент - заходим внутрь
				if (element.getNodeType() != Node.TEXT_NODE) {
					NodeList elementProps = element.getChildNodes();
					for(int j = 0; j < elementProps.getLength(); j++) {
						Node elementProp = elementProps.item(j);
						// Если нода не текст, то это один из параметров элемента - печатаем
						if (elementProp.getNodeType() != Node.TEXT_NODE) {
							if (elementProp.getNodeName().equals("DepCode")) {
								tempDepCode = elementProp.getChildNodes().item(0).getTextContent();
							}
							if (elementProp.getNodeName().equals("DepJob")) {
								tempDepJob = elementProp.getChildNodes().item(0).getTextContent();
							}
							natKey = new NaturalKey(tempDepCode, tempDepJob);
							if (elementProp.getNodeName().equals("Description")) {
								tempDescription = elementProp.getChildNodes().item(0).getTextContent();
							}
						}
					}
					// проверяем ключи на уникальность
					for (NaturalKey pair : map.keySet()) {
						if (natKey.equals(pair)) {
							try {
								throw new Exception("Загружаемый файл некорректен, в xml-файле обнаружены идентичные ключи");
							} catch (Exception e) {
								e.printStackTrace();
								System.exit(0);
							}
						}
					}
					map.put(natKey, tempDescription);
//					for (Map.Entry<NaturalKey, String> tmp : map.entrySet()) {
//						System.out.println(tmp.getValue());
//						System.out.println(tmp.getKey().getDepCode() + " " + tmp.getKey().getDepJob());
//					}
//					System.out.println("===========>>>>");
				}
			}

		} catch (ParserConfigurationException ex) {
			ex.printStackTrace(System.out);
		} catch (SAXException ex) {
			ex.printStackTrace(System.out);
		} catch (IOException ex) {
			ex.printStackTrace(System.out);
		}
		return map;
	}

	public void setCrutch(String code, String job, String descr) {
		try {
			DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
			docBuildFactory.setNamespaceAware(true);
			Document document = docBuildFactory.newDocumentBuilder().newDocument();

			Element dataBase = document.createElement("dataBase");
			document.appendChild(dataBase);

			Element element = document.createElement("Element");
			dataBase.appendChild(element);

			Element depCode = document.createElement("DepCode");
			depCode.setTextContent(code);
			element.appendChild(depCode);

			Element depJob = document.createElement("DepJob");
			depJob.setTextContent(job);
			element.appendChild(depJob);

			Element description = document.createElement("Description");
			description.setTextContent(descr);
			element.appendChild(description);

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(new DOMSource(document), new StreamResult(file));

		} catch (ParserConfigurationException | TransformerConfigurationException e) {
			logger.error(e);
		} catch (TransformerException e) {
			logger.error(e);
		}
	}


	public NaturalKey getNatKey() {
		return natKey;
	}

	public void setNatKey(NaturalKey natKey) {
		this.natKey = natKey;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

}
