/*
 * Copyright (c) 2016 Jivan Ghadage <jivanghadage@gmail.com>.
 */

package in.co.mrfoody.mrfoody.Library.Catalog.catalogProduct;

/**
 * Created by om on 6/2/16.
 */
public class catalogProductInfo {
    String product_id;
    String sku;
    String set;
    String type;
    //ArrayOfString	categories
    //ArrayOfString	websites
    String created_at;
    String updated_at;
    String type_id;
    String name;
    String description;
    String short_description;
    String weight;
    String status;
    String url_key;
    String url_path;
    String visibility;
    //ArrayOfString category_ids
    //ArrayOfString website_ids
    String has_options;
    String gift_message_available;
    String price;
    String special_price;
    String special_from_date;
    String special_to_date;
    String tax_class_id;
    //array	tier_price
    String meta_title;
    String meta_keyword;
    String meta_description;
    String custom_design;
    String custom_layout_update;
    String options_container;
    //associativeArray	additional_attributes
    String enable_googlecheckout;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
