package de.dhbw.assignments.simplesearchserver.test;

import de.dhbw.assignments.simplesearchserver.api.ISearchIndex;
import de.dhbw.assignments.simplesearchserver.api.SearchIndexException;
import de.dhbw.assignments.simplesearchserver.impl.SearchIndexImpl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class SearchIndexTest
{
    //program start
    public static void main(String[] args) {
        //create new search index
        SearchIndexImpl searchIndex = new SearchIndexImpl();

        String document1 = "This is a document";
        String document2 = "This is another document";
        String document3 = "This is a third document";

        Reader doc1Reader = new StringReader(document1);
        Reader doc2Reader = new StringReader(document1);
        Reader doc3Reader = new StringReader(document1);

        //add some documents to the search index
        try
        {
            searchIndex.addDocument(doc1Reader, "document1");
            searchIndex.addDocument(doc2Reader, "document2");
            searchIndex.addDocument(doc3Reader, "document3");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        //search for a word
        try
        {
            var result = searchIndex.searchDocuments("This is a", ISearchIndex.SearchOperator.OR);
            System.out.println(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

       try
       {
           searchIndex.removeDocument("document1");
           searchIndex.removeDocument("document2");
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }
    }
}
