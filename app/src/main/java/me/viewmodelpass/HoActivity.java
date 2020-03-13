package me.viewmodelpass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import me.viewmodelpass.databinding.ActivityHoBinding;

public class HoActivity extends AppCompatActivity {

    ActivityHoBinding binding;
    public static final String TAG = "HoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_ho);

    }

}
