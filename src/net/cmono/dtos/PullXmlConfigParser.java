package net.cmono.dtos;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

public class PullXmlConfigParser implements XmlConfigParser {

	@Override
	public List<XmlConfig> parse(InputStream is) throws Exception {
		List<XmlConfig> books = null;
		XmlConfig book = null;

		// XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		// XmlPullParser parser = factory.newPullParser();

		XmlPullParser parser = Xml.newPullParser(); // ��android.util.Xml����һ��XmlPullParserʵ��
		parser.setInput(is, "UTF-8"); // ���������� ��ָ�����뷽ʽ

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				books = new ArrayList<XmlConfig>();
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("cfg")) {
					book = new XmlConfig();
				} else if (parser.getName().equals("id")) {
					eventType = parser.next();
					book.setId(Integer.parseInt(parser.getText()));
				} else if (parser.getName().equals("name")) {
					eventType = parser.next();
					book.setName(parser.getText());
				} else if (parser.getName().equals("price")) {
					eventType = parser.next();
					book.setPrice(Float.parseFloat(parser.getText()));
				}
				break;
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("cfg")) {
					books.add(book);
					book = null;
				}
				break;
			}
			eventType = parser.next();
		}
		return books;
	}

	@Override
	public String serialize(List<XmlConfig> books) throws Exception {
		// XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		// XmlSerializer serializer = factory.newSerializer();

		XmlSerializer serializer = Xml.newSerializer(); // ��android.util.Xml����һ��XmlSerializerʵ��
		StringWriter writer = new StringWriter();
		serializer.setOutput(writer); // �����������Ϊwriter
		serializer.startDocument("UTF-8", true);
		serializer.startTag("", "cfgs");
		for (XmlConfig book : books) {
			serializer.startTag("", "cfg");
			serializer.attribute("", "id", book.getId() + "");

			serializer.startTag("", "name");
			serializer.text(book.getName());
			serializer.endTag("", "name");

			serializer.startTag("", "price");
			serializer.text(book.getPrice() + "");
			serializer.endTag("", "price");

			serializer.endTag("", "cfg");
		}
		serializer.endTag("", "cfgs");
		serializer.endDocument();

		return writer.toString();
	}
}
