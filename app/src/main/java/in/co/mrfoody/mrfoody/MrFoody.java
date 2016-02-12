package in.co.mrfoody.mrfoody;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.co.mrfoody.mrfoody.Catalog.catalogCategory.catalogCategoryInfo;
import in.co.mrfoody.mrfoody.Catalog.catalogProduct.catalogProductInfo;
import in.co.mrfoody.mrfoody.Catalog.catalogProduct.catalogProductList;
import in.co.mrfoody.mrfoody.Service.mrfoodySer;
import in.co.mrfoody.mrfoody.Catalog.catalogCategory.catalogCategoryLevel;
import in.co.mrfoody.mrfoody.Catalog.catalogProduct.catalogProductAttributeMediaInfo;

public class MrFoody extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String NAMESPACE = "urn:Magento";
    private static final String URL = "http://dev.mrfoody.co.in/api/v2_soap/";
    private ListView mDrawerList;
    private ListView subCategoryDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ArrayAdapter<String> subCategoryAdapter;
    public ArrayList<String> ar = new ArrayList<String>();
    public ArrayList<String> categorySubLevel = new ArrayList<String>();
    public String METHOD = null;
    public String USERNAME = "anotherTestingUser";
    public String APIUSERKEY = "1f46c6a95d4949c979e929acccc254b4";
    public static String sessionId;
    ///public String osArray[] = null;


    public List<catalogCategoryLevel> catalogCategoryLevels = new ArrayList<catalogCategoryLevel>();
    public List<catalogProductList> catalogProductLists = new ArrayList<catalogProductList>();
    public List<catalogProductAttributeMediaInfo> catalogProductAttributeMediaInfos = new ArrayList<catalogProductAttributeMediaInfo>();
    public List<catalogCategoryLevel> catalogSubCategoryLevels = new ArrayList<catalogCategoryLevel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SessionIdGenerator().execute();

        startService(new Intent(this, mrfoodySer.class));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mr_foody);
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
        drawer.setDrawerListener(toggle);
        toggle.syncState();

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

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MrFoody.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
                int category_id = position;
                System.out.println("Main category id clicked"  + category_id);
                catalogCategoryLevel catalogCategoryLevel = catalogCategoryLevels.get(position);
                Log.d("Main clicked categoryID", catalogCategoryLevel.getCategoryId());
                new catalogSubCategoryLevelAsyncTask().execute(Integer.valueOf(catalogCategoryLevel.getCategoryId()));

            }
        });
    }

    public class SessionIdGenerator extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                SoapSerializationEnvelope env = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);

                env.dotNet = false;
                env.xsd = SoapSerializationEnvelope.XSD;
                env.enc = SoapSerializationEnvelope.ENC;
                METHOD = "login";

                SoapObject request = new SoapObject(NAMESPACE, METHOD);

                request.addProperty("username", USERNAME);
                request.addProperty("apiKey", APIUSERKEY);

                env.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                androidHttpTransport.call("", env);
                Object sessionIdObject = env.getResponse();

                Log.d("sessionId", sessionIdObject.toString());

                sessionId = sessionIdObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            new catalogCategoryLevelAsyncTask().execute();
            new catalogProductListAsyncTask().execute();
            super.onPostExecute(s);
        }
    }

    public class catalogCategoryLevelAsyncTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... params) {
            SoapSerializationEnvelope env = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            env.dotNet = false;
            env.xsd = SoapSerializationEnvelope.XSD;
            env.enc = SoapSerializationEnvelope.ENC;

            METHOD = "catalogCategoryLevel";
            SoapObject request = new SoapObject(NAMESPACE, METHOD);

            request.addProperty("username", USERNAME);
            request.addProperty("apiKey", APIUSERKEY);
            request.addProperty("sessionId", sessionId);
            request.addProperty("parentCategory", 6);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            env.setOutputSoapObject(request);
            try {
                androidHttpTransport.call("", env);
                Object catalogCategoryLevelObject = null;
                try {
                    catalogCategoryLevelObject = env.getResponse();
                    Log.d("catalog Category Level", catalogCategoryLevelObject.toString());

                    SoapObject ArrayOfCatalogCategoryEntitiesNoChildren = null; //get response
                    try {
                        ArrayOfCatalogCategoryEntitiesNoChildren = (SoapObject) env.getResponse();

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
                    } catch (SoapFault soapFault) {
                        soapFault.printStackTrace();
                    }

                } catch (SoapFault soapFault) {
                    soapFault.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mDrawerList = (ListView) findViewById(R.id.navList);
            addDrawerItems();
        }
    }

    public class catalogProductListAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            SoapSerializationEnvelope env = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            env.dotNet = false;
            env.xsd = SoapSerializationEnvelope.XSD;
            env.enc = SoapSerializationEnvelope.ENC;

            METHOD = "catalogProductList";
            SoapObject request = new SoapObject(NAMESPACE, METHOD);

            request.addProperty("username", USERNAME);
            request.addProperty("apiKey", APIUSERKEY);
            request.addProperty("sessionId", sessionId);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            env.setOutputSoapObject(request);

            try {
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
                            SoapObject catalogProductImageEntityArray = (SoapObject) env.getResponse();

                            for (int j = 0; j < catalogProductImageEntityArray.getPropertyCount(); j++) {
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
                        System.out.println("Url: " + catalogProductAttributeMediaInfo.getUrl());
                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class catalogSubCategoryLevelAsyncTask extends AsyncTask<Integer, String, String> {

        @Override
        protected String doInBackground(Integer... params) {
            categorySubLevel.clear();
            catalogSubCategoryLevels.clear();
            int id = params[0];
            Log.d("value at asynctask", "" + id);
            SoapSerializationEnvelope env = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            env.dotNet = false;
            env.xsd = SoapSerializationEnvelope.XSD;
            env.enc = SoapSerializationEnvelope.ENC;

            METHOD = "catalogCategoryLevel";
            SoapObject request = new SoapObject(NAMESPACE, METHOD);

            request.addProperty("username", USERNAME);
            request.addProperty("apiKey", APIUSERKEY);
            request.addProperty("sessionId", sessionId);
            request.addProperty("parentCategory", id);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            env.setOutputSoapObject(request);

            try {
                androidHttpTransport.call("", env);
                Object catalogCategoryLevelObject = env.getResponse();
                Log.d("catalog Category Level", catalogCategoryLevelObject.toString());
                SoapObject catalogSubCategoryLevelArray = (SoapObject) env.getResponse(); //get response
                for (int i = 0; i < catalogSubCategoryLevelArray.getPropertyCount(); i++) {
                    catalogCategoryLevel data = new catalogCategoryLevel();
                    SoapObject catalogSubCategoryArray = (SoapObject) catalogSubCategoryLevelArray.getProperty(i);
                    data.setCategoryId(catalogSubCategoryArray.getProperty("category_id").toString());
                    data.setName(catalogSubCategoryArray.getProperty("parent_id").toString());
                    data.setName(catalogSubCategoryArray.getProperty("name").toString());
                    data.setIsActive(catalogSubCategoryArray.getProperty("is_active").toString());
                    data.setPosition(catalogSubCategoryArray.getProperty("position").toString());
                    data.setLevel(catalogSubCategoryArray.getProperty("level").toString());
                    catalogSubCategoryLevels.add(data);
                }
                for(int i = 0; i<catalogSubCategoryLevels.size(); i++ ){
                    catalogCategoryLevel catalogCategoryLevel = catalogSubCategoryLevels.get(i);
                    categorySubLevel.add(catalogCategoryLevel.getName());
                    System.out.println("category_id: " + catalogCategoryLevel.getCategoryId()
                            +"category_name" + catalogCategoryLevel.getName());
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            addSubCategoryMenu();
            //new catalogCategoryInfoAsyncTask().execute();
            super.onPostExecute(s);
        }
    }

    private void addSubCategoryMenu() {
        subCategoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categorySubLevel);
        subCategoryDrawerList = (ListView)findViewById(R.id.subCategoryMenu);
        subCategoryDrawerList.setAdapter(subCategoryAdapter);
        subCategoryDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MrFoody.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
                int category_id = position;
                Log.d("category id clicked", "" + category_id);
                catalogCategoryLevel catalogCategoryLevel = catalogSubCategoryLevels.get(position);
                Log.d("clicked categoryID", catalogCategoryLevel.getCategoryId());
                //new catalogSubCategoryLevelAsyncTask().execute(Integer.valueOf(catalogCategoryLevel.getCategoryId()));

            }
        });
    }


    public class catalogCategoryInfoAsyncTask extends AsyncTask<String, String, String>{
        List<catalogCategoryInfo> catalogCategoryInfos = new ArrayList<catalogCategoryInfo>();

        @Override
        protected String doInBackground(String... params) {
            SoapSerializationEnvelope env = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            env.dotNet = false;
            env.xsd = SoapSerializationEnvelope.XSD;
            env.enc = SoapSerializationEnvelope.ENC;

            METHOD = "catalogCategoryInfo";
            SoapObject request = new SoapObject(NAMESPACE, METHOD);

            request.addProperty("username", USERNAME);
            request.addProperty("apiKey", APIUSERKEY);
            request.addProperty("sessionId", sessionId);
            request.addProperty("categoryId", 8);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            env.setOutputSoapObject(request);

            try {
                androidHttpTransport.call("", env);
                Object catalogCategoryInfoObject = env.getResponse();
                Log.d("catalog Category Info", catalogCategoryInfoObject.toString());
                SoapObject catalogCategoryInfoArray = (SoapObject) env.getResponse(); //get response
                for (int i = 0; i < catalogCategoryInfoArray.getPropertyCount(); i++) {
                    catalogCategoryInfo data = new catalogCategoryInfo();
                    //SoapObject catalogSubCategoryArray = (SoapObject) catalogCategoryInfoArray.getProperty(i);
                    data.setCategory_id(catalogCategoryInfoArray.getProperty("category_id").toString());
                    //data.setName(catalogCategoryInfoArray.getProperty("parent_id").toString());
                    data.setName(catalogCategoryInfoArray.getProperty("name").toString());
                    //data.setPosition(catalogCategoryInfoArray.getProperty("position").toString());
                    //data.setLevel(catalogCategoryInfoArray.getProperty("level").toString());
                    //data.setImage(catalogCategoryInfoArray.getProperty("image").toString());
                    catalogCategoryInfos.add(data);

                }
                for(int i=0; i < catalogCategoryInfos.size(); i++){
                    catalogCategoryInfo catalogCategoryInfo = catalogCategoryInfos.get(i);
                    //categorySubLevel.add(catalogCategoryInfo.getName());
                    Log.d("Infos.getName", catalogCategoryInfo.getName().toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
