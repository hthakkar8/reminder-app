package com.example.suyog.locationtracker;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
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
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class AddRemainder extends Fragment {

    private TextView placename;
    private TextView placeadd;
    private EditText reminderName;
    private EditText reminderStartTime;
    private EditText reminderEndTime;
    private Button setReminder;
    private Button getLocation;
    private boolean start;
    private boolean end;
    private double logitude = 0, latitude = 0;
    private DatabaseReference remindersDatabase;
    private static final int PLACE_PICKER_REQUEST = 1;
    int day,month,year,hour,minute;
    int dayFinal,monthFinal,yearFinal,hourFinal,minuteFinal;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_add_reminder,container,false);

        auth=FirebaseAuth.getInstance();
        remindersDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog=new ProgressDialog(getActivity());
        placeadd=(TextView) view.findViewById(R.id.placeadd);
        placename=(TextView) view.findViewById(R.id.placename);
        reminderName=(EditText) view.findViewById(R.id.reminderName);
        reminderStartTime=(EditText) view.findViewById(R.id.reminderStartTime);
        reminderEndTime=(EditText) view.findViewById(R.id.reminderEndTime);
        setReminder=(Button) view.findViewById(R.id.setReminder);
        getLocation=(Button) view.findViewById(R.id.getadd);

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(getActivity());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                    Log.d("placepicker:","myerror1");
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }


            }


        });


        setReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                addRemainder();

            }
        });

        reminderStartTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (reminderStartTime.getRight() - reminderStartTime.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
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
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (reminderEndTime.getRight() - reminderEndTime.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        getEndDate();
                        return true;
                    }
                }
                return false;
            }
        });

    return  view;
    }





    public void getStartTime(){
        Calendar c=Calendar.getInstance();
        hour=c.get(Calendar.HOUR_OF_DAY);
        minute=c.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener reminderOnTimeSetListener= new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                hourFinal=i;
                minuteFinal=i1;
                reminderStartTime.setText(dayFinal + "/" + monthFinal + "/" + yearFinal + "   " + hourFinal + ":" + minuteFinal);

            }
        };

        TimePickerDialog timePickerDialog= new TimePickerDialog(getActivity(),reminderOnTimeSetListener ,hour,minute, DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.show();
    }

    public void getEndTime(){

        Calendar c=Calendar.getInstance();
        hour=c.get(Calendar.HOUR_OF_DAY);
        minute=c.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener reminderOnTimeSetListener= new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                hourFinal=i;
                minuteFinal=i1;

                reminderEndTime.setText(dayFinal+"/"+monthFinal+"/"+yearFinal+"   "+hourFinal+":"+minuteFinal);

            }
        };

        TimePickerDialog timePickerDialog= new TimePickerDialog(getActivity(),reminderOnTimeSetListener ,hour,minute, DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.show();
    }


    public boolean  getStartDate(){
        Calendar c=Calendar.getInstance();
        year=c.get(Calendar.YEAR);
        month=c.get(Calendar.MONTH);
        day=c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener reminderOndateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                yearFinal=i;
                monthFinal=i1+1;
                dayFinal=i2;

                getStartTime();
            }
        };


        DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(), reminderOndateSetListener,year,month,day);
        datePickerDialog.show();
        return true;
    }


    public void  getEndDate(){
        Calendar c=Calendar.getInstance();
        year=c.get(Calendar.YEAR);
        month=c.get(Calendar.MONTH);
        day=c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener reminderOndateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                yearFinal=i;
                monthFinal=i1+1;
                dayFinal=i2;

                getEndTime();

            }
        };
        DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(), reminderOndateSetListener,year,month,day);
        datePickerDialog.show();
    }


    private Date getDate(EditText e){
        Date rTime=new Date();
        if(!TextUtils.isEmpty(e.getText().toString())) {
            String datesAndTime[] = e.getText().toString().split("   ");
            String date[] = datesAndTime[0].toString().trim().split("/");
            String time[] = datesAndTime[1].toString().trim().split(":");
            rTime.setYear((Integer.parseInt(date[2])-1900));
            rTime.setDate(Integer.parseInt(date[0]));
            rTime.setMonth(Integer.parseInt(date[1]) - 1);
            rTime.setHours(Integer.parseInt(time[0]));
            rTime.setMinutes(Integer.parseInt(time[1]));
            return rTime;
        }
        return null;
    }



    private void addRemainder() {
        SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy   HH:mm");
        Date date=new Date();
        String rname=reminderName.getText().toString().trim();
        String reminderEndDate=null,reminderStartDate=null;
        if(getDate(reminderStartTime) != null && getDate(reminderEndTime) != null ) {
                        reminderStartDate = formatter.format(getDate(reminderStartTime)).toString();
                        reminderEndDate = formatter.format(getDate(reminderEndTime)).toString();
                }

        String pname=placename.getText().toString().trim();
        String padd=placeadd.getText().toString().trim();
        if(!TextUtils.isEmpty(rname) && !TextUtils.isEmpty(pname) && !TextUtils.isEmpty(padd) && reminderEndDate !=null && reminderStartDate != null) {

            String id=remindersDatabase.push().getKey();
            Reminder reminder = new Reminder(id,rname,reminderStartDate,reminderEndDate,pname,padd,logitude,latitude);
            ReminderSet rs = ReminderSet.get(getActivity());
            rs.addReminder(reminder);

            setAlarm(rname,pname,logitude,latitude,id);
            Log.i("Fence","after setAlarm()");
            Toast.makeText(getActivity(),"Reminder Added",Toast.LENGTH_LONG);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content , new ReminderListFragment())
                         .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                         .addToBackStack(null)
                         .commit();

        }

        else{
            Toast.makeText(getActivity(), "Enter valid Details", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);
                placename.setText(place.getName());
                placeadd.setText(place.getAddress());
                logitude = place.getLatLng().longitude;
                latitude = place.getLatLng().latitude;


            }
        }


    }
    public void setAlarm(String rname,String pname,double longitude,double latitude,String id)
    {
        Log.i("Fence","setAlarm()");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getDate(reminderStartTime).getTime());
        Log.i("Fence", String.valueOf(cal.getTimeInMillis()));
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.putExtra("rname",rname);
        intent.putExtra("place",pname);
        intent.putExtra("lng",longitude);
        intent.putExtra("lat",latitude);
        intent.putExtra("id",id);
        Log.i("Fence","got lat lng");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),99, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.i("Fence","PENDING INTENT");
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Log.i("Fence","ALARM MANAGER");
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }


}