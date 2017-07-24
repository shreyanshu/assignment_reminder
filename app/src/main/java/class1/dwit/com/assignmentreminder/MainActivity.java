package class1.dwit.com.assignmentreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import class1.dwit.com.assignmentreminder.Assignment.AssignmentDetail;
import class1.dwit.com.assignmentreminder.adapter.ListAdapter;
import class1.dwit.com.assignmentreminder.database.DatabaseHelper;
import class1.dwit.com.assignmentreminder.domain.Assignment;
import class1.dwit.com.assignmentreminder.utils.AlarmUtils;
import class1.dwit.com.assignmentreminder.utils.HttpHandler;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Context contextOfApplication;

    List<Assignment> assignmentBackupList;

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    private String TAG = MainActivity.class.getSimpleName();
    private static String url = "http://192.168.4.118/json.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contextOfApplication = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SharedPreferences sharedPref = getSharedPreferences("this_batch",Context.MODE_PRIVATE);
        String defaultValue = "2018";
        String batch = sharedPref.getString(getString(R.string.classes), defaultValue);
        final String bat = batch.substring(batch.length()-4);
        ListView listView = (ListView) findViewById(R.id.list_all);
        ListAdapter adapter=new ListAdapter(getApplicationContext(), databaseHelper.getUpcommingAssignment(bat));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Assignment ass = (Assignment)parent.getAdapter().getItem(position);
                Intent inte = new Intent(getApplicationContext() ,AssignmentDetail.class);
//                    inte.putExtra("key",);
                Bundle b = new Bundle();
//                    b.putParcelable();
                inte.putExtra("title",ass.getAssignment_name());
                inte.putExtra("name",ass.getName());
                inte.putExtra("deadline",ass.getDeadline());
                inte.putExtra("batch",ass.getBatch());
                startActivity(inte);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new fetchData().execute();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        assignmentList = new ArrayList<>();
//        new fetchData().execute();
//        List<Assignment> assignmentList = null;
//        assignmentList = databaseHelper.fetchAllAssignment();

    }


    public static Context getContextOfApplication(){
        return contextOfApplication;
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

        final CharSequence[] magnitudescale = {" Class of 2017", " Class of 2018", " Class of 2019", " Class of 2020"};
        AlertDialog.Builder alertMagnitudeScale = new AlertDialog.Builder(MainActivity.this);
        alertMagnitudeScale.setTitle("Remind for assignments of");
        alertMagnitudeScale.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        SharedPreferences sharedPref = getSharedPreferences("this_batch",Context.MODE_PRIVATE);
        String defaultValue = " Class of 2018";
        String batch = sharedPref.getString(getString(R.string.classes), defaultValue);

        int selected = 0;
        if(batch == " Class of 2017")
            selected = 0;

        else if(batch == " Class of 2018")
            selected = 1;

        else if(batch == " Class of 2019")
            selected = 2;

        else
            selected = 3;

        alertMagnitudeScale.setSingleChoiceItems(magnitudescale, selected, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
//                Toast.makeText(MainActivity.this,  "value: "+magnitudescale[item], Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPref = getSharedPreferences("this_batch",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.classes), magnitudescale[item].toString());
                editor.apply();
                dialog.dismiss();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        AlertDialog alert = alertMagnitudeScale.create();
        alert.show();

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

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent i = new Intent(this, ClassOf2017.class);
            startActivity(i);

        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(this, ClassOf2018.class);
            startActivity(i);

        } else if (id == R.id.nav_slideshow) {
            Intent i = new Intent(this, ClassOf2019.class);
            startActivity(i);

        } else if (id == R.id.nav_manage) {
            Intent i = new Intent(this, ClassOf2020.class);
            startActivity(i);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public class fetchData extends AsyncTask<String , Void ,String> {

        ProgressDialog progressDialog;
        List<Assignment> assignmentList = new ArrayList<>();

        fetchData(){
            progressDialog = new ProgressDialog(MainActivity.this);
        }

        @Override
        protected void onPreExecute() {
            /**
             * Execute first before fetching
             * then doInBackground method executed
             */
            progressDialog.setMessage("Fetching Data");
            progressDialog.show();
            progressDialog.setCancelable(true);
            assignmentBackupList = databaseHelper.fetchAllAssignment();
            databaseHelper.delete();
//
        }

        @Override
        protected String doInBackground(String... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpHandler sh = new HttpHandler();
//            adapter.notifyDataSetChanged();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

//            databaseHelper.delete();
            if (jsonStr != null)
            {
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);


                    for (int i = 0; i < jsonArray.length(); i++) {
                        Assignment assign = new Assignment();
                        JSONObject jo = (JSONObject) jsonArray.get(i);

                        int id = jo.getInt("id");
                        String deadline = jo.getString("deadline");
                        String assignment = jo.getString("assignment");
                        String url = jo.getString("url");
                        String batch = jo.getString("class");
                        String name = jo.getString("name");
                        assign.setAssignment_name(assignment);
                        assign.setBatch(batch);
                        assign.setDeadline(deadline);
                        assign.setId(id);
                        assign.setURL(url);
                        assign.setName(name);
//                        databaseHelper.insert_Assignment(assign);
                        assignmentList.add(assign);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

//                    for (Assignment temp : assignmentBackupList) {
//                        databaseHelper.insert_Assignment(temp);
////                    Log.e(TAG, "Added assignment " + temp.getId());
//                    }
                }
            }
            else
            {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

                for (Assignment temp : assignmentBackupList) {
                    databaseHelper.insert_Assignment(temp);
//                    Log.e(TAG, "Added assignment " + temp.getId());
                }



            }
//            System.out.println("AssignmentList Inside DoBAckground is : "+assignmentList);
            if(assignmentList!=null){
                return "Successfully Fetched";
            }else{
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            /**
             * execute after background task performed
             *
             */
            System.out.println("s = " + s);
            progressDialog.dismiss();
            ListView listView = (ListView) findViewById(R.id.list_all);
            if(assignmentList != null)
            {
                for (Assignment temp : assignmentList) {
                    databaseHelper.insert_Assignment(temp);
                    Log.e(TAG, "Added assignment " + temp.getId());
                }
            }
            AlarmUtils.setAlarm(getApplicationContext());
            SharedPreferences sharedPref = getSharedPreferences("this_batch",Context.MODE_PRIVATE);
            String defaultValue = "2018";
            String batch = sharedPref.getString(getString(R.string.classes), defaultValue);
            final String bat = batch.substring(batch.length()-4);
            ListAdapter adapter=new ListAdapter(getApplicationContext(), databaseHelper.getUpcommingAssignment(bat));
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Assignment ass = (Assignment)parent.getAdapter().getItem(position);
                    Intent inte = new Intent(getApplicationContext() ,AssignmentDetail.class);
//                    inte.putExtra("key",);
                    Bundle b = new Bundle();
//                    b.putParcelable();
                    inte.putExtra("title",ass.getAssignment_name());
                    inte.putExtra("name",ass.getName());
                    inte.putExtra("deadline",ass.getDeadline());
                    inte.putExtra("batch",ass.getBatch());
                    startActivity(inte);

                }
            });
            if(s!=null)
                Toast.makeText(MainActivity.this, "JSON Data Fetched from Server", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(MainActivity.this, "OOPS!!! Error ", Toast.LENGTH_SHORT).show();
        }
    }
}

