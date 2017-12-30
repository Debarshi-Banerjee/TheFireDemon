package com.example.debarshibanerjee.projectfiredemon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.debarshibanerjee.projectfiredemon.adapters.SimpleRecyclerViewAdapter;
import com.example.debarshibanerjee.projectfiredemon.events.TestEvent;
import com.example.debarshibanerjee.projectfiredemon.helpers.BetterLinearLayoutManager;
import com.example.debarshibanerjee.projectfiredemon.pojo.Contributor;
import com.example.debarshibanerjee.projectfiredemon.rest.RestClientV1;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_test)
    TextView mTextView;
    @BindView(R.id.bt_test)
    Button mButton;
    @BindView(R.id.bt_test_database)
    Button mButtonDatabase;
    @BindView(R.id.bt_test_clear)
    Button mButtonClear;
    @BindView(R.id.rrs)
    RecyclerView mRecylerView;
    @BindView(R.id.bt_rx)
    Button mButtonRx;


    SimpleRecyclerViewAdapter mSimpleResAdapter;

    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mRecylerView.setLayoutManager(new BetterLinearLayoutManager(this));
        mSimpleResAdapter = new SimpleRecyclerViewAdapter();
        mRecylerView.setAdapter(mSimpleResAdapter);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestClientV1.getInstance().getGitHubRepoContributors("square", "retrofit");
            }
        });
        mButtonRx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestClientV1.getInstance().foobar("square", "retrofit", new Consumer<List<Contributor>>() {
                    @Override
                    public void accept(List<Contributor> contributors) throws Exception {
                        mSimpleResAdapter.swapData(contributors);
                    }
                });

            }
        });
        mButtonDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Contributor> contributorList = SQLite.select().from(Contributor.class).queryList();
                if (!contributorList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Made by DBFlow", Toast.LENGTH_SHORT).show();

                }
                mSimpleResAdapter.swapData(contributorList);

            }
        });
        mButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSimpleResAdapter.swapData(new ArrayList<Contributor>());
            }
        });
    }

    @Override
    protected void onDestroy() {
        mCompositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTestEvent(TestEvent event) {
        if (event.isSuccess()) {
            List<Contributor> contributors = event.getContributors();
            mSimpleResAdapter.swapData(contributors);
        } else {
            Toast.makeText(this, String.valueOf(event.getHttpStatus()), Toast.LENGTH_LONG).show();
        }

    }
}
