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

        String document1 = "elias peter lisa";
        String document2 = "elias dieter lisa";
        String document3 = "elias guenter lena";

        Reader doc1Reader = new StringReader(document1);
        Reader doc2Reader = new StringReader(document2);
        Reader doc3Reader = new StringReader(document3);

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


        try
        {
            //search for a word
            var resultAnd = searchIndex.searchDocuments("lisa peter", ISearchIndex.SearchOperator.AND);
            var resultOr = searchIndex.searchDocuments("lisa peter", ISearchIndex.SearchOperator.OR);

            //print the results
            System.out.println("AND: " + resultAnd);
            System.out.println("OR: " + resultOr);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }
}
