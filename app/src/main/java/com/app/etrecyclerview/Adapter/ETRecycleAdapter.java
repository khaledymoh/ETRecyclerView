package com.app.etrecyclerview.Adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.transition.TransitionManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.etrecyclerview.Helpers.FilterHelperItems;
import com.app.etrecyclerview.Listener.ItemTouchHelperAdapter;
import com.app.etrecyclerview.Listener.ItemTouchHelperViewHolder;
import com.app.etrecyclerview.Listener.OnStartDragListener;
import com.app.etrecyclerview.MainActivity;
import com.app.etrecyclerview.Model.Item;
import com.app.etrecyclerview.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Created by Khaled on 7/6/18.
 */

public class ETRecycleAdapter extends RecyclerView.Adapter<ETRecycleAdapter.ETRecycleViewHolder>
        implements ItemTouchHelperAdapter, Filterable {

    private List<Item> items = new ArrayList<>();
    private List<Item> filteredItems = new ArrayList<>();
    private Context mContext ;
    private OnStartDragListener onStartDragListener ;
    private AppCompatActivity appCompatActivity ;
    private RecyclerView recyclerView ;

    private TextView txtStatus;

    private int mExpandedPosition = -1;
    private int previousExpandedPosition = -1;

    public ETRecycleAdapter(Context mContext, List<Item> items, RecyclerView recyclerView){
        this.mContext = mContext;
        this.items = items;
        this.filteredItems = items;
        this.recyclerView = recyclerView;
    }


    public void setOnStartDragListener(OnStartDragListener onStartDragListener){
        this.onStartDragListener = onStartDragListener;
    }

    @Override
    public ETRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        ETRecycleViewHolder vh = new ETRecycleViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ETRecycleViewHolder holder, final int position) {
        Item item = items.get(position);
        holder.txtTitle.setText(item.getTitle());


        final boolean isExpanded = position == mExpandedPosition;
        holder.etRelative.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Status : " , " Expand Item At Position " + mExpandedPosition);

                mExpandedPosition = isExpanded ? -1:position;
                TransitionManager.beginDelayedTransition(recyclerView);
                notifyItemChanged(position);

            }
        });

        if(isExpanded){
            if(txtStatus != null)
                txtStatus.setText("Status : Expand Item At Position " + mExpandedPosition);

        }


        if (isExpanded)
            previousExpandedPosition = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1:position;
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    //Adapter Touch helper
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        //Here you can use swap technique to perform move
       // Collections.swap(items, fromPosition, toPosition);
        Item i = items.get(fromPosition);
        items.remove(fromPosition);
        items.add(toPosition , i);
        mExpandedPosition = -1;
        if(txtStatus != null)
            txtStatus.setText("Status : Move");
        notifyItemMoved(fromPosition, toPosition);

        notifyItemRangeChanged(toPosition , items.size());

        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        items.remove(position);
        if(txtStatus != null)
            txtStatus.setText("Status : Dismiss");
        notifyItemRemoved(position);
    }

    public class ETRecycleViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnTouchListener, ItemTouchHelperViewHolder{


        private TextView txtTitle, txtContent ;
        private ImageView imgDrag, imgAdd, imgRemove ;
        private CardView etCard ;
        private RelativeLayout etRelative;

        public ETRecycleViewHolder(View v) {
            super(v);

            txtTitle = v.findViewById(R.id.txt_title);
            txtContent = v.findViewById(R.id.txt_content);
            imgDrag = v.findViewById(R.id.img_drag);
            imgAdd = v.findViewById(R.id.img_add);
            imgRemove = v.findViewById(R.id.img_remove);
            etCard = v.findViewById(R.id.et_card);
            etRelative = v.findViewById(R.id.et_details);

            etCard.setOnClickListener(this);
            imgAdd.setOnClickListener(this);
            imgRemove.setOnClickListener(this);

            imgDrag.setOnTouchListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.img_add){
                int randNum =  randomNum();
                appendItem(new Item("Title " + randNum, randNum) , getAdapterPosition());
                notifyItemRangeChanged(getAdapterPosition() , items.size());


            }else if(view.getId() == R.id.img_remove){
                removeItem(getAdapterPosition());
                notifyItemRangeChanged(getAdapterPosition() , items.size());

            }
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {

            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                if(onStartDragListener != null)
                    onStartDragListener.onStartDrag(this);
            }
            return false;
        }

        @Override
        public void onItemSelected() {
            //Change background / or what event you want
            ObjectAnimator animator = ObjectAnimator.ofFloat(etCard, "cardElevation", 10);
            animator.setDuration(500);
            animator.start();
        }

        @Override
        public void onItemClear() {
            //Change background / or what event you want
            ObjectAnimator animator = ObjectAnimator.ofFloat(etCard, "cardElevation", 10, 0);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.setDuration(150);
            animator.start();
        }
    }


    /* ADD/REMOVE/INSERT */

    //Append Item in Specific Position
    private void appendItem(Item item, int position){
        items.add(position,item);
        if(txtStatus != null)
            txtStatus.setText("Status : Append Item At Position " + position);
        notifyItemInserted(position);
    }

    //Remove Item From Position
    private void removeItem(int position){
        items.remove(position);
        if(txtStatus != null)
            txtStatus.setText("Status : Remove Item At Position " + position);
        notifyItemRemoved(position);
    }

    //Insert Item in the last Position
    public void insertItem(Item item){
        items.add(item);
        if(txtStatus != null)
            txtStatus.setText("Status : Insert Item At Position " + (getItemCount() - 1));
        notifyItemInserted(getItemCount() - 1);
    }

    private int randomNum(){
        Random rand = new Random();
        return rand.nextInt(100 - 15) + 15;
    }

    //Descending Sort
    //** Note you can sort Ascending by replace return with this code
    //item.getId() > t1.getId() ? -1 : (item.getId() < t1.getId() ) ? 1 : 0;
    public void sortItems(){
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item item, Item t1) {
                return t1.getId() > item.getId() ? -1 : (t1.getId() < item.getId() ) ? 1 : 0;
            }
        });


    }

    public void setAppCompatActivity(AppCompatActivity appCompatActivity){
        this.appCompatActivity = appCompatActivity;
        txtStatus = appCompatActivity.findViewById(R.id.txt_status);

    }

    public void setSpacecrafts(ArrayList<Item> items) {
        this.items = items;
    }

    @Override
    public Filter getFilter() {
        return new FilterHelperItems().newInstance((ArrayList<Item>) filteredItems, this);
    }
}
