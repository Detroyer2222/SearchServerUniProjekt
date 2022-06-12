package de.dhbw.assignments.simplesearchserver.impl;

import de.dhbw.assignments.simplesearchserver.TokenizerFactory;
import de.dhbw.assignments.simplesearchserver.api.ATokenizer;
import de.dhbw.assignments.simplesearchserver.api.ISearchIndex;
import de.dhbw.assignments.simplesearchserver.api.SearchIndexException;

import java.io.*;
import java.util.*;

public class SearchIndexImpl implements ISearchIndex
{
    private File _directory = new File("./indexFiles");
    private File _sessionFile;
    private int _documents = 0;

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
        if (!containsDocument(p_documentUri))
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
        _documents++;
    }

    @Override
    public void removeDocument(String p_documentUri) throws SearchIndexException
    {
        if (!containsDocument(p_documentUri))
        {
            throw  new SearchIndexException("Document: "+ p_documentUri +"does not exist");
        }

        List<String> file = GetLines();
        List<Integer> linesOfDocument = GetLines(p_documentUri);

        for (var lineNumber : linesOfDocument)
        {
            String line = file.get(lineNumber);
            var lineArguments = line.split(";");
            if (lineArguments.length < 3)
            {
                //line can be removed its only from this one document
                file.remove(lineNumber);
            }

            int index = line.indexOf(p_documentUri);
            int lastIndex = index + p_documentUri.length();

            StringBuilder stringBuilder = new StringBuilder(line);
            stringBuilder.substring(index, lastIndex);

            file.remove(lineNumber);
            file.add(lineNumber, stringBuilder.toString());
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(_sessionFile)))
        {
            for (var line : file)
            {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        _documents--;
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
        try
        {
            new FileWriter(_sessionFile).close();
        }
        catch (Exception e)
        {
            throw  new SearchIndexException("Couldnt clear index");
        }
    }

    @Override
    public Collection<String> searchDocuments(String p_query, SearchOperator p_operator) throws SearchIndexException, IOException
    {
        ATokenizer tokenizer;
        try(BufferedReader reader = new BufferedReader(new StringReader(p_query)))
        {
            tokenizer = TokenizerFactory.createNewTokenizer(reader);
        }

        return searchDocuments(tokenizer, p_operator);
    }

    @Override
    public Collection<String> searchDocuments(ATokenizer p_query, SearchOperator p_operator) throws SearchIndexException, IOException
    {
        Collection<String> results = new HashSet<>();

        List<String> lines = GetLines(p_query);

        if (lines.size() == 0)
        {
            //no query parameter exists
            return results;
        }

        switch (p_operator)
        {
            case AND:

                for (int i = 0; i < lines.size(); i++)
                {
                    var lineArguments = lines.get(i).split(";");
                    lineArguments[0] = "";
                    var argumentList = new ArrayList<String>(Arrays.asList(lineArguments));
                    if (i < 1)
                    {
                        for (int j = 1; j < lineArguments.length - 1; j++)
                        {
                            results.add(lineArguments[j]);
                        }
                    }

                    for (String result : results)
                    {
                        if (!argumentList.contains(result))
                        {
                            results.remove(result);
                        }
                    }
                }
                return results;

            case OR:
                for (String line : lines)
                {
                    var lineArguments = line.split(";");
                    for (int i = 1; i < lineArguments.length - 1; i++)
                    {
                        results.add(lineArguments[i]);
                    }
                }
                return results;
        }
        return new HashSet<>();
    }

    @Override
    public int size()
    {
        return _documents;
    }

    @Override
    public int tokenCount()
    {
        return GetLines().size();
    }

    @Override
    public void load(Reader p_indexReader) throws SearchIndexException, IOException
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(_sessionFile)))
        {
            writer.write(p_indexReader.toString());
        }
    }

    @Override
    public void save(Writer p_indexWriter) throws SearchIndexException, IOException
    {
        try(BufferedReader reader = new BufferedReader(new FileReader(_sessionFile)))
        {
            String line = reader.readLine();
            while (line != null)
            {
                p_indexWriter.write(line);
                line = reader.readLine();
            }
        }
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

    private String GetLine(int lineNumber) throws IOException
    {
        String line;

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

        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(_sessionFile))) {
                int index = 0;
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                    index++;
                }
                return lines;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private List<Integer> GetLines(String text)
    {
        List<Integer> lines = new ArrayList<>();

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
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private List<String> GetLines(ATokenizer tokenizer) throws IOException
    {
        List<String> lines = new ArrayList<>();
        List<String> searchArguments = new ArrayList<>();

        for (String s : tokenizer)
        {
            searchArguments.add(s);
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(_sessionFile)))
        {
            String line;
            while((line = reader.readLine()) != null)
            {
                var lineArguments = line.split(";");

                for (String lineArgument : lineArguments)
                {
                    searchArguments.contains(lineArgument);
                    lines.add(line);
                }
            }
            return lines;
        }
    }
}
