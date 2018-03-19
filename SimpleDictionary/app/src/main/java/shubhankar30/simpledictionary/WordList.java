package shubhankar30.simpledictionary;

/**
 * Created by shubhankarranade30 on 16-03-2018.
 */

public class WordList {
    private String word;
    private String meaning;
    private String type;


    public WordList(String temp_word,String temp_meaning, String temp_type){
        word = temp_word;
        meaning = temp_meaning;
        type = temp_type;
    }

    public String getWord() {
        return word;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getType() {
        return type;
    }
}
