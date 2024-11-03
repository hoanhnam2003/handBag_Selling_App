package com.namha.handbagsellingapp.fragment;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.namha.handbagsellingapp.R;
import com.namha.handbagsellingapp.receiver.NetworkChangeReceiver;

public class HomeFragment extends Fragment implements NetworkChangeReceiver.OnNetworkChangeListener {
    private NetworkChangeReceiver networkChangeReceiver;
    private boolean isNetworkConnected = true; // Biến để theo dõi trạng thái mạng

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Thêm HomeFragment1 vào layout của HomeFragment
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.flHomeContainer, new HomeFragment1());
        transaction.commit();

        // Đăng ký BroadcastReceiver để theo dõi thay đổi mạng
        networkChangeReceiver = new NetworkChangeReceiver(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        requireContext().registerReceiver(networkChangeReceiver, filter);
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        isNetworkConnected = isConnected;

        if (isConnected) {
            // Nếu có mạng, hiển thị thông báo và bật menu
            enableMenu(true);
        } else {
            // Nếu mất mạng, hiển thị thông báo và vô hiệu hóa menu
            enableMenu(false);
            Toast.makeText(requireContext(), "Mất kết nối mạng", Toast.LENGTH_LONG).show();
        }
    }

    private void enableMenu(boolean enable) {
        // Logic để vô hiệu hóa hoặc bật menu
        // Ví dụ, nếu có menu trong Activity chứa Fragment này:
        if (getActivity() != null) {
            getActivity().findViewById(R.id.icMenuBottom).setEnabled(enable); // 'menu' là ID của view menu
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireContext().unregisterReceiver(networkChangeReceiver); // Hủy đăng ký để tránh rò rỉ bộ nhớ
    }
}
