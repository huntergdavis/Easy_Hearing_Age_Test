package com.hunterdavis.easyhearingagetest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class EasyHearingAgeTest extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
		// implement a seekbarchangelistener for this class
		SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				activateFromProgress(progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		};
		
		SeekBar onlySeekBar = (SeekBar) findViewById(R.id.freq);
		onlySeekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        
        
        
		// listener for frequency button
		OnClickListener frequencyListner = new OnClickListener() {
			public void onClick(View v) {
				EditText freqText = (EditText) findViewById(R.id.freqbonus);
				String frequency = freqText.getText().toString();
				if (frequency.length() > 0) {
					float localFreqValue = Float.valueOf(frequency);
					playFrequency(v.getContext(), localFreqValue);
				}

			}
		};
		
		// frequency button
		Button freqButton = (Button) findViewById(R.id.freqbutton);
		freqButton.setOnClickListener(frequencyListner);
		
		// Look up the AdView as a resource and load a request.
		AdView adView = (AdView) this.findViewById(R.id.adView);
		adView.loadAd(new AdRequest());
        
    }
    
    public void activateFromProgress(int progress) {
    	// progress is a range from 0 to 100
    	// age range is from 0 to 100 as well
    	// progress is inverted though
    	// so 100 - progress = realProgress
    	int realProgress = 100-progress;
    	
    	// people can hear from 22000 down to 8000
    	// i am 30 and can hear up to 17000
    	// so first set the frequency to a value 14000/100 * progress
    	int frequency = 8000 + 140 * progress;
    	
    	// set the frequency text
    	TextView freqText = (TextView) findViewById(R.id.freqtext);
    	freqText.setText("Current Audio Frequency is:"+frequency +"hz");
    	
    	// set the age text
    	TextView ageText = (TextView) findViewById(R.id.agetext);
    	ageText.setText("Your Current Hearing Age is: "+realProgress +" years old");
    	
    	
    	// set the explanation age text
    	TextView explanationText = (TextView) findViewById(R.id.explanationtext);
    	String explanationStart = "Based on the currently selected frequency, you fall into the ";
    	String explanationEnd = " age range.";
    	String explanationMiddle = "";
    	
    	if(realProgress < 10)
    	{
    		explanationMiddle = "very young";
    	}
    	else if (realProgress < 20)
    	{
    		explanationMiddle = "teenage";
    	}else if (realProgress < 40)
    	{
    		explanationMiddle = "adult";
    	}
    	else if (realProgress < 60)
    	{
    		explanationMiddle = "middle";
    	}
    	else if (realProgress < 100)
    	{
    		explanationMiddle = "elderly";
    	}
    	else if (realProgress == 100)
    	{
    		explanationMiddle = "ancient";
    	}
    	explanationText.setText(explanationStart + explanationMiddle + explanationEnd);
    	
    	
    	// play the frequency 
    	playFrequency(getBaseContext(),frequency);
    	
    }
    
    
	public void playFrequency(Context context, float frequency) {

		// final float frequency2 = 440;
		float increment = (float) (2 * Math.PI) * frequency / 44100; // angular
																		// increment
																		// for
																		// each
																		// sample
		float angle = 0;
		AndroidAudioDevice device = new AndroidAudioDevice();
		float samples[] = new float[1024];

		for (int j = 0; j < 60; j++) {
			for (int i = 0; i < samples.length; i++) {
				samples[i] = (float) Math.sin(angle);
				angle += increment;
			}

			device.writeSamples(samples);

		}
	}
}