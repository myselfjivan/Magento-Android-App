package in.co.mrfoody.mrfoody.ui;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.co.mrfoody.mrfoody.Library.Catalog.catalogCategory.catalogCategoryLevel;
import in.co.mrfoody.mrfoody.Library.Catalog.catalogProduct.catalogProductAttributeMediaInfo;
import in.co.mrfoody.mrfoody.Library.Catalog.catalogProduct.catalogProductList;
import in.co.mrfoody.mrfoody.R;
import in.co.mrfoody.mrfoody.Service.MrFoodyApplicationConfigurationKeys;
import it.gmariotti.cardslib.library.cards.actions.BaseSupplementalAction;
import it.gmariotti.cardslib.library.cards.actions.TextSupplementalAction;
import it.gmariotti.cardslib.library.cards.material.MaterialLargeImageCard;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by om on 20/3/16.
 */
public class fragmentHome extends Fragment {
    public String METHOD = null;
    ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
    public List<catalogCategoryLevel> catalogCategoryLevels = new ArrayList<catalogCategoryLevel>();
    public ArrayList<String> ar = new ArrayList<String>();
    private ProgressDialog pdia;
    public List<catalogProductList> catalogProductLists = new ArrayList<catalogProductList>();
    public ArrayList<String> mainViewProductsArrayList = new ArrayList<String>();
    public List<catalogProductAttributeMediaInfo> catalogProductAttributeMediaInfos = new ArrayList<catalogProductAttributeMediaInfo>();

    public fragmentHome() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new catalogProductListAsyncTask().execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_home, container, false);
    }

        /*Allows you to retrieve one level of categories by a website, a store view, or a parent category.*/

    /*Allows you to retrieve the list of products.*/
    public class catalogProductListAsyncTask extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(getContext());
            pdia.setMessage("Loading...");
            pdia.show();
        }

        @Override
        protected String doInBackground(String... params) {
            SoapSerializationEnvelope env = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            //persons = new ArrayList<>();

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
                androidHttpTransport.call("", env, headerPropertyArrayList);
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
                                " Name :" + catalogProductList.getName() + "KSU " + catalogProductList.getSku());
                        //persons.add(new Person(catalogProductList.getName(), catalogProductList.getName(), R.drawable.emma));
                        mainViewProductsArrayList.add(catalogProductList.getName());
                        try {
                            request = new SoapObject(MrFoodyApplicationConfigurationKeys.NAMESPACE, "catalogProductAttributeMediaList");
                            request.addProperty("sessionId", MrFoodyApplicationConfigurationKeys.sessionId);
                            request.addProperty("product", catalogProductList.getProduct_id());
                            env.setOutputSoapObject(request);
                            androidHttpTransport.call("", env, headerPropertyArrayList);
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
            //updateHomeViewTopProducts();
            pdia.dismiss();
            updateView();
            //catalogProductAttributeMediaInfo catalogProductAttributeMediaInfo = catalogProductAttributeMediaInfos.get(j);
            //System.out.println("Url: " + catalogProductAttributeMediaInfo.getUrl());
            //new updateMainViwProductList().execute(j);
            super.onPostExecute(s);
        }
    }

    private void updateView() {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < catalogProductAttributeMediaInfos.size(); i++) {

            // Set supplemental actions as text

            final catalogProductAttributeMediaInfo catalogProductAttributeMediaInfo = catalogProductAttributeMediaInfos.get(i);
            final catalogProductList catalogProductList = catalogProductLists.get(i);

            ArrayList<BaseSupplementalAction> actions = new ArrayList<BaseSupplementalAction>();

            // Set supplemental actions
            TextSupplementalAction t1 = new TextSupplementalAction(getActivity(), R.id.ic1);
            t1.setOnActionClickListener(new BaseSupplementalAction.OnActionClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    Toast.makeText(getActivity(), " Click on Text SHARE ", Toast.LENGTH_SHORT).show();
                }
            });
            actions.add(t1);

            TextSupplementalAction t2 = new TextSupplementalAction(getActivity(), R.id.ic3);
            t2.setOnActionClickListener(new BaseSupplementalAction.OnActionClickListener() {
                @Override
                public void onClick(Card card, View view) {

                    //Toast.makeText(getActivity(), " Product id is " + catalogProductList.getProduct_id(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), " Product SKU " + card.getId(), Toast.LENGTH_SHORT).show();
                }
            });
            actions.add(t2);
            //Create a Card, set the title over the image and set the thumbnail
            MaterialLargeImageCard card =
                    MaterialLargeImageCard.with(getActivity())
                            .setTextOverImage(catalogProductList.getName())
                            .setTitle(catalogProductList.getName())
                            .setupSupplementalActions(R.layout.carddemo_native_material_supplemental_actions_large_icon, actions)
                                    //.setSubTitle("A wonderful place")
                            .useDrawableExternal(new MaterialLargeImageCard.DrawableExternal() {
                                @Override
                                public void setupInnerViewElements(ViewGroup parent, View viewImage) {

                                    Picasso.with(getActivity()).setIndicatorsEnabled(true);  //only for debug tests
                                    Picasso.with(getActivity())
                                            .load(catalogProductAttributeMediaInfo.getUrl())
                                            .into((ImageView) viewImage);
                                }
                            })
                            .build();

            cards.add(card);
        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

        CardListView listView = (CardListView) getActivity().findViewById(R.id.home_list);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }

    }

    public class shoppingCartProductAddAsyncTask extends AsyncTask<Integer, String, String> {

        @Override
        protected String doInBackground(Integer... params) {
            SoapSerializationEnvelope env = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            env.dotNet = false;
            env.xsd = SoapSerializationEnvelope.XSD;
            env.enc = SoapSerializationEnvelope.ENC;

            METHOD = "shoppingCartProductAdd";
            SoapObject request = new SoapObject(MrFoodyApplicationConfigurationKeys.NAMESPACE, METHOD);

            request.addProperty("username", MrFoodyApplicationConfigurationKeys.USERNAME);
            request.addProperty("apiKey", MrFoodyApplicationConfigurationKeys.APIUSERKEY);
            request.addProperty("sessionId", MrFoodyApplicationConfigurationKeys.sessionId);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(MrFoodyApplicationConfigurationKeys.URL);
            env.setOutputSoapObject(request);
            try {
                androidHttpTransport.call("", env, headerPropertyArrayList);
                Object shoppingCartProductUpdateObject = env.getResponse();
                Log.d("CartProduct Update", shoppingCartProductUpdateObject.toString());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


    public class shoppingCartProductUpdateAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            SoapSerializationEnvelope env = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            env.dotNet = false;
            env.xsd = SoapSerializationEnvelope.XSD;
            env.enc = SoapSerializationEnvelope.ENC;

            METHOD = "shoppingCartProductUpdate";
            SoapObject request = new SoapObject(MrFoodyApplicationConfigurationKeys.NAMESPACE, METHOD);

            request.addProperty("username", MrFoodyApplicationConfigurationKeys.USERNAME);
            request.addProperty("apiKey", MrFoodyApplicationConfigurationKeys.APIUSERKEY);
            request.addProperty("sessionId", MrFoodyApplicationConfigurationKeys.sessionId);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(MrFoodyApplicationConfigurationKeys.URL);
            env.setOutputSoapObject(request);
            try {
                androidHttpTransport.call("", env, headerPropertyArrayList);
                Object shoppingCartProductUpdateObject = env.getResponse();
                Log.d("CartProduct Update", shoppingCartProductUpdateObject.toString());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

}
