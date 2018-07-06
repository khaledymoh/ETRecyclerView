package com.app.etrecyclerview.Model;

/**
 * Created by Khaled on 7/6/18.
 */

public class Item {
    public String title ;
    public int id ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Item(String title, int id){
        this.title = title;
        this.id = id ;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
