package edu.upc.fib;

import javafx.util.Pair;

import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * Created by Pau on 5/12/2016.
 */
public class DomainController {

    Library library = new Library();

    public Set<String> getAuthorDocumentTitles(String authorName){
        return library.getAuthorDocumentTitles(authorName);
    }

    public Set<String> getAuthorsByPrefix(String prefix){
        return library.getAuthorsByPrefix(prefix);
    }

    public Vector<String> getDocumentContent(String authorName, String title){
        return library.getDocumentContent(authorName, title);
    }

    public List<Pair<String, String>> getSimilarDocuments(String authorName,String title,Integer nDocuments){
        return library.getSimilarDocuments(authorName,title,nDocuments);
    }

    public Boolean verifyExpression(String expression1, String expression2){
        return library.verifyExpression(expression1,expression2);
    }

    public Set<String> getAuthorNames(){
        return library.getAuthorNames();
    }

    public Boolean addAuthor(String authorName){
        return library.addAuthor(authorName);
    }

    public Boolean modifyAuthor(String authorName, String newAuthorName){
        return library.modifyAuthor(authorName, newAuthorName);
    }

    public Boolean removeAuthor(String authorName){
        return library.removeAuthor(authorName);
    }

    public Set<String> getDocumentTitles(){
        return getDocumentTitles();
    }

    public Boolean addDocument(String AuthorName, String title, Vector<String> content){
        return library.addDocument(AuthorName, title, content);
    }

    public Boolean modifyDocumentAuthor(String authorName,String title,String newAuthorName){
        return modifyDocumentAuthor(authorName, title, newAuthorName);
    }

    public Boolean modifyDocumentTitle(String authorName,String title,String newTitle){
        return modifyDocumentTitle(authorName, title, newTitle);
    }

    public Boolean modifyDocumentContent(String authorName,String title,String newContent){
        return modifyDocumentContent(authorName, title, newContent);
    }

    public Boolean removeDocument(String AuthorName, String title){
        return library.removeDocument(AuthorName, title);
    }
}
