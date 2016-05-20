/*
 * Copyright (c) 2016 Jivan Ghadage <jivanghadage@gmail.com>.
 */

package in.co.mrfoody.mrfoody.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.co.mrfoody.mrfoody.Catalog.catalogCategory.catalogCategoryAssignedProducts;
import in.co.mrfoody.mrfoody.Catalog.catalogCategory.catalogCategoryInfo;
import in.co.mrfoody.mrfoody.Catalog.catalogCategory.catalogCategoryLevel;
import in.co.mrfoody.mrfoody.Catalog.catalogProduct.catalogProductAttributeMediaInfo;
import in.co.mrfoody.mrfoody.Catalog.catalogProduct.catalogProductList;
import in.co.mrfoody.mrfoody.R;
import in.co.mrfoody.mrfoody.Service.MrFoodyApplicationConfigurationKeys;
import in.co.mrfoody.mrfoody.Service.mrfoodySer;

public class MrFoody extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mainViewProductsList;
    private ListView mDrawerList;
    private ListView subCategoryDrawerList;
    private ListView subCategoryAssignedProductImagesDrawerList;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private TextView product_name;
    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    //private productAdapter pAdapter;

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
    private ProgressDialog pdia;
    private List<String> itemList;
    NavigationDrawerActivity navigationDrawerActivity = new NavigationDrawerActivity();

    private List<Person> persons;
    private RecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SessionIdGenerator().execute();
        headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));

        startService(new Intent(this, mrfoodySer.class));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mr_foody);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //rv = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        //rv.setLayoutManager(llm);
        //rv.setHasFixedSize(true);


        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        //        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        //toggle.syncState();
        new DrawerBuilder().withActivity(this).build();
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName(R.string.drawer_item_home);
        SecondaryDrawerItem item2 = (SecondaryDrawerItem) new SecondaryDrawerItem().withName(R.string.drawer_item_settings);
        //SecondaryDrawerItem item2 = new SecondaryDrawerItem().withName(R.string.drawer_item_settings);
        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        return false;
                    }
                })
                .build();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new fragmentHome(), "HOME");
        adapter.addFragment(new fragmentHotels(), "Hotels");
        adapter.addFragment(new fragmentRestaurant(), "Restaurants");
        adapter.addFragment(new fragmentMess(), "Mess");
        adapter.addFragment(new fragmentCakeShop(), "Cake Shops");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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
                System.out.println("Main category id clicked" + category_id);
                catalogCategoryLevel catalogCategoryLevel = catalogCategoryLevels.get(position);
                Log.d("Main clicked categoryID", catalogCategoryLevel.getCategoryId());
                new catalogSubCategoryLevelAsyncTask().execute(Integer.valueOf(catalogCategoryLevel.getCategoryId()));

            }
        });
    }

    /*getting session id from mrfoody.co.in store */
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

                androidHttpTransport.call("", env, headerPropertyArrayList);
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
            if (MrFoodyApplicationConfigurationKeys.sessionId == null) {
                Toast.makeText(MrFoody.this, "Unable to acquire session Id!", Toast.LENGTH_SHORT).show();
            } else {
                //navigationDrawerActivity.new catalogCategoryLevelAsyncTask().execute();
                //new catalogCategoryLevelAsyncTask().execute();
                //new catalogProductListAsyncTask().execute();
            }
            super.onPostExecute(s);
        }
    }

    /*Allows you to retrieve one level of categories by a website, a store view, or a parent category.*/
/*
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
                androidHttpTransport.call("", env, headerPropertyArrayList);
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
        }
    }
*/
    /*Allows you to retrieve the list of restaurants,hotels and other sub categories. Here retriving the main page product or onclick category product*/
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
                androidHttpTransport.call("", env, headerPropertyArrayList);
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
                for (int i = 0; i < catalogSubCategoryLevels.size(); i++) {
                    catalogCategoryLevel catalogCategoryLevel = catalogSubCategoryLevels.get(i);
                    categorySubLevel.add(catalogCategoryLevel.getName());
                    System.out.println("category_id: " + catalogCategoryLevel.getCategoryId()
                            + "category_name" + catalogCategoryLevel.getName());
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
            //addSubCategoryMenu();
            //new catalogCategoryInfoAsyncTask().execute();
            super.onPostExecute(s);
        }
    }
/*
    private void addSubCategoryMenu() {
        subCategoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categorySubLevel);
        subCategoryDrawerList = (ListView) findViewById(R.id.subCategoryMenu);
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
*/

    /*Allows you to retrieve information about the required category.*/
    public class catalogCategoryInfoAsyncTask extends AsyncTask<String, String, String> {
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
                androidHttpTransport.call("", env, headerPropertyArrayList);
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
                for (int i = 0; i < catalogCategoryInfos.size(); i++) {
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
                androidHttpTransport.call("", env, headerPropertyArrayList);
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
                for (int i = 0; i < catalogCategoryAssignedProductsList.size(); i++) {
                    catalogCategoryAssignedProducts catalogCategoryAssignedProducts = catalogCategoryAssignedProductsList.get(i);
                    //categorySubLevel.add(catalogCategoryInfo.getName());
                    Log.d("Info Get Product Id", " " + catalogCategoryAssignedProducts.getProduct_id());
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
            System.out.println("catalogCategoryAssignedProductsList.size" + catalogCategoryAssignedProductsList.size());
            for (int i = 0; i < catalogCategoryAssignedProductsList.size(); i++) {
                catalogCategoryAssignedProducts catalogCategoryAssignedProducts = catalogCategoryAssignedProductsList.get(i);
                Log.d("all the products id", "" + catalogCategoryAssignedProducts.getProduct_id());
                Log.d("starting asynctask ", " " + i);
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
                androidHttpTransport.call("", env, headerPropertyArrayList);
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
                    System.out.println("catalogCategoryProductLists.size " + catalogCategoryProductLists.size());
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
            //getSubCategoryAssignedProductImages();
            super.onPostExecute(bitmap);
        }
    }
/*
    private void getSubCategoryAssignedProductImages() {

        subCategoryssignedProductImageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subCategoryAssigenedProductImages);
        subCategoryAssignedProductImagesDrawerList = (ListView) findViewById(R.id.subCategoryMenu);
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
/*
    public class productAdapter extends RecyclerView.Adapter<productAdapter.MyViewHolder> {
        private List<Movie> productList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, year, genre;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
            }
        }


        public productAdapter(List<Movie> productList) {
            this.productList = productList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_list_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            catalogCategoryLevel movie = catalogSubCategoryLevels.get(position);
            holder.title.setText(movie.getName());
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

    }

    private void addProductList() {
        product_name = (TextView) findViewById(R.id.title);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        pAdapter = new productAdapter(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pAdapter);

    }
*/

}
