package com.example.gangzhang.car;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CarControlFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CarControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarControlFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ImageButton mStartButton;
    private ImageButton mFindButtton;
    private ImageButton mJoinButton;
    private ImageButton mSetAlarmButton;
    //true:need start false:need stop
    private boolean mStartButtonPressed;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarControlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarControlFragment newInstance(String param1, String param2) {
        CarControlFragment fragment = new CarControlFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CarControlFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.test1, container, false);
        mStartButton = (ImageButton)view.findViewById(R.id.start_car);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartButtonPressed = !mStartButtonPressed;
                mStartButton.setSelected(mStartButtonPressed);
                //TODO :start the car

            }
        });
        mStartButton.setSelected(false);
        mStartButtonPressed = false;

        mFindButtton = (ImageButton)view.findViewById(R.id.find_car);
        mFindButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:start map of car
                Intent it = new Intent(getActivity(),FindMyCarActivity.class);

                startActivity(it);
            }
        });



        mSetAlarmButton = (ImageButton)view.findViewById(R.id.set_alarm_car);
        mSetAlarmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO: join setalarm
            }
        });

        ActionBar actionBar = getActivity().getActionBar();
        if(actionBar !=null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            View actionbarLayout = LayoutInflater.from(getActivity()).inflate(R.layout.actionbar_cheche, new FrameLayout(getActivity()),false);
            actionBar.setCustomView(actionbarLayout);
        }
        return view;


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
