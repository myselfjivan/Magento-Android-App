package in.co.mrfoody.mrfoody;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import in.co.mrfoody.mrfoody.Catalog.catalogCategory.catalogCategoryAssignedProducts;
import in.co.mrfoody.mrfoody.Catalog.catalogCategory.catalogCategoryInfo;
import in.co.mrfoody.mrfoody.Catalog.catalogProduct.catalogProductList;
import in.co.mrfoody.mrfoody.Service.mrfoodySer;
import in.co.mrfoody.mrfoody.Catalog.catalogCategory.catalogCategoryLevel;
import in.co.mrfoody.mrfoody.Catalog.catalogProduct.catalogProductAttributeMediaInfo;
import in.co.mrfoody.mrfoody.Service.MrFoodyApplicationConfigurationKeys;

public class MrFoody extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mainViewProductsList;
    private ListView mDrawerList;
    private ListView subCategoryDrawerList;
    private ListView subCategoryAssignedProductImagesDrawerList;

    private ArrayAdapter<String> mainViewProductsAdapter;
    private ArrayAdapter<String> mAdapter;
    private ArrayAdapter<String> subCategoryAdapter;
    private ArrayAdapter<String> subCategoryssignedProductImageAdapter;

    public ArrayList<String> mainViewProductsArrayList = new ArrayList<String>();
    public ArrayList<String> ar = new ArrayList<String>();
    public ArrayList<String> categorySubLevel = new ArrayList<String>();
    public ArrayList<String> subCategoryAssigenedProductImages = new ArrayList<String>();

    public String METHOD = null;

    public List<catalogCategoryLevel> catalogCategoryLevels = new ArrayList<catalogCategoryLevel>();
    public List<catalogProductList> catalogProductLists = new ArrayList<catalogProductList>();
    public List<catalogProductAttributeMediaInfo> catalogProductAttributeMediaInfos = new ArrayList<catalogProductAttributeMediaInfo>();
    public List<catalogProductAttributeMediaInfo> catalogCategoryProductAttributeMediaInfos = new ArrayList<catalogProductAttributeMediaInfo>();
    public List<catalogCategoryLevel> catalogSubCategoryLevels = new ArrayList<catalogCategoryLevel>();
    public List<catalogCategoryAssignedProducts> catalogCategoryAssignedProductsList = new ArrayList<catalogCategoryAssignedProducts>();
    public List<catalogProductList> catalogCategoryProductLists = new ArrayList<catalogProductList>();
    ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SessionIdGenerator().execute();
        headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));

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

                SoapObject request = new SoapObject(MrFoodyApplicationConfigurationKeys.NAMESPACE, METHOD);

                request.addProperty("username", MrFoodyApplicationConfigurationKeys.USERNAME);
                request.addProperty("apiKey", MrFoodyApplicationConfigurationKeys.APIUSERKEY);

                env.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(MrFoodyApplicationConfigurationKeys.URL);

                androidHttpTransport.call("", env,headerPropertyArrayList);
                Object sessionIdObject = env.getResponse();

                Log.d("sessionId", sessionIdObject.toString());

                MrFoodyApplicationConfigurationKeys.sessionId = sessionIdObject.toString();
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
            SoapObject request = new SoapObject(MrFoodyApplicationConfigurationKeys.NAMESPACE, METHOD);

            request.addProperty("username", MrFoodyApplicationConfigurationKeys.USERNAME);
            request.addProperty("apiKey", MrFoodyApplicationConfigurationKeys.APIUSERKEY);
            request.addProperty("sessionId", MrFoodyApplicationConfigurationKeys.sessionId);
            request.addProperty("parentCategory", 6);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(MrFoodyApplicationConfigurationKeys.URL);

            env.setOutputSoapObject(request);
            try {
                androidHttpTransport.call("", env,headerPropertyArrayList);
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
            SoapObject request = new SoapObject(MrFoodyApplicationConfigurationKeys.NAMESPACE, METHOD);

            request.addProperty("username", MrFoodyApplicationConfigurationKeys.USERNAME);
            request.addProperty("apiKey", MrFoodyApplicationConfigurationKeys.APIUSERKEY);
            request.addProperty("sessionId", MrFoodyApplicationConfigurationKeys.sessionId);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(MrFoodyApplicationConfigurationKeys.URL);
            env.setOutputSoapObject(request);

            try {
                androidHttpTransport.call("", env,headerPropertyArrayList);
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
                        mainViewProductsArrayList.add(catalogProductList.getName());
                        try {
                            request = new SoapObject(MrFoodyApplicationConfigurationKeys.NAMESPACE, "catalogProductAttributeMediaList");
                            request.addProperty("sessionId", MrFoodyApplicationConfigurationKeys.sessionId);
                            request.addProperty("product", catalogProductList.getProduct_id());
                            env.setOutputSoapObject(request);
                            androidHttpTransport.call("", env,headerPropertyArrayList);
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
                        //new updateMainViwProductList().execute(j);
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

        @Override
        protected void onPostExecute(String s) {
            updateHomeViewTopProducts();
            for (int j = 0; j < catalogProductAttributeMediaInfos.size(); j++) {
                //catalogProductAttributeMediaInfo catalogProductAttributeMediaInfo = catalogProductAttributeMediaInfos.get(j);
                //System.out.println("Url: " + catalogProductAttributeMediaInfo.getUrl());
                new updateMainViwProductList().execute(j);
            }
            super.onPostExecute(s);
        }
    }
    public class updateMainViwProductList extends AsyncTask<Integer, String, Bitmap> {

        @Override
        protected Bitmap doInBackground(Integer... params) {
            int j = params[0];
            catalogProductAttributeMediaInfo catalogProductAttributeMediaInfo = catalogProductAttributeMediaInfos.get(j);
            System.out.println("Url: " + catalogProductAttributeMediaInfo.getUrl());
            String url = catalogProductAttributeMediaInfo.getUrl();
            // initilize the default HTTP client object
            final DefaultHttpClient client = new DefaultHttpClient();

            //forming a HttoGet request
            final HttpGet getRequest = new HttpGet(url);
            try {

                HttpResponse response = client.execute(getRequest);

                //check 200 OK for success
                final int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode != HttpStatus.SC_OK) {
                    Log.w("ImageDownloader", "Error " + statusCode +
                            " while retrieving bitmap from " + url);
                    return null;

                }

                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    try {
                        // getting contents from the stream
                        inputStream = entity.getContent();

                        // decoding stream data back into image Bitmap that android understands
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        return bitmap;
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            } catch (Exception e) {
                // You Could provide a more explicit error message for IOException
                getRequest.abort();
                Log.e("ImageDownloader", "Something went wrong while" +
                        " retrieving bitmap from " + url + e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            updateHomeViewTopProducts();
            super.onPostExecute(bitmap);
        }
    }

    public void updateHomeViewTopProducts(){
        mainViewProductsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mainViewProductsArrayList);
        mainViewProductsList = (ListView)findViewById(R.id.subCategoryMenu);
        mainViewProductsList.setAdapter(mainViewProductsAdapter);
        mainViewProductsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MrFoody.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
                //int category_id = position;
                //Log.d("category id clicked", "" + category_id);
                //catalogProductAttributeMediaInfo catalogProductAttributeMediaInfo = catalogCategoryProductAttributeMediaInfos.get(position);
                //Log.d("clicked categoryID", catalogCategoryLevel.getCategoryId());
                //new catalogCategoryAssignedProductsAsyncTask().execute(Integer.valueOf(catalogCategoryLevel.getCategoryId()));
            }
        });
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
            SoapObject request = new SoapObject(MrFoodyApplicationConfigurationKeys.NAMESPACE, METHOD);

            request.addProperty("username", MrFoodyApplicationConfigurationKeys.USERNAME);
            request.addProperty("apiKey", MrFoodyApplicationConfigurationKeys.APIUSERKEY);
            request.addProperty("sessionId", MrFoodyApplicationConfigurationKeys.sessionId);
            request.addProperty("parentCategory", id);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(MrFoodyApplicationConfigurationKeys.URL);
            env.setOutputSoapObject(request);

            try {
                androidHttpTransport.call("", env,headerPropertyArrayList);
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
                new catalogCategoryAssignedProductsAsyncTask().execute(Integer.valueOf(catalogCategoryLevel.getCategoryId()));
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
            SoapObject request = new SoapObject(MrFoodyApplicationConfigurationKeys.NAMESPACE, METHOD);

            request.addProperty("username", MrFoodyApplicationConfigurationKeys.USERNAME);
            request.addProperty("apiKey", MrFoodyApplicationConfigurationKeys.APIUSERKEY);
            request.addProperty("sessionId", MrFoodyApplicationConfigurationKeys.sessionId);
            request.addProperty("categoryId", 8);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(MrFoodyApplicationConfigurationKeys.URL);
            env.setOutputSoapObject(request);

            try {
                androidHttpTransport.call("", env,headerPropertyArrayList);
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

    public class catalogCategoryAssignedProductsAsyncTask extends AsyncTask<Integer, String, String> {
        String METHOD = null;

        @Override
        protected String doInBackground(Integer... params) {
            catalogCategoryProductLists.clear();
            catalogCategoryAssignedProductsList.clear();
            catalogCategoryProductAttributeMediaInfos.clear();
            subCategoryAssigenedProductImages.clear();
            int id = params[0];
            Log.d("value at asynctask", "" + id);
            SoapSerializationEnvelope env = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            env.dotNet = false;
            env.xsd = SoapSerializationEnvelope.XSD;
            env.enc = SoapSerializationEnvelope.ENC;

            METHOD = "catalogCategoryAssignedProducts";
            SoapObject request = new SoapObject(MrFoodyApplicationConfigurationKeys.NAMESPACE, METHOD);

            request.addProperty("username", MrFoodyApplicationConfigurationKeys.USERNAME);
            request.addProperty("apiKey", MrFoodyApplicationConfigurationKeys.APIUSERKEY);
            request.addProperty("sessionId", MrFoodyApplicationConfigurationKeys.sessionId);
            request.addProperty("categoryId", id);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(MrFoodyApplicationConfigurationKeys.URL);
            env.setOutputSoapObject(request);
            try {
                androidHttpTransport.call("", env,headerPropertyArrayList);
                Object catalogCategoryAssignedProductsObject = env.getResponse();
                Log.d("catalog Category Info", catalogCategoryAssignedProductsObject.toString());
                SoapObject catalogCategoryAssignedProductsSoapObject = (SoapObject) env.getResponse(); //get response
                for (int i = 0; i < catalogCategoryAssignedProductsSoapObject.getPropertyCount(); i++) {
                    catalogCategoryAssignedProducts data = new catalogCategoryAssignedProducts();
                    SoapObject catalogCategoryAssignedProductsSoapObject2 = (SoapObject) catalogCategoryAssignedProductsSoapObject.getProperty(i);
                    data.setProduct_id((Integer) catalogCategoryAssignedProductsSoapObject2.getProperty("product_id"));
                    //data.setName(catalogCategoryInfoArray.getProperty("parent_id").toString());
                    data.setSku(catalogCategoryAssignedProductsSoapObject2.getProperty("sku").toString());
                    //data.setPosition(catalogCategoryInfoArray.getProperty("position").toString());
                    //data.setLevel(catalogCategoryInfoArray.getProperty("level").toString());
                    //data.setImage(catalogCategoryInfoArray.getProperty("image").toString());
                    catalogCategoryAssignedProductsList.add(data);

                }
                for(int i=0; i < catalogCategoryAssignedProductsList.size(); i++){
                    catalogCategoryAssignedProducts catalogCategoryAssignedProducts = catalogCategoryAssignedProductsList.get(i);
                    //categorySubLevel.add(catalogCategoryInfo.getName());
                    Log.d("Info Get Product Id", " "+catalogCategoryAssignedProducts.getProduct_id());
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
            System.out.println("catalogCategoryAssignedProductsList.size" +catalogCategoryAssignedProductsList.size());
            for(int i=0; i < catalogCategoryAssignedProductsList.size(); i++) {
                catalogCategoryAssignedProducts catalogCategoryAssignedProducts = catalogCategoryAssignedProductsList.get(i);
                Log.d("all the products id", ""+ catalogCategoryAssignedProducts.getProduct_id());
                Log.d("starting asynctask "," "+i);
                new catalogCategoryAssignedProductInfoAsyncTask().execute(catalogCategoryAssignedProducts.getProduct_id());
            }
            super.onPostExecute(s);
        }
    }

    public class catalogCategoryAssignedProductInfoAsyncTask extends AsyncTask<Integer, String, Bitmap> {

        @Override
        protected Bitmap doInBackground(Integer... params) {
            subCategoryAssigenedProductImages.clear();
            int id = params[0];
            Log.d("Product id at asynctask", "" + id);

            SoapSerializationEnvelope env = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            env.dotNet = false;
            env.xsd = SoapSerializationEnvelope.XSD;
            env.enc = SoapSerializationEnvelope.ENC;

            METHOD = "catalogProductInfo";
            SoapObject request = new SoapObject(MrFoodyApplicationConfigurationKeys.NAMESPACE, METHOD);

            request.addProperty("username", MrFoodyApplicationConfigurationKeys.USERNAME);
            request.addProperty("apiKey", MrFoodyApplicationConfigurationKeys.APIUSERKEY);
            request.addProperty("sessionId", MrFoodyApplicationConfigurationKeys.sessionId);
            request.addProperty("productId", id);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(MrFoodyApplicationConfigurationKeys.URL);
            env.setOutputSoapObject(request);

            try {
                androidHttpTransport.call("", env,headerPropertyArrayList);
                Object catalogProductListObject = env.getResponse();
                Log.d("catalog Product List", catalogProductListObject.toString());

                SoapObject catalogProductEntityArray = (SoapObject) env.getResponse(); //get response
                try {
                    catalogProductList data = new catalogProductList();
                    data.setProduct_id(catalogProductEntityArray.getProperty("product_id").toString());
                    data.setSku(catalogProductEntityArray.getProperty("sku").toString());
                    data.setName(catalogProductEntityArray.getProperty("name").toString());
                    data.setSet(catalogProductEntityArray.getProperty("set").toString());
                    data.setType(catalogProductEntityArray.getProperty("type").toString());
                    catalogCategoryProductLists.add(data);
                    System.out.println("catalogCategoryProductLists.size "+catalogCategoryProductLists.size());
/*
                        try {
                            request = new SoapObject(MrFoodyApplicationConfigurationKeys.NAMESPACE, "catalogProductAttributeMediaList");
                            request.addProperty("sessionId", MrFoodyApplicationConfigurationKeys.sessionId);
                            request.addProperty("product", id);
                            env.setOutputSoapObject(request);
                            androidHttpTransport.call("", env);
                            SoapObject catalogProductImageEntityArray = (SoapObject) env.getResponse();
                            Log.d("Image responce",catalogProductImageEntityArray.toString());

                            for (int j = 0; j < catalogProductImageEntityArray.getPropertyCount(); j++) {
                                catalogProductAttributeMediaInfo mediaData = new catalogProductAttributeMediaInfo();
                                SoapObject catalogProductImageEntity = (SoapObject) catalogProductImageEntityArray.getProperty(j);
                                mediaData.setLabel(catalogProductImageEntity.getProperty("label").toString());
                                mediaData.setUrl(catalogProductImageEntity.getProperty("url").toString());
                                catalogCategoryProductAttributeMediaInfos.add(mediaData);
                            }

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
*/
                    for (int j = 0; j < catalogCategoryProductLists.size(); j++) {
                        //subCategoryAssigenedProductImages.clear();
                        //catalogProductAttributeMediaInfo catalogProductAttributeMediaInfo = catalogCategoryProductAttributeMediaInfos.get(j);
                        //System.out.println("Url: " + catalogProductAttributeMediaInfo.getUrl());
                        catalogProductList catalogProductList = catalogCategoryProductLists.get(j);
                        subCategoryAssigenedProductImages.add(catalogProductList.getName());
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

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //subCategoryAssignedProductImagesDrawerList.clear();
            getSubCategoryAssignedProductImages();
            super.onPostExecute(bitmap);
        }
    }

    private void getSubCategoryAssignedProductImages() {
        subCategoryssignedProductImageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subCategoryAssigenedProductImages);
        subCategoryAssignedProductImagesDrawerList = (ListView)findViewById(R.id.subCategoryMenu);
        subCategoryAssignedProductImagesDrawerList.setAdapter(subCategoryssignedProductImageAdapter);
        subCategoryAssignedProductImagesDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MrFoody.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
                //int category_id = position;
                //Log.d("category id clicked", "" + category_id);
                //catalogProductAttributeMediaInfo catalogProductAttributeMediaInfo = catalogCategoryProductAttributeMediaInfos.get(position);
                //Log.d("clicked categoryID", catalogCategoryLevel.getCategoryId());
                //new catalogCategoryAssignedProductsAsyncTask().execute(Integer.valueOf(catalogCategoryLevel.getCategoryId()));
            }
        });
    }

}
