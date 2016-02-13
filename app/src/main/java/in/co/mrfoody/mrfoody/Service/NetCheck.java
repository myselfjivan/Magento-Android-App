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

    catalogCategoryInfo{category_id=5; is_active=1; position=2; level=2; parent_id=6; all_children=5; children=
    ; created_at = 2015 - 12 - 25 T19:
    28:48 + 05:30;
        updated_at = 2016 - 02 - 04 17:57:15;
        name = Restaurants;
        url_key = hotel2;
        description = all the restaurants in kolhapur;
        path = 1 / 6 / 5;
        url_path = hotel2.html;
        children_count = 0;
        display_mode = PRODUCTS;
        is_anchor = 0;
        available_sort_by = ArrayOfString {
        }
        ;
        default_sort_by = position;
        include_in_menu = 1;
        custom_use_parent_settings = 1;
    }
    */
    /*
    catalogCategoryInfo

    {
        category_id = 3;
        is_active = 1;
        position = 1;
        level = 2;
        parent_id = 6;
        all_children = 3, 11, 12, 13, 8, 9, 10;
        children = 8, 9, 11;
        created_at = 2015 - 12 - 22 T13:
    39:33 + 05:30;
        updated_at = 2016 - 02 - 06 10:22:39;
        name = Hotel;
        url_key = hotel;
        description = testing category;
        meta_title = Hotels at Kolhapur|MrFoody.co.in;
        meta_keywords = all the hotels in kolhapur;
        path = 1 / 6 / 3;
        url_path = hotel.html;
        children_count = 6;
        display_mode = PRODUCTS_AND_PAGE;
        is_anchor = 1;
        available_sort_by = ArrayOfString {
    }
        ;
        page_layout = three_columns;
        default_sort_by = position;
        include_in_menu = 1;
        custom_use_parent_settings = 0;
        custom_apply_to_products = 0;
    }
    */
    /*
    catalogCategoryInfo{category_id=8; is_active=1; position=1; level=3; parent_id=3; all_children=8; children=
    ; created_at = 2015 - 12 - 25 T22:
    15:41 + 05:30;
        updated_at = 2016 - 02 - 06 10:34:27;
        name = Hotel 1;
        url_key = hotel1;
        description = Hotel Zoraba;
        path = 1 / 6 / 3 / 8;
        url_path = hotel / hotel1.html;
        children_count = 0;
        display_mode = PRODUCTS;
        is_anchor = 0;
        available_sort_by = ArrayOfString {
        }
        ;
        custom_design = rwd / default ;
            page_layout = three_columns;
            default_sort_by = position;
            include_in_menu = 1;
            custom_use_parent_settings = 0;
            custom_apply_to_products = 0;
    }
    */
    /*
    catalogAssignedProductArray

    {
        item = catalogAssignedProduct {
        product_id = 1;
        type = simple;
        set = 4;
        sku = sku;
        position = 1;
    }
        ;
        item = catalogAssignedProduct {
        product_id = 2;
        type = simple;
        set = 4;
        sku = mixveg;
        position = 1;
    }
        ;
        item = catalogAssignedProduct {
        product_id = 3;
        type = simple;
        set = 4;
        sku = Chikken TIkka;
        position = 1;
    } ;
    }
    */
}