package net.onedaybeard.util;

import com.badlogic.gdx.graphics.Color;

public final class Colors
{
	private Colors()
	{
		
	}
	
	public static Color rgb(int red, int green, int blue)
	{
		return rgba(red, green, blue, 255);
	}
	
	public static Color rgba(int red, int green, int blue, int alpha)
	{
		return new Color(red/255f, green/255f, blue/255f, alpha/255);
	}
	
	public static Color rgba(int red, int green, int blue, float alpha)
	{
		return new Color(red/255f, green/255f, blue/255f, alpha);
	}
}
