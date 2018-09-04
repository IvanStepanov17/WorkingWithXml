package type;

/**
 * Объекты данного класса представляют из себя натуральные ключи, сотоящие из двух строковых полей <b>depCode</b> и <b>depJob</b>.
 * В классе переопределены методы hashCode() и equals() для корректного сравнения наличия идентичных натуральных ключей при
 * загрузке данных из xml-файла в БД. (т.е. для проверки xml-файла на валидность перед его загрузкой в БД)
 * @see DOM.getValuesXML
 * @author Ivan
 * @version 1.0
 */

public class NaturalKey {
	
	/**
	 * @param depCode - часть натурального ключа.
	 * @param depJob - часть натурального ключа.
	 */
	String depCode;
	String depJob;
	
	public NaturalKey(String depCode, String depJob) {
		this.depCode = depCode;
		this.depJob = depJob;
	}

	public String getDepCode() {
		return depCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((depCode == null) ? 0 : depCode.hashCode());
		result = prime * result + ((depJob == null) ? 0 : depJob.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NaturalKey other = (NaturalKey) obj;
		if (depCode == null) {
			if (other.depCode != null)
				return false;
		} else if (!depCode.equals(other.depCode))
			return false;
		if (depJob == null) {
			if (other.depJob != null)
				return false;
		} else if (!depJob.equals(other.depJob))
			return false;
		return true;
	}

	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}

	public String getDepJob() {
		return depJob;
	}

	public void setDepJob(String depJob) {
		this.depJob = depJob;
	}
}
