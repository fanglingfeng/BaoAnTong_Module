package com.tjsoft.webhall.lib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoTouchViewPage extends ViewPager{

    private boolean scrollble = true;  
    
    public NoTouchViewPage(Context context) {  
        super(context);  
    }  
  
    public NoTouchViewPage(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
  
    @SuppressLint("ClickableViewAccessibility")
	@Override  
    public boolean onTouchEvent(MotionEvent ev) {  
        if (!scrollble) {  
            return true;  
        } else{
        	return super.onTouchEvent(ev);  
        }
        
    }  
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!scrollble){
        	return false;
        }else{
        	return super.onInterceptTouchEvent(ev);
        }
        	
           
    }
  
    public boolean isScrollble() {  
        return scrollble;  
    }  
  
    public void setScrollble(boolean scrollble) {  
        this.scrollble = scrollble;  
    } 

}
