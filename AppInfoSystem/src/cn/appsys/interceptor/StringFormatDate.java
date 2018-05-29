package cn.appsys.interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class StringFormatDate implements Converter<String, Date>{
	
	private  String format;
	public StringFormatDate(String format)
	{
		this.format=format;
		
	}
	
	public Date convert(String inputStr)
	{
		try {
			return new SimpleDateFormat(format).parse(inputStr);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
		
	}

}
