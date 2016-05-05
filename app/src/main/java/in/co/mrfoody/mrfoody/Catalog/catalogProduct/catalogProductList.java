/*
 * Copyright (c) 2016 Jivan Ghadage <jivanghadage@gmail.com>.
 */

package in.co.mrfoody.mrfoody.Catalog.catalogProduct;

/**
 * Created by om on 5/2/16.
 */

public class catalogProductList {

    String product_id;
    String sku;
    String name;
    String set;
    String type;
    private CategoryIds category_id;
    private WebsiteIds website_ids;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CategoryIds getCategory_id() {
        return category_id;
    }

    public void setCategory_id(CategoryIds category_id) {
        this.category_id = category_id;
    }

    public WebsiteIds getWebsite_ids() {
        return website_ids;
    }

    public void setWebsite_ids(WebsiteIds website_ids) {
        this.website_ids = website_ids;
    }

}

    class CategoryIds{
        private String item;
    }

    class WebsiteIds{
        private String item;
    }
