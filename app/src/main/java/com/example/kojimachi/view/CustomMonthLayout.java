package jp.co.kojimachi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import jp.co.kojimachi.R;
import jp.co.kojimachi.constant.AppConstants;

public class CustomMonthLayout extends FrameLayout {
    public CustomMonthLayout(Context context) {
        super(context);
        init(context, null);
    }

    public CustomMonthLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private Context context;

    public interface OnArrowClickListener {
        void onClickPreMonth(Calendar calendar);

        void onClickNextMonth(Calendar calendar);
    }

    public interface OnArrowDayClickListener {
        void onClickPreDay(Calendar calendar);

        void onClickNextDay(Calendar calendar);
    }

    private OnArrowClickListener onArrowClickListener;
    private OnArrowDayClickListener onArrowDayClickListener;

    public void setOnArrowClickListener(OnArrowClickListener onArrowClickListener) {
        this.onArrowClickListener = onArrowClickListener;
    }

    public void setOnArrowDayClickListener(OnArrowDayClickListener onArrowDayClickListener) {
        this.onArrowDayClickListener = onArrowDayClickListener;
    }

    private ImageView imgArrowLeft;
    private ImageView imgArrowRight;
    private TextView tvScheduledTime;
    private String tvCalendar;
    private Calendar tmpCalendar;
    private Calendar minCalendar;
    private Calendar maxCalendar;
    private SimpleDateFormat formatDate = new SimpleDateFormat(AppConstants.DATE_FORMAT_ARROW, new Locale("ja"));
    private SimpleDateFormat formatDay = new SimpleDateFormat(AppConstants.DATE_FORMAT_ARROW_DAY, new Locale("ja"));
    private boolean isDay = false;
    private long mLastClickTime = 0;

    public void setMinCalendar(Calendar minCalendar) {
        this.minCalendar = minCalendar;
        checkMinMax();
    }

    public Calendar getTmpCalendar() {
        return tmpCalendar;
    }

    public void setMaxCalendar(Calendar maxCalendar) {
        this.maxCalendar = maxCalendar;
        checkMinMax();
    }

    public void checkMinMax() {
        try {
            if (minCalendar != null) {
                if (minCalendar.getTime().after(tmpCalendar.getTime())) {
                    disableArrow(true);
                } else enableArrow(true);
            }

            if (maxCalendar != null) {
                if (maxCalendar.getTime().before(tmpCalendar.getTime())) {
                    disableArrow(false);
                } else {
                    enableArrow(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enableArrow(boolean isLeft) {
        if (isLeft) {
            imgArrowLeft.setImageResource(R.drawable.ic_left_green_arrow);
            imgArrowLeft.setEnabled(true);
        } else {
            imgArrowRight.setImageResource(R.drawable.ic_right_green_arrow);
            imgArrowRight.setEnabled(true);
        }
    }

    private void disableArrow(boolean isLeft) {
        if (isLeft) {
            imgArrowLeft.setImageResource(R.drawable.ic_left_gray_arrow);
            imgArrowLeft.setEnabled(false);
        } else {
            imgArrowRight.setImageResource(R.drawable.ic_right_gray_arrow);
            imgArrowRight.setEnabled(false);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.custom_layout_month_year, this);
        tvScheduledTime = view.findViewById(R.id.tvScheduledTime);
        imgArrowLeft = view.findViewById(R.id.imgArrowLeft);
        imgArrowRight = view.findViewById(R.id.imgArrowRight);
        tmpCalendar = Calendar.getInstance();
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomMonthLayout);
            isDay = typedArray.getBoolean(R.styleable.CustomMonthLayout_isDay, false);
            typedArray.recycle();
        }
        if (isDay) {
            tvCalendar = formatDay.format(tmpCalendar.getTime());
            tvScheduledTime.setText(tvCalendar);

            imgArrowLeft.setOnClickListener(v -> {
                notForDoubleClick();
                preDay();
            });
            imgArrowRight.setOnClickListener(v -> {
                notForDoubleClick();
                nextDay();
            });
        } else {
            tvCalendar = formatDate.format(tmpCalendar.getTime());
            tvScheduledTime.setText(tvCalendar);
            imgArrowLeft.setOnClickListener(v -> {
                notForDoubleClick();
                preMonth();
            });
            imgArrowRight.setOnClickListener(v -> {
                notForDoubleClick();
                nextMonth();
            });
        }
    }

    private void notForDoubleClick() {
        if (System.currentTimeMillis() - mLastClickTime < 350) {
            return;
        }
        mLastClickTime = System.currentTimeMillis();
    }

    private void preMonth() {
        try {
            tmpCalendar.set(Calendar.MONTH, tmpCalendar.get(Calendar.MONTH) - 1);
            checkMinMax();
            tvCalendar = formatDate.format(tmpCalendar.getTime());
            tvScheduledTime.setText(tvCalendar);
            onArrowClickListener.onClickPreMonth(tmpCalendar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void nextMonth() {
        try {
            tmpCalendar.set(Calendar.MONTH, tmpCalendar.get(Calendar.MONTH) + 1);
            checkMinMax();
            tvCalendar = formatDate.format(tmpCalendar.getTime());
            tvScheduledTime.setText(tvCalendar);
            onArrowClickListener.onClickNextMonth(tmpCalendar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void preDay() {
        try {
            tmpCalendar.set(Calendar.DAY_OF_MONTH, tmpCalendar.get(Calendar.DAY_OF_MONTH) - 1);
            checkMinMax();
            tvCalendar = formatDay.format(tmpCalendar.getTime());
            tvScheduledTime.setText(tvCalendar);
            onArrowDayClickListener.onClickPreDay(tmpCalendar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void nextDay() {
        try {
            tmpCalendar.set(Calendar.DAY_OF_MONTH, tmpCalendar.get(Calendar.DAY_OF_MONTH) + 1);
            checkMinMax();
            tvCalendar = formatDay.format(tmpCalendar.getTime());
            tvScheduledTime.setText(tvCalendar);
            onArrowDayClickListener.onClickNextDay(tmpCalendar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
