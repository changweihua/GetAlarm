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

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.Random;

public class ExplosionAnimator extends ValueAnimator {

    /**
     * 默认的播放时间
     */
    static long DEFAULT_DURATION = 0x400;
    /**
     * 加速度补间器
     */
    private static final Interpolator DEFAULT_INTERPOLATOR = new AccelerateInterpolator(0.6f);
    private static final float END_VALUE = 1.4f;
    private static final float X = Utils.dp2Px(5);
    private static final float Y = Utils.dp2Px(20);
    private static final float V = Utils.dp2Px(2);
    private static final float W = Utils.dp2Px(1);

    //绘制的画笔
    private Paint mPaint;
    private Particle[] mParticles;
    //要绘制的区域
    private Rect mBound;
    //要爆炸的view
    private View mContainer;

    public ExplosionAnimator(View container, Bitmap bitmap, Rect bound) {

        //用来画烟花
        mPaint = new Paint();
        //爆炸区域
        mBound = new Rect(bound);

        //生成爆炸烟花点
        int partLen = 15;
        mParticles = new Particle[partLen * partLen];

        //随机的从生成的快照里获得颜色 用作烟花点的颜色
        Random random = new Random(System.currentTimeMillis());
        int w = bitmap.getWidth() / (partLen + 2);
        int h = bitmap.getHeight() / (partLen + 2);

        for (int i = 0; i < partLen; i++) {
            for (int j = 0; j < partLen; j++) {

                //要取颜色的位置
                final int x = (j + 1) * w;
                final int y = (i + 1) * h;

                //获取颜色
                final int color = bitmap.getPixel(x, y);
                //生成爆炸粒子
                mParticles[(i * partLen) + j] = generateParticle(color, random);
            }
        }

        //保存当前的视图
        mContainer = container;

        //设置值
        setFloatValues(0f, END_VALUE);
        //设置补间器
        setInterpolator(DEFAULT_INTERPOLATOR);
        //设置动画时长
        setDuration(DEFAULT_DURATION);
    }

    /**
     * 生成爆炸粒子
     * @param color 爆炸粒子的颜色
     * @param random
     * @return 爆炸粒子
     */
    private Particle generateParticle(int color, Random random) {

        //生成烟花点
        Particle particle = new Particle();
        particle.color = color;

        //设置半径
        particle.radius = V;

        //产生随机大小的base radius
        if (random.nextFloat() < 0.2f) {
            particle.baseRadius = V + ((X - V) * random.nextFloat());
        } else {
            particle.baseRadius = W + ((V - W) * random.nextFloat());
        }

        float nextFloat = random.nextFloat();
        particle.top = mBound.height() * ((0.18f * random.nextFloat()) + 0.2f);
        particle.top = nextFloat < 0.2f ? particle.top : particle.top + ((particle.top * 0.2f) * random.nextFloat());
        particle.bottom = (mBound.height() * (random.nextFloat() - 0.5f)) * 1.8f;
        float f = nextFloat < 0.2f ? particle.bottom : nextFloat < 0.8f ? particle.bottom * 0.6f : particle.bottom * 0.3f;
        particle.bottom = f;
        particle.mag = 4.0f * particle.top / particle.bottom;
        particle.neg = (-particle.mag) / particle.bottom;
        f = mBound.centerX() + (Y * (random.nextFloat() - 0.5f));
        particle.baseCx = f;
        particle.cx = f;
        f = mBound.centerY() + (Y * (random.nextFloat() - 0.5f));
        particle.baseCy = f;
        particle.cy = f;
        particle.life = END_VALUE / 10 * random.nextFloat();
        particle.overflow = 0.4f * random.nextFloat();
        particle.alpha = 1f;
        return particle;
    }

    public boolean draw(Canvas canvas) {
        //直到播放完动画
        if (!isStarted()) {
            return false;
        }

        //遍历烟花点 然后绘制
        for (Particle particle : mParticles) {

            //设置烟花点的属性
            particle.advance((float) getAnimatedValue());

            //如果不是透明的 那就绘制出来
            if (particle.alpha > 0f) {
                mPaint.setColor(particle.color);
                mPaint.setAlpha((int) (Color.alpha(particle.color) * particle.alpha));
                canvas.drawCircle(particle.cx, particle.cy, particle.radius, mPaint);
            }
        }

        //这里配合view 的 draw 互相调用
        mContainer.invalidate();
        return true;
    }

    @Override
    public void start() {
        super.start();
        //这里配合view 的 draw 互相调用
        mContainer.invalidate(mBound);
    }

    private class Particle {
        float alpha;
        int color;
        float cx;
        float cy;
        float radius;
        float baseCx;
        float baseCy;
        float baseRadius;
        float top;
        float bottom;
        float mag;
        float neg;
        float life;
        float overflow;

        public void advance(float factor) {
            float f = 0f;

            //这代表一个烟花点消逝的条件
            float normalization = factor / END_VALUE;
            if (normalization < life || normalization > 1f - overflow) {
                alpha = 0f;
                return;
            }

            //然后计算出烟花点的半径 坐标 透明度参数
            //纯数学计算
            normalization = (normalization - life) / (1f - life - overflow);
            float f2 = normalization * END_VALUE;
            if (normalization >= 0.7f) {
                f = (normalization - 0.7f) / 0.3f;
            }
            alpha = 1f - f;
            f = bottom * f2;
            cx = baseCx + f;
            cy = (float) (baseCy - this.neg * Math.pow(f, 2.0)) - f * mag;
            radius = V + (baseRadius - V) * f2;
        }
    }
}
