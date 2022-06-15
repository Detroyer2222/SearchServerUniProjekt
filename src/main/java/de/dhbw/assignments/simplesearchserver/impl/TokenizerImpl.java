package de.dhbw.assignments.simplesearchserver.impl;

import de.dhbw.assignments.simplesearchserver.api.ATokenizer;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class TokenizerImpl extends ATokenizer
{
    private int _position = 0;
    private Queue<String> _tokens;

    /**
     * Erzeugt einen neuen Tokenizer.
     *
     * @param p_reader Das Reader-Objekt, von dem die einzelnen Token extrahiert
     *                 werden.
     */
    protected TokenizerImpl(Reader p_reader)
    {
        super(p_reader);
        GetTokens(p_reader);
    }

    private void GetTokens(Reader p_reader)
    {
        StringBuilder sb = new StringBuilder();
        _tokens = new LinkedList<String>();

        char[] buffer = new char[1024];
        int read = 0;
        while (true)
        {
            try
            {
                if (!((read = p_reader.read(buffer)) > 0)) break;
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            sb.append(buffer, 0, read);
        }

        String tokens = sb.toString();

        for (String token : tokens.split(" "))
        {
            _tokens.add(token);
        }
    }

    @Override
    protected String extractNextToken()
    {
        if (_position >= _tokens.size())
        {
            return null;
        }
        else
        {
            return _tokens.remove();
        }
    }
}
