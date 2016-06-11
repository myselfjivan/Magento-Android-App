/*
 * Copyright (c) 2016 Jivan Ghadage <jivanghadage@gmail.com>.
 */

package in.co.mrfoody.mrfoody.Library.Catalog.catalogCategory;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import in.co.mrfoody.mrfoody.Service.MrFoodyApplicationConfigurationKeys;

/**
 * Created by om on 12/2/16.
 */
public class catalogCategoryAssignedProducts {

    int product_id;
    String type;
    int set;
    String sku;
    int position;

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


}