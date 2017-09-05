package com.ghostlystudios.autolog.Controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Click Listener for RecyclerView
 */

public interface ClickListener {
    void onClick(View view, int position);
    void onLongClick(View view, int position);

    class RecyclerClickListener implements RecyclerView.OnItemTouchListener{
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerClickListener(Context context, final RecyclerView recyclerView,
                                     final ClickListener clickListener){
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent){
                    return true;
                }
                @Override
                public void onLongPress(MotionEvent motionEvent){
                    View childView = recyclerView.findChildViewUnder(motionEvent.getX(),
                            motionEvent.getY());
                    clickListener.onLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
            });

        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent){
            View childView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
            if(childView != null && clickListener != null && gestureDetector.onTouchEvent(motionEvent))
                clickListener.onClick(childView, recyclerView.getChildAdapterPosition(childView));
            return false;
        }
        @Override
        public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent){
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept){
        }

    }
}
