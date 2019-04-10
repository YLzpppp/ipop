package com.jasoncareter.ipop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.jasoncareter.ipop.R;


public class iImageView extends AppCompatImageView {

    private int type;
    private static final int Type_circle = 0;
    private static final int Type_round = 1;

    // Image
    private Bitmap mSrc ;

    //radius size
    private int mRadius;

    //widget's width
    private int mWidth;

    //widget's height
    private int mHeight;

    public iImageView(Context context, AttributeSet attrs , int defStyle) {
        super(context, attrs ,defStyle);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs , R.styleable.iImageView,defStyle,0);

        int n = array.getIndexCount();
        for ( int i = 0 ; i < n ; i++){
            int attr = array.getIndex(i);
            switch (attr){
                case R.styleable.iImageView_src :
                    mSrc = BitmapFactory.decodeResource(getResources() , array.getResourceId(attr ,0));
                    break;
                case R.styleable.iImageView_type:
                    type = array.getInt(attr , 0);
                    break;
                case R.styleable.iImageView_borderRadius:
                    mRadius = array.getDimensionPixelSize(attr , (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10f,getResources().getDisplayMetrics()));
                    break;
            }
        }
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if ( specMode == MeasureSpec.EXACTLY ){
            mWidth = specSize ;
        }else {
            int designByImg =  getPaddingLeft() + getPaddingRight()+mSrc.getWidth();
            if ( specMode == MeasureSpec.AT_MOST){
                mWidth = Math.min(designByImg , specSize);
            }else
                mWidth = designByImg;
        }
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if ( specMode == MeasureSpec.EXACTLY){
            mHeight = specSize ;
        }else{
            int design = getPaddingTop() + getPaddingBottom() + mSrc.getHeight();
            if ( specMode == MeasureSpec.AT_MOST){
                mHeight = Math.min(design , specSize);
            }else
                mHeight = design;
        }
        setMeasuredDimension(mWidth , mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (type)
        {
            // 如果是TYPE_CIRCLE绘制圆形
            case Type_circle :

                int min = Math.min(mWidth, mHeight);
                /**
                 * 长度如果不一致，按小的值进行压缩
                 */
                mSrc = Bitmap.createScaledBitmap(mSrc, min, min, false);

                canvas.drawBitmap(createCircleImage(mSrc, min), 0, 0, null);
                break;
            case Type_round :
                canvas.drawBitmap(createRoundConerImage(mSrc), 0, 0, null);
                break;
        }
    }

    private Bitmap createCircleImage(Bitmap source, int min)
    {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(target);

        canvas.drawCircle(min / 2, min / 2, min / 2, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    private Bitmap createRoundConerImage(Bitmap source)
    {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rect, mRadius, mRadius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }



}
