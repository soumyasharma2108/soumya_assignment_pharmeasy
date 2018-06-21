package practice.soumya.com.soumya_assignment.di;

import javax.inject.Singleton;

import dagger.Component;
import practice.soumya.com.soumya_assignment.networking.NetworkModule;
import practice.soumya.com.soumya_assignment.ui.details.DetailsActivity;
import practice.soumya.com.soumya_assignment.ui.home.HomeActivity;


@Singleton
@Component(modules = {NetworkModule.class})
public interface AppComponent {
    void inject(HomeActivity mainActivity);

    void inject(DetailsActivity detailsActivity);
}
