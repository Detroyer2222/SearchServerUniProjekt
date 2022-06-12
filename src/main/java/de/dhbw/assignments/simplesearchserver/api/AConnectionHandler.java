package de.dhbw.assignments.simplesearchserver.api;

import java.io.Closeable;
import java.net.Socket;

/**
 * Basis-Klasse f&uuml;r einen Connection Handler.
 * <p>
 * Ein Connection-Handler dient als Kommunikator zwischen SearchServer und
 * Client. Er implementiert das eigentliche Kommunikationsprotokoll.
 * <p>
 * Dies ist eine abstrakte Klasse.<br>
 * Subklassen m&uuml;ssen die Methode {@link #extractNextToken()}
 * implementieren.
 * <p>
 * <b>Anmerkung:</b> Dieses File darf nicht editiert werden!
 *
 *
 * @author Michael Schlegel
 *
 */
public abstract class AConnectionHandler implements Closeable
{
	/**
	 * Der Clientsocket zur Kommunikation mit einem Remote-Client.
	 */
	protected Socket socket;

	/**
	 * Der SearchIndex, in dem gesucht werden soll.
	 */
	protected ISearchIndex searchIndex;

	/**
	 * Der Basis Konstruktor. Dieser muß von der implementierenden Klasse aufgerufen
	 * werden.
	 *
	 * @param p_socket      Clientsocket
	 * @param p_searchIndex Suchindex
	 */
	public AConnectionHandler(final Socket p_socket, final ISearchIndex p_searchIndex)
	{
		socket = p_socket;
		searchIndex = p_searchIndex;
	}

	/**
	 * Diese Methode implementiert die eigentliche Kommunikations-Logik und muss von
	 * der erbenden Klasse implementiert werden.
	 */
	public abstract void handleConnection();

}
