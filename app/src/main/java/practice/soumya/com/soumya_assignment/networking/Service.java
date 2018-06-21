package practice.soumya.com.soumya_assignment.networking;


import practice.soumya.com.soumya_assignment.models.Result;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Service {
    private final NetworkService networkService;

    public Service(NetworkService networkService) {
        this.networkService = networkService;
    }

    public Subscription loadPage(final GetResponseCallback callback, int page) {


        return networkService.getTopResults(page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Result>>() {
                    @Override
                    public Observable<? extends Result> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(Result cityListResponse) {
                        callback.onSuccess(cityListResponse);

                    }
                });
    }


    public interface GetResponseCallback{
        void onSuccess(Result cityListResponse);

        void onError(NetworkError networkError);
    }
}
