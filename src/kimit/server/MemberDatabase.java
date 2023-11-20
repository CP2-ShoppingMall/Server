package kimit.server;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MemberDatabase
{
	private final String Path;
	private ArrayList<Member> Members;
	private FileInputStream FileIn;
	private FileOutputStream FileOut;
	private ObjectInputStream In;
	private ObjectOutputStream Out;

	public MemberDatabase(String path)
	{
		Path = path;
		Members = new ArrayList<>();

		try
		{
			File file = new File(Path);
			if (!file.exists())
				file.createNewFile();
			FileOut = new FileOutputStream(Path, true);
			FileIn = new FileInputStream(Path);
			Out = new ObjectOutputStream(FileOut);
			In = new ObjectInputStream(FileIn);

			Members = (ArrayList<Member>) In.readObject();
		}
		catch (EOFException ignored)
		{

		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public void add(Member member) throws IOException
	{
		Members.add(member);
		Out.writeObject(Members);
	}

	public void close() throws IOException
	{
		if (In != null && FileIn != null)
		{
			In.close();
			FileIn.close();
		}
		if (Out != null && FileOut != null)
		{
			Out.close();
			FileOut.close();
		}
	}

	public Member getMember(String id)
	{
		for (Member loop : Members)
			if (loop.getID().equals(id))
				return loop;
		return null;
	}
}
