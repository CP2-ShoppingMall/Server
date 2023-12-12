package kimit.server;

import java.io.*;
import java.util.ArrayList;

public class Database<T extends Serializable>
{
	private final String Path;
	private ArrayList<T> Data;
	private FileInputStream FileIn;
	private FileOutputStream FileOut;
	private ObjectInputStream In;
	private ObjectOutputStream Out;

	public Database(String path)
	{
		Path = path;
		Data = new ArrayList<>();

		try
		{
			File file = new File(Path);
			if (!file.exists())
				file.createNewFile();
			FileOut = new FileOutputStream(Path, true);
			FileIn = new FileInputStream(Path);
			Out = new ObjectOutputStream(new BufferedOutputStream(FileOut));
			In = new ObjectInputStream(new BufferedInputStream(FileIn));

			Data = ((ArrayList<T>) In.readObject());
		}
		catch (EOFException ignored)
		{

		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public void add(T t) throws IOException
	{
		Data.add(t);
		Out.reset();
		Out.writeObject(Data);
	}

	public void close() throws IOException
	{
		Out.writeObject(Data);
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

	public ArrayList<T> getData()
	{
		return Data;
	}
}
