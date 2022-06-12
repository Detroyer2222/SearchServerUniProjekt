package de.dhbw.assignments.simplesearchserver;

import java.io.Reader;

import de.dhbw.assignments.simplesearchserver.api.ATokenizer;
import de.dhbw.assignments.simplesearchserver.util.InstantiationHelper;

/**
 * Hilfsklasse, welche verwendet wird, um auf die eigentlichen Implementierungen
 * von {@link ATokenizer} zuzugreifen.
 * <p>
 * <b>Anmerkung:</b> Dieses File darf nicht editiert werden!
 *
 * @author Michael Schlegel
 *
 */
public final class TokenizerFactory
{
	/**
	 * Vollst&auml;ndiger Name der Klasse, die {@link ATokenizer} ableitet und
	 * implementiert
	 */
	public static final String TOKENIZER_IMPL_CLASS = "de.dhbw.assignments.simplesearchserver.impl.TokenizerImpl";

	/**
	 * Erzeugt und liefert einen neue Tokenizer-Instanz, welche von
	 * {@link ATokenizer} erbt.
	 *
	 * @param p_reader Der {@link Reader Reader}, von welchem die Token extrahiert
	 *                 werden sollen.
	 *
	 * @return Die Tokenizer-Instanz.
	 */
	public static final ATokenizer createNewTokenizer(final Reader p_reader)
	{
		ATokenizer l_instance = InstantiationHelper.loadImplementationClass(TOKENIZER_IMPL_CLASS,
		        new Class<?>[] { Reader.class }, new Object[] { p_reader }, ATokenizer.class);

		return l_instance;

		// return new TokenizerImpl(p_reader);
	}
}
