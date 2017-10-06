package com.example.debarshibanerjee.projectfiredemon.callbacks;

import com.example.debarshibanerjee.projectfiredemon.events.BaseEvent;
import com.example.debarshibanerjee.projectfiredemon.events.TestEvent;
import com.example.debarshibanerjee.projectfiredemon.pojo.Contributor;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by debarshibanerjee on 30/09/17.
 */

public class TestCallback extends BaseCallback<List<Contributor>> {
    public TestCallback() {
        super(TestEvent.class);
    }

    @Override
    protected void onSuccess(Call<List<Contributor>> call, Response<List<Contributor>> response, List<Contributor> json) {
        TestEvent event=new TestEvent(BaseEvent.ErrorType.NO_ERROR);
        event.setContributors(json);

        for(Contributor c:json){
            c.save();
        }
        postEvent(event);
    }
}
