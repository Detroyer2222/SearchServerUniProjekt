package de.dhbw.assignments.simplesearchserver.impl;

import de.dhbw.assignments.simplesearchserver.api.ATokenizer;

import java.io.Reader;

public class TokenizerImpl extends ATokenizer
{
    private int _position = 0;

    /**
     * Erzeugt einen neuen Tokenizer.
     *
     * @param p_reader Das Reader-Objekt, von dem die einzelnen Token extrahiert
     *                 werden.
     */
    protected TokenizerImpl(Reader p_reader)
    {
        super(p_reader);
    }

    @Override
    protected String extractNextToken()
    {

        String l_token = "";

        if(_position >= m_reader.toString().length() )
        {
            return null;
        }

        for(int i = _position; i < this.m_reader.toString().length(); i++)
        {
            char l_char = m_reader.toString().charAt(i);
            if( Character.isLetterOrDigit(l_char) )
            {
                break;
            }
            _position++;
        }

        for(int i = _position; i < this.m_reader.toString().length(); i++)
        {
            _position++;

            char l_char = m_reader.toString().charAt(i);

            if( Character.isLetterOrDigit(l_char) )
            {
                l_token = l_token + l_char;
            }
            else
            {
                break;
            }
        }

        return l_token;
    }
}
