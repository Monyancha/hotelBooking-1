package ru.nicetoh8u.hotelbooking.ListView;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import ru.nicetoh8u.hotelbooking.R;

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
        Picasso.get().load("file:"+mCtx.getDir("imageDir",
                Context.MODE_PRIVATE).toString()+"/"+apart.getApartId()+".jpg")
                .resize(250,250).into(imageView);


       /* try {

            File f=new File(mCtx.getDir("imageDir", Context.MODE_PRIVATE), apart.getApartId()+".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

            imageView.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }*/



        return listViewItem;
    }
}
