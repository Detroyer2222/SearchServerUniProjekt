package de.dhbw.assignments.simplesearchserver.api;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;

/**
 * Basisinterface f&uur einen einfachen Search-Index.
 *
 * @author Michael Schlegel
 *
 */
public interface ISearchIndex
{
	/**
	 * F&uuml;gt ein neues Dokument in den Suchindex ein. <br>
	 * Es wird der textuelle Inhalt des Dokumentes &uuml;bergeben und die URI des
	 * Dokumentes.<br>
	 * F&uuml;r die Indexierung wird der Inhalt des Dokumentes in einzelne Token
	 * zerlegt.<br>
	 * Die URI dient auch als eindeutige ID des Dokumentes im Index.
	 * <p>
	 * <b>Anmerkung</b>: Ein Dokument darf nur genau einmal zum Index
	 * hinzugef&uuml;gt werden. Wird es ein weiteres mal hinzugef&uuml;gt, so wird
	 * eine Exception geworfen.
	 *
	 * @param p_content     Textueller Inhalt des Dokumentes.
	 * @param p_documentUri Die URI des Dokumentes.
	 * @throws SearchIndexException Wenn sich das Dokument bereits im Index
	 *                              befindet.
	 * @throws IOException          Wenn Fehler beim Tokenizing auftreten.
	 */
	void addDocument(final Reader p_content, final String p_documentUri) throws SearchIndexException, IOException;

	/**
	 * Alternative Methode zu {@link #addDocument(Reader, String)}.<br>
	 * F&uuml;gt ein neues Dokument in den Suchindex ein. <br>
	 * Es wird der textuelle Inhalt des Dokumentes &uuml;bergeben und die URI des
	 * Dokumentes.<br>
	 * Die URI dient auch als eindeutige ID des Dokumentes im Index.
	 * <p>
	 * <b>Anmerkung</b>: Ein Dokument darf nur genau einmal zum Index
	 * hinzugef&uuml;gt werden. Wird es ein weiteres mal hinzugef&uuml;gt, so wird
	 * eine Exception geworfen.
	 *
	 * @param p_content     Textueller Inhalt des Dokumentes.
	 * @param p_documentUri Die URI des Dokumentes.
	 * @throws SearchIndexException Wenn sich das Dokument bereits im Index
	 *                              befindet.
	 * @throws IOException          Wenn Fehler beim Tokenizing auftreten.
	 */
	void addDocument(final ATokenizer p_content, final String p_documentUri) throws SearchIndexException, IOException;

	/**
	 * L&ouml;scht ein Dokument aus dem Index. <br>
	 * Das zu l&ouml;schende Dokument wird &uuml;ber seine URI referenziert.
	 * <p>
	 * <b>Anmerkung</b>: Ein zu l&ouml;schendes Dokument mu&szlig; sich im Index
	 * befinden. Sollte dies nicht der Fall sein, so wird eine Exception geworfen.
	 *
	 * @param p_documentUri Die URI des Dokumentes.
	 * @throws SearchIndexException Wenn sich das zu l&ouml;schende Dokument nicht
	 *                              im Index befindet.
	 */
	void removeDocument(final String p_documentUri) throws SearchIndexException, IOException;

	/**
	 * Gibt an, ob ein Dokument sich im Index befindet.
	 *
	 * @param p_documentUri Die URI des Dokumentes.
	 * @return <code>true</code> oder <code>false</code>
	 */
	boolean containsDocument(String p_documentUri);

	/**
	 * L&ouml;scht alle Dokumente und Tokens aus dem Index.
	 *
	 * @throws SearchIndexException Wenn das L&ouml;schen nicht sauber
	 *                              ausgef&uuml;hrt werden konnte.
	 */
	void clear() throws SearchIndexException;

	/**
	 * Erm&ouml;glicht das Suchen nach Dokumenten.<br>
	 * Als Ergebnis wird eine Liste aller Dokumenten-URI's geliefert, die der
	 * Suchphrase entsprechen.<br>
	 * Die Suchphrase wird intern in einzelne Token {@link ATokenizer} zerlegt und
	 * mit dem Suchindex abgeglichen. Ein Suchphrase kann aus mehreren Tokens
	 * bestehen. Die Verkn&uuml;pfung der Token, wird &uuml;ber den @{link
	 * SearchOperator} festgelegt.<br>
	 * <b>AND</b>: Alle Token m&uuml;ssen in einem Dokument vorkommen.<br>
	 * <b>OR</b>: Mindestens eines der Token mu&szlig; im Dokument vorhanden sein.
	 *
	 * <p>
	 * <b>Anmerkung</b>: Diese Methode liefert niemals <code>null</code>
	 * zur&uuml;ck.
	 *
	 * @param p_query    Suchphrase
	 * @param p_operator Suchoperator
	 * @return Collection mit dokumenten-URI's
	 * @throws SearchIndexException Wird geworfen, wenn w&auml;hrend der Suche ein
	 *                              Fehler auftritt.
	 * @throws IOException          Wenn Fehler beim Tokenizing auftreten.
	 */
	Collection<String> searchDocuments(final String p_query, final SearchOperator p_operator)
	        throws SearchIndexException, IOException;

	/**
	 * Alternative Methode zu {@link #searchDocuments(String, SearchOperator)}.<br>
	 * Erm&ouml;glicht das Suchen nach Dokumenten.<br>
	 * Als Ergebnis wird eine Liste aller Dokumenten-URI's geliefert, die der
	 * Suchphrase entsprechen.<br>
	 * Die Suchphrase kann aus mehreren Token bestehen. Die Verkn&uuml;pfung der
	 * Token, wird &uuml;ber den @{link SearchOperator} festgelegt.<br>
	 * <b>{@link SearchOperator.AND AND}</b>: Alle Token m&uuml;ssen in einem
	 * Dokument vorkommen.<br>
	 * <b>{@link SearchOperator.OR OR}</b>: Mindestens eines der Token mu&szlig; im
	 * Dokument vorhanden sein.
	 *
	 * <p>
	 * <b>Anmerkung</b>: Diese Methode liefert niemals <code>null</code>
	 * zur&uuml;ck.
	 *
	 * @param p_query    Suchphrase, welche bereits Tokenized vorliegt
	 * @param p_operator {@link SearchOperator}
	 * @return Collection mit dokumenten-URI's
	 * @throws SearchIndexException Wird geworfen, wenn w&auml;hrend der Suche ein
	 *                              Fehler auftritt.
	 * @throws IOException          Wenn Fehler beim Tokenizing auftreten.
	 */
	Collection<String> searchDocuments(final ATokenizer p_query, final SearchOperator p_operator)
	        throws SearchIndexException, IOException;

	/**
	 * Liefert die Anzahl an indexierten/suchbaren Dokumenten im Index.
	 *
	 * @return Anzahl der suchbaren Dokumente.
	 */
	int size();

	/**
	 * Liefert die Anzahl an suchbaren Token im Index.
	 *
	 * @return Anzahl der suchbaren Token.
	 */
	int tokenCount();

	/**
	 * Liest die Indexdaten aus einer Resource.
	 * Die bereits im Index vorhandenen Daten werden durch die zu ladenden Daten ersetzt.
	 *
	 * @param p_indexReader Resource, aus der die Indexdaten gelesen werden
	 *
	 * @throws IOException          Fehler beim Lesen der Daten in die Resource
	 * @throws SearchIndexException Fehler beim Zugriff auf den SearchIndex
	 */
	void load(Reader p_indexReader) throws SearchIndexException, IOException;

	/**
	 * Speichert die Indexdaten in eine beliebige Resource, die &uuml;ber einen
	 * Writer angesprochen wird.
	 *
	 * @param p_indexWriter Resource, in der die Indexdaten gespeichert werden
	 *
	 * @throws IOException          Fehler beim Schreiben der Daten in die Resource
	 * @throws SearchIndexException Fehler beim Zugriff auf den SearchIndex
	 */
	void save(Writer p_indexWriter) throws SearchIndexException, IOException;

	/**
	 * SearchOperator
	 *
	 * @author Michael Schlegel
	 *
	 */
	enum SearchOperator
	{
		/**
		 * AND Operator
		 */
		AND,

		/**
		 * OR Operator
		 */
		OR
	}
}
