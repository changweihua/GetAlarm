package net.cmono.dtos;

import java.io.InputStream;
import java.util.List;

public interface XmlConfigParser {
	/**
	 * 解析输入流 得到Book对象集合
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public List<XmlConfig> parse(InputStream is) throws Exception;

	/**
	 * 序列化Book对象集合 得到XML形式的字符串
	 * 
	 * @param books
	 * @return
	 * @throws Exception
	 */
	public String serialize(List<XmlConfig> books) throws Exception;
}
