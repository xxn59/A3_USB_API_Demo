/*
 * Copyright (C) 2012 Yuuichi Akagawa
 *
 * This code is modify from yanzm's HelloADK.
 * https://github.com/yanzm/HelloADK
 * Original copyright: 
 *  Copyright (C) 2011 yanzm, uPhyca Inc.,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ammlab.android.helloadk;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.app.ActionBar;


public class HelloADKActivity extends Activity implements Runnable {

	private static final String TAG = "HelloADK";

	private static final String ACTION_USB_PERMISSION = "org.ammlab.android.app.helloadk.action.USB_PERMISSION";

	private PendingIntent mPermissionIntent;
	private boolean mPermissionRequestPending;

	private UsbManager mUsbManager;
	private UsbAccessory mAccessory;


//	private Window mWindow;
	private ActionBar actionBar;
	// Declare Tab Variable
	ActionBar.Tab Tab1, Tab2, Tab3;
	Fragment fragmentTab1 = new Tab1();
	Fragment fragmentTab2 = new Tab2(); 
	Fragment fragmentTab3 = new Tab3(); 

	ParcelFileDescriptor mFileDescriptor;

	FileInputStream mInputStream;
	FileOutputStream mOutputStream;

	//	private ToggleButton mToggleButton;
	//	private Button mButtonExit;
	//	private Button mButtonOpen;
	//	private ToggleButton mBtnStatusButton;
	//	private TextView mStatusView;
	//	private SeekBar mSeekBar;
	//	private SeekBar mSeekBar2;
	//	private SeekBar mSeekBar3;


	SensorManager sensorManager;// 缁狅紕鎮婇崳銊ヮ嚠鐠烇拷
	private Sensor gyroSensor;// 闂勶拷閾伙拷 娴肩姵鍔呴崳銊ヮ嚠鐠烇拷
	private Sensor acceSensor;// 閸旂姾銆�
	private Sensor quatSensor;// 閺冨娴嗛柅鍌炲櫤

	private TextView tv_X;
	private TextView tv_Y;
	private TextView tv_Z;
	
	private Handler mHandler;

//	private BluetoothAdapter mBluetoothAdapter = null;
//	private BluetoothSocket btSocket = null;
//	private OutputStream outStream = null;
//	private InputStream inStream = null;
	// private static final UUID MY_UUID =
	// UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	// //鏉╂瑦娼弰顖濇憫閻楁瑤瑕嗛崣锝夛拷姘辨暏閻ㄥ垊UID閿涘奔绗夌憰浣规纯閺�锟�
	// private static String address = "00:06:71:00:61:47"; // <==鐟曚浇绻涢幒銉ф畱閽冩繄澧拋鎯ь槵MAC閸︽澘娼�

	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbAccessory accessory = (UsbAccessory) intent
							.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);// UsbManager.getAccessory(intent);

					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						openAccessory(accessory);
					} else {
						Log.d(TAG, "permission denied for accessory "
								+ accessory);
					}
					mPermissionRequestPending = false;
				}
			} else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
				UsbAccessory accessory = (UsbAccessory) intent
						.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);// UsbManager.getAccessory(intent);
				if (accessory != null && accessory.equals(mAccessory)) {
					closeAccessory();
				}
			}
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_main);

		//		LayoutInflater inflater = LayoutInflater.from(this);
		//		View viewInflatedFromXml = inflater.inflate(R.layout.tab1, null);
		//		setContentView(viewInflatedFromXml);

		//requestWindowFeature(Window.FEATURE_ACTION_BAR);
		//setContentView(R.layout.main);

		//		mWindow = getWindow();
		//		mWindow.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);

		//		mWindow.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 娣囨繃瀵旂仦蹇撶鐢晲瀵�

		actionBar = getActionBar();
		//actionBar.hide();
		//actionBar.show();
		// Hide Actionbar Icon
		actionBar.setDisplayShowHomeEnabled(true);

		// Hide Actionbar Title

		actionBar.setDisplayShowTitleEnabled(true);

		// Create Actionbar Tabs
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Set Tab Icon and Titles

		Tab1 = actionBar.newTab().setText("Basics");
		Tab2 = actionBar.newTab().setText("Console");
		Tab3 = actionBar.newTab().setText("Extras");
		// Set Tab Listeners
		Tab1.setTabListener(new TabListener(fragmentTab1));
		Tab2.setTabListener(new TabListener(fragmentTab2));
		Tab3.setTabListener(new TabListener(fragmentTab3));
		// Add tabs to actionbar
		actionBar.addTab(Tab1);
		actionBar.addTab(Tab2);
		actionBar.addTab(Tab3);
		



		mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);// UsbManager.getInstance(this);
		// Broadcast Intent for myPermission
		mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
				ACTION_USB_PERMISSION), 0);

		// Register Intent myPermission and remove accessory
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_USB_PERMISSION);
		filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
		registerReceiver(mUsbReceiver, filter);

		// ///////////////////////////////////////////////////////////////////////////
		if (getLastNonConfigurationInstance() != null) {
			mAccessory = (UsbAccessory) getLastNonConfigurationInstance();
			openAccessory(mAccessory);
		}
		// ///////////////////////////////////////////////////////////////////////////

		
}

	@Override
	public void onResume() {
		super.onResume();
		// sensorManager.registerListener(sensoreventlistener, gyroSensor,
		// SensorManager.SENSOR_DELAY_GAME);
		// sensorManager.registerListener(sensoreventlistener, acceSensor,
		// SensorManager.SENSOR_DELAY_GAME);
		// sensorManager.registerListener(sensoreventlistener, quatSensor,
		// SensorManager.SENSOR_DELAY_GAME); fsdaf

		if (mInputStream != null && mOutputStream != null) {
			return;
		}

		UsbAccessory[] accessories = mUsbManager.getAccessoryList();
		UsbAccessory accessory = (accessories == null ? null : accessories[0]);
		if (accessory != null) {
			if (mUsbManager.hasPermission(accessory)) {
				openAccessory(accessory);
			} else {
				synchronized (mUsbReceiver) {
					if (!mPermissionRequestPending) {
						mUsbManager.requestPermission(accessory,
								mPermissionIntent);
						mPermissionRequestPending = true;
					}
				}
			}
		} else {
			Log.d(TAG, "mAccessory is null");
		}
		// BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

		// try {
		//
		// btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
		//
		// } catch (IOException e) {
		//
		// Log.e(TAG, "ON RESUME: Socket creation failed.", e);
		//
		// }
		// mBluetoothAdapter.cancelDiscovery();
		// try {
		//
		// btSocket.connect();
		//
		// Log.e(TAG,
		// "ON RESUME: BT connection established, data transfer link open.");
		//
		// } catch (IOException e) {
		//
		// try {
		// btSocket.close();
		//
		// } catch (IOException e2) {
		//
		// Log
		// .e(TAG,"ON RESUME: Unable to close socket during connection failure",
		// e2);
		// }
		// }
	}

	@Override
	public void onPause() {
		super.onPause();
		closeAccessory();
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mUsbReceiver);
		// sensorManager.unregisterListener(sensoreventlistener);
		super.onDestroy();
	}


	private static final float NS2S = 1.0f / 1000000000.0f;
	private double timestamp;
	private double[] angle = { 0, 0, 0 };
	private double Yaw = 0.0f, Pitch = 0.0f, Roll = 0.0f;
	private double Kp = 2.0f, Ki = 0.005f, Kd = 0.5f;
	private double exInt = 0.0f, eyInt = 0.0f, ezInt = 0.0f; // scaled integral
	// error
	private double q0 = 1.0f, q1 = 0.0f, q2 = 0.0f, q3 = 0.0f; // quaternion
	private SensorEventListener sensoreventlistener = new SensorEventListener() {
		private long acceTime;
		private long gyroTime;
		private long quatTime;
		private float[] acce = { 0, 0, 0, };
		private float[] gyro = { 0, 0, 0, };
		private float[] quat = { 0, 0, 0, 0 };
		private float[] euler = { 0, 0, 0 };

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			// System.out.println("===========ll---   ");
			float[] values = event.values;
			if (event.sensor == gyroSensor) {
				gyro[0] = values[0];
				gyro[1] = values[1];
				gyro[2] = values[2];
				this.gyroTime = event.timestamp;
			}

			if (event.sensor == acceSensor) {
				acce[0] = values[0];
				acce[1] = values[1];
				acce[2] = values[2];
				this.acceTime = event.timestamp;
			}
			if (event.sensor == quatSensor) {
				float[] quaternion = new float[4];
				float[] RotMat = new float[9];
				float[] ypr = new float[3];
				SensorManager.getQuaternionFromVector(quaternion, event.values);
				SensorManager.getRotationMatrixFromVector(RotMat, event.values);
				SensorManager.getOrientation(RotMat, ypr);
				quat[0] = quaternion[0];
				quat[1] = quaternion[1];
				quat[2] = quaternion[2];
				quat[3] = quaternion[3];
				euler[0] = ypr[0];
				euler[1] = ypr[1];
				euler[2] = ypr[2];
				this.quatTime = event.timestamp;
			}

			double dT = 0.0f;
			if (timestamp != 0) {
				dT = ((double) event.timestamp - timestamp) * NS2S;
				// * 57.3248407643312f;
				// angle[0] += values[0] * dT;
				// angle[1] += values[1] * dT;
				// angle[2] += values[2] * dT;
			}
			// IMUupdate(dT);

			// tv_X.setText("X:" + "\n" + "gyro:" + Float.toString(gyro[0]) +
			// "\n"
			// + "acce:" + Float.toString(acce[0]));
			// tv_Y.setText("Y:" + "\n" + "gyro:" + Float.toString(gyro[1]) +
			// "\n"
			// + "acce:" + Float.toString(acce[1]));
			// tv_Z.setText("Z:" + "\n" + "gyro:" + Float.toString(gyro[2]) +
			// "\n"
			// + "acce:" + Float.toString(acce[2]));
			tv_X.setText("Yaw  : " + Float.toString(euler[0] * 57.32484f));
			tv_Y.setText("Pitch: " + Float.toString(euler[1] * 57.32484f));
			tv_Z.setText("Row  : " + Float.toString(euler[2] * 57.32484f));

		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}

	};

	private int enableControl;

	

	private void openAccessory(UsbAccessory accessory) {
		mFileDescriptor = mUsbManager.openAccessory(accessory);

		if (mFileDescriptor != null) {
			mAccessory = accessory;
			FileDescriptor fd = mFileDescriptor.getFileDescriptor();

			mInputStream = new FileInputStream(fd);
			mOutputStream = new FileOutputStream(fd);

			// communication thread start
			Thread thread = new Thread(null, this, "DemoKit");
			thread.start();
			Log.d(TAG, "accessory opened");

			//			enableControls(true);
			enableControl = 1;
			getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
		} else {
			Log.d(TAG, "accessory open fail");
		}
	}

	private void closeAccessory() {
		//		enableControls(false);
		enableControl = 0;
		getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
		
		try {
			if (mFileDescriptor != null) {
				mFileDescriptor.close();
			}
		} catch (IOException e) {
		} finally {
			mFileDescriptor = null;
			mAccessory = null;
		}
	}

	//	private void enableControls(boolean enable) {
	//		if (enable) {
	//			mStatusView.setText("connected");
	//		} else {
	//			mStatusView.setText("not connect");
	//		}
	//		mToggleButton.setEnabled(enable);
	//	}

	
	private static final int MESSAGE_LED = 1;
	// USB read thread
	@Override
	public void run() {
		int ret = 0;
		byte[] buffer = new byte[16384];
		int i;

		// Accessory -> Android
		while (ret >= 0) {
			try {
				ret = mInputStream.read(buffer);
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}

			if (ret > 0) {
				Log.d(TAG, ret + " bytes message received.");
			}
			i = 0;
			while (i < ret) {
				int len = ret - i;

				switch (buffer[i]) {
				case 0x1:
					if (len >= 2) {
						Message m = Message.obtain(mHandler, MESSAGE_LED);
						m.arg1 = (int) buffer[i + 1];
						mHandler.sendMessage(m);
						i += 2;
					}
					break;

				default:
					Log.d(TAG, "unknown msg: " + buffer[i]);
					i = len;
					break;
				}
			}

		}
	}
	
	public void setHandler(Handler handler)
	{
		mHandler = handler;
	}

	

	// Android -> Accessory
	public void sendCommand(byte command, byte value) {
		byte[] buffer = new byte[2];
		buffer[0] = command;
		buffer[1] = value;
		if (mOutputStream != null) {
			try {
				mOutputStream.write(buffer);
			} catch (IOException e) {
				Log.e(TAG, "write failed", e);
			}
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuItem connection_status = menu.findItem(R.id.connection_status);
		if(enableControl == 1)
		{
			connection_status.setIcon(R.drawable.connect_48);
		}
		else
		{
			connection_status.setIcon(R.drawable.disconnect_2_48);
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		return super.onMenuItemSelected(featureId, item);
	}
}
