/*
 * Copyright (c) 2016 Jivan Ghadage <jivanghadage@gmail.com>.
 */

package in.co.mrfoody.mrfoody.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import in.co.mrfoody.mrfoody.R;
import in.co.mrfoody.mrfoody.Service.MrFoodyApplicationConfigurationKeys;
import it.gmariotti.cardslib.library.cards.material.MaterialLargeImageCard;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;

/**
 * Created by om on 19/3/16.
 */

public class fragmentHotels extends Fragment {

    public String METHOD = null;
    ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
    public List<catalogCategoryLevel> catalogSubCategoryLevels = new ArrayList<catalogCategoryLevel>();
    public ArrayList<String> categorySubLevel = new ArrayList<String>();
    public List<catalogCategoryLevel> catalogCategoryLevels = new ArrayList<catalogCategoryLevel>();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public fragmentHotels() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("on Create", "fragment Hotel");
        new catalogSubCategoryLevelAsyncTask().execute(8);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hotel, container, false);
    }


    public class catalogSubCategoryLevelAsyncTask extends AsyncTask<Integer, String, String> {

        @Override
        protected String doInBackground(Integer... params) {
            //categorySubLevel.clear();
            //catalogSubCategoryLevels.clear();
            int id = 3;
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
            updateView();
            super.onPostExecute(s);
        }
    }

    private void updateView() {
        ArrayList<Card> cards = new ArrayList<Card>();

        for (int i = 0; i < catalogCategoryLevels.size(); i++) {
            catalogCategoryLevel catalogCategoryLevel = catalogSubCategoryLevels.get(i);
            // Create a Card
            //Card card = new Card(getContext());
            // Create a CardHeader
            //CardHeader header = new CardHeader(getContext());
            // Add Header to card
            //header.setTitle("Angry bird: " + i);
            //card.setTitle("sample title");
            //card.addCardHeader(header);
            //Create a Card, set the title over the image and set the thumbnail
            MaterialLargeImageCard card =
                    MaterialLargeImageCard.with(getActivity())
                            .setTextOverImage(catalogCategoryLevel.getName())
                            .setTitle(catalogCategoryLevel.getName())
                                    //.setSubTitle("A wonderful place")
                            .useDrawableExternal(new MaterialLargeImageCard.DrawableExternal() {
                                @Override
                                public void setupInnerViewElements(ViewGroup parent, View viewImage) {

                                    Picasso.with(getActivity()).setIndicatorsEnabled(true);  //only for debug tests
                                    Picasso.with(getActivity())
                                            .load("http://dev.mrfoody.co.in/media/catalog/category/hotel1.jpg")
                                            .into((ImageView) viewImage);
                                }
                            })
                            .build();
            card.setType(3);
            //CardThumbnail thumb = new CardThumbnail(getContext());
            //thumb.setDrawableResource(listImages[i]);
            //card.addCardThumbnail(thumb);

            cards.add(card);
        }

        CardArrayRecyclerViewAdapter mCardArrayAdapter = new CardArrayRecyclerViewAdapter(getActivity(), cards);

        //Staggered grid view
        CardRecyclerView mRecyclerView = (CardRecyclerView) getActivity().findViewById(R.id.carddemo_recyclerview);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Set the empty view
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(mCardArrayAdapter);
        }

    }
}
