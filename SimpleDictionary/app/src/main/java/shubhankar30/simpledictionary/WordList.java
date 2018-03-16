package shubhankar30.simpledictionary;

/**
 * Created by shubhankarranade30 on 16-03-2018.
 */

public class WordList {
    private String word;
    private String meaning;

    public WordList(String temp_word,String temp_meaning){
        word = temp_word;
        meaning = temp_meaning;
    }

    public String getWord() {
        return word;
    }

    public String getMeaning() {
        return meaning;
    }
}
