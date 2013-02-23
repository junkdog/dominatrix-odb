package net.onedaybeard.dominatrix.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.onedaybeard.dominatrix.annotation.Salvageable;

@Salvageable("maybe flesh it out some more, but otherwise a recurring thing in projects.")
public final class Hash
{
	private Hash()
	{
		
	}
	
	public static String md5(String text)
	{
		try
		{
			return md5(text.getBytes("UTF-8"));
//			return md5(text.getBytes("ASCII"));
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static String md5(InputStream stream)
	{
		return md5(getBytes(stream));
	}
	
	public static String md5(byte[] bytes)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(bytes);
			byte[] digest = md.digest();

			return encodeHexString(digest);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private static String encodeHexString(byte[] data)
	{
		int length = data.length;
		StringBuilder hexString = new StringBuilder(length * 2);
		for (int i = 0; i < length; i++)
		{
			// make sure we get the entire hash
			String hashPart = Integer.toHexString(0xFF & data[i]);

			if (hashPart.length() == 1)
				hexString.append('0');

			hexString.append(hashPart);
		}
		return hexString.toString();
	}
	
	private static byte[] getBytes(InputStream stream)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			DataInputStream reader = new DataInputStream(stream);
			
			byte[] buf = new byte[8096];
			int read;
			while ((read = reader.read(buf)) != -1)
			{
				baos.write(buf, 0, read);
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				stream.close();
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		return baos.toByteArray();
	}
}
