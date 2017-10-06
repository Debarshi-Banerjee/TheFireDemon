package com.example.debarshibanerjee.projectfiredemon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.debarshibanerjee.projectfiredemon.events.TestEvent;
import com.example.debarshibanerjee.projectfiredemon.pojo.Contributor;
import com.example.debarshibanerjee.projectfiredemon.rest.RestClientV1;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_test)
    TextView mTextView;
    @BindView(R.id.bt_test)
    Button mButton;
    @BindView(R.id.bt_test_database)
    Button mButtonDatabase;
    @BindView(R.id.bt_test_clear)
    Button mButtonClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestClientV1.getInstance().getGitHubRepoContributors("square", "retrofit");
            }
        });
        mButtonDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Contributor> contributorList = SQLite.select().from(Contributor.class).queryList();
                if (!contributorList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Made by DBFlow", Toast.LENGTH_SHORT).show();

                    StringBuffer s = new StringBuffer();
                    for (Contributor contributor : contributorList) {
                        s.append(contributor.getLogin()).append(" ").append(contributor.getContributions()).append("\n");
                    }
                    mTextView.setText(s.toString());
                }
            }
        });
        mButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextView.setText("");
            }
        });
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
            StringBuffer s = new StringBuffer();
            for (Contributor contributor : contributors) {
                s.append(contributor.getLogin()).append(" ").append(contributor.getContributions()).append("\n");
            }
            mTextView.setText(s.toString());
        } else {
            Toast.makeText(this, String.valueOf(event.getHttpStatus()), Toast.LENGTH_LONG).show();
        }

    }
}
