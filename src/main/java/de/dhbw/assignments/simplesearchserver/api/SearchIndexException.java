package de.dhbw.assignments.simplesearchserver.api;

/**
 * SearchIndex-Exception Klasse
 *
 * @author Michael Schlegel
 *
 */
public class SearchIndexException extends Exception
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public SearchIndexException(final String p_message)
	{
		super(p_message);
	}

	public SearchIndexException(final String p_message, final Throwable p_cause)
	{
		super(p_message, p_cause);
	}
}
