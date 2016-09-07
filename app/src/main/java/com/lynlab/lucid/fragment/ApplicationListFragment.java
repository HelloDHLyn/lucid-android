package com.lynlab.lucid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lynlab.lucid.R;
import com.lynlab.lucid.api.LucidApiManager;
import com.lynlab.lucid.model.Application;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author lyn
 * @since 2016/09/03
 */
public class ApplicationListFragment extends Fragment {

    /**
     * 애플리케이션 설치 상태 enum
     */
    private enum State {
        NOT_INSTALLED,      // 미설치
        NOT_LATEST,         // 설치되었으나 최신 버전 아님
        INSTALLED,          // 최신 버전 설치됨
    }

    private ApplicationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_application_list, container, false);

        adapter = new ApplicationAdapter();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_application_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        requestData();

        return view;
    }

    /**
     * 애플리케이션 데이터를 불러온다.
     */
    private void requestData() {
        LucidApiManager apiManager = LucidApiManager.getInstance(getContext());

        Call<List<Application>> call = apiManager.getService().getApplications();
        call.enqueue(new Callback<List<Application>>() {
            @Override
            public void onResponse(Call<List<Application>> call, Response<List<Application>> response) {
                if (response.isSuccessful()) {
                    adapter.setApplications(response.body());
                    adapter.notifyDataSetChanged();
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<Application>> call, Throwable t) {

            }
        });
    }


    private class ApplicationAdapter extends RecyclerView.Adapter<ApplicationViewHolder> {

        private List<Application> applications;

        public ApplicationAdapter() {
            applications = new ArrayList<>();
        }

        @Override
        public ApplicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_application, parent, false);
            return new ApplicationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ApplicationViewHolder holder, int position) {
            holder.bind(applications.get(position));
            holder.statusTextView.setText("미설치");
        }

        @Override
        public int getItemCount() {
            return applications.size();
        }

        public void setApplications(List<Application> applications) {
            this.applications = applications;
        }
    }

    private class ApplicationViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView packageTextView;
        TextView statusTextView;

        public ApplicationViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.item_application_name);
            packageTextView = (TextView) itemView.findViewById(R.id.item_application_package);
            statusTextView = (TextView) itemView.findViewById(R.id.item_application_status);
        }

        public void bind(Application application) {
            nameTextView.setText(application.getName());
            packageTextView.setText(application.getPackageName());
        }
    }
}
