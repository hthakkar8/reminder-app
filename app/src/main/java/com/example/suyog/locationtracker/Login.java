package com.example.suyog.locationtracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by SUYOG on 9/8/2017.
 */

public class Login extends Fragment {

    private EditText loginEmail;
    private EditText loginPassword;
    private Button loginbtn;
    private Button already;
    ScrollView scrollView;
    FirebaseAuth auth;
    ProgressDialog progresDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.login,container,false);
        auth=FirebaseAuth.getInstance();
        progresDialog=new ProgressDialog(getActivity());
        loginEmail=(EditText) view.findViewById(R.id.loginEmail);
        loginPassword=(EditText) view.findViewById(R.id.loginPassword);
        loginbtn=(Button) view.findViewById(R.id.loginbtn);
        already=(Button) view.findViewById(R.id.linksignup);

        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,new Register())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=loginEmail.getText().toString().trim();
                String pass=loginPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getActivity(),"Enter Valid Email",Toast.LENGTH_LONG).show();
                    return;
                }


                if(TextUtils.isEmpty(pass)){
                    Toast.makeText(getActivity(),"Enter Valid Password",Toast.LENGTH_LONG).show();
                    return;
                }

                progresDialog.setMessage("Signing In");
                progresDialog.show();

                auth.signInWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(getActivity(),new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                Intent i=new Intent((MainActivity)getActivity(), ReminderActivity.class);
                                ((MainActivity)getActivity()).startActivity(i);
                                ((MainActivity)getActivity()).finish();

                            }
                            else{
                                progresDialog.dismiss();
                                Toast.makeText(getActivity(),"Login Failed",Toast.LENGTH_LONG).show();

                            }
                        }
                    });
            }
        });
        return view;
    }
}
