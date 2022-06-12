package de.dhbw.assignments.simplesearchserver;

import de.dhbw.assignments.simplesearchserver.api.ISearchIndex;
import de.dhbw.assignments.simplesearchserver.util.InstantiationHelper;

/**
 * Hilfsklasse, welche verwendet wird, um auf die eigentlichen Implementierungen
 * von {@link ISearchIndex} zuzugreifen.
 * <p>
 * <b>Anmerkung:</b> Dieses File darf nicht editiert werden!
 *
 * @author Michael Schlegel
 *
 */
public final class SearchIndexFactory
{
	/**
	 * Vollst&auml;ndiger Name der Klasse, die {@link ISearchIndex} implementiert
	 */
	public static final String SEARCHINDEX_IMPL_CLASS = "de.dhbw.assignments.simplesearchserver.impl.SearchIndexImpl";

	/**
	 * Erzeugt und liefert eine SearchIndex-Instanz, welche von {@link ISearchIndex}
	 * erbt.
	 *
	 * @return Die neue SearchIndex-Instanz.
	 */
	public static final ISearchIndex createNewIndex()
	{
		ISearchIndex l_instance = InstantiationHelper.loadImplementationClass(SEARCHINDEX_IMPL_CLASS,
		        new Class<?>[] {}, new Object[] {}, ISearchIndex.class);
		
		return l_instance;
	}
}
