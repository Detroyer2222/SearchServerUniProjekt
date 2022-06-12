package de.dhbw.assignments.simplesearchserver;

import java.net.Socket;

import de.dhbw.assignments.simplesearchserver.api.AConnectionHandler;
import de.dhbw.assignments.simplesearchserver.api.ISearchIndex;
import de.dhbw.assignments.simplesearchserver.util.InstantiationHelper;

/**
 * Hilfsklasse, welche verwendet wird, um auf die eigentlichen Implementierungen
 * von {@link AConnectionHandler} zuzugreifen.
 * <p>
 * <b>Anmerkung:</b> Dieses File darf nicht editiert werden!
 *
 * @author Michael Schlegel
 *
 */
public class ConnectionHandlerFactory
{
	/**
	 * Vollst&auml;ndiger Name der Klasse, die {@link AConnectionHandler} ableitet
	 * und implementiert
	 */
	public static final String CONNECTIONHANDLER_IMPL_CLASS = "de.dhbw.assignments.simplesearchserver.impl.ConnectionHandlerImpl";

	/**
	 * Erzeugt und liefert eine neue ConnectionHandler-Instanz, welche von
	 * {@link AConnectionHandler} erbt.
	 *
	 * @return Die neue ConnectionHandler-Instanz.
	 */
	public static AConnectionHandler createNewConnectionHandler(final Socket p_clientSocket,
	        final ISearchIndex p_searchIndex)
	{
		AConnectionHandler l_instance = InstantiationHelper.loadImplementationClass(CONNECTIONHANDLER_IMPL_CLASS,
		        new Class<?>[] { Socket.class, ISearchIndex.class }, new Object[] { p_clientSocket, p_searchIndex },
		        AConnectionHandler.class);

		return l_instance;

		// return new ConnectionHandlerImpl(p_clientSocket);
	}
}
