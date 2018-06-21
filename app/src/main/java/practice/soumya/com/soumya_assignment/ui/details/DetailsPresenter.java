package practice.soumya.com.soumya_assignment.ui.details;

import practice.soumya.com.soumya_assignment.models.Result;
import practice.soumya.com.soumya_assignment.networking.Constants;
import practice.soumya.com.soumya_assignment.networking.NetworkError;
import practice.soumya.com.soumya_assignment.networking.Service;
import practice.soumya.com.soumya_assignment.ui.home.HomeView;
import practice.soumya.com.soumya_assignment.utils.AppLogger;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class DetailsPresenter  {
    private final Service service;
    private final DetailsView view;
    private CompositeSubscription subscriptions;

    public DetailsPresenter(Service service, DetailsView view) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
    }

    public void doConnect() {
     //add logic for Connection for other person
        view.showToast(Constants.WIPMessage);

    }
    public void doValidate(String email){
        boolean valid = true;

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.notValidate("enter a valid email address");
            valid = false;
        } else {
            view.validate();
        }
    }
    public void onStop() {
        subscriptions.unsubscribe();
    }


}
