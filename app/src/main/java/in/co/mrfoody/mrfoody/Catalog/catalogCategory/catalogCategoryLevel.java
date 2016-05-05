/*
 * Copyright (c) 2016 Jivan Ghadage <jivanghadage@gmail.com>.
 */

package in.co.mrfoody.mrfoody.Catalog.catalogCategory;

/**
 * Created by om on 1/2/16.
 */
public class catalogCategoryLevel {

    String category_id;
    String parent_id;
    String name;
    String is_active;
    String position;
    String level;


    public String getCategoryId() {
        return category_id;
    }

    public void setCategoryId(String category_id) {
        this.category_id = category_id;
    }

    public String getParnetId(){
        return parent_id;
    }

    public void setParentId(String parent_id){
        this.parent_id = parent_id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPosition(){
        return position;
    }

    public void setPosition(String position){
        this.position = position;
    }

    public String getIsActive(){
        return is_active;
    }

    public void setIsActive(String is_active){
        this.is_active = is_active;
    }

    public String getLevel(){
        return level;
    }

    public void setLevel(String level){
        this.level = level;
    }

}
