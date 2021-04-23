package com.example.kojimachi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.Objects;

import com.example.kojimachi.R;

public class AdapterViewPageLogo extends PagerAdapter {

    private int[] imgLogo;
    private final LayoutInflater layoutInflater;
    private int currentPosition = 0;

    public AdapterViewPageLogo(Context context) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateListLogo(int[] imgLogo) {
        this.imgLogo = imgLogo;
        notifyDataSetChanged();
    }

    public int getCountImgLogo() {
        return imgLogo.length;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (object);
    }

    public Object instantiateItem(@NonNull ViewGroup container,  int position) {
        View itemView = layoutInflater.inflate(R.layout.item_viewpage_login, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imgViewPageLogin);
        if (currentPosition >= getCountImgLogo()){
            currentPosition = 0;
        }
        if (currentPosition < imgLogo.length) {
            imageView.setImageResource(imgLogo[currentPosition]);
            currentPosition++;
            Objects.requireNonNull(container).addView(itemView);
        }
        return itemView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
