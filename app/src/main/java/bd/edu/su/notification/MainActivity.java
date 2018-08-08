package bd.edu.su.notification;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = findViewById(R.id.tabLayout_id);
        mViewPager = findViewById(R.id.viewPager_id);
        mViewPager.setOffscreenPageLimit(2);
        mAuth = FirebaseAuth.getInstance();

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        mViewPagerAdapter.addFragment(new ProfileFragment(),"Profile");
        mViewPagerAdapter.addFragment(new UsersFragment(),"Users");
        mViewPagerAdapter.addFragment(new NotificationFragment(),"Notifications");

        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.getTabAt(0).setIcon(R.drawable.account);
        mTabLayout.getTabAt(1).setIcon(R.drawable.account_group);
        mTabLayout.getTabAt(2).setIcon(R.drawable.bell);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if(firebaseUser == null){
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }
    }
}
