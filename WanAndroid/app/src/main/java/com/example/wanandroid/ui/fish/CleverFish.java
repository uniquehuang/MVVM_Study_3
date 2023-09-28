package com.example.wanandroid.ui.fish;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

/**
 * @Author: Jack Ou
 * @CreateDate: 2020/9/2 22:19
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/9/2 22:19
 * @UpdateRemark: 更新说明
 */
public class CleverFish extends Drawable {

    private Paint mPaint;
    private Path mPath;
    private int OTHER_ALPHA = 110;
    private int BODY_ALPHA = 160;
    private float frequence = 1;

    //重心
    private PointF middlePoint;
    private PointF headPoint;
    private float originMainAngle = 90;

    /**
     * 鱼的长度值
     */
    //鱼头半径
    public final float HEAD_RADIUS = 30;
    //鱼身长度
    private final float BODY_LENGTH = HEAD_RADIUS * 3.2f;
    //鱼鳍的长度
    private final float RIGHT_FINS_LENGTH = HEAD_RADIUS * 1.3f;
    private final float FIND_FINS_POINT_LENGTH = HEAD_RADIUS * 0.9f;
    //节肢圆半径
    private final float BIG_CIRCLE_RADIUS = HEAD_RADIUS * 0.7f;
    private final float MID_CIRCLE_RADIUS = BIG_CIRCLE_RADIUS * 0.6f;
    private final float SMALL_CIRCLE_RADIUS = MID_CIRCLE_RADIUS * 0.4f;
    //寻找圆心的线长
    private final float FIND_MID_CIRCLE_LENGTH = BIG_CIRCLE_RADIUS * (1 + 0.6f);
    private final float FIND_SMALL_CIRCLE_LENGTH = MID_CIRCLE_RADIUS * (0.4f + 2.7f);
    private final float FIND_TRIANGLE_LENGTH = MID_CIRCLE_RADIUS * 2.7f;

    private float currentValue = 0;

    public CleverFish() {
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        //抗锯齿  圆角更光滑
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        //防抖动，颜色更连贯
        mPaint.setDither(true);
        //设置颜色
        mPaint.setColor(Color.argb(OTHER_ALPHA, 244, 92, 71));

        middlePoint = new PointF(4.19f * HEAD_RADIUS, 4.19f * HEAD_RADIUS);

        //设置属性动画  0 -360的是一个周期，设置360的倍数,利用sin实现周期变化
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 720f);
        valueAnimator.setDuration(3 * 1000);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        //重复的次数
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        //设置插值器，线性插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                currentValue = (float) valueAnimator.getAnimatedValue();
                invalidateSelf();
            }
        });
        valueAnimator.start();
    }

    @Override
    public void draw(Canvas canvas) {

        float headAngle = (float) (originMainAngle + Math.sin(Math.toRadians(currentValue * 1.2 * frequence)) * 10);
        //鱼头圆心坐标
        headPoint = calculatePoint(middlePoint, BODY_LENGTH / 2, headAngle);
        canvas.drawCircle(headPoint.x, headPoint.y, HEAD_RADIUS, mPaint);

        //右鱼鳍起点
        PointF finsRightStartPoint = calculatePoint(headPoint, FIND_FINS_POINT_LENGTH, headAngle - 110);
        drawRightFins(canvas, finsRightStartPoint, headAngle, true);
        //左鱼鳍起点
        PointF finsLeftStartPoint = calculatePoint(headPoint, FIND_FINS_POINT_LENGTH, headAngle + 110);
        drawRightFins(canvas, finsLeftStartPoint, headAngle, false);

        //节肢1
        PointF bodyBottomCenter = calculatePoint(headPoint, BODY_LENGTH, headAngle - 180);
        PointF triangleTop = drawSegment(canvas, bodyBottomCenter, BIG_CIRCLE_RADIUS, MID_CIRCLE_RADIUS, FIND_MID_CIRCLE_LENGTH, headAngle, true);

        //节肢2
        drawSegment(canvas, triangleTop, MID_CIRCLE_RADIUS, SMALL_CIRCLE_RADIUS, FIND_SMALL_CIRCLE_LENGTH, headAngle, false);

        //此处sin cos一样的
        float findEdgeLength = (float) (Math.cos(Math.toRadians(currentValue * 1.5 * frequence)) * BIG_CIRCLE_RADIUS);
        //画三角形
        drawTriangle(canvas, triangleTop, findEdgeLength, FIND_TRIANGLE_LENGTH, headAngle - 180);
        drawTriangle(canvas, triangleTop, findEdgeLength - 20, FIND_TRIANGLE_LENGTH - 10, headAngle - 180);

        //画身体
        drawBody(canvas, headPoint, bodyBottomCenter, headAngle);
    }

    private void drawBody(Canvas canvas, PointF headPoint, PointF bodyBottomCenter, float headAngle) {
        PointF headPointRight = calculatePoint(headPoint, HEAD_RADIUS, headAngle - 90);
        PointF headPointLeft = calculatePoint(headPoint, HEAD_RADIUS, headAngle + 90);
        PointF bodyBottomRight = calculatePoint(bodyBottomCenter, BIG_CIRCLE_RADIUS, headAngle - 90);
        PointF bodyBottomLeft = calculatePoint(bodyBottomCenter, BIG_CIRCLE_RADIUS, headAngle + 90);

        //计算控制点 决定鱼的胖瘦
        PointF rightControlPoint = calculatePoint(headPoint, BODY_LENGTH * 0.56f, headAngle - 130);
        PointF leftControlPoint = calculatePoint(headPoint, BODY_LENGTH * 0.56f, headAngle + 130);

        mPath.reset();
        mPath.moveTo(headPointRight.x, headPointRight.y);
        mPath.quadTo(rightControlPoint.x, rightControlPoint.y, bodyBottomRight.x, bodyBottomRight.y);
        mPath.lineTo(bodyBottomLeft.x, bodyBottomLeft.y);
        mPath.quadTo(leftControlPoint.x, leftControlPoint.y, headPointLeft.x, headPointLeft.y);
        mPaint.setAlpha(BODY_ALPHA);
        canvas.drawPath(mPath, mPaint);
    }

    private void drawTriangle(Canvas canvas, PointF startPoint, float triangleRadius, float length, float headAngle) {
        float segmentAngle = (float) (headAngle + Math.sin(Math.toRadians(currentValue * 1.5 * frequence)) * 35);
        //三角形底边中心
        PointF triangleCenter = calculatePoint(startPoint, length, segmentAngle);
        PointF triangleRight = calculatePoint(triangleCenter, triangleRadius, segmentAngle - 90);
        PointF triangleLeft = calculatePoint(triangleCenter, triangleRadius, segmentAngle + 90);

        //画三角形
        mPath.reset();
        mPath.moveTo(startPoint.x, startPoint.y);
        mPath.lineTo(triangleRight.x, triangleRight.y);
        mPath.lineTo(triangleLeft.x, triangleLeft.y);
        canvas.drawPath(mPath, mPaint);
    }

    private PointF drawSegment(Canvas canvas, PointF trapezoidBottomCenter, float bigRadius, float smallRadius, float findLength, float headAngle, boolean hasCircle) {
        float segmentAngle;
        if (hasCircle) {
            //节肢1
            //乘以2是改变频率
            segmentAngle = (float) (headAngle + Math.cos(Math.toRadians(currentValue * 1.5 * frequence)) * 15);
        } else {
            //节肢1
            segmentAngle = (float) (headAngle + Math.sin(Math.toRadians(currentValue * 1.5 * frequence)) * 35);
        }

        PointF trapezoidUpperCenter = calculatePoint(trapezoidBottomCenter, findLength, segmentAngle - 180);
        //梯形4个点
        PointF trapezoidBottomRight = calculatePoint(trapezoidBottomCenter, bigRadius, segmentAngle - 90);
        PointF trapezoidBottomLeft = calculatePoint(trapezoidBottomCenter, bigRadius, segmentAngle + 90);
        PointF trapezoidUpperRight = calculatePoint(trapezoidUpperCenter, smallRadius, segmentAngle - 90);
        PointF trapezoidUpperLeft = calculatePoint(trapezoidUpperCenter, smallRadius, segmentAngle + 90);

        //画圆
        if (hasCircle) {
            canvas.drawCircle(trapezoidBottomCenter.x, trapezoidBottomCenter.y, bigRadius, mPaint);
        }
        canvas.drawCircle(trapezoidUpperCenter.x, trapezoidUpperCenter.y, smallRadius, mPaint);

        //画梯形
        mPath.reset();
        mPath.moveTo(trapezoidBottomLeft.x, trapezoidBottomLeft.y);
        mPath.lineTo(trapezoidBottomRight.x, trapezoidBottomRight.y);
        mPath.lineTo(trapezoidUpperRight.x, trapezoidUpperRight.y);
        mPath.lineTo(trapezoidUpperLeft.x, trapezoidUpperLeft.y);
        canvas.drawPath(mPath, mPaint);
        return trapezoidUpperCenter;
    }

    private void drawRightFins(Canvas canvas, PointF finsStartPoint, float headAngle, boolean isRight) {
        //贝塞尔曲线控制角度，可以自己试
        float controlAngle = 115;
        //减去180的原因是点在开始点左边
        float changLength = (float) Math.sin(Math.toRadians(currentValue * 1.5 * frequence)) * 50f + RIGHT_FINS_LENGTH * 1.5f;

        PointF finsEndPoint = calculatePoint(finsStartPoint, RIGHT_FINS_LENGTH, headAngle - 180);
        PointF controlPoint = calculatePoint(finsStartPoint,changLength ,
                isRight ? headAngle - controlAngle : headAngle + controlAngle);
//        PointF controlPoint = calculatePoint(finsStartPoint, RIGHT_FINS_LENGTH * 1.8f,
//                isRight ? headAngle - controlAngle : headAngle + controlAngle);

        //绘制鱼鳍
        mPath.reset();
        mPath.moveTo(finsStartPoint.x, finsStartPoint.y);
        //二阶贝塞尔曲线方法
        mPath.quadTo(controlPoint.x, controlPoint.y, finsEndPoint.x, finsEndPoint.y);
        canvas.drawPath(mPath, mPaint);
    }

    public PointF calculatePoint(PointF startPoint, float length, float angle) {
        //X坐标计算
        float x = (float) Math.cos(Math.toRadians(angle)) * length;
        //Y坐标计算
        float y = (float) Math.sin(Math.toRadians(angle - 180)) * length;
        return new PointF(startPoint.x + x, startPoint.y + y);
    }

    @Override
    public void setAlpha(int i) {
        mPaint.setAlpha(i);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    //PixelFormat.TRANSLUCENT绘制的地方不透明，其他地方透明
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    //Drawable的宽高
    @Override
    public int getIntrinsicWidth() {
        return (int) (2 * 4.19f * HEAD_RADIUS);
    }

    //Drawable的宽高
    @Override
    public int getIntrinsicHeight() {
        return (int) (2 * 4.19f * HEAD_RADIUS);
    }

    public PointF getMiddlePoint() {
        return middlePoint;
    }

    public PointF getHeadPoint(){
        return headPoint;
    }

    public void setFrequence(float frequence) {
        this.frequence = frequence;
    }

    public void setOriginMainAngle(float originMainAngle) {
        this.originMainAngle = originMainAngle;
    }
}
