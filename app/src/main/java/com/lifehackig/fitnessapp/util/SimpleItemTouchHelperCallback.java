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
import com.lifehackig.fitnessapp.adapters.FirebaseListAdapterInterface;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {
    private FirebaseListAdapterInterface mAdapter;
    private Paint paint = new Paint();
    private Context mContext;

    public SimpleItemTouchHelperCallback(int int1, int int2, FirebaseListAdapterInterface adapter, Context context) {
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
    public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

            View itemView = viewHolder.itemView;

            float right = itemView.getRight();
            float left = itemView.getLeft();
            float top = itemView.getTop();
            float bottom = itemView.getBottom();
            float height = itemView.getHeight();
            float rectWidth = height / 3;

            paint.setColor(Color.RED);
            RectF background = new RectF(left, top, right, bottom);
            canvas.drawRect(background, paint);
            Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_delete_forever_white_24dp);
            RectF iconDest = new RectF(right - 2 * rectWidth ,top + rectWidth, right - rectWidth, bottom - rectWidth);
            canvas.drawBitmap(icon, null, iconDest, paint);

        }
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
