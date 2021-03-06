package com.example.jadynai.infinatecard;

/**
 * Copyright (C) 2017 Wasabeef
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;

public class SlideAnimator extends BaseItemAnimator {


    private static final String TAG = "SlideAnimator";

    public SlideAnimator() {

    }

    public SlideAnimator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    @Override
    protected void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
        Log.d(TAG, "animateRemoveImpl: " + holder.itemView.getTag().toString());
    }

    @Override
    protected void preAnimateAddImpl(RecyclerView.ViewHolder holder) {
        Log.d(TAG, "preAnimateAddImpl: " + holder.itemView.getTag().toString());
        DataExchangeMgr.getInstance().saveOrignData((int) holder.itemView.getTag(R.id.view_data));
        if (isHorizDirection()) {
            int width = holder.itemView.getRootView().getWidth();
            ViewCompat.setTranslationX(holder.itemView, CardConfig.sViewholderDirection == ItemTouchHelper.LEFT ? -width : width);
        } else {
            int height = holder.itemView.getRootView().getHeight();
            ViewCompat.setTranslationY(holder.itemView, CardConfig.sViewholderDirection == ItemTouchHelper.UP ? -height : height);
        }
    }

    @Override
    protected void animateAddImpl(final RecyclerView.ViewHolder holder) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate(holder.itemView)
                .setDuration(getAddDuration())
                .setInterpolator(mInterpolator)
                .setListener(new DefaultAddVpaListener(holder) {
                    @Override
                    public void onAnimationStart(View view) {
                        super.onAnimationStart(view);
                        //将此view数据设置为滑开的数据
                        ((ImageView) mViewHolder.itemView.findViewById(R.id.show_img)).setImageResource(DataExchangeMgr.getInstance().getCurrentData());
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        super.onAnimationCancel(view);
                        //数据还原
                        ((ImageView) mViewHolder.itemView.findViewById(R.id.show_img)).setImageResource(DataExchangeMgr.getInstance().getCurrentData());
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        super.onAnimationEnd(view);
                        //数据还原
                        ((ImageView) mViewHolder.itemView.findViewById(R.id.show_img)).setImageResource(DataExchangeMgr.getInstance().getCurrentData());
                    }
                })
                .setStartDelay(50);
        if (isHorizDirection()) {
            viewPropertyAnimatorCompat
                    .translationX(0)
                    .start();
        } else {
            viewPropertyAnimatorCompat
                    .translationY(0)
                    .start();
        }
    }

    private boolean isHorizDirection() {
        return CardConfig.sViewholderDirection == ItemTouchHelper.LEFT || CardConfig.sViewholderDirection == ItemTouchHelper.RIGHT;
    }
}
