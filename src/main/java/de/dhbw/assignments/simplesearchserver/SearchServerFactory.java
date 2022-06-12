package de.dhbw.assignments.simplesearchserver;

import de.dhbw.assignments.simplesearchserver.api.ATokenizer;
import de.dhbw.assignments.simplesearchserver.api.ISearchIndex;

/**
 * Hilfsklasse, welche verwendet wird, um auf die eigentlichen Implementierungen
 * von {@link ATokenizer} zuzugreifen.
 * <p>
 * <b>Anmerkung:</b> Dieses File darf nicht editiert werden!
 *
 * @author Michael Schlegel
 *
 */
public final class SearchServerFactory
{
	/**
	 * Erzeugt und liefert eine neue SearchServer-Instanz, welche von
	 * {@link SearchServerImpl} erbt.
	 *
	 * @return Die neue SearchServer-Instanz.
	 */
	public static final SearchServerImpl createNewSearchServer(final int p_port, final ISearchIndex p_searchIndex)
	{
		return new SearchServerImpl(p_port, p_searchIndex);
	}
}
