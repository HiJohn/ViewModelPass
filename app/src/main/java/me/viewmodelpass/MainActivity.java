package me.viewmodelpass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import me.viewmodelpass.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    MePagerAdapter pagerAdapter;


    MeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MeViewModel.class);

        viewModel.content.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.topTitle.setText(s);
            }
        });

        pagerAdapter = new MePagerAdapter(getSupportFragmentManager());
        binding.vp.setAdapter(pagerAdapter);
        binding.tabs.setupWithViewPager(binding.vp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void toHo(MenuItem item) {
        startActivity(new Intent(this,HoActivity.class));
    }
}
