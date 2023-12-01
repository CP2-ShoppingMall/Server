package kimit.protocol;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterLoginPacket extends Packet
{
	private final String ID;
	private final String Password;

	public RegisterLoginPacket(HeaderCode header, String id, String password)
	{
		super(header);
		ID = id;

		StringBuilder builder = new StringBuilder();
		try
		{
			final MessageDigest md = MessageDigest.getInstance("SHA3-256");
			final byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
			for (byte loop : bytes)
				builder.append(String.format("%02x", loop));
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		Password = builder.toString();
	}

	public String getID()
	{
		return ID;
	}

	public String getPassword()
	{
		return Password;
	}

	@Override
	public String toString()
	{
		return super.toString() + ", ID: " + ID + ", Password: " + Password;
	}
}
