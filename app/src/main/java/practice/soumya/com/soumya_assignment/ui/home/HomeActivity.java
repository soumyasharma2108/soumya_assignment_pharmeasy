package practice.soumya.com.soumya_assignment.ui.home;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import practice.soumya.com.soumya_assignment.BaseApp;
import practice.soumya.com.soumya_assignment.R;
import practice.soumya.com.soumya_assignment.models.Data;
import practice.soumya.com.soumya_assignment.networking.NetworkError;
import practice.soumya.com.soumya_assignment.networking.Service;
import practice.soumya.com.soumya_assignment.utils.AppLogger;
import practice.soumya.com.soumya_assignment.utils.NetworkUtils;
import practice.soumya.com.soumya_assignment.utils.uiutility.PaginationAdapterCallback;
import practice.soumya.com.soumya_assignment.utils.uiutility.PaginationScrollListener;

public class HomeActivity extends BaseApp implements HomeView, PaginationAdapterCallback {

    HomeAdapter adapter;
    @Inject
    public Service service;

    private static final String TAG = HomeActivity.class.getSimpleName();
    HomePresenter presenter;
    LinearLayoutManager linearLayoutManager;
    @BindView(R.id.main_recycler)
    RecyclerView rv;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    @BindView(R.id.main_progress)
    ProgressBar progressBar;

    @OnClick(R.id.error_btn_retry)
    public void onRetry() {

        presenter.loadPage(currentPage);

    }


    @BindView(R.id.error_txt_cause)
    TextView txtError;


    private static final int PAGE_START = 1;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    public int TOTAL_PAGES;
    private int currentPage = PAGE_START;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderView();
        getAppComponent().inject(this);
        presenter = new HomePresenter(service, this);
        setup();
    }

    public void renderView() {
        setContentView(R.layout.activity_main);
        // bind the view using butterknife
        ButterKnife.bind(this);

    }


    @Override
    public void showWait() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void removeWait() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String appErrorMessage) {
        AppLogger.e(TAG,appErrorMessage);
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            txtError.setText(appErrorMessage);
            removeWait();
        }
    }


    @Override
    public void showMoreData(List<Data> results) {
        errorLayout.setVisibility(View.GONE);
        isLoading = false;
        adapter.removeLoadingFooter();
        adapter.addAll(results);
        if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;
    }

    @Override
    public void showData(List<Data> results) {
        errorLayout.setVisibility(View.GONE);
        removeWait();
        isLoading = false;
        adapter.addAll(results);
        if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;
    }

    @Override
    public void setTotalPage(Integer totalPages) {
        TOTAL_PAGES = totalPages;
    }

    @Override
    public void retryPageLoad() {
        presenter.loadPage(currentPage);
    }


    private void setup() {
        //init service and load data
        adapter = new HomeAdapter(this);

        linearLayoutManager = new LinearLayoutManager
                (this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);
        showWait();
        if (NetworkUtils.isNetworkConnected(this)) {

            presenter.loadPage(currentPage);
        } else {
            onFailure(NetworkError.NETWORK_ERROR_MESSAGE);
        }
        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
               AppLogger.d(TAG,"currentPage:"+currentPage);
                isLoading = true;
                currentPage += 1;
                presenter.loadPage(currentPage);


            }

            @Override
            public int getTotalPageCount() {
                AppLogger.d(TAG,"TOTAL_PAGES:"+TOTAL_PAGES);
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
    }
}
