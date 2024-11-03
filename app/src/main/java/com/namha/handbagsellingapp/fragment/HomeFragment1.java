package com.namha.handbagsellingapp.fragment;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.namha.handbagsellingapp.R;
import com.namha.handbagsellingapp.adapter.homePagerAdapter;
import com.namha.handbagsellingapp.receiver.NetworkChangeReceiver;

import java.util.Arrays;
import java.util.List;

public class HomeFragment1 extends Fragment implements NetworkChangeReceiver.OnNetworkChangeListener {
    private static final long DELAY_MS = 2000; // Thời gian delay giữa các slide
    private ViewPager2 viewPager;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int currentPage = 0;
    private boolean isNetworkConnected = true; // Để theo dõi trạng thái mạng

    private List<TextView> indicatorDots;
    private NetworkChangeReceiver networkChangeReceiver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Tìm ViewPager2 và thiết lập adapter
        viewPager = view.findViewById(R.id.banner);
        List<Integer> imageList = Arrays.asList(
                R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner4
        );

        // Khởi tạo adapter với listener
        homePagerAdapter adapter = new homePagerAdapter(requireContext(), imageList, position -> {
            updateIndicator(position); // Cập nhật indicator khi click vào item
        });
        viewPager.setAdapter(adapter);

        // Tìm các TextView indicator từ layout của fragment
        indicatorDots = Arrays.asList(
                view.findViewById(R.id.dot1),
                view.findViewById(R.id.dot2),
                view.findViewById(R.id.dot3),
                view.findViewById(R.id.dot4)
        );

        // Thiết lập sự kiện chuyển trang
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateIndicator(position); // Cập nhật indicator khi trang được chọn
            }
        });

        // Khởi tạo BroadcastReceiver để theo dõi trạng thái mạng
        networkChangeReceiver = new NetworkChangeReceiver(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        requireContext().registerReceiver(networkChangeReceiver, filter);

        // Bắt đầu tự động cuộn banner
        startAutoScroll();
    }

    private void startAutoScroll() {
        stopAutoScroll(); // Dừng mọi cuộn trước đó để tránh lặp lại Runnable

        runnable = new Runnable() {
            @Override
            public void run() {
                if (isNetworkConnected && viewPager != null && viewPager.getAdapter() != null) {
                    currentPage = (currentPage + 1) % viewPager.getAdapter().getItemCount();
                    viewPager.setCurrentItem(currentPage, true);
                    handler.postDelayed(this, DELAY_MS); // Sử dụng DELAY_MS để tạo độ trễ ổn định
                }
            }
        };
        handler.postDelayed(runnable, DELAY_MS); // Đặt lại Runnable với thời gian trì hoãn
    }

    private void stopAutoScroll() {
        handler.removeCallbacks(runnable);
    }

    private void updateIndicator(int position) {
        // Đặt tất cả các chấm về màu "không chọn"
        for (TextView dot : indicatorDots) {
            dot.setBackgroundResource(R.drawable.ellipse_2);
        }
        // Đặt chấm tại vị trí hiện tại về màu "được chọn"
        if (position < indicatorDots.size()) {
            indicatorDots.get(position).setBackgroundResource(R.drawable.ellipse_1);
        }
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        isNetworkConnected = isConnected;
        if (isConnected) {
            if (viewPager != null) {
                currentPage = viewPager.getCurrentItem();
            }
            startAutoScroll(); // Khởi động lại cuộn khi có kết nối mạng
        } else {
            stopAutoScroll(); // Dừng cuộn khi mất mạng
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable); // Ngăn rò rỉ bộ nhớ
        requireContext().unregisterReceiver(networkChangeReceiver);
    }
}
