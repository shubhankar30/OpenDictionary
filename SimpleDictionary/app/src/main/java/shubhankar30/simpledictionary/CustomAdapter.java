package shubhankar30.simpledictionary;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by shubhankarranade30 on 14-03-2018.
 * Github link: https://github.com/shubhankar30
 * Email-id: shubhankarranade30@gmail.com
 */

public class CustomAdapter extends ArrayAdapter<WordList>{

    private LayoutInflater mInflater;
    private ArrayList<WordList> words;
    private int mViewResourceId;

    public CustomAdapter(Context context, int textViewResourceId, ArrayList<WordList> words){
        super(context, textViewResourceId, words);
        this.words=words;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parents){
        convertView = mInflater.inflate(mViewResourceId, null );

        WordList word = words.get(position);

        if(word != null){
            TextView Word = (TextView) convertView.findViewById(R.id.wordId);
            TextView Type = (TextView) convertView.findViewById(R.id.typeId);
            TextView Meaning = (TextView) convertView.findViewById(R.id.meaningId);

            if(Word != null){
                Word.setText((word.getWord()));
            }
            if(Meaning != null){
                Meaning.setText(Html.fromHtml((word.getMeaning())));//render <b> </b> from HTML
            }
            if(Type != null){
                Type.setText(Html.fromHtml((word.getType())));//render <b> </b> from HTML
            }

        }
        return convertView;
    }

}
