package com.lynlab.lucid.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.lynlab.lucid.R;
import com.lynlab.lucid.api.LucidApiManager;
import com.lynlab.lucid.databinding.ActivityApplicationDetailBinding;
import com.lynlab.lucid.model.Application;
import com.lynlab.lucid.model.Release;

import org.parceler.Parcels;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 애플리케이션 세부 정보를 볼 수 있는 액티비티.
 *
 * @author lyn
 * @since 2016/09/09
 */
public class ApplicationDetailActivity extends AppCompatActivity {

    public static final String KEY_EXTRA_APPLICATION = "extra_application";

    private Application application;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityApplicationDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_application_detail);
        application = Parcels.unwrap(getIntent().getParcelableExtra(KEY_EXTRA_APPLICATION));
        binding.setApplication(application);

        requestData();
    }

    private void requestData() {
        LucidApiManager apiManager = LucidApiManager.getInstance(this);
        Call<List<Release>> call = apiManager.getService().getReleasesOfPackage(application.getPackageName());
        call.enqueue(new Callback<List<Release>>() {
            @Override
            public void onResponse(Call<List<Release>> call, Response<List<Release>> response) {
                Log.d("TEST", String.valueOf(response.body().size()));
            }

            @Override
            public void onFailure(Call<List<Release>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private class ReleaseViewModel {

    }

    private class ReleaseViewHolder extends RecyclerView.ViewHolder {

        public ReleaseViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(ReleaseViewModel item) {

        }
    }
}
