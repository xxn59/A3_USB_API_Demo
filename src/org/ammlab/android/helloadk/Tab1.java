package org.ammlab.android.helloadk;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.app.Fragment;

public class Tab1 extends Fragment{
	
	private static final String TAG = "HelloADK";
	
	private ToggleButton mToggleButton;
	private Button mButtonExit;
	private Button mButtonOpen;
	private ToggleButton mBtnStatusButton;
	private TextView mStatusView;
	private SeekBar mSeekBar;
	private SeekBar mSeekBar2;
	private SeekBar mSeekBar3;
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1, container, false);
        
        mToggleButton = (ToggleButton) rootView.findViewById(R.id.toggleBtn);
		mStatusView = (TextView) rootView.findViewById(R.id.status);
		mBtnStatusButton = (ToggleButton) rootView.findViewById(R.id.btnstatusBtn);
		mSeekBar = (SeekBar) rootView.findViewById(R.id.seekBar1);
		mSeekBar2 = (SeekBar) rootView.findViewById(R.id.seekBar2);
		mSeekBar3 = (SeekBar) rootView.findViewById(R.id.seekBar3);
		mButtonExit = (Button) rootView.findViewById(R.id.buttonExit);
		mButtonOpen = (Button) rootView.findViewById(R.id.buttonOpen);

		mToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				byte command = 0x1;
				byte value = (byte) (isChecked ? 0x1 : 0x0);
//				sendCommand(command, value);
			}
		});
		
		mButtonExit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				java.lang.System.exit(0);
			}
		});
		
		mButtonOpen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				openAccessory(mAccessory);
			}
		});

		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// Dragging knob
				byte value = (byte) (progress * 255 / 100);
				byte command = 0x2;
				Log.d(TAG, "Current Value:" + progress + "," + value);
//				sendCommand(command, value);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// Touch knob
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// Release knob
				// mSeekBar.setProgress(50);
			}
		});
		mSeekBar2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// Dragging knob
				byte value = (byte) (progress * 255 / 100);
				byte command = 0x3;
				Log.d(TAG, "Current Value:" + progress + "," + value);
//				sendCommand(command, value);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// Touch knob
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// Release knob
				// mSeekBar.setProgress(50);
			}
		});
		mSeekBar3.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// Dragging knob
				byte value = (byte) (progress * 255 / 100);
				byte command = 0x4;
				Log.d(TAG, "Current Value:" + progress + "," + value);
//				sendCommand(command, value);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// Touch knob
//				String message;
//				byte[] msgBuffer;
//				try {
//					outStream = btSocket.getOutputStream();
//				} catch (IOException e) {
//					Log.e(TAG, "ON RESUME : Output Stream creation failed.", e);
//				}
//				message = Integer.toString(seekBar.getProgress());
//				msgBuffer = message.getBytes();
//				try {
//					outStream.write(msgBuffer);
//				} catch (IOException e) {
//					Log.e(TAG, "ON RESUME : Exception during write.", e);
//				}
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// Release knob
				// mSeekBar.setProgress(50);
//				String message;
//				byte[] msgBuffer;
//				try {
//					outStream = btSocket.getOutputStream();
//				} catch (IOException e) {
//					Log.e(TAG, "ON RESUME : Output Stream creation failed.", e);
//				}
//				message = Integer.toString(seekBar.getProgress());
//				msgBuffer = message.getBytes();
//				try {
//					outStream.write(msgBuffer);
//				} catch (IOException e) {
//					Log.e(TAG, "ON RESUME : Exception during write.", e);
//				}
			}
		});

		enableControls(false);
        return rootView;
    }
	
	
	private void enableControls(boolean enable) {
		if (enable) {
			mStatusView.setText("connected");
		} else {
			mStatusView.setText("not connect");
		}
		mToggleButton.setEnabled(enable);
	}
}