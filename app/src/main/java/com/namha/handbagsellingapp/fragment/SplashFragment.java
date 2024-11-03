package com.namha.handbagsellingapp.fragment;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.namha.handbagsellingapp.R;
import com.namha.handbagsellingapp.receiver.NetworkChangeReceiver;

public class SplashFragment extends Fragment implements NetworkChangeReceiver.OnNetworkChangeListener {
    private static final int DELAY = 2000; // 2 seconds delay
    private ImageView logoImageView;
    private ProgressBar progressBar;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logoImageView = view.findViewById(R.id.logo);
        progressBar = view.findViewById(R.id.progress_bar);

        // Kiểm tra kết nối mạng ban đầu
        if (isNetworkAvailable()) {
            navigateToMainFragment();
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }

        // Khởi tạo và đăng ký BroadcastReceiver
        networkChangeReceiver = new NetworkChangeReceiver(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        requireActivity().registerReceiver(networkChangeReceiver, filter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Hủy đăng ký BroadcastReceiver khi view bị hủy
        requireActivity().unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        if (isConnected) {
            progressBar.setVisibility(View.GONE);
            navigateToMainFragment();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Mất kết nối mạng. Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
        }
    }

    // Hàm chuyển đến LoginFragment
    private void navigateToMainFragment() {
        Fragment loginFragment = new LoginFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, loginFragment);
        transaction.commit();
    }

    // Kiểm tra kết nối mạng
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
