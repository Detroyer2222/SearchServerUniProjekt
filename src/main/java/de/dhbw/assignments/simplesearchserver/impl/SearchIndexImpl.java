package de.dhbw.assignments.simplesearchserver.impl;

import de.dhbw.assignments.simplesearchserver.TokenizerFactory;
import de.dhbw.assignments.simplesearchserver.api.ATokenizer;
import de.dhbw.assignments.simplesearchserver.api.ISearchIndex;
import de.dhbw.assignments.simplesearchserver.api.SearchIndexException;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        ATokenizer tokenizer = TokenizerFactory.createNewTokenizer(p_content);
        addDocument(tokenizer, p_documentUri);
    }

    @Override
    public void addDocument(ATokenizer p_content, String p_documentUri) throws SearchIndexException, IOException
    {
        //Check if document already is in index
        if (containsDocument(p_documentUri))
        {
            throw new SearchIndexException("Document: "+ p_documentUri +" already exists");
        }

        //Add document to index
        while(p_content.hasNext())
        {
            List<String> indexedFile = GetLines();
            String token = p_content.next();
            int lineNumber;
            if ((lineNumber = GetLine(token)) < 0)
            {
                //Token does not exist in index
                indexedFile.add(p_content.next() +";"+ p_documentUri +";");
            }
            else
            {
                //Token does exist in Index
                String newLine = "";
                String line = indexedFile.get(lineNumber);
                int offset = GetOffset(line);

                for (int i = 0; i < line.length(); i++) {

                    // Insert the original string character
                    // into the new string
                    newLine += line.charAt(i);

                    if (i == offset) {

                        // Insert the string to be inserted
                        // into the new string
                        newLine += (p_documentUri +";");
                    }
                }
            }

            try(BufferedWriter writer = new BufferedWriter(new FileWriter(_sessionFile)))
            {
                for (var line : indexedFile)
                {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
    }

    @Override
    public void removeDocument(String p_documentUri) throws SearchIndexException
    {

    }

    @Override
    public boolean containsDocument(String p_documentUri)
    {
        if (GetLine(p_documentUri) < 0)
        {
            return true;
        }

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

    private int GetLine(String text)
    {
        try
        {
            try(BufferedReader reader = new BufferedReader(new FileReader(_sessionFile)))
            {
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
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return -1;
    }

    private String GetLine(int lineNumber)
    {
        String line;
        try
        {
            try(BufferedReader reader = new BufferedReader(new FileReader(_sessionFile)))
            {
                int index = 0;
                while((line = reader.readLine()) != null)
                {
                    if (lineNumber == index)
                    {
                        return line;
                    }
                    index++;
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

        return line;
    }

    private int GetOffset(String line)
    {
        int offset = line.indexOf(';');

        return offset;
    }

    private List<String> GetLines()
    {
        List<String> lines = new ArrayList<>();
        try
        {
            try(BufferedReader reader = new BufferedReader(new FileReader(_sessionFile)))
            {
                int index = 0;
                String line;
                while((line = reader.readLine()) != null)
                {
                    lines.add(line);
                    index++;
                }
                return lines;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private List<Integer> GetLines(String text)
    {
        List<Integer> lines = new ArrayList<>();
        try
        {
            try(BufferedReader reader = new BufferedReader(new FileReader(_sessionFile)))
            {
                int index = 0;
                String line;
                while((line = reader.readLine()) != null)
                {
                    if (line.contains(text))
                        lines.add(index);
                    index++;
                }
                return lines;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
