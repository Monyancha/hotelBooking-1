package ru.nicetoh8u.hotelbooking;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Belal on 9/5/2017.
 */

public class ListViewAdapter extends ArrayAdapter<Apart> {

    //the hero list that will be displayed
    private List<Apart> apartList;

    //the context object
    private Context mCtx;

    //here we are getting the herolist and context
    //so while creating the object of this adapter class we need to give herolist and context
    public ListViewAdapter(List<Apart> apartList, Context mCtx) {
        super(mCtx, R.layout.list_items, apartList);
        this.apartList = apartList;
        this.mCtx = mCtx;
    }

    //this method will return the list item
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //getting the layoutinflater
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        //creating a view with our xml layout
        View listViewItem = inflater.inflate(R.layout.list_items, null, true);

        //getting text views
        TextView textViewName = listViewItem.findViewById(R.id.textViewName);
        TextView textViewCost = listViewItem.findViewById(R.id.textViewCost);
        ImageView imageView = listViewItem.findViewById(R.id.imageView);

        //Getting the hero for the specified position
        Apart apart = apartList.get(position);

        //setting hero values to textviews
        textViewName.setText(apart.getApart_name());
        textViewCost.setText("Стоимость номера за сутки: " + apart.getApart_cost() + "Р");
       Picasso.get().load(apart.getApart_image_url()).resize(250,250).into(imageView);

        //returning the listitem
        return listViewItem;
    }
}
