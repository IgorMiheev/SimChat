package com.simbirsoft.simchat.service.utils;

public class StringParse {
	public static boolean isLong(String s) {
		if (s == null || s.equals("")) {
			return false;
		}

		try {
			@SuppressWarnings("unused")
			Long val = Long.parseLong(s);
			return true;
		} catch (NumberFormatException e) {
			// не является числом
		}
		return false;
	}
}
