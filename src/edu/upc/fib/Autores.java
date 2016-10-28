package edu.upc.fib;

import java.util.TreeMap;
import java.util.Vector;

public class Autores {
    //> TreeMap de autores (donde cada autor tendrá un vector con los punteros o ids de los documentos de los que es autor)
        // Separar en una clase aparte?
        // Crear una clase que implementa TreeMap y hacernos una versión con funcionalidades que necesitemos?
    TreeMap<Frase, Vector<Documento>> documentAuthors;

    public Autores() {
        documentAuthors = new TreeMap<>();
    }

    public Boolean existsAuthor(Frase author) {
        return documentAuthors.containsKey(author);
    }

    public void addAuthor(String author) {
        Frase newAuthor = new Frase(author);
        if (!existsAuthor(newAuthor)) {
            documentAuthors.put(newAuthor, new Vector());
        }
        // Decidir que hacer si ya existe
    }

    // Hay que actualizarlo cuando se añada un nuevo documento a la biblioteca
    public void addDocumentToAuthor() {

    }


}
