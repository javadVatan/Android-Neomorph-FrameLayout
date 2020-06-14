package com.chinodev.androidneomorphframelayout;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

public class NeomorphFrameLayout extends FrameLayout {
    //attributes
    private String SHAPE_STYLE;
    private String SHAPE_TYPE;
    private String SHADOW_TYPE;
    private int CORNER_RADIUS;
    private int ELEVATION, PADDING_RIGHT, PADDING_TOP, PADDING_BOTTOM, PADDING_LEFT;
    private int STROKE_WIDTH;
    private int HIGHLIGHT_COLOR;
    private int SHADOW_COLOR;
    private int BACKGROUND_COLOR;
    private int SELECTED_COLOR;
    private int LAYER_TYPE;
    private boolean SHADOW_VISIBLE;
    private int SELECTED_PADDING;

    //global variables
    /*   private int SHAPE_PADDING = 0;*/

    //constants
    private final String SHAPE_STYLE_STORK = "1";
    private final String SHAPE_STYLE_FILL = "2";

    private final String SHAPE_TYPE_RECTANGLE = "1";
    private final String SHAPE_TYPE_CIRCLE = "2";
    private final String SHADOW_TYPE_OUTER = "1";
    private final String SHADOW_TYPE_INNER = "2";
    //global objects
    private Paint basePaint;
    private Paint paintShadow;
    private Paint paintHighLight;
    private Paint paintSelected;
    private Path basePath;
    private Path pathShadow;
    private Path pathHighlight;
    private Path pathSelected;

    private RectF rectangle;
    private RectF rectangleShadow;


    public NeomorphFrameLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public NeomorphFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public NeomorphFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        getAttrs(context, attrs);
        initPaints();
        rectangle = new RectF(PADDING_LEFT, PADDING_TOP, this.getWidth() - PADDING_RIGHT, this.getHeight() - PADDING_LEFT);
        rectangleShadow  = new RectF(PADDING_LEFT, PADDING_TOP, (this.getWidth()/2) - PADDING_RIGHT, (this.getHeight()/2) - PADDING_LEFT);
    }

    public void getAttrs(Context context, AttributeSet attrs) {
        int defaultElevation = (int) context.getResources().getDimension(R.dimen.neomorph_view_elevation);
        int defaultStroke = (int) context.getResources().getDimension(R.dimen.neomorph_view_stroke);
        int defaultCornerRadius = (int) context.getResources().getDimension(R.dimen.neomorph_view_corner_radius);
        int defaultSelectedPadding = (int) context.getResources().getDimension(R.dimen.neomorph_view_selected_padding);

        if (attrs != null) {
            //get attrs array
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NeomorphFrameLayout);
            //get all attributes
            SHAPE_TYPE = a.getString(R.styleable.NeomorphFrameLayout_neomorph_view_type);
            if (SHAPE_TYPE == null) {
                SHAPE_TYPE = SHAPE_TYPE_RECTANGLE;
            }

            SHADOW_TYPE = a.getString(R.styleable.NeomorphFrameLayout_neomorph_shadow_type);
            if (SHADOW_TYPE == null) {
                SHADOW_TYPE = SHADOW_TYPE_OUTER;
            }

            SHAPE_STYLE = a.getString(R.styleable.NeomorphFrameLayout_neomorph_style);
            if (SHAPE_STYLE == null) {
                SHAPE_STYLE = SHAPE_STYLE_FILL;
            }

            ELEVATION = a.getDimensionPixelSize(R.styleable.NeomorphFrameLayout_neomorph_elevation, defaultElevation);

            PADDING_LEFT = a.getDimensionPixelSize(R.styleable.NeomorphFrameLayout_neomorph_padding_left, ELEVATION * 2);
            PADDING_BOTTOM = a.getDimensionPixelSize(R.styleable.NeomorphFrameLayout_neomorph_padding_bottom, ELEVATION * 2);
            PADDING_TOP = a.getDimensionPixelSize(R.styleable.NeomorphFrameLayout_neomorph_padding_top, ELEVATION * 2);
            PADDING_RIGHT = a.getDimensionPixelSize(R.styleable.NeomorphFrameLayout_neomorph_padding_right, ELEVATION * 2);

            SELECTED_PADDING = a.getDimensionPixelSize(R.styleable.NeomorphFrameLayout_neomorph_selected_padding, defaultSelectedPadding);

            STROKE_WIDTH = a.getDimensionPixelSize(R.styleable.NeomorphFrameLayout_neomorph_stroke_width, defaultStroke);
            CORNER_RADIUS = a.getDimensionPixelSize(R.styleable.NeomorphFrameLayout_neomorph_corner_radius, defaultCornerRadius);
            BACKGROUND_COLOR = a.getColor(R.styleable.NeomorphFrameLayout_neomorph_background_color,
                    ContextCompat.getColor(context, R.color.neomorph_background_color));
            SELECTED_COLOR = a.getColor(R.styleable.NeomorphFrameLayout_neomorph_selected_color, -1);
            SHADOW_COLOR = a.getColor(R.styleable.NeomorphFrameLayout_neomorph_shadow_color,
                    ContextCompat.getColor(context, R.color.neomorph_shadow_color));
            HIGHLIGHT_COLOR = a.getColor(R.styleable.NeomorphFrameLayout_neomorph_highlight_color,
                    ContextCompat.getColor(context, R.color.neomorph_highlight_color));
            BACKGROUND_COLOR = a.getColor(R.styleable.NeomorphFrameLayout_neomorph_background_color,
                    ContextCompat.getColor(context, R.color.neomorph_background_color));
            SHADOW_VISIBLE = a.getBoolean(R.styleable.NeomorphFrameLayout_neomorph_shadow_visible, true);
            String layerType = a.getString(R.styleable.NeomorphFrameLayout_neomorph_layer_type);

            if (layerType == null || layerType.equals("1")) {
                LAYER_TYPE = View.LAYER_TYPE_SOFTWARE; //SW by default
            } else LAYER_TYPE = View.LAYER_TYPE_HARDWARE;

            a.recycle();
        } else {
            SHAPE_TYPE = "rectangle";
            ELEVATION = defaultElevation;
            CORNER_RADIUS = defaultCornerRadius;
            BACKGROUND_COLOR = ContextCompat.getColor(context, R.color.neomorph_background_color);
            SHADOW_COLOR = ContextCompat.getColor(context, R.color.neomorph_shadow_color);
            HIGHLIGHT_COLOR = ContextCompat.getColor(context, R.color.neomorph_highlight_color);
            LAYER_TYPE = View.LAYER_TYPE_SOFTWARE;
            SHADOW_VISIBLE = true;
            SHADOW_TYPE = SHADOW_TYPE_OUTER;
        }
    }

    private void initPaints() {
        basePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintShadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintHighLight = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintSelected = new Paint(Paint.ANTI_ALIAS_FLAG);

        basePaint.setColor(BACKGROUND_COLOR);
        paintShadow.setColor(BACKGROUND_COLOR);
        paintHighLight.setColor(BACKGROUND_COLOR);

        if (SELECTED_COLOR != -1)
            paintSelected.setColor(SELECTED_COLOR);

        switch (SHAPE_STYLE) {
            case SHAPE_STYLE_FILL:
                basePaint.setStyle(Paint.Style.FILL);
                paintHighLight.setStyle(Paint.Style.FILL);
                paintShadow.setStyle(Paint.Style.FILL);
                paintSelected.setStyle(Paint.Style.FILL);
                break;

            case SHAPE_STYLE_STORK:
                basePaint.setStrokeWidth(STROKE_WIDTH);
                paintHighLight.setStrokeWidth(STROKE_WIDTH);
                paintShadow.setStrokeWidth(STROKE_WIDTH);
                basePaint.setStyle(Paint.Style.STROKE);
                paintHighLight.setStyle(Paint.Style.STROKE);
                paintShadow.setStyle(Paint.Style.STROKE);
                paintSelected.setStyle(Paint.Style.STROKE);
                break;
        }

        if (SHADOW_VISIBLE) {
            paintShadow.setShadowLayer(ELEVATION, ELEVATION, ELEVATION, SHADOW_COLOR);
            paintHighLight.setShadowLayer(ELEVATION, -ELEVATION, -ELEVATION, HIGHLIGHT_COLOR);
        }

        basePath = new Path();
        pathHighlight = new Path();
        pathShadow = new Path();
        pathSelected = new Path();

        setWillNotDraw(false);
        setLayerType(LAYER_TYPE, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectangle = new RectF(PADDING_LEFT, PADDING_TOP, this.getWidth() - PADDING_RIGHT, this.getHeight() - PADDING_BOTTOM);
        rectangleShadow  = new RectF(PADDING_LEFT, PADDING_TOP, (this.getWidth()/2) - PADDING_RIGHT, (this.getHeight()/2) - PADDING_LEFT);

        resetPath(w, h);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setPadding(PADDING_LEFT, PADDING_TOP, PADDING_RIGHT, PADDING_BOTTOM);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (SHADOW_TYPE) {
            case SHADOW_TYPE_INNER:
                canvas.clipPath(basePath);
                break;
            default:
            case SHADOW_TYPE_OUTER:
                break;
        }

        if (SHADOW_VISIBLE) {
            paintShadow.setAlpha(155);
            paintHighLight.setAlpha(155);
        } else {
            paintShadow.setAlpha(0);
            paintHighLight.setAlpha(0);
        }

        canvas.drawPath(basePath, basePaint);
        canvas.drawPath(pathShadow, paintShadow);
        canvas.drawPath(pathHighlight, paintHighLight);
        if (SELECTED_COLOR != -1)
            canvas.drawPath(pathSelected, paintSelected);
    }

    private void resetPath(int w, int h) {
        basePath.reset();
        pathHighlight.reset();
        pathShadow.reset();
        pathSelected.reset();
        int biggestPadding = Math.max(PADDING_LEFT, Math.max(PADDING_RIGHT, Math.max(PADDING_BOTTOM, PADDING_TOP)));

        switch (SHAPE_TYPE) {
            case SHAPE_TYPE_CIRCLE:
                //get max suitable diameter, which is the smallest dimension
                int maxDiameter = this.getWidth() < this.getHeight() ? this.getWidth() : this.getHeight();
                int radius = (maxDiameter / 2) - biggestPadding;
                basePath.addCircle(w >> 1, h >> 1, radius, Path.Direction.CW);
                pathHighlight.addCircle(w >> 1, h >> 1, radius, Path.Direction.CW);
                pathShadow.addCircle(w >> 1, h >> 1, radius, Path.Direction.CW);
                pathSelected.addCircle(w >> 1, h >> 1, radius - SELECTED_PADDING, Path.Direction.CW);
                break;
            default:
            case SHAPE_TYPE_RECTANGLE:
                basePath.addRoundRect(rectangle, CORNER_RADIUS, CORNER_RADIUS, Path.Direction.CW);
                pathHighlight.addRoundRect(rectangle, CORNER_RADIUS, CORNER_RADIUS, Path.Direction.CW);
                pathShadow.addRoundRect(rectangle, CORNER_RADIUS, CORNER_RADIUS, Path.Direction.CW);

                RectF selectedReact = new RectF(PADDING_LEFT + SELECTED_PADDING, PADDING_TOP + SELECTED_PADDING,
                        this.getWidth() - PADDING_RIGHT - SELECTED_PADDING, this.getHeight() - PADDING_BOTTOM - SELECTED_PADDING);
                pathSelected.addRoundRect(selectedReact, CORNER_RADIUS, CORNER_RADIUS, Path.Direction.CW);
                break;
        }

        if (SHADOW_TYPE.equals(SHADOW_TYPE_INNER)) {
            if (!pathHighlight.isInverseFillType()) {
                pathHighlight.toggleInverseFillType();
            }
            if (!pathShadow.isInverseFillType()) {
                pathShadow.toggleInverseFillType();
            }
        }

        basePath.close();
        pathHighlight.close();
        pathShadow.close();
        pathSelected.close();
    }

    public void setShadowInner() {
        SHADOW_VISIBLE = true;
        SHADOW_TYPE = SHADOW_TYPE_INNER;
        initPaints();
        resetPath(getWidth(), getHeight());
        invalidate();
    }

    public void setShadowOuter() {
        SHADOW_VISIBLE = true;
        SHADOW_TYPE = SHADOW_TYPE_OUTER;
        initPaints();
        resetPath(getWidth(), getHeight());
        invalidate();
    }

    public void switchShadowType() {
        SHADOW_VISIBLE = true;

        if (SHADOW_TYPE.equals(SHADOW_TYPE_INNER)) {
            SHADOW_TYPE = SHADOW_TYPE_OUTER;
        } else SHADOW_TYPE = SHADOW_TYPE_INNER;

        initPaints();
        resetPath(getWidth(), getHeight());
        invalidate();
    }

    public void setShadowNone() {
        SHADOW_VISIBLE = false;
        initPaints();
        resetPath(getWidth(), getHeight());
        invalidate();
    }

    public void setSelectedColor(@ColorInt int color) {
        SELECTED_COLOR = color;
        initPaints();
        resetPath(getWidth(), getHeight());
        invalidate();
    }

    public void setSelectedNone() {
        SELECTED_COLOR =-1;
        initPaints();
        resetPath(getWidth(), getHeight());
        invalidate();
    }

}