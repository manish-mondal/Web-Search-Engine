package Dictionary;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dictionary {

    private static IDictionary dict;
    private static BufferedReader bf_DE;

    public void EnglishDict(){
        try {
            //loading JWI dictionary from the Installed location
            URL url = new URL("file",null, "C:\\Program Files (x86)\\WordNet\\2.1\\dict");
            // /home/project/WordNet-3.0/dict
            dict = new edu.mit.jwi.Dictionary(url);
            dict.open(); //opening the dictionary
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getEngSynonyms(String term)
    {
        // set stores identical objects only once
        Set<String> synonyms = new HashSet<>();
        Set<IWordID> contextTerms = new HashSet<>();
        IIndexWord noun,verb,adj,adv;
        //checking whether query terms has a synonym in any of the following context
        if((noun = dict.getIndexWord (term, POS.NOUN)) != null)
            contextTerms.addAll(noun.getWordIDs());
        if((verb=dict.getIndexWord (term, POS.VERB)) != null)
            contextTerms.addAll(verb.getWordIDs());
        if((adj=dict.getIndexWord (term, POS.ADJECTIVE)) != null)
            contextTerms.addAll(adj.getWordIDs());
        if((adv=dict.getIndexWord (term, POS.ADVERB)) != null)
            contextTerms.addAll(adv.getWordIDs());

        for (IWordID wordId : contextTerms) // context base terms list N V A A
        {
            IWord w = dict.getWord(wordId); // (term as N | term as V.....) all has different id
            synonyms.add(w.getLemma()); // Storing the occurrence in Synonyms Set             getLemma == toString() of synonym
            ISynset synset = w.getSynset();// getting Word in the specific context  N|V|A|A
            for (IWord iWord : synset.getWords()) //all synonyms in that specific context
            {
                synonyms.add(iWord.getLemma()); //store context specific synonyms
            }
        }
        synonyms.add(term);
        return new ArrayList<>(synonyms);
    }

    public static void main (String[] args) throws IOException {
        Dictionary d=new Dictionary();
        d.EnglishDict();
        List<String> synsE = d.getEngSynonyms("bottle");
        System.out.println("\n--Eng--");
        for (String syn : synsE) {
            System.out.print(syn+"\t");
        }
    }
}
