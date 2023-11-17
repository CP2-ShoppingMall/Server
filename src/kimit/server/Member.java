package kimit.server;

import java.io.Serial;
import java.io.Serializable;

public class Member implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	private final String ID;
	private final String Password;

	public Member(String id, String password)
	{
		ID = id;
		Password = password; // TODO : password encrypt
	}

	public String getID()
	{
		return ID;
	}

	public String getPassword()
	{
		return Password;
	}
}
