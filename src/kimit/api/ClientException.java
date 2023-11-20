package kimit.api;

import kimit.protocol.HeaderCode;

public class ClientException extends Exception
{
	private final HeaderCode Error;

	public ClientException(HeaderCode error, String message)
	{
		super(message);
		Error = error;
	}

	public HeaderCode getError()
	{
		return Error;
	}
}
