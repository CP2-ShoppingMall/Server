package kimit.packet;

public class RegisterLoginPacket extends Packet
{
	private final String ID;
	private final String Password;

	public RegisterLoginPacket(HeaderCode header, String id, String password)
	{
		super(header);
		ID = id;
		Password = password;
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
