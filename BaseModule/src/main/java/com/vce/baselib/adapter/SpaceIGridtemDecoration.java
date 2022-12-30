

package com.vce.baselib.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

public class SpaceIGridtemDecoration extends RecyclerView.ItemDecoration {
    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;

    private static final String TAG = "DividerItem";
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private int spaceHeight = 0;
    private int spaceWidth=0;
    private int spanCount=0;

    /**
     * Current orientation. Either {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    private int mOrientation;

    private final Rect mBounds = new Rect();

    public SpaceIGridtemDecoration(Context context,int spanCount,int spacew,int spaceh) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        a.recycle();
        this.spaceWidth=spacew;
        this.spaceHeight=spaceh;
        this.spanCount=spanCount;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int lastLine=childCount / spanCount;
        int bottom=0;
        int right=0;
        int left =0;
        int position=parent.getChildAdapterPosition(view);
//        if (position / spanCount < lastLine) {
//            bottom=spaceHeight;
//        } else {
//            bottom=spaceHeight;
//        }
        bottom = spaceHeight;
//        if((position+1) % spanCount == 0 && position!=0){
//            right=0;
//        }else {
            right=spaceWidth/2;
//        }
//        if((position+1) % spanCount == 1 || position == 0 ){
//            left=0;
//        }else {
            left=spaceWidth/2;
//        }

        outRect.set(left,0,right,bottom);
    }
}
