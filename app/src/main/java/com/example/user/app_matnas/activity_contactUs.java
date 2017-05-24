package com.example.user.app_matnas;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class activity_contactUs extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private float zoomLevel = 17;
    private Toolbar toolbar;
    private TextView toolBarText;
    private String FACEBOOK_URL = "https://www.facebook.com/matnaspat";
    private String FACEBOOK_PAGE_ID = "400566206678340";

    //form
    private EditText name, phone, message;

    private TextInputLayout inputLayoutName, inputLayoutPhone, inputLayoutMessage;
    String s_name, s_phone, s_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(R.string.text_contactus);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        name = (EditText) findViewById(R.id.form_name);
        phone = (EditText) findViewById(R.id.form_phone);
        message = (EditText) findViewById(R.id.form_message);


        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_phone);
        inputLayoutMessage = (TextInputLayout) findViewById(R.id.input_layout_message);


    }


    public void onClickSubmitForm(View view)
    {
        name.addTextChangedListener(new MyTextWatcher(name));
        phone.addTextChangedListener(new MyTextWatcher(phone));
        message.addTextChangedListener(new MyTextWatcher(message));

        s_name = name.getText().toString().trim();
        s_phone = phone.getText().toString().trim();
        s_message = message.getText().toString().trim();
        submitForm();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in matnas and move the camera
        LatLng LocationMatnas = new LatLng(31.749959, 35.200732);
        mMap.addMarker(new MarkerOptions().position(LocationMatnas).title("מרכז קהילתי פאני קפלן פת"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LocationMatnas, zoomLevel));
    }

    public void onClickMail(View view) {
        Uri mail = Uri.parse("mailto:matnaspat@fannykaplan.org");
        Intent intent = new Intent(Intent.ACTION_VIEW, mail);
        startActivity(intent);
    }

    public void onClickFacebook(View view) {
        Intent facebook = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(this);
        facebook.setData(Uri.parse(facebookUrl));
        startActivity(facebook);
    }

    public void onClickWhatsapp(View view) {
        Uri whatsapp = Uri.parse("https://chat.whatsapp.com/EvtmaQvQRQc9WLMjwd9ihv");
        Intent intent = new Intent(Intent.ACTION_VIEW, whatsapp);
        startActivity(intent);

    }

    public void onClickPhone(View view) {
        Intent phone = new Intent(Intent.ACTION_DIAL);
        phone.setData(Uri.parse("tel:026782858"));
        phone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(phone);

    }

    public void onClickFacebookMessenger(View view) {
        Uri messenger = Uri.parse("http://m.me/matnaspat");
        Intent intent = new Intent(Intent.ACTION_VIEW, messenger);
        startActivity(intent);

    }

    //method to get the right URL to use in the intent
    private String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }


    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateName()) {
            return;
        }
        if (!validatePhone()) {
            return;
        }
        if (!validateMessage()) {
            return;
        }

        SendData();
    }

    private boolean validateName()
    {
        if (s_name.isEmpty())
        {
            inputLayoutName.setError(getString(R.string.err_msg_fname));
            requestFocus(name);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateMessage() {
        if (s_message.isEmpty()) {
            inputLayoutMessage.setError(getString(R.string.err_msg_neighborhood));
            requestFocus(message);
            return false;
        } else {
            inputLayoutMessage.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validatePhone() {
        if (s_phone.isEmpty() || s_phone.length() != 10) {
            inputLayoutPhone.setError(getString(R.string.err_msg_phone));
            requestFocus(phone);
            return false;
        } else {
            inputLayoutPhone.setErrorEnabled(false);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.form_name:
                    validateName();
                    break;
                case R.id.form_message:
                    validateMessage();
                    break;
                case R.id.form_phone:
                    validatePhone();
                    break;
            }
        }
        }


    private void SendData() {
        //Creating SendMail object
        SendMail sm = new SendMail(this, getResources().getString(R.string.contactUs_title), s_name, s_phone, s_message);

        //Executing sendmail to send email
        sm.execute();
    }

}