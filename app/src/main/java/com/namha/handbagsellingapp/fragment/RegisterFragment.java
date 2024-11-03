package com.namha.handbagsellingapp.fragment;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.namha.handbagsellingapp.R;
import com.namha.handbagsellingapp.receiver.NetworkChangeReceiver;

public class RegisterFragment extends Fragment implements NetworkChangeReceiver.OnNetworkChangeListener {
    private EditText editTenDangNhap, editMatKhau, editNhaplaiMatKhau, editDiaChi, editTenDayDu;
    private ImageView imageEye;
    private TextView txtQuenMatKhau, txtDangNhap;
    private Button btnDangNhap;
    private ProgressBar progressBar;
    private NetworkChangeReceiver networkChangeReceiver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_resigner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTenDangNhap = view.findViewById(R.id.edit_tenDangNhap);
        editMatKhau = view.findViewById(R.id.edit_matKhau);
        editNhaplaiMatKhau = view.findViewById(R.id.edit_nhapLaiMatKhau);
        editDiaChi = view.findViewById(R.id.edit_diaChi);
        editTenDayDu = view.findViewById(R.id.edit_tenDayDu);
        imageEye = view.findViewById(R.id.eye_icon);
        txtQuenMatKhau = view.findViewById(R.id.forgot);
        txtDangNhap = view.findViewById(R.id.txt_dangNhap);
        progressBar = view.findViewById(R.id.progress_bar);
        btnDangNhap = view.findViewById(R.id.btn_Register);

        // Initialize and register NetworkChangeReceiver
        networkChangeReceiver = new NetworkChangeReceiver(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        requireActivity().registerReceiver(networkChangeReceiver, filter);

        // Set OnClickListener for the "Đăng nhập" text view
        txtDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check network availability before navigating
                if (isNetworkAvailable()) {
                    navigateToLoginFragment();
                } else {
                }
            }
        });
    }
    @Override
    public void onNetworkChange(boolean isConnected) {
        if (isConnected) {
            hideProgressBar();
        } else {
            showProgressBar();
            Toast.makeText(getContext(), "Mất kết nối mạng. Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
        }
    }

    // Hiển thị ProgressBar
    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    // Ẩn ProgressBar
    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister the BroadcastReceiver when the view is destroyed
        requireActivity().unregisterReceiver(networkChangeReceiver);
    }

    // Navigate to loginFragment
    private void navigateToLoginFragment() {
        Fragment loginFragment = new LoginFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, loginFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Check network availability
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
