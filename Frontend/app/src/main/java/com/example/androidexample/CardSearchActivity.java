package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CardSearchActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button btnJsonObjReq;

    private CardView cardResultContainer;
    private ImageView cardImage;
    private String cardUrl;
    private TextView cardName, cardType, cardSet, cardRarity, cardPrice;

    private EditText cardNameEdit, cardTypeEdit, cardSetEdit, cardRarityEdit, cardPriceEdit;
    private Button cardEditBtn, cardSaveBtn;

    private JSONObject card;

    private static final String URL_JSON_OBJECT = "http://coms-3090-022.class.las.iastate.edu:8080/api/cards/1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carddetail);

        // Search UI
        btnJsonObjReq  = findViewById(R.id.card_search_btn);
        searchEditText = findViewById(R.id.card_search_field);

        // Card display UI
        cardResultContainer = findViewById(R.id.card_result_container);
        cardImage           = findViewById(R.id.card_image);
        cardName            = findViewById(R.id.card_name);
        cardType            = findViewById(R.id.card_type);
        cardSet             = findViewById(R.id.card_set);
        cardRarity          = findViewById(R.id.card_rarity);
        cardPrice           = findViewById(R.id.card_price);

        // Edit UI
        cardNameEdit   = findViewById(R.id.card_name_edit);
        cardTypeEdit   = findViewById(R.id.card_type_edit);
        cardSetEdit    = findViewById(R.id.card_set_edit);
        cardRarityEdit = findViewById(R.id.card_rarity_edit);
        cardPriceEdit  = findViewById(R.id.card_price_edit);
        cardEditBtn    = findViewById(R.id.card_edit_btn);
        cardSaveBtn    = findViewById(R.id.card_save_btn);

        btnJsonObjReq.setOnClickListener(v -> makeJsonObjReq());
        cardEditBtn.setOnClickListener(v -> toggleEditMode(true));
        cardSaveBtn.setOnClickListener(v -> makeJsonObjPutReq());
    }

    private void toggleEditMode(boolean editing) {

        cardName.setVisibility(editing ? View.GONE : View.VISIBLE);
        cardNameEdit.setVisibility(editing ? View.VISIBLE : View.GONE);

        cardType.setVisibility(editing ? View.GONE : View.VISIBLE);
        cardTypeEdit.setVisibility(editing ? View.VISIBLE : View.GONE);

        cardSet.setVisibility(editing ? View.GONE : View.VISIBLE);
        cardSetEdit.setVisibility(editing ? View.VISIBLE : View.GONE);

        cardRarity.setVisibility(editing ? View.GONE : View.VISIBLE);
        cardRarityEdit.setVisibility(editing ? View.VISIBLE : View.GONE);

        cardPrice.setVisibility(editing ? View.GONE : View.VISIBLE);
        cardPriceEdit.setVisibility(editing ? View.VISIBLE : View.GONE);


        cardEditBtn.setVisibility(editing ? View.GONE : View.VISIBLE);
        cardSaveBtn.setVisibility(editing ? View.VISIBLE : View.GONE);


        if (editing) {
            cardNameEdit.setText(cardName.getText().toString().replace("Name: ", ""));
            cardTypeEdit.setText(cardType.getText().toString().replace("Type: ", ""));
            cardSetEdit.setText(cardSet.getText().toString().replace("Set: ", ""));
            cardRarityEdit.setText(cardRarity.getText().toString().replace("Rarity: ", ""));
            cardPriceEdit.setText(cardPrice.getText().toString().replace("Price: $", ""));
        }
    }

    private void makeJsonObjReq() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                URL_JSON_OBJECT,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        card = response;
                        displayCardData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        cardName.setText("Error loading card.");
                        cardResultContainer.setVisibility(View.VISIBLE);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new HashMap<>();
            }

            @Override
            protected Map<String, String> getParams() {
                return new HashMap<>();
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }

    private void makeJsonObjPutReq() {
        try {
            // Build updated JSON body from the edit fields
            JSONObject updatedCard = new JSONObject();
            updatedCard.put("imageUrl", cardUrl);
            updatedCard.put("cardName",   cardNameEdit.getText().toString());
            updatedCard.put("cardType",   cardTypeEdit.getText().toString());
            updatedCard.put("cardSet",    cardSetEdit.getText().toString());
            updatedCard.put("cardRarity", cardRarityEdit.getText().toString());
            updatedCard.put("price",      Double.parseDouble(cardPriceEdit.getText().toString()));

            JsonObjectRequest putRequest = new JsonObjectRequest(
                    Request.Method.PUT,
                    URL_JSON_OBJECT,
                    updatedCard,
                    response -> {
                        Log.d("Volley PUT Response", response.toString());
                        card = response;
                        displayCardData(response);
                        toggleEditMode(false);
                    },
                    error -> {
                        Log.e("Volley PUT Error", error.toString());
                        cardName.setText("Error saving card.");
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return new HashMap<>();
                }
            };

            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(putRequest);

        } catch (JSONException e) {
            Log.e("PUT Build Error", e.getMessage());
        }
    }

    private void displayCardData(JSONObject response) {
        try {
            String name   = response.getString("cardName");
            String type   = response.getString("cardType");
            String set    = response.getString("cardSet");
            String rarity = response.getString("cardRarity");
            double price  = response.getDouble("price");
            String imgUrl = response.getString("imageUrl");
            cardUrl = imgUrl;

            cardName.setText("Name: "     + name);
            cardType.setText("Type: "     + type);
            cardSet.setText("Set: "       + set);
            cardRarity.setText("Rarity: " + rarity);
            cardPrice.setText("Price: $"  + String.format("%.2f", price));

            Glide.with(getApplicationContext())
                    .load(imgUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(cardImage);

            cardResultContainer.setVisibility(View.VISIBLE);

        } catch (JSONException e) {
            Log.e("Card Parse Error", e.getMessage());
            cardName.setText("Failed to parse card data.");
            cardResultContainer.setVisibility(View.VISIBLE);
        }
    }
}