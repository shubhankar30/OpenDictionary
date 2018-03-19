package shubhankar30.simpledictionary;

import android.content.SharedPreferences;

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
        //Single Quotes Error
       // if(word.contains("''")){
         //   word = word.replaceAll("''", "'");
        //}

        return word;
    }

    public String getMeaning() {
      //  if(meaning.contains("''")){
        //    meaning = meaning.replaceAll("''", "'");
      //  }
        return meaning;
    }

    public String getType() {
       // if(type.contains("''")){
      //      type = type.replaceAll("''", "'");
      //  }
        return type;
    }
}
