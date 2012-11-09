package net.onedaybeard.reflect;

import java.lang.reflect.Field;
import java.util.Date;

public class ReflexerTest
{
	private ReflexerTest()
	{
		
	}

	public static void main(String[] args)
		throws Exception
	{
		ReflexerTest rf = new ReflexerTest();
		rf.being();
	}
	
	private void being() throws Exception
	{
		Date date = new Date();
		System.out.println("Declared for date: ");
		
		print(date);
		
		Reflex reflex = new Reflex();
		
		reflex.on(date, "fastTime").set("666");
		reflex.on(date, "defaultCenturyStart").set("1");
		int data = reflex.on(date, "defaultCenturyStart").get();
		System.out.println();
		
		print(date);
	}

	private void print(Date date) throws IllegalAccessException
	{
		Field[] fields = date.getClass().getDeclaredFields();
		for (Field f : fields)
		{
			if (!f.isAccessible())
				f.setAccessible(true);
			
			System.out.printf("%-20s %-42s %s\n", f.getName(), f.getType(), f.get(date));
		}
	}
}
