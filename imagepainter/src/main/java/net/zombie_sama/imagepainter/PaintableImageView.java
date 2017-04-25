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
    // ���ű�
    private float scale;
    // ԴͼƬ
    private Bitmap bitmapSrc;
    // ����ͼƬ
    private Bitmap bitmapCache;
    // View�����
    private float ratioView;
    // Bitmap�����
    private float ratioImage;
    // ƫ����
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
        if (bitmapSrc == null) bitmapSrc = ((BitmapDrawable) getDrawable()).getBitmap();
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
     * ���û�����ɫ
     *
     * @param color ��ɫ
     */
    public void setPaintColor(int color) {
        paint.setColor(color);
    }

    /**
     * ��ȡ������ɫ
     */
    public int getPaintColor() {
        return paint.getColor();
    }

    /**
     * ���û��ʿ��
     *
     * @param width ���
     */
    public void setPaintStrokeWidth(float width) {
        paint.setStrokeWidth(width);
    }

    /**
     * ��ȡ���ʿ��
     */
    public float getPaintStrokeWidth() {
        return paint.getStrokeWidth();
    }

    public Paint getPaint() {
        return paint;
    }

    /**
     * ���û���
     *
     * @param paint {@link Paint}
     */
    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    /**
     * ����ͼƬ
     */
    public void rest() {
        setImageBitmap(bitmapSrc);
    }

    /**
     * ��ȡ��ǰBitmap
     */
    public Bitmap getResult() {
        return bitmapCache;
    }

    /**
     * ���ű������ڵķ���
     */
    private enum ScaleDirection {
        width, height;
    }

    /**
     * ���û���һ��ʱ�Ļص�
     *
     * @param onDrawDoneListener �ص�
     */
    public void setOnDrawDoneListener(OnDrawDoneListener onDrawDoneListener) {
        this.onDrawDoneListener = onDrawDoneListener;
    }

    interface OnDrawDoneListener {
        void onDrawDone(Bitmap bitmap);
    }
}
