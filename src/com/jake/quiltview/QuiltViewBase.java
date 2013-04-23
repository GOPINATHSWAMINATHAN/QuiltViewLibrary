package com.jake.quiltview;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;

import java.util.ArrayList;


public class QuiltViewBase extends GridLayout {
	
	public int[] size;
	public int columns;
	public int rows;
	public int view_width = -1;
	public int view_height = -1;
	public boolean isVertical = true;
    public int baseWidth, baseHeight;
	public ArrayList<View> views;

	public QuiltViewBase(Context context, boolean isVertical, int baseWidth, int baseHeight) {
		super(context);
		this.isVertical = isVertical;
        this.baseWidth = baseWidth;
        this.baseHeight = baseHeight;
		if(view_width == -1){
			DisplayMetrics metrics = this.getResources().getDisplayMetrics();
			int width = metrics.widthPixels;
			int height = metrics.heightPixels - 120;
			view_width = width - this.getPaddingLeft() - this.getPaddingRight();
			view_height = height - this.getPaddingTop() - this.getPaddingBottom();
		}
		views = new ArrayList<View>();
		setup();
	}
	
	public void setup(){
		if(isVertical){
			setupVertical();
		} else {
			setupHorizontal();
		}
	}
	
	public void setupVertical(){
		size = getBaseSizeVertical();
		this.setColumnCount(columns);
		this.setOrientation(HORIZONTAL);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		this.setLayoutParams(params);
	}
	
	public void setupHorizontal(){
		size = getBaseSizeHorizontal();
		this.setRowCount(rows);
		this.setOrientation(VERTICAL);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		this.setLayoutParams(params);
	}
	
	public void addPatch(View view){
		view.setLayoutParams(getNextViewParams());
		addView(view);
		views.add(view);
	}

    public GridLayout.LayoutParams getNextViewParams() {
        QuiltViewPatch child = getNextPatch();

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = size[0]*child.width_ratio;
        params.height = size[1]*child.height_ratio;
        Log.e("TSC", "width: "+params.width+" height: "+params.height);
        params.rowSpec = GridLayout.spec(Integer.MIN_VALUE, child.height_ratio);
        params.columnSpec = GridLayout.spec(Integer.MIN_VALUE, child.width_ratio);

        return params;
    }

    public QuiltViewPatch getNextPatch() {
        int count = this.getChildCount();
        return QuiltViewPatch.init(count, columns);
    }
	
	public void refresh(){
		this.removeAllViewsInLayout();
		setup();
		for(View view : views){
			addPatch(view);
		}
	}
	
	public int[] getBaseSizeVertical(){
		int[] size = new int[2];
		
		float width_height_ratio = (3.0f/4.0f);
		
		int base_width = getBaseWidth();
		int base_height = getBaseHeight();
		
		size[0] = base_width; // width
		size[1] = base_height; // height
		return size;
	}
	
	public int[] getBaseSizeHorizontal(){
		int[] size = new int[2];
		
		float width_height_ratio = (4.0f/3.0f);

		int base_width = getBaseWidth();
        int base_height = getBaseHeight();
		
		size[0] = base_width; // width
		size[1] = base_height; // height
		return size;
	}
	
	public int getBaseWidth(){

        if (baseWidth != 0) {
            columns = view_width / baseWidth;
            return baseWidth;
        }

		if(view_width < 500){
			columns = 2;
		} else if(view_width < 801){
			columns = 3;
		} else if(view_width < 1201){
			columns = 4;
		} else if(view_width < 1601){
			columns = 5;
		} else {
			columns = 6;
		}
		return (view_width / columns);
	}
	
	public int getBaseHeight(){

        if (baseHeight != 0) {
            rows = view_height / baseHeight;
            return baseHeight;
        }

		if(view_height < 350){
			rows = 2;
		} else if(view_height < 650){
			rows = 3;
		} else if(view_height < 1050){
			rows = 4;
		} else if(view_height < 1250){
			rows = 5;
		} else {
			rows = 6;
		}
		return (view_height / rows);
	}
	 
	 /*@Override 
	 protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
	    int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
	    int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    
	    view_width = parentWidth;
        view_height = parentHeight;
        
        setup(isVertical);
	 }*/
	
	@Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
            super.onSizeChanged(xNew, yNew, xOld, yOld);
            view_width = xNew;
            view_height = yNew;
    }

}
