package io.github.abhishekwl.huntclient.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.abhishekwl.huntclient.Helpers.ApiClient;
import io.github.abhishekwl.huntclient.Helpers.ApiInterface;
import io.github.abhishekwl.huntclient.Models.Customer;
import io.github.abhishekwl.huntclient.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.splashLogoImageView)
    ImageView splashLogoImageView;
    @BindString(R.string.web_client_id) String webClientId;

    private static final int RC_SIGN_IN = 512;
    private Unbinder unbinder;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        setContentView(R.layout.activity_splash);

        unbinder = ButterKnife.bind(SplashActivity.this);
        initializeComponents();
        initializeViews();
    }

    private void initializeComponents() {
        firebaseAuth = FirebaseAuth.getInstance();
        apiInterface = ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestProfile()
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), googleSignInOptions);
        if (firebaseAuth.getCurrentUser()==null) signIn();
        else fetchCurrentCustomer();
    }

    private void initializeViews() {
        Glide.with(getApplicationContext()).load(R.drawable.price_tag_white).into(splashLogoImageView);
    }

    private void fetchCurrentCustomer() {
        apiInterface.getCustomer(firebaseAuth.getUid()).enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(@NonNull Call<Customer> call, @NonNull Response<Customer> response) {
                if (response.body()==null) createNewCustomer();
                else navigateToNextActivity(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Customer> call, @NonNull Throwable t) {
                createNewCustomer();
            }
        });
    }

    private void createNewCustomer() {
        apiInterface.createCustomer(firebaseAuth.getUid(), Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName(), firebaseAuth.getCurrentUser().getEmail(), Objects.requireNonNull(firebaseAuth.getCurrentUser().getPhotoUrl()).toString())
                .enqueue(new Callback<Customer>() {
                    @Override
                    public void onResponse(@NonNull Call<Customer> call, @NonNull Response<Customer> response) {
                        if (response.body()==null) notifyMessage("There was a problem creating your account, please try again.");
                        else navigateToNextActivity(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<Customer> call, @NonNull Throwable t) {
                        notifyMessage(t.getMessage());
                    }
                });
    }

    private void navigateToNextActivity(Customer customer) {
        Intent mainActivityIntent = new Intent(SplashActivity.this, MainActivity.class);
        mainActivityIntent.putExtra("CUSTOMER", customer);
        startActivity(mainActivityIntent);
        finish();
    }

    private void signIn() {
        startActivityForResult(googleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (Exception e) {
                notifyMessage(e.getMessage());
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount googleSignInAccount) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(googleSignInAccount.getDisplayName())
                        .setPhotoUri(googleSignInAccount.getPhotoUrl())
                        .build();
                Objects.requireNonNull(firebaseAuth.getCurrentUser()).updateProfile(userProfileChangeRequest).addOnSuccessListener(aVoid -> {
                  fetchCurrentCustomer();
                }).addOnFailureListener(e -> notifyMessage(e.getMessage()));
            } else notifyMessage(Objects.requireNonNull(task.getException()).getMessage());
        });
    }

    private void notifyMessage(String message) {
        Snackbar.make(splashLogoImageView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", v -> signIn()).setActionTextColor(Color.YELLOW)
                .show();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
