package edu.temple.yahoo_stock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	private Button go;
	private EditText stock, company, price;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		stock = (EditText) findViewById(R.id.edtQuote_name);
		company = (EditText) findViewById(R.id.edtCompanyName);
		price = (EditText) findViewById(R.id.edtPrice);
		go = (Button) findViewById(R.id.btnGO);

		go.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				String theStock = stock.getText().toString();
				String URL = "http://finance.yahoo.com/webservice/v1/symbols/"
						+ theStock + "/quote?format=json";
				new RetrieveMessages().execute(URL);

			}

			class RetrieveMessages extends AsyncTask<String, Void, String> {
				protected String doInBackground(String... urls) {
					HttpClient client = new DefaultHttpClient();
					String json = "";
					try {
						String line = "";
						HttpGet request = new HttpGet(urls[0]);
						HttpResponse response = client.execute(request);
						BufferedReader rd = new BufferedReader(
								new InputStreamReader(response.getEntity()
										.getContent()));
						while ((line = rd.readLine()) != null) {
							json += line + System.getProperty("line.separator");
						}
					} catch (IllegalArgumentException e1) {
						e1.printStackTrace();
					} catch (IOException e2) {
						e2.printStackTrace();
					}

					Log.e("Json:", json);
					return json;
				}

				protected void onPostExecute(String result) {

					JSONObject reader = null;
					try {
						reader = new JSONObject(result);

						JSONObject list = reader.getJSONObject("list");
						JSONArray resources = list.getJSONArray("resources");

						JSONObject resource = resources.getJSONObject(0);
						JSONObject fields = resource.getJSONObject("resource")
								.getJSONObject("fields");

						String ComName = fields.getString("name");
						String priceCompany = fields.getString("price");

						company.setText("Company: " + ComName);
						price.setText("price: " + priceCompany);

					} catch (JSONException e) {
							e.printStackTrace();
					}

				}
			}

		});
	}
}
