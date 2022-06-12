package de.dhbw.assignments.simplesearchserver.impl;

import de.dhbw.assignments.simplesearchserver.api.ATokenizer;
import de.dhbw.assignments.simplesearchserver.api.ISearchIndex;
import de.dhbw.assignments.simplesearchserver.api.SearchIndexException;

import java.io.*;
import java.util.Collection;
import java.util.UUID;

public class SearchIndexImpl implements ISearchIndex
{
    private File _directory = new File("./indexFiles");
    private File _sessionFile;

    //constructor
    public SearchIndexImpl()
    {
        if(!_directory.exists())
        {
            //if not, create it
            _directory.mkdir();
        }

        //create a new session file
        _sessionFile = new File(_directory, UUID.randomUUID() + ".txt");
    }

    @Override
    public void addDocument(Reader p_content, String p_documentUri) throws SearchIndexException, IOException
    {

    }

    @Override
    public void addDocument(ATokenizer p_content, String p_documentUri) throws SearchIndexException, IOException
    {

    }

    @Override
    public void removeDocument(String p_documentUri) throws SearchIndexException
    {

    }

    @Override
    public boolean containsDocument(String p_documentUri)
    {
        return false;
    }

    @Override
    public void clear() throws SearchIndexException
    {

    }

    @Override
    public Collection<String> searchDocuments(String p_query, SearchOperator p_operator) throws SearchIndexException, IOException
    {
        return null;
    }

    @Override
    public Collection<String> searchDocuments(ATokenizer p_query, SearchOperator p_operator) throws SearchIndexException, IOException
    {
        return null;
    }

    @Override
    public int size()
    {
        return 0;
    }

    @Override
    public int tokenCount()
    {
        return 0;
    }

    @Override
    public void load(Reader p_indexReader) throws SearchIndexException, IOException
    {

    }

    @Override
    public void save(Writer p_indexWriter) throws SearchIndexException, IOException
    {

    }

    private int GetLineOfText(String text) throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(_sessionFile));
        String line;
        int lineNumber = 0;
        while ((line = reader.readLine()) != null)
        {
            lineNumber++;
            if(line.contains(text))
            {
                return lineNumber;
            }
        }
        return -1;
    }

}
