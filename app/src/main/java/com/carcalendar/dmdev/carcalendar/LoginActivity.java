package com.carcalendar.dmdev.carcalendar;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import model.UserManager;
import model.Vehicle.Vehicle;
import model.util.ImageUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameET;
    private EditText passET;
    private Button loginBtn;
    private Button registerBTn;
    private ProgressBar progressBar;
    private TaskStackBuilder stackBuilder;
    private boolean fromNotification = false;
    public static final int DATA_OKEY = 0;
    public static final int BAD_DATA = -1;
    public static final int NO_DATA = -10;
    private UserManager manager = UserManager.getInstance();

    /**
     * Sets the username field to vehicleOwner
     * Disables the username field
     * Disables the register button
     * Sets the fromNotificationFlag to true
     * @param vehicleOwner the owner of the vehicle
     */
    private void setUpActivityForLoginFromNotification(String vehicleOwner) {
        usernameET.setText(vehicleOwner);
        usernameET.setEnabled(false);
        registerBTn.setEnabled(false);
        fromNotification = true;
    }

    /**
     * Adds the intent with it's parent stack and launches it.
     * The current activity is destroyed using finish()
     * @param intent the intent to launch
     */
    private void goToDetailsViewWithBackStack(Intent intent) {
        try {
            stackBuilder.addNextIntentWithParentStack(intent);
            stackBuilder.getPendingIntent(0,
                    PendingIntent.FLAG_UPDATE_CURRENT).send();
            finish();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Required when calling the activity from a notification
        // and when user kills app after notification is clicked
        // If the user kills the app the db is uninitialized
        // Initializes the db
        if(!manager.isDbInitialized()) {
            manager.setDbContext(this);
        }

        stackBuilder = TaskStackBuilder.create(getApplicationContext());;
        usernameET = (EditText) findViewById(R.id.usernameET);
        registerBTn = (Button) findViewById(R.id.regBtn);

        final Intent intentToDetails = new Intent(this,
                HoldViewVehicleFragmentActivity.class);

        // Uses getLoggedUserFromDB because when the activity is started from a notification
        // and the loggedUser reference is cleared
        boolean userLogged = manager.getLoggedUserFromDB() != null;

        if(LoaderActivity.DatabaseAvailable(this)){
            // Handles intents from notifications
            if (getIntent().hasExtra("username")){
                intentToDetails.putExtra("vehicle",
                        getIntent().getSerializableExtra("vehicle"));

                String vehicleOwner =
                        getIntent().getStringExtra("username");
                if(userLogged) {
                    // Go directly to details view
                    if (manager.getLoggedUserName().equals(vehicleOwner)) {
                        goToDetailsViewWithBackStack(intentToDetails);
                        finish();
                    } else {
                        // Logout current user
                        manager.userLogoutForDB();
                    }
                }
                setUpActivityForLoginFromNotification(vehicleOwner);
            } else if (userLogged) {
                Intent toMain = new Intent(this.getApplicationContext(), GarageActivity.class);
                finish();
                startActivity(toMain);
            }
        }

        passET = (EditText) findViewById(R.id.passET);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        registerBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toReg = new Intent(view.getContext(),RegisterActivity.class);
                startActivityForResult(toReg,DATA_OKEY);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(manager.authenticateLogin(usernameET.getText().toString(),passET.getText().toString())){
                    if(fromNotification) {
                        goToDetailsViewWithBackStack(intentToDetails);
                    } else {
                        Intent toMain = new Intent(view.getContext(), LoaderActivity.class);
                        startActivity(toMain);
                        finish();
                    }
                }
                else{
                   Toast.makeText(view.getContext(),"Wrong data or not registered !",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == DATA_OKEY){
            if(resultCode == DATA_OKEY){
                usernameET.setText(data.getStringExtra("Username"));
                passET.setText(data.getStringExtra("Password"));
            }
            if(resultCode == NO_DATA){
                usernameET.setHint(R.string.enter_username);
                passET.setHint(R.string.enter_password);
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class LoginMapper extends AsyncTaskLoader {
        public LoginMapper(Context context) {
            super(context);
        }

        @Override
        public Object loadInBackground() {
            ArrayList<Vehicle> list = (ArrayList<Vehicle>) manager.getRegisteredUserVehicles();
            for (Vehicle x : list) {
                ImageUtils.mapImageToVehicle(x, ImageUtils.getBitmapFromPath(x.getPathToImage()));
            }
            return null;
        }
    }

}

