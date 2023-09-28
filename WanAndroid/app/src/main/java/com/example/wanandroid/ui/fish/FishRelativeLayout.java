package com.example.wanandroid.ui.fish;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * @author Jack_Ou  created on 2020/9/3.
 */
public class FishRelativeLayout extends RelativeLayout {

    private Paint mPaint;
    private ImageView mIvFish;
    private CleverFish cleverFish;
    private float touchX;
    private float touchY;
    private float ripple;
    private int alpha;

    private int OTHER_ALPHA = 110;
    public FishRelativeLayout(Context context) {
        this(context, null);
    }

    public FishRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FishRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        //viewGroup默认不会调用onDraw方法的需要设置一下
        setWillNotDraw(false);

        //用于画点击点和水波纹
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);
        mPaint.setColor(Color.argb(OTHER_ALPHA, 244, 92, 71));

        //鱼的容器
        mIvFish = new ImageView(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mIvFish.setLayoutParams(layoutParams);
        cleverFish = new CleverFish();
        mIvFish.setImageDrawable(cleverFish);
        addView(mIvFish);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setAlpha(alpha);
        canvas.drawCircle(touchX, touchY, ripple * 150, mPaint);

        invalidate();
    }

    /**
     * ObjectAnimatior通过反射机制实现了ValueAnimator，
     * 通过获得this中的对应属性，来返回属性动画值
     * 也需要在ondraw中调用invalidate()刷新界面
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = event.getX();
        touchY = event.getY();

        mPaint.setAlpha(100);
        ObjectAnimator ripple = ObjectAnimator.ofFloat(this, "ripple", 0, 1f).setDuration(1000);
        ripple.start();

        drawSwingPath();
        return super.onTouchEvent(event);
    }

    private void drawSwingPath() {
        //得到鱼的重心,相对坐标
        PointF middlePoint = cleverFish.getMiddlePoint();
        //鱼重心的绝对坐标 fishMiddle = O
        PointF fishMiddle = new PointF(mIvFish.getX() + middlePoint.x, mIvFish.getY() + middlePoint.y);
        //鱼头的绝对坐标 headControl = A
        PointF headControl = new PointF(mIvFish.getX() + cleverFish.getHeadPoint().x,
                mIvFish.getY() + cleverFish.getHeadPoint().y);
        //结束点绝对坐标  endPoint = B
        PointF endPoint = new PointF(touchX, touchY);

        //控制点2 设定为鱼头 重心 结束点夹角的一般
        // 求夹角公式：cosAOB = （OA * OB）/ |OA|*|OB|
        // OA = (Ax - Ox,Ay - Oy)
        // OB = (Bx - Ox,By - Oy)
        // OA * OB = (Ax - Ox)*(Bx - Ox) + (Ay - Oy)(By - Oy)
        // |OA| 表示OA的模
        // angle是AOB大角
        final float angle = getAngle(fishMiddle, headControl, endPoint) / 2;
        float delta = getAngle(fishMiddle, new PointF(fishMiddle.x + 1, fishMiddle.y), headControl);
        PointF control2 = cleverFish.calculatePoint(fishMiddle, (float) (cleverFish.HEAD_RADIUS * 1.6f), angle + delta);

        Path path = new Path();
        //此处要减去重心的距离，否者会抖一下
        path.moveTo(fishMiddle.x - middlePoint.x, fishMiddle.y - middlePoint.y);
        path.cubicTo(headControl.x - middlePoint.x, headControl.y - middlePoint.y,
                control2.x - middlePoint.x, control2.y - middlePoint.y,
                endPoint.x - middlePoint.x, endPoint.y - middlePoint.y);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mIvFish, "x", "y", path);
        objectAnimator.setDuration(3000);
        //监听动画开始和结束，调节摆尾频率
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //在此处设置频率可能会产生抖动
                //cleverFish.setFrequence(1f);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
//                cleverFish.setFrequence(4f);
            }
        });

        //获取path的切线值  使用pathMeasure来获取
        final PathMeasure pathMeasure = new PathMeasure(path,false);
        //用于存放x y
        final float[] tan = new float[2];
        //获取路径上变化的x y
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //animatedFraction是已经执行了的百分比
                float animatedFraction = valueAnimator.getAnimatedFraction();
                //此处设置会在斜率比较大的时候摆率比较高
                cleverFish.setFrequence(3f * (1- animatedFraction) + 1);
                //pathMeasure.getLength() * animatedFraction 已经经过了百分比
                //getPosTan获得路径上的tan值
                //通过该方法把值放入tan数组中
                pathMeasure.getPosTan(pathMeasure.getLength() * animatedFraction, null, tan);
                //Y坐标是反的，所以用负号
                float angle = (float) Math.toDegrees(Math.atan2(-tan[1], tan[0]));
                cleverFish.setOriginMainAngle(angle);
            }
        });
        objectAnimator.start();
    }

    private float getAngle(PointF O, PointF A, PointF B) {
        float AOB = (A.x - O.x) * (B.x - O.x) + (A.y - O.y) * (B.y - O.y);
        float OALength = (float) Math.sqrt((A.x - O.x) * (A.x - O.x) + (A.y - O.y) * (A.y - O.y));
        // OB 的长度
        float OBLength = (float) Math.sqrt((B.x - O.x) * (B.x - O.x) + (B.y - O.y) * (B.y - O.y));
        float cosAOB = AOB / (OALength * OBLength);
        // 反余弦
        float angleAOB = (float) Math.toDegrees(Math.acos(cosAOB));

        //AB与X轴的tan值 - OB与X轴的tan值
        float direction = (A.y - B.y) / (A.x - B.x) - (O.y - B.y) / (O.x - B.x);

        //点在鱼右边direction小于0  在左边 direction大于0
        if (direction == 0) {
            if (AOB >= 0) {
                return 0;
            } else {
                return 180;
            }
        } else {
            if (direction > 0) {
                return -angleAOB;
            } else {
                return angleAOB;
            }
        }
    }

    public float getRipple() {
        return ripple;
    }

    public void setRipple(float ripple) {
        //alpha也可以写在ondraw中
        alpha = (int) (100 * (1 - ripple));
        this.ripple = ripple;
    }
}
