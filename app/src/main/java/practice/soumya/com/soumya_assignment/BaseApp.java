package practice.soumya.com.soumya_assignment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import practice.soumya.com.soumya_assignment.di.DaggerAppComponent;

import practice.soumya.com.soumya_assignment.di.AppComponent;
import practice.soumya.com.soumya_assignment.networking.NetworkModule;

public class BaseApp  extends AppCompatActivity {
    AppComponent appComponent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File cacheFile = new File(getCacheDir(), "responses");
        appComponent = DaggerAppComponent.builder().networkModule(new NetworkModule(cacheFile)).build();

    }

    public  AppComponent getAppComponent() {
        return appComponent;
    }
}

