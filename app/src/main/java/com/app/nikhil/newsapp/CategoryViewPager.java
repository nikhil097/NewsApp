package com.app.nikhil.newsapp;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CategoryViewPager extends ViewPager {

        private boolean enabled;

        public CategoryViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.enabled = true;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
         //   super.onTouchEvent(event);
            return false;

        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
        //    super.onInterceptTouchEvent(event);
            return false;

        }

        public void setPagingEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }


