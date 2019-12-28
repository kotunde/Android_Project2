package com.example.mymovieapp.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymovieapp.ApiInterface;
import com.example.mymovieapp.Models.MovieResults;
import com.example.mymovieapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment
{
    public static String BASE_URL = "https://api.themoviedb.org";
    public static  int PAGE = 1;
    public static String API_KEY = "78cf14f404a2a390e49c26846d6ec63d";
    public static String LANGUAGE ="en-US";
    public static  String CATEGORY = "popular";

    public HomeFragment()
    {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2)
    {
        HomeFragment fragment = new HomeFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View retView = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home");
        initView(retView);
        return retView;
    }

    public void initView(View view)
    {
        //retrofit with TMDB
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<MovieResults> call = apiInterface.listOfMovies(CATEGORY,API_KEY,LANGUAGE,PAGE);

        call.enqueue(new Callback<MovieResults>()
        {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response)
            {
                MovieResults results = response.body();
                List<MovieResults.Result> listOfMovies = results.getResults();
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t)
            {
                t.printStackTrace();
            }
        });

    }


}
