package com.app.etrecyclerview.Listener;

/**
 * Created by Khaled on 7/6/18.
 */

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);

}
