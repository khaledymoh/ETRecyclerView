package com.app.etrecyclerview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.app.etrecyclerview.Adapter.ETRecycleAdapter;
import com.app.etrecyclerview.Listener.OnStartDragListener;
import com.app.etrecyclerview.Listener.SimpleItemTouchHelperCallback;
import com.app.etrecyclerview.Model.Item;
import com.arlib.floatingsearchview.FloatingSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RecyclerView etRecView ;
    private FloatingActionButton fab ;
    private FloatingSearchView mSearchView;
    private TextView txtStatus ;

    private ItemTouchHelper mItemTouchHelper;
    private LinearLayoutManager recLinearLayoutManager ;
    private ETRecycleAdapter etRecAdapter ;
    private List<Item> items = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();

        etRecAdapter.setOnStartDragListener(new OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                mItemTouchHelper.startDrag(viewHolder);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random rand = new Random();
                int newNum =  rand.nextInt(100 - 15) + 15;
                etRecAdapter.insertItem(new Item("Title " + newNum , newNum));
            }
        });


        //Technique to hide/show FAB with RecyclerView
        etRecView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0 && !fab.isShown())
                    fab.show();
                else if(dy > 0 && fab.isShown())
                    fab.hide();
            }
        });


        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.sort_items) {
                    etRecAdapter.sortItems();
                    etRecAdapter.notifyDataSetChanged();
                }

            }
        });

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                txtStatus.setText("Status : Search On " + newQuery );
                etRecAdapter.getFilter().filter(newQuery);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void init(){

        for (int i = 1 ; i <= 20 ;i++){
            switch (i){
                case 4 :
                    items.add(new Item("Jordan " + i, i));
                    break;
                case 5 :
                    items.add(new Item("United States " + i, i));
                    break;
                case 9 :
                    items.add(new Item("Egypt " + i, i));
                    break;
                case 11 :
                    items.add(new Item("United Kingdom " + i, i));
                    break;
                case 15 :
                    items.add(new Item("Japan " + i, i));
                    break;
                case 17 :
                    items.add(new Item("Palestine " + i, i));
                    break;
                default:
                    items.add(new Item("Title " + i, i));
            }



        }


        etRecView = findViewById(R.id.et_rec);
        fab =  findViewById(R.id.fab);
        txtStatus = findViewById(R.id.txt_status);
        mSearchView = findViewById(R.id.floating_search_view);

        recLinearLayoutManager = new LinearLayoutManager(this);
        etRecView.setLayoutManager(recLinearLayoutManager);

        etRecAdapter = new ETRecycleAdapter(this, items,etRecView);

        etRecView.setAdapter(etRecAdapter);


        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(etRecAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(etRecView);

        etRecAdapter.setAppCompatActivity(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.sort_items) {
            etRecAdapter.sortItems();
            etRecAdapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
