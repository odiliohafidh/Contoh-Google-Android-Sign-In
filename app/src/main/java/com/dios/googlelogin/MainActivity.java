package com.dios.googlelogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {
    private LinearLayout llProfile;
    private Button btn_sign_out;
    private SignInButton btn_sign_in;
    private ImageView imgProfilePic;
    private TextView txtName,txtEmail,idpengguna;
    private GoogleApiClient googleApiClient;
    private static final  int REQ_CODE=9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient
                .Builder(this).enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions)
                .build();
        init();
    }
    private void init(){
        llProfile = (LinearLayout) findViewById(R.id.llProfile);
        btn_sign_out = (Button) findViewById(R.id.btn_sign_out);
        btn_sign_in = (SignInButton) findViewById(R.id.btn_sign_in);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        idpengguna = (TextView) findViewById(R.id.idpengguna);
        btn_sign_out.setVisibility(View.GONE);
        btn_sign_in.setOnClickListener(this);
        btn_sign_out.setOnClickListener(this);
        llProfile.setVisibility(View.GONE);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_sign_in:
                signin();
                break;
            case R.id.btn_sign_out:
                signout();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private void signin(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }

    private void signout(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateui(false);
            }
        });
    }

    private  void handleresult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            String nama = account.getDisplayName();
            String iduser = account.getId();
            String email = account.getEmail();
            String foto = String.valueOf(account.getPhotoUrl());
            txtName.setText(nama);
            idpengguna.setText(iduser);
            txtEmail.setText(email);
            Glide.with(this).load(foto).error(R.mipmap.ic_launcher_round).into(imgProfilePic);
            updateui(true);
        }else {
            updateui(false);
        }
    }

    private void updateui(boolean islogin){
        if (islogin){
            llProfile.setVisibility(View.VISIBLE);
            btn_sign_in.setVisibility(View.GONE);
            btn_sign_out.setVisibility(View.VISIBLE);
        }else{
            llProfile.setVisibility(View.GONE);
            btn_sign_in.setVisibility(View.VISIBLE);
            btn_sign_out.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQ_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleresult(result);
        }
    }
}
