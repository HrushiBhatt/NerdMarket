package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
public class UserActivity extends AppCompatActivity {
    private Button signupBackButton;
    private Button deleteAccountButton;
    private Button toNotificationsButton;
    private Button toAdminButton;
    private Button loginBackButton;
    private ImageButton hamburgerDropdownButton;
    private ImageButton cardBinderButton;
    private ImageButton cardDetailsButton;
    private ImageButton toHomeButton;
    private int id;
    private String username;
    private boolean isAdmin;


}
