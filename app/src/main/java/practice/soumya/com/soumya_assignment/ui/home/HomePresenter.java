package practice.soumya.com.soumya_assignment.ui.home;


import practice.soumya.com.soumya_assignment.models.Result;
import practice.soumya.com.soumya_assignment.networking.NetworkError;
import practice.soumya.com.soumya_assignment.networking.Service;
import practice.soumya.com.soumya_assignment.utils.AppLogger;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class HomePresenter {
    private final Service service;
    private final HomeView view;
    private CompositeSubscription subscriptions;

    public HomePresenter(Service service, HomeView view) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
    }

    public void loadPage(final int currentPage) {

        Subscription subscription = service.loadPage(new Service.GetResponseCallback() {
            @Override
            public void onSuccess(Result result) {
                view.removeWait();
                view.setTotalPage(result.getTotalPages());
                if(currentPage==1)view.showData(result.getData());
                else view.showMoreData(result.getData());
            }

            @Override
            public void onError(NetworkError networkError) {
                AppLogger.e("NetworkError",networkError.getMessage());
                view.removeWait();
                view.onFailure(networkError.getAppErrorMessage());
            }

        },currentPage);

        subscriptions.add(subscription);
    }
    public void onStop() {
        subscriptions.unsubscribe();
    }


}
