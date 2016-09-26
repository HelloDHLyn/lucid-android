package com.lynlab.lucid.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.lynlab.lucid.R;
import com.lynlab.lucid.api.LucidApiManager;
import com.lynlab.lucid.async.DownloadFromUrlTask;
import com.lynlab.lucid.databinding.ActivityApplicationDetailBinding;
import com.lynlab.lucid.model.Application;
import com.lynlab.lucid.model.InstallState;
import com.lynlab.lucid.model.Release;
import com.lynlab.lucid.util.ApplicationUtil;

import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private ReleaseAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityApplicationDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_application_detail);
        application = Parcels.unwrap(getIntent().getParcelableExtra(KEY_EXTRA_APPLICATION));
        binding.setApplication(application);

        adapter = new ReleaseAdapter();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_application_detail_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        requestData();
    }

    private void requestData() {
        LucidApiManager apiManager = LucidApiManager.getInstance(this);
        Call<List<Release>> call = apiManager.getService().getReleasesOfPackage(application.getPackageName());
        call.enqueue(new Callback<List<Release>>() {
            @Override
            public void onResponse(Call<List<Release>> call, Response<List<Release>> response) {
                if (!response.isSuccessful()) {
                    // TODO: Do something...
                } else {
                    adapter.setItems(Stream.of(response.body()).map(ReleaseViewModel::new).collect(Collectors.toList()));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Release>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private class ReleaseAdapter extends RecyclerView.Adapter<ReleaseViewHolder> {

        private List<ReleaseViewModel> items;

        public ReleaseAdapter() {
            items = new ArrayList<>();
        }

        @Override
        public ReleaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ApplicationDetailActivity.this).inflate(R.layout.item_release, parent, false);
            return new ReleaseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ReleaseViewHolder holder, int position) {
            holder.bind(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void setItems(List<ReleaseViewModel> items) {
            this.items = items;
        }
    }

    private class ReleaseViewModel {
        int versionCode;
        String versionName;
        boolean isLatest;
        boolean isRelease;
        String path;
        Date date;
        InstallState state;

        public ReleaseViewModel(Release release) {
            this.versionCode = release.getVersionCode();
            this.versionName = release.getVerisonName();
            this.isLatest = release.isLatest();
            this.isRelease = release.isRelease();
            this.path = release.getPath();
            this.date = new Date();

            try {
                if (ApplicationUtil.checkInstalledVersion(ApplicationDetailActivity.this,
                        application.getPackageName(), versionName, versionCode)) {
                    this.state = InstallState.INSTALLED;
                } else {
                    this.state = InstallState.NOT_LATEST;
                }
            } catch (PackageManager.NameNotFoundException e) {
                this.state = InstallState.NOT_INSTALLED;
            }
        }

        public void installApkFromPath() {
            DownloadFromUrlTask task = new DownloadFromUrlTask(ApplicationDetailActivity.this, path);
            task.setOnDownload(this::onDownload);
            task.execute();
        }

        private void onDownload(String filePath) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private class ReleaseViewHolder extends RecyclerView.ViewHolder {

        TextView versionTextView;
        TextView dateTextView;

        TextView installTextView;
        TextView installedTextView;

        public ReleaseViewHolder(View itemView) {
            super(itemView);
            versionTextView = (TextView) itemView.findViewById(R.id.item_release_version);
            dateTextView = (TextView) itemView.findViewById(R.id.item_release_date);

            installTextView = (TextView) itemView.findViewById(R.id.item_release_install);
            installedTextView = (TextView) itemView.findViewById(R.id.item_release_installed);
        }

        public void bind(ReleaseViewModel item) {
            versionTextView.setText(String.format(Locale.KOREA, "%s (%d)", item.versionName, item.versionCode));
            dateTextView.setText(DateFormat.getDateFormat(ApplicationDetailActivity.this).format(item.date));

            if (item.state == InstallState.INSTALLED) {
                installedTextView.setVisibility(View.VISIBLE);
            } else {
                installTextView.setVisibility(View.VISIBLE);
                installTextView.setOnClickListener(view -> item.installApkFromPath());
            }
        }
    }
}
