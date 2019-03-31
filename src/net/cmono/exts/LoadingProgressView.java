package net.cmono.exts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

public class LoadingProgressView extends View {
	private int width;
	private int height;
	private int progress;
	private int maxProgress = 100;

	private Path mPath;
	private Paint mPaintCircle;
	private Paint mPaintWave;
	private Paint mPaintText;
	private Bitmap mBitmapBubble;
	private Canvas mCanvasBitmap;

	private int size = 0;// 水波动幅度
	private int count;// 水流动距离
	private boolean isAdd = true;
	private static final int START_WAVE = 0x21;

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
		invalidate();
	}

	public int getMaxProgress() {
		return maxProgress;
	}

	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case START_WAVE:
				count += 30;
				if (count >= 180) {
					count = 0;
				}
				if (isAdd) {
					size += 7;
					if (size > 41) {
						isAdd = false;
					}
				} else {
					size -= 7;
					if (size <= -41) {
						isAdd = true;
					}
				}
				invalidate();
				sendEmptyMessageDelayed(START_WAVE, 100);
				break;
			}
		}
	};

	public LoadingProgressView(Context context) {
		super(context);
	}

	public LoadingProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mPaintCircle = new Paint();
		mPaintCircle.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaintCircle.setColor(Color.argb(0X4f, 0x4d, 0x4d, 0xff));

		mPaintText = new Paint();
		mPaintText.setColor(Color.WHITE);
		mPaintText.setTextSize(50);
		mPaintText.setTextAlign(Paint.Align.CENTER);

		mPaintWave = new Paint();
		mPaintWave.setColor(Color.argb(0xaa, 0xff, 0x7c, 0x00));
		mPaintWave.setStyle(Paint.Style.FILL);
		// 不显示非重叠部分，并且重叠部分显示自己
		PorterDuffXfermode mode = new PorterDuffXfermode(
				PorterDuff.Mode.SRC_ATOP);
		mPaintWave.setXfermode(mode);

		mPath = new Path();
		handler.sendEmptyMessageDelayed(START_WAVE, 1000);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
		setMeasuredDimension(width, height);

		mBitmapBubble = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		mCanvasBitmap = new Canvas(mBitmapBubble);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		mPath.reset();
		// canvas.drawColor(Color.argb(0xaa, 0x88, 0x7e, 0x7f));//自定义颜色
		mCanvasBitmap.drawCircle(width / 2, height / 2, 200, mPaintCircle);
		mPath.reset();
		// 用path圈出一个矩形，把水波和球的包含进去
		mPath.moveTo(width / 2 + 200, height / 2 + 200 - progress / maxProgress
				* 400);
		mPath.lineTo(width / 2 + 200, height / 2 + 200);
		mPath.lineTo(0, height / 2 + 200);
		mPath.lineTo(0, height / 2 + 200 - progress / maxProgress * 400);
		/*
		 * 画一条个模拟流动的波浪
		 */
		// 当count增大时，重绘会显示向前流动效果,count的值不能大于width/2-200
		mPath.lineTo(count, height / 2 + 200 - (float) progress / maxProgress
				* 400);
		// mPath.moveTo(count,200);
		// size的从大到小从小到大变化，重绘时会产生波浪起伏效果
		for (int i = 0; i < 20; i++) {
			/*
			 * rQuadTo()方法每次都会自动移动到下一位置，参数依次为水平幅度， 垂直幅度，水平位移，处置位移
			 */
			mPath.rQuadTo(20, size, 90, 0);
			mPath.rQuadTo(20, -size, 90, 0);
		}
		mPath.close();
		mCanvasBitmap.drawPath(mPath, mPaintWave);
		canvas.drawBitmap(mBitmapBubble, 0, 0, null);
		// 绘制文本，当前进度
		canvas.drawText(progress * 100 / maxProgress + "%", width / 2,
				height / 2, mPaintText);

	}
}
