//Retrofit with tmdb
//https://www.youtube.com/watch?v=F9e7qvubo8Y
//Pagination
//https://androidwave.com/pagination-in-recyclerview/

package com.example.mymovieapp.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.mymovieapp.ApiInterface;
import com.example.mymovieapp.Helpers.MoviesAdapter;
import com.example.mymovieapp.Helpers.PaginationListener;
import com.example.mymovieapp.Models.MovieResults;
import com.example.mymovieapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;
import static com.example.mymovieapp.Helpers.PaginationListener.PAGE_START;


public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{
    public static String BASE_URL = "https://api.themoviedb.org";
    public static  int PAGE = 1;
    public static String API_KEY = "78cf14f404a2a390e49c26846d6ec63d";
    public static String LANGUAGE ="en-US";
    public static  String CATEGORY = "popular";

    RecyclerView mRecyclerView;
    MoviesAdapter mMoviesAdapter;
    List<MovieResults.Result> listOfMovies;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;


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
        //makes the options appear in Toolbar
        setHasOptionsMenu(true);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.search_bar, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) searchViewItem.getActionView();
        //search(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                //mMoviesAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void initView(View view)
    {
        //setup RecyclerView
        mRecyclerView = view.findViewById(R.id.rv_movieList);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mMoviesAdapter = new MoviesAdapter(getContext(),new ArrayList<MovieResults.Result>());
        mRecyclerView.setAdapter(mMoviesAdapter);
        doApiCall();

        //add scroll listener while user reach in bottom load more will call
        mRecyclerView.addOnScrollListener(new PaginationListener(layoutManager)
        {
            @Override
            protected void loadMoreItems()
            {
                isLoading = true;
                currentPage++;
                doApiCall();
            }
            @Override
            public boolean isLastPage()
            {
                return isLastPage;
            }
            @Override
            public boolean isLoading()
            {
                return isLoading;
            }
        });

    }
    private void doApiCall()
    {
        listOfMovies = new ArrayList<>();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //retrofit with TMDB
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                //instaciate interface
                ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                //create a call, which executes asynchronously
                Call<MovieResults> call = apiInterface.listOfMovies(CATEGORY,API_KEY,LANGUAGE,currentPage);

                call.enqueue(new Callback<MovieResults>()
                {
                    @Override
                    public void onResponse(Call<MovieResults> call, Response<MovieResults> response)
                    {
                        //parse results
                        MovieResults results = response.body();
                        listOfMovies = results.getResults();
                        //mMoviesAdapter.addItems(listOfMovies);
                        itemCount = listOfMovies.size();
                        //mMoviesAdapter.notifyDataSetChanged(); //already called in the addItems method
                        //mRecyclerView.setAdapter(mMoviesAdapter); // Ha nem frissiti a recycler view-ot

                        //manage progress view
                        if (currentPage != PAGE_START) mMoviesAdapter.removeLoading();
                        mMoviesAdapter.addItems(listOfMovies);
                        swipeRefreshLayout.setRefreshing(false);

                        // check weather is last page or not
                        if (currentPage < totalPage)
                        {
                            mMoviesAdapter.addLoading();
                        }
                        else
                        {
                            isLastPage = true;
                        }
                        isLoading = false;
                    }

                    @Override
                    public void onFailure(Call<MovieResults> call, Throwable t)
                    {
                        t.printStackTrace();
                    }
                });
            }
        }, 1500);
    }

    @Override
    public void onRefresh()
    {
        itemCount = 0;
        currentPage = PAGE_START;
        mMoviesAdapter.clear();
        doApiCall();

        //TODO refresh icon does not disappear
        swipeRefreshLayout.setRefreshing(false);
        /*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                if(swipeRefreshLayout.isRefreshing())
                {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, 1000);*/

    }

}
