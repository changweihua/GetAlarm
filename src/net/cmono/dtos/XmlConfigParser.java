package net.cmono.dtos;

import java.io.InputStream;
import java.util.List;

public interface XmlConfigParser {
	/**
	 * ���������� �õ�Book���󼯺�
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public List<XmlConfig> parse(InputStream is) throws Exception;

	/**
	 * ���л�Book���󼯺� �õ�XML��ʽ���ַ���
	 * 
	 * @param books
	 * @return
	 * @throws Exception
	 */
	public String serialize(List<XmlConfig> books) throws Exception;
}
