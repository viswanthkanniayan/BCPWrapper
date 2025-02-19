package com.ecs.bcp.utils;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ServiceUtils {
	
	public static String getTomorrowDate(String inDate)
	{
		
		if (inDate == null || isEmpty(inDate))
			return null;
		
		
		SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
		Date date=null;
		try {
			 date = st.parse(inDate);
			Calendar c = Calendar.getInstance(); 
			c.setTime(date); 
			c.add(Calendar.DATE, 1);
			date = c.getTime();
			return st.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return null;
		
			
	}

	public static Gson gsonSerializeNulls() {
		return new GsonBuilder().serializeNulls().create();
	}

	public static String generateRandomAgentId() throws Exception {

		SecureRandom random = new SecureRandom();
		int num = random.nextInt(100000000);
		String formatted = String.format("%08d", num);
		return "" + formatted;
	}
	
	public static String generateSubscriberOrderId() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("ssmmMddHHyyyy");
		//int len =12;
		
		Random ran = new Random();
		String ranNo = String.valueOf(ran.nextLong()).substring(1,5);
		ranNo = sdf.format(new Date())+ranNo;
	//	System.out.println("ranNo------"+ranNo);
	
	//	System.out.println(ranNo);
		
		return ranNo;
	}

	public static boolean isEmpty(String data) {
		if (data == null)
			return true;

		if (data.trim().length() == 0)
			return true;

		return false;
	}

	public static Long castLongObject(Object object) {
		Long result = 0l;
		try {
			if (object instanceof Long)
				result = ((Long) object).longValue();
			else if (object instanceof Integer) {
				result = ((Integer) object).longValue();
			} else if (object instanceof String) {
				result = Long.valueOf((String) object);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// do something
		}
		return result;
	}

	public static String intToString(int value) {
		return String.valueOf(value);
	}

	public static int StringToIntt(String value) {
		return Integer.parseInt(value);
	}

	public static long StringToLong(String value) {
		return Long.parseLong(value);
	}

	public static String longToString(long value) {
		return String.valueOf(value);
	}
	

	public static void main(String[] args) throws Exception {
		
		
	}	
	
}
