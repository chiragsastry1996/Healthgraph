package com.acharya.healthgraph.GameList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.acharya.healthgraph.HttpHandler;
import com.acharya.healthgraph.Login.Login;
import com.acharya.healthgraph.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Person> personList;
    private PersonAdapter personAdapter;
    private static long back_pressed;
    ProgressBar progressBar;
    public  static String[] games = new String[8];
    public  static String[] url = new String[8];
    public  static String[] url1 = new String[8];
    //ArrayList of ArrayList to get multiple Json Values (2D JSON)
    public static ArrayList<ArrayList<String>> gameDetails;
    public static ArrayList <ArrayList<String>> game_images;
    private GoogleApiClient mGoogleApiClient;
    int auth=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

        recyclerView = (RecyclerView) findViewById(R.id.names_list_view);
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);

        initURL();

        personList = new ArrayList<>();
        gameDetails = new ArrayList<>();
        game_images = new ArrayList<>();
        new GetContacts().execute();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        Intent mIntent = getIntent();
        Bundle bundle = mIntent.getExtras();
        if (bundle != null) {
            auth = bundle.getInt("auth");
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),"Damn!! There seems to be too much data.\n\nPlease wait...",Toast.LENGTH_SHORT).show();
            }
        }, 3000);

    }

    public void initURL(){
        url[0] = "http://store.steampowered.com/api/appdetails/?appids=403640";
        url[1] = "http://store.steampowered.com/api/appdetails/?appids=271590";
        url[2] = "http://store.steampowered.com/api/appdetails/?appids=374320";
        url[3] = "http://store.steampowered.com/api/appdetails/?appids=230410";
        url[4] = "http://store.steampowered.com/api/appdetails/?appids=440";
        url[5] = "http://store.steampowered.com/api/appdetails/?appids=570";
        url[6] = "http://store.steampowered.com/api/appdetails/?appids=292030";
        url[7] = "http://store.steampowered.com/api/appdetails/?appids=252950";

        url1[0] = "https://api.steampowered.com/ISteamUserStats/GetNumberOfCurrentPlayers/v1/?appid=403640";
        url1[1] = "https://api.steampowered.com/ISteamUserStats/GetNumberOfCurrentPlayers/v1/?appid=271590";
        url1[2] = "https://api.steampowered.com/ISteamUserStats/GetNumberOfCurrentPlayers/v1/?appid=374320";
        url1[3] = "https://api.steampowered.com/ISteamUserStats/GetNumberOfCurrentPlayers/v1/?appid=230410";
        url1[4] = "https://api.steampowered.com/ISteamUserStats/GetNumberOfCurrentPlayers/v1/?appid=440";
        url1[5] = "https://api.steampowered.com/ISteamUserStats/GetNumberOfCurrentPlayers/v1/?appid=570";
        url1[6] = "https://api.steampowered.com/ISteamUserStats/GetNumberOfCurrentPlayers/v1/?appid=292030";
        url1[7] = "https://api.steampowered.com/ISteamUserStats/GetNumberOfCurrentPlayers/v1/?appid=252950";

        games[0] = "403640";
        games[1] = "271590";
        games[2] = "374320";
        games[3] = "230410";
        games[4] = "440";
        games[5] = "570";
        games[6] = "292030";
        games[7] = "252950";


    }

    private String TAG = MainActivity.class.getSimpleName();



    protected void onResume() {
        super.onResume();
    }


    //Fetching the JSON Object
    public class GetContacts extends AsyncTask<Void, Void, Void> {



        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        //GET request done in background
        @Override
        public Void doInBackground(Void... arg0) {
            //Calling the HTTPHandler
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
        for(int i=0;i<url.length;i++) {
            String jsonStr = sh.makeServiceCall(url[i]);
            String jsonStr1 = sh.makeServiceCall(url1[i]);

            Log.e(TAG, "Response from url: " + jsonStr1 );

            if (jsonStr != null && jsonStr1 != null) {
                try {

                    //Create JSON Object
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject game = jsonObj.getJSONObject(games[i]);
                    JSONObject data = game.getJSONObject("data");
                    JSONObject requirements = data.getJSONObject("pc_requirements");
                    JSONObject meta = data.getJSONObject("metacritic");
                  //  JSONObject metaObj = meta.getJSONObject(0);
                    JSONArray screenshots = data.getJSONArray("screenshots");

                    JSONObject jsonObj1 = new JSONObject(jsonStr1);
                    JSONObject response = jsonObj1.getJSONObject("response");

                    String name = data.getString("name");
                    String image = data.getString("header_image");
                    String users = response.getString("player_count");
                    String required_age = data.getString("required_age");
                    String about_the_game = data.getString("about_the_game");
                    String detailed_description = data.getString("detailed_description");
                    String short_description = data.getString("short_description");
                    String minimum = requirements.getString("minimum");
                    String metacritic = meta.getString("score");


                    String recommended;

                    try {
                        recommended = requirements.getString("recommended");

                    }
                    catch (Exception e) {
                        recommended = "Recommended Requirements data\nnot available for this game";

                    }

                    ArrayList<String> screenshot_images = new ArrayList<>();
                    for(int j=0;j<5;j++) {

                        JSONObject images = screenshots.getJSONObject(j);
                        String simage = images.getString("path_thumbnail");
                        screenshot_images.add(simage);

                    }
                    game_images.add(screenshot_images);


                    //Get all the attribute values from the Json Object
                    ArrayList<String> details = new ArrayList<>();

                    details.add(name);
                    details.add(users);
                    details.add(image);
                    details.add(required_age);
                    details.add(about_the_game);
                    details.add(detailed_description);
                    details.add(short_description);
                    details.add(minimum);
                    details.add(recommended);
                    details.add(metacritic);


                    gameDetails.add(details);

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    LENGTH_LONG)
                                    .show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                LENGTH_LONG)
                                .show();
                    }
                });

            }

        }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //On UI Thread, perform Front-End tasks
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    progressBar.setVisibility(View.GONE);
                    for (int j = 0; j < gameDetails.size(); j++)
                        personList.add(new Person(gameDetails.get(j).get(0), gameDetails.get(j).get(1), gameDetails.get(j).get(2)));
                    personAdapter = new PersonAdapter(MainActivity.this, 0, personList);
                    recyclerView.setAdapter(personAdapter);


                }

            });
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sign_out: {
                if(auth==1) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    // ...
                                    Toast.makeText(getApplicationContext(),"Google account Logged Out",Toast.LENGTH_SHORT).show();
                                    Intent i=new Intent(getApplicationContext(),Login.class);
                                    startActivity(i);
                                    finish();
                                }
                            });
                }
                else if(auth == 2) {
                    Toast.makeText(getApplicationContext(),"Facebook account Logged Out",Toast.LENGTH_SHORT).show();
                    LoginManager.getInstance().logOut();
                    Intent i=new Intent(getApplicationContext(),Login.class);
                    startActivity(i);
                    finish();
                }

            }
               return true;
        }

        return false;
    }

    @Override
    public void onBackPressed()
    {
        if(back_pressed +2000 >System.currentTimeMillis()) {

            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Press back again to Exit App ",Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

}

