package com.lynlab.lucid.fragment;

import android.content.Intent;
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
import com.lynlab.lucid.activity.ApplicationDetailActivity;
import com.lynlab.lucid.api.LucidApiManager;
import com.lynlab.lucid.model.Application;
import com.lynlab.lucid.util.ApplicationUtil;

import org.parceler.Parcels;

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
                    List<Application> applications = response.body();
                    List<ApplicationViewModel> items = new ArrayList<>(applications.size());

                    for (Application application : applications) {
                        State state;
                        if (ApplicationUtil.isApplicationInstalled(getContext(), application.getPackageName())) {
                            state = State.INSTALLED;
                        } else {
                            state = State.NOT_INSTALLED;
                        }

                        items.add(new ApplicationViewModel(application, state));
                    }

                    adapter.setItems(items);
                    adapter.notifyDataSetChanged();
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<Application>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private class ApplicationAdapter extends RecyclerView.Adapter<ApplicationViewHolder> {

        private List<ApplicationViewModel> items;

        public ApplicationAdapter() {
            items = new ArrayList<>();
        }

        @Override
        public ApplicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_application, parent, false);
            return new ApplicationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ApplicationViewHolder holder, int position) {
            holder.bind(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void setItems(List<ApplicationViewModel> items) {
            this.items = items;
        }
    }


    private class ApplicationViewModel {
        Application application;
        State state;

        public ApplicationViewModel(Application application, State state) {
            this.application = application;
            this.state = state;
        }

        public Application getApplication() {
            return application;
        }

        public State getState() {
            return state;
        }
    }

    private class ApplicationViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView nameTextView;
        TextView packageTextView;
        TextView statusTextView;

        public ApplicationViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            nameTextView = (TextView) itemView.findViewById(R.id.item_application_name);
            packageTextView = (TextView) itemView.findViewById(R.id.item_application_package);
            statusTextView = (TextView) itemView.findViewById(R.id.item_application_status);
        }

        public void bind(ApplicationViewModel item) {
            Application application = item.getApplication();

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(getContext(), ApplicationDetailActivity.class);
                intent.putExtra(ApplicationDetailActivity.KEY_EXTRA_APPLICATION, Parcels.wrap(application));
                startActivity(intent);
            });
            nameTextView.setText(application.getName());
            packageTextView.setText(application.getPackageName());

            String stateText;
            switch (item.getState()) {
                case INSTALLED:
                    stateText = getString(R.string.application_state_installed);
                    break;

                case NOT_INSTALLED:
                default:
                    stateText = getString(R.string.application_state_not_installed);
                    break;
            }

            statusTextView.setText(stateText);
        }
    }
}
