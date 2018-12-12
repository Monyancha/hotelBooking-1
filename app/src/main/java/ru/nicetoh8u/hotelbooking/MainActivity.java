package ru.nicetoh8u.hotelbooking;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.nicetoh8u.hotelbooking.ListView.Apart;
import ru.nicetoh8u.hotelbooking.ListView.ListViewAdapter;
import ru.nicetoh8u.hotelbooking.ListView.OnSwipeTouchListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final String JSON_URL = "http://10.0.2.2:8080/aparts/";

    //listview object
    private ListView listView;
    private Realm realm;
    //the hero list where we will store all the hero objects after parsing json
    private List<Apart> apartList;
    private List<Apart> prevApartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Realm init
        Realm.init(this);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //initializing listview and hero list
        listView = (ListView) findViewById(R.id.listView);
        apartList = new ArrayList<>();
        prevApartList = new ArrayList<>();

        //this method will fetch and parse the data
        loadApartList();

        listView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {
                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeBottom() {
                loadApartList();
                //Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (id == R.id.nav_login) {
            drawer.closeDrawer(GravityCompat.START);
            Intent login = new Intent(this, Login.class);
            startActivity(login);
        } else if (id == R.id.nav_map) {
            drawer.closeDrawer(GravityCompat.START);
            Intent map = new Intent(this, MapHotels.class);
            startActivity(map);

        } else if (id == R.id.nav_orders) {

        } else if (id == R.id.nav_hotels) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void loadApartList() {
        //getting the progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        realm = Realm.getDefaultInstance();
        prevApartList = apartList;
        apartList = new ArrayList<>();
        //making the progressbar visible
        progressBar.setVisibility(View.VISIBLE);

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion


                        try {
                            response = "{ \"aparts\":" + response + "}";
                            //getting the whole json object from the response
                            System.out.println("este - " + response.toString());
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray apartArray = obj.getJSONArray("aparts");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < apartArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject apartObject = apartArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                apartList.add(new Apart(Integer.parseInt(apartObject.getString("apartId")),
                                        apartObject.getString("apart_name"),
                                        apartObject.getString("apart_city"),
                                        apartObject.getString("apart_address"),
                                        apartObject.getString("apart_phone"),
                                        apartObject.getString("apart_x"),
                                        apartObject.getString("apart_y"),
                                        Integer.parseInt(apartObject.getString("apart_cost")),
                                        apartObject.getString("apart_description"),
                                        apartObject.getString("apart_image_url")));


//check to not download files every refresh
                                if (!((i < prevApartList.size())
                                        && (prevApartList.get(i).getApartId().equals(apartList.get(i).getApartId()))
                                        && (prevApartList.get(i).getApart_image_url().equals(apartList.get(i).getApart_image_url()))
                                        && (prevApartList.get(i).getApart_name().equals(apartList.get(i).getApart_name()))
                                ))
                                    getBitMap(apartObject.getString("apart_image_url"), Integer.parseInt(apartObject.getString("apartId")));


                            }

                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    RealmResults<Apart> aparts = realm.where(Apart.class).findAll();
                                    aparts.deleteAllFromRealm();
                                }
                            });

                            realm.beginTransaction();

                            for (Apart apart : apartList)
                                realm.copyToRealm(apart);

                            realm.commitTransaction();


                            //creating custom adapter object
                            ListViewAdapter adapter = new ListViewAdapter(apartList, getApplicationContext());

                            //adding the adapter to listview
                            listView.setAdapter(adapter);

                            progressBar.setVisibility(View.INVISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs




                        Toast.makeText(getApplicationContext(), "Нет соединения с сервером", Toast.LENGTH_SHORT).show();

                        apartList = new ArrayList<>();

                        RealmResults<Apart> aparts = realm.where(Apart.class).findAll();

                       for (int i=0;i<aparts.size();i++)
                           apartList.add(aparts.get(i));


                        //creating custom adapter object
                        ListViewAdapter adapter = new ListViewAdapter(apartList, getApplicationContext());

                        //adding the adapter to listview
                        listView.setAdapter(adapter);

                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });

        //5000ms connection limit
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }

    //getting Bitmap from url
    public void getBitMap(String url, Integer id) {


        Picasso.get().load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                saveToInternalStorage(bitmap, id);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }


            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });

    }

    //saving bitmap into  internal storage
    private String saveToInternalStorage(Bitmap bitmapImage, Integer id) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir

        File mypath = new File(directory, id + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


    public void onDestroy() {

        super.onDestroy();

        realm.close();

    }

}
