package edu.upc.fib;

import javax.print.Doc;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Documents {
    //private TreeMap<String, Vector<Document>> mDocuments; // Vector de documentos con el mismo título
    //private TreeMap<String, HashMap<String, Document>> mDocuments; // Vector de documentos con el mismo título <título, <autor, Document>>
    private TreeMap<String, Hashtable<Author, Document>> mDocuments; // Vector de documentos con el mismo título <título, <Author, Document>>

    private Hashtable<String, Integer> mWordFrequency; // Frecuencia de cada palabra en todos los documentos <palabra, apariciones>

    // MEJORABLE ????????????????????????????????????????????????????????????????
    private Hashtable<String, Vector<Document>> mWordDocuments; // Documentos en los que la palabra aparece <título, Vector<Document>>

    private Hashtable<String, Double> mWordsWeight; // Peso de cada palabra en todos los documentos

    private Vector<String> connectorWords; // Lista de palabras funcionales

    public Documents() {
        mDocuments = new TreeMap<>();
        mWordFrequency = new Hashtable<>();
        mWordDocuments = new Hashtable<>();
        mWordsWeight = new Hashtable<>();

        // Cargar palabras funcionales
        connectorWords = new Vector<>();
        try (BufferedReader br = new BufferedReader(new FileReader("empty.sp"))) {
            String line = br.readLine();
            while (line != null) {
                connectorWords.add(new String(line));
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Gestión de documentos

    public boolean addDocument(Author author, String title, Vector<String> content) {
        Document newDocument = new Document(author, title, content, connectorWords);
        if (author.addDocument(newDocument)) {
            //Vector<Document> vDocuments;
            //HashMap<String, Document> hDocuments;
            Hashtable<Author, Document> hDocuments;
            if (mDocuments.containsKey(title)) {
                hDocuments = mDocuments.get(title);
            } else {
                //hDocuments = new Vector<>();
                hDocuments = new Hashtable<>();
            }
            //hDocuments.add(newDocument);
            //hDocuments.put(author.getName().toString(), newDocument);
            hDocuments.put(author, newDocument);
            mDocuments.put(title, hDocuments);

            // Puede ser que no envie la direccion de memoria que tendra en mDocuments? => FIX IT
            updateWordFrequency(newDocument, true);
            return true;
        }
        return false;
    }

    //> Actualiza mWordFrequency y mWordDocuments
    // Recalcular la frecuencia global de todas las palabras despues de cada modificacion no es eficiente
    // Esta funcion se usara para añadir y quitar frecuencias
    // Cuando haya modificaciones o borrados, se llamara primero a esta funcion para quitar la frencuencia de las palabras
    public void updateWordFrequency(Document document, boolean increase) {
        Hashtable<String, Integer> wordFrequency = document.getWordFrequency();
        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
            String word = entry.getKey();
            if (!increase) {
                if (mWordFrequency.get(word) == 1) {
                    mWordFrequency.remove(word);
                } else {
                    mWordFrequency.put(word, mWordFrequency.get(word) - entry.getValue());
                }
            } else if (mWordFrequency.containsKey(word)) {
                mWordFrequency.put(word, entry.getValue() + mWordFrequency.get(word));
            } else {
                mWordFrequency.put(word, entry.getValue());
            }

            if (!increase) {
                if (mWordDocuments.get(word).size() == 1) {
                    mWordDocuments.remove(word);
                } else {
                    Vector<Document> newVDocuments = mWordDocuments.get(word);
                    newVDocuments.remove(document);
                    mWordDocuments.put(word, newVDocuments);
                }
            } else if (mWordDocuments.containsKey(word)) {
                Vector<Document> newVDocuments = mWordDocuments.get(word);
                newVDocuments.add(document);
                mWordDocuments.put(word, newVDocuments);
            } else {
                Vector<Document> newVDocuments = new Vector<>();
                newVDocuments.add(document);
                mWordDocuments.put(word, newVDocuments);
            }
        }
        updateWordWeight();
    }

    public void updateWordWeight() {
        mWordsWeight = new Hashtable<>();
        for(Map.Entry<String, Integer> word : mWordFrequency.entrySet()) {
            mWordsWeight.put(word.getKey(), Math.log10(mDocuments.size()/word.getValue()));
        }
    }

    public void modifyDocumentAuthor(Author author, String title, Author newAuthor) {
        Hashtable<Author, Document> newDocuments = mDocuments.get(title);
        Document document = newDocuments.get(author);
        document.setAuthor(newAuthor);
        newDocuments.put(newAuthor, document);
        newDocuments.remove(author);
        mDocuments.replace(title, newDocuments);

        author.removeDocument(title);
        newAuthor.addDocument(document);
    }

    public void modifyDocumentTitle(Author author, String title, String newTitle) {
        Document document = mDocuments.get(title).get(author);
        document.setTitle(new Sentence(newTitle));

        // Borrar documento de donde estaba
        mDocuments.get(title).remove(author);
        if (mDocuments.get(title).size() == 0) {
            mDocuments.remove(title);
        }

        // Añadir el documento donde tiene que estar
        Hashtable<Author, Document> newDocuments;
        if (mDocuments.containsKey(newTitle)) {
            newDocuments = mDocuments.get(newTitle);
        } else {
            newDocuments = new Hashtable<>();
        }
        newDocuments.put(author, document);
        mDocuments.put(newTitle, newDocuments);

        // Modificarlo dentro del autor
        author.removeDocument(title);
        author.addDocument(document);
    }

    public void modifyDocumentContent(Author author, String title, Vector<String> newContent) {
        updateWordFrequency(mDocuments.get(title).get(author), false);
        mDocuments.get(title).get(author).setContent(new Content(newContent));
        updateWordFrequency(mDocuments.get(title).get(author), true);
    }

    public boolean removeDocument(Author author, String title) {
        if (mDocuments.containsKey(title)) {
            if (mDocuments.get(title).containsKey(author)) {
                Document removedDocument = mDocuments.get(title).get(author);
                updateWordFrequency(removedDocument, false);
                mDocuments.get(title).remove(author);

                if (mDocuments.get(title).size() == 0) {
                    mDocuments.remove(title);
                }

                author.removeDocument(title);
                return true;
            }
        }
        return false;
    }

    public Set<String> getDocumentTitles() {
        return mDocuments.keySet();
    }

    public Vector<String> getDocumentContent(Author author, String title) {
        return mDocuments.get(title).get(author).getContent().toStrings();
    }

    public boolean existsDocument(Author author, String title) {
        if (mDocuments.containsKey(title)) {
            if (mDocuments.get(title).containsKey(author)) {
                return true;
            }
        }
        return false;
    }

}
