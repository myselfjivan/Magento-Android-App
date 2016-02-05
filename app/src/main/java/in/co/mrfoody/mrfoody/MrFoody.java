package in.co.mrfoody.mrfoody;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import in.co.mrfoody.mrfoody.Ksoap2Parser.ResponceParser;
import in.co.mrfoody.mrfoody.Service.mrfoodySer;
import in.co.mrfoody.mrfoody.Catalog.catalogCategoryLevel;

public class MrFoody extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String NAMESPACE = "urn:Magento";
    private static final String URL = "http://dev.mrfoody.co.in/api/v2_soap/";
    catalogCategoryLevel catalogCategoryLevel = new catalogCategoryLevel();
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    public ArrayList<String> ar = new ArrayList<String>();
    ///public String osArray[] = null;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    public List<catalogCategoryLevel> list = new ArrayList<catalogCategoryLevel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SessionIdGenerator().execute();

        startService(new Intent(this, mrfoodySer.class));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mr_foody);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActivityTitle = getTitle().toString();
        //setupDrawer();

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
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.mr_foody, menu);
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

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addDrawerItems() {
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ar);
        mDrawerList.setAdapter(mAdapter);
    }

/*
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /// Called when a drawer has settled in a completely open state.
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

           // /** Called when a drawer has settled in a completely closed state.
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);

    }
*/
    public class SessionIdGenerator extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            try {

                SoapSerializationEnvelope env = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);

                env.dotNet = false;
                env.xsd = SoapSerializationEnvelope.XSD;
                env.enc = SoapSerializationEnvelope.ENC;

                SoapObject request = new SoapObject(NAMESPACE, "login");

                request.addProperty("username","testingUser");
                request.addProperty("apiKey","1f46c6a95d4949c979e929acccc254b4");

                env.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                androidHttpTransport.call("", env);
                Object result = env.getResponse();

                Log.d("sessionId", result.toString());

                String sessionId = result.toString();
                request = new SoapObject(NAMESPACE, "catalogCategoryLevel");
                request.addProperty("sessionId",sessionId );
                request.addProperty("parentCategory",6);
                //request.addProperty("storeView",);
                //request.addProperty("attributes",);

                env.setOutputSoapObject(request);
                androidHttpTransport.call("", env);

                result = env.getResponse();

                Log.d("catalog Category Level", result.toString());

                SoapObject resultRes = (SoapObject)env.getResponse(); //get response

                try {
                    for (int i = 0; i < resultRes.getPropertyCount(); i++) {
                        in.co.mrfoody.mrfoody.Catalog.catalogCategoryLevel data = new catalogCategoryLevel();
                        SoapObject root = (SoapObject) resultRes.getProperty(i);
                        data.setCategoryId(root.getProperty("category_id").toString());
                        data.setName(root.getProperty("name").toString());
                        list.add(data);
                    }

                    for (int i = 0; i < list.size(); i++) {
                        catalogCategoryLevel catalogCategoryLevel = list.get(i);
                        ar.add(catalogCategoryLevel.getName());
                        System.out.println("Category Id : "  + catalogCategoryLevel.getCategoryId() + " Name :"  + catalogCategoryLevel.getName());
                    }

                } catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
               // } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                //} catch (IllegalAccessException e) {
                //    // TODO Auto-generated catch block
                //    e.printStackTrace();
                //} catch (InstantiationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            mDrawerList = (ListView)findViewById(R.id.navList);
            addDrawerItems();
            //setupDrawer();
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setHomeButtonEnabled(true);

            super.onPostExecute(s);
        }
    }
}
