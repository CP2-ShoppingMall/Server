package kimit.packet;

import java.io.Serial;
import java.io.Serializable;

public class Packet implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;

	private final HeaderCode Header;

	public Packet(HeaderCode header)
	{
		Header = header;
	}

	public HeaderCode getHeader()
	{
		return Header;
	}

	@Override
	public String toString()
	{
		return "Header: " + Header.toString();
	}
}
