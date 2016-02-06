package in.co.mrfoody.mrfoody.Service;

import android.os.AsyncTask;

/**
 * Created by om on 23/1/16.
 */
public class NetCheck extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... params) {
        return null;
    }
/*
    catalogProductEntityArray

    {
        item = catalogProductEntity {
        product_id = 1;
        sku = sku;
        name = product name;
        set = 4;
        type = simple;
        category_ids = ArrayOfString {
            item = 2;
            item = 3;
            item = 9;
            item = 10;
            item = 11;
            item = 12;
        }
        ;
        website_ids = ArrayOfString {
            item = 1;
        }
        ;
    } ;
        item = catalogProductEntity {
        product_id = 2;
        sku = mixveg;
        name = Veg MIx;
        set = 4;
        type = simple;
        category_ids = ArrayOfString {
            item = 3;
            item = 6;
            item = 8;
            item = 9;
            item = 10;
            item = 11;
            item = 12;
        }
        ;
        website_ids = ArrayOfString {
            item = 1;
        }
        ;
    } ;
        item = catalogProductEntity {
        product_id = 3;
        sku = Chikken TIkka;
        name = Chikken TIkka;
        set = 4;
        type = simple;
        category_ids = ArrayOfString {
            item = 11;
            item = 13;
        }
        ;
        website_ids = ArrayOfString {
            item = 1;
        }
        ;
    } ;
    }
*/
    /*
    ArrayOfCatalogCategoryEntitiesNoChildren

    {
        item = catalogCategoryEntityNoChildren {
        category_id = 3;
        parent_id = 6;
        name = Hotel;
        is_active = 1;
        position = 1;
        level = 2;
    }
        ;
        item = catalogCategoryEntityNoChildren {
        category_id = 5;
        parent_id = 6;
        name = Restaurants;
        is_active = 1;
        position = 2;
        level = 2;
    }
        ;
        item = catalogCategoryEntityNoChildren {
        category_id = 7;
        parent_id = 6;
        name = Cake Shops;
        is_active = 1;
        position = 3;
        level = 2;
    } ;
        item = catalogCategoryEntityNoChildren {
        category_id = 14;
        parent_id = 6;
        name = Mess;
        is_active = 1;
        position = 4;
        level = 2;
    }
        ;
    }
    */
    /*
    catalogProductReturnEntity

    {
        product_id = 1;
        sku = sku;
        set = 4;
        type = simple;
        categories = ArrayOfString {
        item = 2;
        item = 3;
        item = 9;
        item = 10;
        item = 11;
        item = 12;
    }
        ;
        websites = ArrayOfString {
        item = 1;
    }
        ;
        created_at = 2015 - 12 - 22 T10:
    43:01 + 05:30;
        updated_at = 2016 - 01 - 04 10:58:26;
        type_id = simple;
        name = product name;
        description = description of product;
        short_description = product short description;
        weight = 10.0000;
        status = 1;
        url_key = product - name;
        url_path = product - name.html;
        visibility = 4;
        category_ids = ArrayOfString {
        item = 2;
        item = 3;
        item = 9;
        item = 10;
        item = 11;
        item = 12;
    }
        ;
        has_options = 0;
        price = 10.0000;
        tax_class_id = 0;
        tier_price = catalogProductTierPriceEntityArray {
    }
        ;
        options_container = container1;
    }
    */
/*
    catalogProductImageEntityArray

    {
        item = catalogProductImageEntity {
        file =/c / h / chikentikka.jpg;
        label = chikkenTikka;
        position = 1;
        exclude = 0;
        url = http://dev.mrfoody.co.in/media/catalog/product/c/h/chikentikka.jpg;
        types=ArrayOfString{
        item=image;
        item=small_image;
        item=thumbnail;
        };
        };
        }
    }
*/
}