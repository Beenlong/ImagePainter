package net.zombie_sama.imagepainter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class PaintableImageView extends android.support.v7.widget.AppCompatImageView {
    private Paint paint;
    private ScaleDirection direction;
    private Canvas canvas;
    private boolean isPainting;
    private float startX = 0, startY = 0;
    private OnDrawDoneListener onDrawDoneListener;
    // 缩放比
    private float scale;
    // 源图片
    private Bitmap bitmapSrc;
    // 缓存图片
    private Bitmap bitmapCache;
    // View长宽比
    private float ratioView;
    // Bitmap长宽比
    private float ratioImage;
    // 偏移量
    private float offset;

    public PaintableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3f);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v == PaintableImageView.this) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            isPainting = false;
                            draw(event);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            draw(event);
                            break;
                        case MotionEvent.ACTION_UP:
                            draw(event);
                            isPainting = false;
                            if (onDrawDoneListener != null) {
                                onDrawDoneListener.onDrawDone(bitmapCache);
                            }
                            break;
                    }
                    return true;
                } else {
                    isPainting = false;
                    return false;
                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            loadBitmap();
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        loadBitmap();
    }

    private void draw(MotionEvent event) {
        float x = event.getX() / scale;
        float y = event.getY() / scale;
        if (direction == ScaleDirection.height) {
            x -= offset;
        } else {
            y -= offset;
        }
        if (!isPainting) {
            isPainting = true;
            startX = x;
            startY = y;
        } else {
            Log.i("draw", "x:from " + startX + " to " + event.getX() + " y:from " + startY + " to " + event.getY());
            canvas.drawLine(startX, startY, x, y, paint);
            startX = x;
            startY = y;
            super.setImageBitmap(bitmapCache);
        }
    }

    private void loadBitmap() {
        bitmapSrc = ((BitmapDrawable) getDrawable()).getBitmap();
        bitmapCache = bitmapSrc.copy(bitmapSrc.getConfig(), true);
        if (canvas == null) canvas = new Canvas(bitmapCache);
        canvas.setBitmap(bitmapCache);
        super.setImageBitmap(bitmapCache);
        ratioView = (float) getWidth() / (float) getHeight();
        ratioImage = (float) bitmapCache.getWidth() / (float) bitmapCache.getHeight();
        if (ratioView > ratioImage) {
            scale = (float) getHeight() / (float) bitmapCache.getHeight();
            direction = ScaleDirection.height;
            offset = Math.abs(((float) getWidth() - ((float) getWidth() / scale)) / 2);
        } else {
            scale = (float) getWidth() / (float) bitmapCache.getWidth();
            direction = ScaleDirection.width;
            offset = Math.abs(((float) getHeight() - ((float) getHeight() / scale)) / 2);
        }
    }

    /**
     * 设置画笔颜色
     *
     * @param color 颜色
     */
    public void setPaintColor(int color) {
        paint.setColor(color);
    }

    /**
     * 获取画笔颜色
     *
     * @return color
     */
    public int getPaintColor() {
        return paint.getColor();
    }

    /**
     * 设置画笔宽度
     *
     * @param width 宽度
     */
    public void setPaintStrokeWidth(float width) {
        paint.setStrokeWidth(width);
    }

    /**
     * 获取画笔宽度
     *
     * @return width
     */
    public float getPaintStrokeWidth() {
        return paint.getStrokeWidth();
    }

    public Paint getPaint() {
        return paint;
    }

    /**
     * 设置画笔
     *
     * @param paint {@link Paint}
     */
    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    /**
     * 重置图片
     */
    public void reset() {
        setImageBitmap(bitmapSrc);
    }

    /**
     * 获取当前Bitmap
     *
     * @return bitmap
     */
    public Bitmap getResult() {
        return bitmapCache;
    }

    /**
     * 缩放比所基于的方向
     */
    private enum ScaleDirection {
        width, height;
    }

    /**
     * 设置画完一笔时的回调
     *
     * @param onDrawDoneListener 回调
     */
    public void setOnDrawDoneListener(OnDrawDoneListener onDrawDoneListener) {
        this.onDrawDoneListener = onDrawDoneListener;
    }

    public interface OnDrawDoneListener {
        void onDrawDone(Bitmap bitmap);
    }
}
