package com.example.suyog.locationtracker;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Hardik on 17-Sep-17.
 */

public class ViewReminderFragment extends Fragment {
    private EditText reminderName;
    private ReminderSet reminderSet;
    private List<Reminder> rs;
    private Reminder r;
    private EditText reminderStartTime;
    private EditText reminderEndTime;
    private TextView placeAddress;
    private TextView placename;
    private Button editReminder;
    private Button getLocation;
    private boolean start;
    private boolean end;
    private double logitude = 0, latitude = 0;
    private DatabaseReference remindersDatabase;
    private static final int PLACE_PICKER_REQUEST = 1;
    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    private Integer position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_reminder, container, false);
        position = (Integer) getArguments().getSerializable("REMINDERID");

        reminderSet = ReminderSet.get(getContext());
        rs = reminderSet.getReminders();
        r = rs.get(position);


        reminderName = view.findViewById(R.id.reminderName);
        reminderStartTime = view.findViewById(R.id.reminderStartTime);
        reminderEndTime = view.findViewById(R.id.reminderEndTime);
        placeAddress = view.findViewById(R.id.placeadd);
        placename = view.findViewById(R.id.placename);

        reminderStartTime.setText(r.getReminderStartTime());
        reminderEndTime.setText(r.getReminderEndTime());
        placename.setText(r.getPlacename());
        placeAddress.setText(r.getPlaceaddress());
        reminderName.setText(r.getReminderName());
        auth = FirebaseAuth.getInstance();
        remindersDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(getActivity());

        placename = (TextView) view.findViewById(R.id.placename);
        editReminder = (Button) view.findViewById(R.id.setReminder);
        getLocation = (Button) view.findViewById(R.id.getadd);
        editReminder.setText("Save");

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(getActivity());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                    Log.d("placepicker:", "myerror1");
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }


            }


        });


        editReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                editRemainder();

            }
        });

        reminderStartTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (reminderStartTime.getRight() - reminderStartTime.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        getStartDate();
                        return true;
                    }
                }
                return false;
            }
        });

        reminderEndTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (reminderEndTime.getRight() - reminderEndTime.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        getEndDate();
                        return true;
                    }
                }
                return false;
            }
        });

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.view_reminder_fragment_menu, menu);
    }

    public void getStartTime() {
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener reminderOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                hourFinal = i;
                minuteFinal = i1;
                reminderStartTime.setText(dayFinal + "/" + monthFinal + "/" + yearFinal + "   " + hourFinal + ":" + minuteFinal);

            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), reminderOnTimeSetListener, hour, minute, DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.show();
    }

    public void getEndTime() {

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener reminderOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                hourFinal = i;
                minuteFinal = i1;

                reminderEndTime.setText(dayFinal + "/" + monthFinal + "/" + yearFinal + "   " + hourFinal + ":" + minuteFinal);

            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), reminderOnTimeSetListener, hour, minute, DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.show();
    }


    public boolean getStartDate() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener reminderOndateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                yearFinal = i;
                monthFinal = i1 + 1;
                dayFinal = i2;

                getStartTime();
            }
        };


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), reminderOndateSetListener, year, month, day);
        datePickerDialog.show();
        return true;
    }


    public void getEndDate() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener reminderOndateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                yearFinal = i;
                monthFinal = i1 + 1;
                dayFinal = i2;

                getEndTime();

            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), reminderOndateSetListener, year, month, day);
        datePickerDialog.show();
    }


    private Date getDate(EditText e) {
        Date rTime = new Date();
        if (!TextUtils.isEmpty(e.getText().toString())) {
            Log.d("viewrem:", "iside if");
            String datesAndTime[] = e.getText().toString().split("   ");
            Log.d("viewrem:", "1");
            String date[] = datesAndTime[0].toString().trim().split("/");
            Log.d("viewrem:", "2");
            String time[] = datesAndTime[1].toString().trim().split(":");
            Log.d("viewrem:", "3");
            //Log.d("viewrem: ",date[0]+" "+(date[1])+" "+date[3]+" "+time[0]+" "+time[1]);
            rTime.setYear((Integer.parseInt(date[2]) - 1900));
            rTime.setDate(Integer.parseInt(date[0]));
            rTime.setMonth(Integer.parseInt(date[1]) - 1);
            rTime.setHours(Integer.parseInt(time[0]));
            rTime.setMinutes(Integer.parseInt(time[1]));
            Log.d("viewrem:", "4");
            return rTime;
        }
        return null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);
                placename.setText(place.getName());
                placeAddress.setText(place.getAddress());
                logitude = place.getLatLng().longitude;
                latitude = place.getLatLng().latitude;
            }
        }


    }

    public void editRemainder() {
        Log.d("viewrem:", "editReminder");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy   HH:mm");
        String rname = reminderName.getText().toString().trim();
        String reminderEndDate = null, reminderStartDate = null;
        Log.d("viewrem:", "editReminder2");

        if (getDate(reminderStartTime) != null && getDate(reminderEndTime) != null) {
            reminderStartDate = formatter.format(getDate(reminderStartTime)).toString();
            reminderEndDate = formatter.format(getDate(reminderEndTime)).toString();
            Log.d("viewrem:", "editReminder3");

        }
        Log.d("viewrem:", "editReminder4");

        String pname = placename.getText().toString().trim();
        String padd = placeAddress.getText().toString().trim();
        Log.d("viewrem:", "editReminder5");

        if (!TextUtils.isEmpty(rname) && !TextUtils.isEmpty(pname) && !TextUtils.isEmpty(padd) && reminderEndDate != null && reminderStartDate != null) {

            String id = r.getKey();
            Reminder reminder = new Reminder(id, rname, reminderStartDate, reminderEndDate, pname, padd, logitude, latitude);


            reminderSet.editReminder(position, reminder);

            Toast.makeText(getActivity(), "Reminder Updated", Toast.LENGTH_LONG);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, new ReminderListFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit();

        } else {
            Toast.makeText(getActivity(), "Enter valid Details", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_reminder:
                reminderSet.deleteReminder(position);
                getFragmentManager().popBackStack();
                return true;
            default:
               return  super.onOptionsItemSelected(item);


        }
    }
}
