/*
 * Faith-Anne Kocadag
 * Assignment 9
 * April 9, 2014
 */

package com.example.project9;

import java.io.DataInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	private EditText addressField;
	private EditText secondsField;
	private EditText minutesField;
	private EditText hoursField;
	private EditText daysField;
	private EditText yearsField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addressField = (EditText) findViewById(R.id.address_field);
		Button button = (Button) findViewById(R.id.get_button);
		secondsField = (EditText) findViewById(R.id.seconds_field);
		minutesField = (EditText) findViewById(R.id.minutes_field);
		hoursField = (EditText) findViewById(R.id.hours_field);
		daysField = (EditText) findViewById(R.id.days_field);
		yearsField = (EditText) findViewById(R.id.years_field);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = addressField.getText().toString();
				new TimeTask().execute(url);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public class TimeTask extends AsyncTask<String, Void, Long> {
		
		@Override
		protected Long doInBackground(String... params) {
			Long time = 0L;
			try {
				Socket socket = new Socket();
				InetSocketAddress address = new InetSocketAddress(params[0], 37);
				socket.connect(address);

				DataInputStream input = new DataInputStream(
						socket.getInputStream());
				time = input.readInt() & 0xffffffffL;
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return time;
		}

		@Override
		protected void onPostExecute(Long seconds) {
			long minutes = seconds / 60;
			long hours = minutes / 60;
			int days = (int) hours / 24;
			int years = days / 365;

			secondsField.setText(seconds + " Seconds");
			minutesField.setText(minutes + " Minutes");
			hoursField.setText(hours + " Hours");
			daysField.setText(days + " Days");
			yearsField.setText(years + " Years");
		}
	}
}
