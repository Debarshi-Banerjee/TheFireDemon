package com.example.debarshibanerjee.projectfiredemon.events;

import com.example.debarshibanerjee.projectfiredemon.pojo.Contributor;

import java.util.List;

import retrofit2.Response;

/**
 * Created by debarshibanerjee on 30/09/17.
 */

public class TestEvent extends BaseEvent {
    List<Contributor> contributors;

    public List<Contributor> getContributors() {
        return contributors;
    }

    public void setContributors(List<Contributor> contributors) {
        this.contributors = contributors;
    }

    public TestEvent(Response response) {
        super(response);
    }

    public TestEvent(ErrorType errorType) {
        super(errorType);
    }

    public TestEvent(ErrorType errorType, int appErrorCode) {
        super(errorType, appErrorCode);
    }
}
