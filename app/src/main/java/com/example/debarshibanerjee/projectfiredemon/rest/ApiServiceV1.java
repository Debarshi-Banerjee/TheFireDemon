package com.example.debarshibanerjee.projectfiredemon.rest;

import com.example.debarshibanerjee.projectfiredemon.pojo.Contributor;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by debarshibanerjee on 28/09/17.
 */

interface ApiServiceV1 {
    @GET("repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> repoContributors(
            @Path("owner") String owner,
            @Path("repo") String repo);

    @GET("repos/{owner}/{repo}/contributors")
    Single<List<Contributor>> repoContributorsRx(
            @Path("owner") String owner,
            @Path("repo") String repo
    );


    @GET("repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> repoContribuorsRxObs(
                @Path("owner") String owner,
                @Path("repo") String repo
    );
}
