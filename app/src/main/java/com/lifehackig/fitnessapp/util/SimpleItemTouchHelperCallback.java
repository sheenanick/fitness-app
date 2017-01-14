package com.lifehackig.fitnessapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.adapters.FirebaseExerciseListAdapter;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {
    private FirebaseExerciseListAdapter mAdapter;
    private Paint p = new Paint();
    private Context mContext;

    public SimpleItemTouchHelperCallback(int int1, int int2, FirebaseExerciseListAdapter adapter, Context context) {
        super(int1, int2);
        mAdapter = adapter;
        mContext = context;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

            View itemView = viewHolder.itemView;

            float right = itemView.getRight();
            float left = itemView.getLeft();
            float top = itemView.getTop();
            float bottom = itemView.getBottom();
            float height = itemView.getHeight();
            float rectWidth = height / 3;

            p.setColor(Color.RED);
            RectF background = new RectF(left, top, right, bottom);
            c.drawRect(background,p);
            Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_delete_forever_white_24dp);
            RectF icon_dest = new RectF(right - 2*rectWidth ,top + rectWidth, right - rectWidth, bottom - rectWidth);
            c.drawBitmap(icon,null,icon_dest,p);

        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
