/*
 * Copyright (c) 2016 Jivan Ghadage <jivanghadage@gmail.com>.
 */

package in.co.mrfoody.mrfoody.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.co.mrfoody.mrfoody.Library.Catalog.catalogCategory.catalogCategoryLevel;
import in.co.mrfoody.mrfoody.R;
import in.co.mrfoody.mrfoody.Service.MrFoodyApplicationConfigurationKeys;

/**
 * Created by om on 5/5/16.
 */
public class NavigationDrawerActivity extends AppCompatActivity {
    private ArrayAdapter<String> mAdapter;
    private ListView mDrawerList;
    public ArrayList<String> ar = new ArrayList<String>();
    public String METHOD = null;
    public List<catalogCategoryLevel> catalogCategoryLevels = new ArrayList<catalogCategoryLevel>();
    ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mr_foody);
        headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));

    }

    /*Allows you to retrieve one level of categories by a website, a store view, or a parent category.*/
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
            //mDrawerList = (ListView) findViewById(R.id.navList);
            addDrawerItems();

        }
    }

    private void addDrawerItems() {
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ar);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(NavigationDrawerActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
                int category_id = position;
                System.out.println("Main category id clicked" + category_id);
                catalogCategoryLevel catalogCategoryLevel = catalogCategoryLevels.get(position);
                Log.d("Main clicked categoryID", catalogCategoryLevel.getCategoryId());
                //new catalogSubCategoryLevelAsyncTask().execute(Integer.valueOf(catalogCategoryLevel.getCategoryId()));

            }
        });
    }

}
