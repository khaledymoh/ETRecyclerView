package com.app.etrecyclerview.Helpers;

import android.widget.Filter;

import com.app.etrecyclerview.Adapter.ETRecycleAdapter;
import com.app.etrecyclerview.Model.Item;

import java.util.ArrayList;

/**
 * Created by Khaled on 10/27/17.
 */

public class FilterHelperItems extends Filter {

    static ArrayList<Item> currentList ;
    static ETRecycleAdapter adapter ;

    public  static FilterHelperItems newInstance(ArrayList<Item> currentList , ETRecycleAdapter adapter){
        FilterHelperItems.currentList = currentList ;
        FilterHelperItems.adapter = adapter ;

        return new FilterHelperItems();
    }



    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        if(constraint != null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();
            ArrayList<Item> foundList = new ArrayList<>();
            String spacecarft  ;

            for(int i = 0 ; i < currentList.size() ; i++) {
                spacecarft = currentList.get(i).title;

                if(spacecarft.toUpperCase().contains(constraint)){
                    foundList.add(currentList.get(i));
                }
            }

            filterResults.count = foundList.size();
            filterResults.values = foundList ;
        }else {

            filterResults.count = currentList.size();
            filterResults.values = currentList ;
        }

        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.setSpacecrafts((ArrayList<Item>) results.values);
        adapter.notifyDataSetChanged();


    }
}
