package com.example.mymovieapp.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymovieapp.R;

public class CinemaFragment extends Fragment
{

    public CinemaFragment()
    {
        // Required empty public constructor
    }

    public static CinemaFragment newInstance(String param1, String param2)
    {
        CinemaFragment fragment = new CinemaFragment();
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
        if (getArguments() != null)
        {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View retView = inflater.inflate(R.layout.fragment_cinema, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Cinema");
        return retView;
    }

}
