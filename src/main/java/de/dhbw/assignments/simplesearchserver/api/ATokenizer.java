package de.dhbw.assignments.simplesearchserver.api;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Basis-Klasse f&uuml;r einen Tokenizer.
 * <p>
 * Ein Tokenizer extrahiert einzelne Token von einem gegebenen {@link Reader}.
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
public abstract class ATokenizer implements Iterable<String>, Iterator<String>, Closeable
{
	/**
	 * Der Reader, von welchem die einzelnen Token extrahiert werden.
	 */
	protected final Reader m_reader;

	/**
	 * Das Token, welches als n&auml;chstes geliefert wird.
	 */
	private String m_nextToken;

	/**
	 * Erzeugt einen neuen Tokenizer.
	 *
	 * @param p_reader Das Reader-Objekt, von dem die einzelnen Token extrahiert
	 *                 werden.
	 */
	protected ATokenizer(final Reader p_reader)
	{
		Objects.requireNonNull(p_reader, "Reader object must not be null!");

		this.m_reader = p_reader;
	}

	/**
	 * Liefert das Iterator-Objekt, welches vom {@link Iterable}-Interface erwartet
	 * wird.
	 */
	@Override
	public final Iterator<String> iterator()
	{
		return this;
	}

	/**
	 * Gibt an, ob ein weiteres Token geliefert werden kann.
	 *
	 * @return <code>true</code> oder <code>false</code>
	 */
	@Override
	public boolean hasNext()
	{
		if (m_nextToken == null)
		{
			m_nextToken = extractNextToken();
		}

		return m_nextToken != null;
	}

	/**
	 * Liefert das n&auml;chste Token.
	 *
	 * @return Das n&auml;chste Token
	 * @throws NoSuchElementException if the iteration has no more elements
	 */
	@Override
	public String next()
	{
		final String l_nextToken;

		if (hasNext())
		{
			l_nextToken = m_nextToken;
			m_nextToken = null;
		}
		else
		{
			throw new NoSuchElementException("No further tokens can be extracted!");
		}

		return l_nextToken;
	}

	/**
	 * Gibt belegte Ressourcen, welche von diesem Objekt belgt werden, wieder frei.
	 * <p>
	 * Wenn Sie diese Methode &uuml;berschreiben, rufen Sie immer super.close() auf.
	 * <p>
	 * <b>Anmerkung:</b> Die Standardimplementierung schlie&szlig;t den
	 * Eingabe-Reader, also rufen Sie unbedingt super.close() auf, wenn Sie diese
	 * Methode außer Kraft setzen.
	 *
	 * @throws IOException Wenn der Reader nicht geschlossen werden konnte.
	 */
	@Override
	public void close() throws IOException
	{
		if (m_reader != null)
		{
			m_reader.close();
		}
	}

	/**
	 * Extrahiert, modifiziert (normalisiert) und liefert das n&auml;chste Token vom
	 * Reader-Objekt. <br>
	 * Wenn kein Token mehr geliefert werden kann, so muß diese Methode den Wert
	 * <code>null</code> liefern.
	 * <p>
	 * <b>Anmerkung:</b> Jede Subklasse mu&szlig; diese Methode implementieren. Wenn
	 * ein Fehler in dieser Methode auftritt, so mu&szlig; dieser unterdr&uuml;ckt
	 * und <code>null</code> geliefert werden.
	 *
	 * @return Das n&auml;chste Token oder <code>null</code>
	 */
	protected abstract String extractNextToken();
}
