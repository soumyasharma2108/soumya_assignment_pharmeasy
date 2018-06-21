package practice.soumya.com.soumya_assignment.ui.details;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import practice.soumya.com.soumya_assignment.BaseApp;
import practice.soumya.com.soumya_assignment.R;
import practice.soumya.com.soumya_assignment.di.AppComponent;
import practice.soumya.com.soumya_assignment.models.Data;
import practice.soumya.com.soumya_assignment.networking.Constants;
import practice.soumya.com.soumya_assignment.networking.Service;

public class DetailsActivity extends BaseApp implements DetailsView {
    private static final String TAG = DetailsActivity.class.getSimpleName();
    private static final int REQUEST_SIGNUP = 0;
    Data data;
    DetailsPresenter detailsPresenter;
    @Inject
    public Service service;
    @BindView(R.id.input_email)
    EditText emailText;
    @BindView(R.id.avatar)
    ImageView imgAvtar;
    @BindView(R.id.name)
    TextView name;

    @OnClick(R.id.btn_connect)
    public void connect() {
        detailsPresenter.doConnect();
    }

    @OnClick(R.id.btn_sendemail)
    public void sendEmail() {
        detailsPresenter.doValidate(emailText.getText().toString());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderView();
        detailsPresenter = new DetailsPresenter(service, this);


    }

    private void renderView() {
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        getAppComponent().inject(this);
        data = (Data)  Parcels.unwrap(getIntent().getExtras().getParcelable(Constants.Data));
        name.setText(data.getFirstName() + " " + data.getLastName());
        Glide.with(this).load(data.getAvatar()).into(imgAvtar);


    }


    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }


    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void notValidate(String enter_a_valid_email_address) {
        showToast(getResources().getString(R.string.invalid_email));
    }

    @Override
    public void validate() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ emailText.getText().toString()});
        email.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.hello)+" "+name.getText().toString());
        email.putExtra(Intent.EXTRA_TEXT,getResources().getString(R.string.lets_connect) );

        //need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose Email client :"));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        finish();


    }
}
