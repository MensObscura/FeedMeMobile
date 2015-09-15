package com.example.thibault.feedme.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.thibault.feedme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thibault on 14/09/2015.
 */
public class ListAnnounceAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> announces;

    // Gets the context so it can be used later
    public ListAnnounceAdapter(Context c) {
        mContext = c;
        announces = new ArrayList<String>();
        announces.add("Choucroute");
        announces.add("Bouillabaisse");
        announces.add("Filet de veau");
        announces.add("Pieds de porc");
        announces.add("vessie de lapin");
        announces.add("Cuisses de grenouilles");
        announces.add("Escargot en sauce");
        announces.add("Rognons à la biere");
        announces.add("Foie de morue");
        announces.add("Jambon à la thibaud");
        announces.add("Canard laqué Chino-Algérien");
    }

    // Total number of things contained within the adapter
    public int getCount() {
        return announces.size();
    }

    // Require for structure, not really used in my code.
    public Object getItem(int position) {
        return null;
    }

    // Require for structure, not really used in my code. Can
    // be used to get the id of an item in the adapter for
    // manual control.
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View outputView;
        TextView title;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            outputView = convertView;

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            outputView = inflater.inflate(R.layout.card_announce, null);


        } else {
            outputView = convertView;
        }
        title = (TextView) outputView.findViewById(R.id.TVcard_title);
        title.setText(announces.get(position));


        outputView.setId(position);

        return outputView;
    }
}
