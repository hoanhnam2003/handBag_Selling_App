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

public class LoginFragment extends Fragment implements NetworkChangeReceiver.OnNetworkChangeListener {
    private EditText editTenDangNhap, editMatKhau;
    private ImageView imageEye;
    private TextView txtQuenMatKhau, txtDangKy;
    private Button btnDangNhap;
    private ProgressBar progressBar;
    private NetworkChangeReceiver networkChangeReceiver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        editTenDangNhap = view.findViewById(R.id.edit_tenDangNhap);
        editMatKhau = view.findViewById(R.id.edit_matKhau);
        imageEye = view.findViewById(R.id.eye_icon);
        txtQuenMatKhau = view.findViewById(R.id.forgot);
        txtDangKy = view.findViewById(R.id.txt_dangKy);
        progressBar = view.findViewById(R.id.progress_bar);
        btnDangNhap = view.findViewById(R.id.login);

        // Initialize and register NetworkChangeReceiver
        networkChangeReceiver = new NetworkChangeReceiver(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        requireActivity().registerReceiver(networkChangeReceiver, filter);

        // Set OnClickListener for the "Đăng ký" text view
        txtDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check network availability before navigating
                if (isNetworkAvailable()) {
                    navigateToRegisterFragment();
                }
            }
        });

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra kết nối mạng
                if (!isNetworkAvailable()) {
                    return; // Ngăn không cho thực hiện tiếp nếu không có kết nối
                }

                // Lấy tên đăng nhập và mật khẩu
                String tenDangNhap = editTenDangNhap.getText().toString();
                String matKhau = editMatKhau.getText().toString();

                // Kiểm tra thông tin đăng nhập
                if (tenDangNhap.equals("0349732600") && matKhau.equals("Nam20022003@")) {
                    Toast.makeText(getContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    navigateToHomeFragment();
                } else {
                    Toast.makeText(getContext(), "Tên đăng nhập hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        imageEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editMatKhau.getTransformationMethod() instanceof android.text.method.PasswordTransformationMethod) {
                    editMatKhau.setTransformationMethod(null);
                    imageEye.setImageResource(R.drawable.eye);
                } else {
                    editMatKhau.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                    imageEye.setImageResource(R.drawable.mdi_light_eye_off);
                }
                editMatKhau.setSelection(editMatKhau.getText().length());
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

    // Navigate to RegisterFragment
    private void navigateToRegisterFragment() {
        Fragment registerFragment = new RegisterFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, registerFragment);
        transaction.addToBackStack(null); // Optional: add to back stack
        transaction.commit();
    }

    // Navigate to RegisterFragment
    private void navigateToHomeFragment() {
        Fragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, homeFragment);
        transaction.addToBackStack(null); // Optional: add to back stack
        transaction.commit();
    }

    // Check network availability
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
