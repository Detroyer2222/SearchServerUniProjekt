package de.dhbw.assignments.simplesearchserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import de.dhbw.assignments.simplesearchserver.api.AConnectionHandler;
import de.dhbw.assignments.simplesearchserver.api.ISearchIndex;

/**
 * Implementierung eines einfachen SearchServers.
 * <p>
 * <b>Anmerkung:</b> Dieses File darf nicht editiert werden!
 *
 * @author Michael Schlegel
 *
 */
public final class SearchServerImpl
{
	/**
	 * Port des Servers
	 */
	private final int port;

	/**
	 * Der Index, auf dem gesucht wird
	 */
	private final ISearchIndex searchIndex;

	/**
	 * IsRunning - Flag
	 */
	private boolean isRunning = false;

	/**
	 * Ausf&uuml;hrender Thread.
	 */
	private Thread runnerThread;

	/**
	 * Registrierung f&uuml;r die zugreifenden Verbindungen
	 */
	private Set<AConnectionHandler> connectionHandlerRegistry;

	/**
	 * Konstruktor.
	 *
	 * @param p_port Port, andem der SearchServer Verbindungen akzeptiert
	 * @param p_searchIndex SearchIndex in dem gesucht wird
	 */
	SearchServerImpl(final int p_port, final ISearchIndex p_searchIndex)
	{
		port = p_port;

		searchIndex = p_searchIndex;

		connectionHandlerRegistry = ConcurrentHashMap.newKeySet();
	}

	/**
	 * Liefert den Port, andem der SearchServer Verbindungen akzeptiert.
	 *
	 * @return Port
	 */
	public int getPort()
	{
		return port;
	}

	/**
	 * Eigentlicher "Prozess", der Verbindungenen entgegen nimmt und diese zur
	 * parallelen Ausf&uuml;hrung bringt.
	 */
	private void process()
	{
		isRunning = true;

		try (ServerSocket l_serverSocket = new ServerSocket(port))
		{
			System.out.println("Server started. Listening on port=" + port);

			l_serverSocket.setSoTimeout(500);

			while (!Thread.currentThread().isInterrupted() && isRunning)
			{
				Socket l_clientSocket;
				try
				{
					l_clientSocket = l_serverSocket.accept();
				}
				catch (SocketTimeoutException ex)
				{
					continue;
				}

				System.out.println("New Client accepted!");

				final AConnectionHandler l_connectionHandler = ConnectionHandlerFactory
				        .createNewConnectionHandler(l_clientSocket, searchIndex);

				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try (l_connectionHandler)
						{
							connectionHandlerRegistry.add(l_connectionHandler);

							l_connectionHandler.handleConnection();
						}
						catch (IOException ex)
						{
							System.out.println("Could not close well!");
						}
						finally
						{
							connectionHandlerRegistry.remove(l_connectionHandler);
						}
					}
				}).start();
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			isRunning = false;

			for (AConnectionHandler l_handler : connectionHandlerRegistry)
			{
				try
				{
					l_handler.close();
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
				}
			}

			connectionHandlerRegistry.clear();

			System.out.println("Server stopped. Do not listening on port=" + port + " anymore!");
		}
	}

	/**
	 * Startet den SearchServer.
	 * <p>
	 * Der Server wird in einem eigenen Thread ausgef&uuml;hrt.
	 */
	public synchronized void start()
	{
		if (runnerThread == null || !runnerThread.isAlive())
		{
			runnerThread = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					process();
				}
			});

			runnerThread.start();
		}
	}

	/**
	 * Stoppt den SearchServer.
	 * <p>
	 * Der ausf&uuml;hrende Thread des SearchServers wird beendet. Alle Verbindungen
	 * werden geschlossen.
	 */
	public synchronized void stop()
	{
		if (runnerThread != null)
		{
			runnerThread.interrupt();

			try
			{
				long l_timer = System.currentTimeMillis();

				for (int i = 0; i < 3; i++)
				{
					runnerThread.join(5000);

					if (isRunning)
					{
						System.out.printf("SearchServer not stopped after 5sec. Still wait 5sec");
					}
					else
					{
						break;
					}
				}

				double l_sec = (System.currentTimeMillis() - l_timer) / 1000d;
				if (isRunning)
				{
					System.out.printf("SearchServer did not stopped after %.1f seconds.\r\n", l_sec);
				}
				else
				{
					System.out.printf("SearchServer stopped after %.1f seconds.\r\n", l_sec);
				}
			}
			catch (InterruptedException ex)
			{
			}

			runnerThread = null;
		}
	}
}
