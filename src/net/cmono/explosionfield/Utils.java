/*
 * Copyright (C) 2015 tyrantgit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.cmono.explosionfield;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

public class Utils {

    private Utils() {
    }

    /**
     * 像素密度
     */
    private static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;
    /**
     * 用来绘图
     */
    private static final Canvas sCanvas = new Canvas();

    /**
     * 将dp 转为 像素
     * @param dp
     * @return
     */
    public static int dp2Px(int dp) {
        return Math.round(dp * DENSITY);
    }

    /**
     * 从视图获得它的图像
     * @param view 要爆炸的view
     * @return 它的图像
     */
    public static Bitmap createBitmapFromView(View view) {

        //如果当前的是ImageView 类型
        //那么最方便了 它的Drawable 是 BitmapDrawable的
        //可以直接获得其中的图
        if (view instanceof ImageView) {
            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable != null && drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }
        }

        //如果不是
        //那么首先 就要使他失去焦点
        //因为获得了焦点的视图可能会随时就改变
        view.clearFocus();

        //生成视图的快照 但是这个快照是空白的
        //只是当前尺寸和视图一样
        Bitmap bitmap = createBitmapSafely(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888, 1);

        //如果成功获得了快照
        if (bitmap != null) {
            synchronized (sCanvas) {

                //先设置背景为那个空白的快照
                Canvas canvas = sCanvas;
                canvas.setBitmap(bitmap);

                //将视图绘制在canvas中
                view.draw(canvas);

                //然后一处空白的快照
                //以此来获得真正的视图快照
                canvas.setBitmap(null);
            }
        }

        //现在空白的快照已经有了view的样子
        //是真正的快照了
        return bitmap;
    }

    /**
     * 创建一个和指定尺寸大小一样的bitmap
     * @param width 宽
     * @param height 高
     * @param config 快照配置 详见 {@link android.graphics.Bitmap.Config}
     * @param retryCount 当生成空白bitmap发生oom时  我们会尝试再试试生成bitmap 这个为尝试的次数
     * @return 一个和指定尺寸大小一样的bitmap
     */
    public static Bitmap createBitmapSafely(int width, int height, Bitmap.Config config, int retryCount) {
        try {
            //创建空白的bitmap
            return Bitmap.createBitmap(width, height, config);

            //如果发生了oom
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            if (retryCount > 0) {
                //主动gc 然后再次试试
                System.gc();
                return createBitmapSafely(width, height, config, retryCount - 1);
            }

            //直到次数用光
            return null;
        }
    }
}
