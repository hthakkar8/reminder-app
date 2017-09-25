package com.example.suyog.locationtracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Hardik on 9/9/2017.
 */

public class ReminderListFragment extends Fragment
{

    private ReminderAdapter mAdapter;
    private RecyclerView mReminderRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_reminder_list, container, false);

        mReminderRecyclerView = (RecyclerView) view.findViewById(R.id.reminder_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        mReminderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateUI();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private class ReminderHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView mCaptionTextView;
        private TextView mTimeTextView;
        private TextView mLocationTextView;
        private TextView mExactTime;
        private Reminder mReminder;


        public ReminderHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.list_reminder_item, parent, false));
            mCaptionTextView = (TextView) itemView.findViewById(R.id.caption);
            mTimeTextView = (TextView) itemView.findViewById(R.id.time);
            mLocationTextView = (TextView) itemView.findViewById(R.id.location);
            mExactTime = (TextView) itemView.findViewById(R.id.hhmm);

            itemView.setOnClickListener(this);
        }

        public void bind(Reminder r)  {
            mReminder = r;
            mCaptionTextView.setText(mReminder.getReminderName());
            Date date=null;
            try {
                date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(mReminder.getReminderStartTime());

            }
            catch(Exception e){
                    e.printStackTrace();
            }
            String months[] = {"Jan", "Feb", "Mar", "Apr",
                    "May", "Jun", "Jul", "Aug", "Sept",
                    "Oct", "Nov", "Dec"};
            mTimeTextView.setText(String.valueOf(date.getDate())+"-"+months[date.getMonth()]+"-"+String.valueOf(date.getYear()+1900));
            mLocationTextView.setText(mReminder.getPlacename());
            int hour = date.getHours();
            int minute = date.getMinutes();
            String t="AM";
            if(hour>=12)
            {
                t = "PM";
                if(hour!=12)
                    hour = hour % 12;
            }
            String h;
            if(hour<10)
               h = "0"+Integer.toString(hour);
            else
                h = Integer.toString(hour);
            String m;
            if(minute<10)
                m = "0"+Integer.toString(minute);
            else
                m = Integer.toString(minute);
            String exactTime = h+":"+m+" "+t;
            mExactTime.setText(exactTime);
        }

        @Override
        public void onClick(View view)
        {
            Toast.makeText(getContext(),"ArrayList Index "+getAdapterPosition(),Toast.LENGTH_LONG);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Bundle args = new Bundle();
            args.putSerializable("REMINDERID",new Integer(getAdapterPosition()));
            ViewReminderFragment fragment = new ViewReminderFragment();
            fragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.content,fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack("ListView").commit();

        }
    }

    private class ReminderAdapter extends RecyclerView.Adapter<ReminderHolder>
    {
        private List<Reminder> mReminders;

        public ReminderAdapter(List<Reminder> reminders)
        {

            mReminders = reminders;
        }

        @Override

        public ReminderHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ReminderHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ReminderHolder holder, int position) {
            Reminder reminder = mReminders.get(position);
            holder.bind(reminder);
        }

        @Override
        public int getItemCount()
        {

            return mReminders.size();
        }
    }

    private void updateUI() {

        ReminderSet reminderSet = ReminderSet.get(getActivity());
        List<Reminder> reminders = reminderSet.getReminders();
        mAdapter = new ReminderAdapter(reminders);
        mReminderRecyclerView.setAdapter(mAdapter);
    }

}
