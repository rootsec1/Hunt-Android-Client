package io.github.abhishekwl.huntclient.Activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import butterknife.BindColor;
import butterknife.BindFont;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.abhishekwl.huntclient.Adapters.DepartmentNamesMainRecyclerViewAdapter;
import io.github.abhishekwl.huntclient.Adapters.MainViewPagerAdapter;
import io.github.abhishekwl.huntclient.Helpers.ApiClient;
import io.github.abhishekwl.huntclient.Helpers.ApiInterface;
import io.github.abhishekwl.huntclient.Models.Customer;
import io.github.abhishekwl.huntclient.R;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.logoImageView)
    ImageView logoImageView;
    @BindView(R.id.logoTextView)
    TextView logoTextView;
    @BindView(R.id.mainDepartmentsRecyclerView)
    RecyclerView mainDepartmentsRecyclerView;
    @BindView(R.id.mainDepartmentsSortImageView)
    ImageView mainDepartmentsSortImageView;
    @BindView(R.id.mainProfileImageView)
    ImageView mainProfileImageView;
    @BindView(R.id.mainWishListImageView)
    ImageView mainWishListImageView;
    @BindView(R.id.mainSearchImageView)
    ImageView mainSearchImageView;
    @BindView(R.id.mainCartImageView)
    ImageView mainCartImageView;
    @BindView(R.id.mainBottomCardView)
    CardView mainBottomCardView;
    @BindView(R.id.mainProfilePictureImageView)
    ImageView mainProfilePictureImageView;
    @BindView(R.id.mainViewPager)
    ViewPager mainViewPager;
    @BindFont(R.font.fredoka_one_regular)
    Typeface fredokaOneTypeface;
    @BindColor(android.R.color.black)
    int colorTextBlack;

    private Unbinder unbinder;
    private FirebaseAuth firebaseAuth;
    private ApiInterface apiInterface;
    private Customer currentCustomer;
    private MainViewPagerAdapter mainViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(MainActivity.this);
        initializeComponents();
        initializeViews();
    }

    private void initializeComponents() {
        firebaseAuth = FirebaseAuth.getInstance();
        apiInterface = ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);
        currentCustomer = getIntent().getParcelableExtra("CUSTOMER");
    }

    private void initializeViews() {
        Glide.with(getApplicationContext()).load(R.drawable.price_tag_black).into(logoImageView);
        Glide.with(getApplicationContext()).load(Objects.requireNonNull(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getPhotoUrl()).toString()).into(mainProfilePictureImageView);
        setupDepartmentsRecyclerView();
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(),getResources().getStringArray(R.array.departments));
        mainViewPager.setAdapter(mainViewPagerAdapter);
    }

    private void setupDepartmentsRecyclerView() {
        mainDepartmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        mainDepartmentsRecyclerView.setHasFixedSize(true);
        mainDepartmentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mainDepartmentsRecyclerView.setAdapter(new DepartmentNamesMainRecyclerViewAdapter(getResources().getStringArray(R.array.departments), getApplicationContext()));
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
