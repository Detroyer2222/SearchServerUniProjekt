package de.dhbw.assignments;

import de.dhbw.assignments.simplesearchserver.SearchIndexFactory;
import de.dhbw.assignments.simplesearchserver.SearchServerFactory;
import de.dhbw.assignments.simplesearchserver.SearchServerImpl;
import de.dhbw.assignments.simplesearchserver.api.ISearchIndex;

public class SimpleSearchServerLauncher
{
	public static final int DEFAULT_PORT = 6000;
	public static final String DEFAULT_ROOT_DIR = "./news";

	public static void main(final String[] args)
	{
		int l_listenPort = DEFAULT_PORT;
		String l_rootDirectory = DEFAULT_ROOT_DIR;

		for (String l_arg : args)
		{
			if (l_arg.startsWith("-port="))
			{
				try
				{
					l_listenPort = Integer.parseInt(l_arg.substring("-port=".length()));
				}
				catch (NumberFormatException ex)
				{
					System.out.println("Port ist keine gueltige Zahl!");
					System.exit(-1);
				}
			}
		}

		if (l_listenPort <= 0 || l_listenPort >= 65535)
		{
			System.out.println("Port befindet sich nicht in der gueltigen Range!");
			System.exit(-1);
		}

		if (l_rootDirectory.isEmpty())
		{
			System.out.println("'root' ist kein gueltiger Verzeichnisname!");
			System.exit(-1);
		}

		printBanner();

		System.out.println();
		System.out.println("Port: " + l_listenPort);
		System.out.println("Dir : " + l_rootDirectory);

		ISearchIndex l_searchIndex = SearchIndexFactory.createNewIndex();

		final SearchServerImpl l_searchServer = SearchServerFactory.createNewSearchServer(l_listenPort, l_searchIndex);
		l_searchServer.start();

		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				System.out.println("Shutdown Hook is running !");

				if (l_searchServer != null)
				{
					l_searchServer.stop();
				}
			}
		});
	}

	private static void printBanner()
	{
		System.out.println("          _          _                         _         ");
		System.out.println("  ___    /_\\   _____(_)__ _ _ _  _ __  ___ _ _| |_   ___ ");
		System.out.println(" |___|  / _ \\ (_-<_-< / _` | ' \\| '  \\/ -_) ' \\  _| |___|");
		System.out.println("       /_/ \\_\\/__/__/_\\__, |_||_|_|_|_\\___|_||_\\__|      ");
		System.out.println("                      |___/                              ");
	}
}
