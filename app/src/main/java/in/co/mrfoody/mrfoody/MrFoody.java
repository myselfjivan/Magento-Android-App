package in.co.mrfoody.mrfoody;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ListView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import in.co.mrfoody.mrfoody.Catalog.catalogProduct.catalogProductList;
import in.co.mrfoody.mrfoody.Service.mrfoodySer;
import in.co.mrfoody.mrfoody.Catalog.catalogCategory.catalogCategoryLevel;
import in.co.mrfoody.mrfoody.Catalog.catalogProduct.catalogProductAttributeMediaInfo;

public class MrFoody extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String NAMESPACE = "urn:Magento";
    private static final String URL = "http://dev.mrfoody.co.in/api/v2_soap/";
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    public ArrayList<String> ar = new ArrayList<String>();
    ///public String osArray[] = null;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    public List<catalogCategoryLevel> catalogCategoryLevels = new ArrayList<catalogCategoryLevel>();
    public List<catalogProductList> catalogProductLists = new ArrayList<catalogProductList>();
    public List<catalogProductAttributeMediaInfo> catalogProductAttributeMediaInfos = new ArrayList<catalogProductAttributeMediaInfo>();

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
    public class SessionIdGenerator extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {

                SoapSerializationEnvelope env = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);

                env.dotNet = false;
                env.xsd = SoapSerializationEnvelope.XSD;
                env.enc = SoapSerializationEnvelope.ENC;

                SoapObject request = new SoapObject(NAMESPACE, "login");

                request.addProperty("username", "anotherTestingUser");
                request.addProperty("apiKey", "1f46c6a95d4949c979e929acccc254b4");

                env.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                androidHttpTransport.call("", env);
                Object sessionIdObject = env.getResponse();

                Log.d("sessionId", sessionIdObject.toString());

                String sessionId = sessionIdObject.toString();
                request = new SoapObject(NAMESPACE, "catalogCategoryLevel");
                request.addProperty("sessionId", sessionId);
                request.addProperty("parentCategory", 6);
                //request.addProperty("storeView",);
                //request.addProperty("attributes",);

                env.setOutputSoapObject(request);
                androidHttpTransport.call("", env);
                Object catalogCategoryLevelObject = env.getResponse();

                Log.d("catalog Category Level", catalogCategoryLevelObject.toString());

                SoapObject ArrayOfCatalogCategoryEntitiesNoChildren = (SoapObject) env.getResponse(); //get response

                try {
                    for (int i = 0; i < ArrayOfCatalogCategoryEntitiesNoChildren.getPropertyCount(); i++) {
                        catalogCategoryLevel data = new catalogCategoryLevel();
                        SoapObject catalogCategoryEntityNoChildren = (SoapObject) ArrayOfCatalogCategoryEntitiesNoChildren.getProperty(i);
                        data.setCategoryId(catalogCategoryEntityNoChildren.getProperty("category_id").toString());
                        data.setParentId(catalogCategoryEntityNoChildren.getProperty("parent_id").toString());
                        data.setName(catalogCategoryEntityNoChildren.getProperty("name").toString());
                        data.setIsActive(catalogCategoryEntityNoChildren.getProperty("is_active").toString());
                        data.setPosition(catalogCategoryEntityNoChildren.getProperty("position").toString());
                        data.setLevel(catalogCategoryEntityNoChildren.getProperty("level").toString());
                        catalogCategoryLevels.add(data);
                    }

                    for (int i = 0; i < catalogCategoryLevels.size(); i++) {
                        catalogCategoryLevel catalogCategoryLevel = catalogCategoryLevels.get(i);
                        ar.add(catalogCategoryLevel.getName());
                        System.out.println("Category Id : " + catalogCategoryLevel.getCategoryId() +
                                " Name :" + catalogCategoryLevel.getName());
                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                request = new SoapObject(NAMESPACE, "catalogProductList");
                request.addProperty("sessionId", sessionId);
                //request.addProperty("parentCategory", 6);
                //request.addProperty("storeView",);
                //request.addProperty("attributes",);

                env.setOutputSoapObject(request);
                androidHttpTransport.call("", env);
                Object catalogProductListObject = env.getResponse();

                Log.d("catalog Product List", catalogProductListObject.toString());

                SoapObject catalogProductEntityArray = (SoapObject) env.getResponse(); //get response

                try {
                    for (int i = 0; i < catalogProductEntityArray.getPropertyCount(); i++) {
                        catalogProductList data = new catalogProductList();
                        SoapObject catalogProductEntity = (SoapObject) catalogProductEntityArray.getProperty(i);
                        data.setProduct_id(catalogProductEntity.getProperty("product_id").toString());
                        data.setSku(catalogProductEntity.getProperty("sku").toString());
                        data.setName(catalogProductEntity.getProperty("name").toString());
                        data.setSet(catalogProductEntity.getProperty("set").toString());
                        data.setType(catalogProductEntity.getProperty("type").toString());
                        //SoapObject CategoryIdsArrayOfString = (SoapObject) catalogProductEntity.getProperty(i);
                        //data.setCategoryId(CategoryIdsArrayOfString.getProperty("item").toString());
                        //data.setCategoryId(CategoryIdsArrayOfString.getProperty().toString());
                        catalogProductLists.add(data);
                    }

                    for (int i = 0; i < catalogProductLists.size(); i++) {
                        catalogProductList catalogProductList = catalogProductLists.get(i);
                        //ar.add(catalogProductList.getProduct_id());
                        System.out.println("product Id : " + catalogProductList.getProduct_id() +
                                " Name :" + catalogProductList.getName());
                        try {
                            request = new SoapObject(NAMESPACE, "catalogProductAttributeMediaList");
                            request.addProperty("sessionId", sessionId);
                            request.addProperty("product", catalogProductList.getProduct_id());
                            env.setOutputSoapObject(request);
                            androidHttpTransport.call("", env);
                            Object catalogProductAttributeMediaListObject = env.getResponse();
                            //Log.d(" Media List", catalogProductAttributeMediaListObject.toString());
                            SoapObject catalogProductImageEntityArray = (SoapObject) env.getResponse();

                            for(int j = 0; j< catalogProductImageEntityArray.getPropertyCount(); j++){
                                catalogProductAttributeMediaInfo data = new catalogProductAttributeMediaInfo();
                                SoapObject catalogProductImageEntity = (SoapObject) catalogProductImageEntityArray.getProperty(j);
                                data.setLabel(catalogProductImageEntity.getProperty("label").toString());
                                data.setUrl(catalogProductImageEntity.getProperty("url").toString());
                                catalogProductAttributeMediaInfos.add(data);

                            }


                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    for (int j = 0; j < catalogProductAttributeMediaInfos.size(); j++) {
                        catalogProductAttributeMediaInfo catalogProductAttributeMediaInfo = catalogProductAttributeMediaInfos.get(j);
                        System.out.println("Url: "+ catalogProductAttributeMediaInfo.getUrl());
                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            mDrawerList = (ListView) findViewById(R.id.navList);
            addDrawerItems();
            //setupDrawer();
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setHomeButtonEnabled(true);

            super.onPostExecute(s);
        }
    }
}
