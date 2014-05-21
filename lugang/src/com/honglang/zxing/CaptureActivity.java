/*
 * Copyright (C) 2008 ZXing authors
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

package com.honglang.zxing;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.result.ResultParser;
import com.honglang.lugang.Constant;
import com.honglang.lugang.R;
import com.honglang.lugang.SessionManager;
import com.honglang.lugang.billsearch.SearchActivity;
import com.honglang.lugang.login.LoginActivity;
import com.honglang.lugang.office.DealingActivity;
import com.honglang.lugang.office.OrderActivity;
import com.honglang.lugang.out.OutActivity;
import com.honglang.lugang.out.OutListActivity;
import com.honglang.lugang.out.PreviewActivity;
import com.honglang.lugang.qrcode.BlankActivity;
import com.honglang.lugang.qrcode.InActivity;
import com.honglang.zxing.camera.CameraManager;
import com.honglang.zxing.history.HistoryManager;
import com.honglang.zxing.result.ResultHandler;
import com.honglang.zxing.result.ResultHandlerFactory;
import com.honglang.zxing.result.TextResultHandler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
@SuppressLint("NewApi")
public final class CaptureActivity extends Activity implements OnClickListener,
		SurfaceHolder.Callback {

	private TextView title;
	private Button back;
//	private Button ok;
	private int TYPE;
	
	private LinearLayout linear;
	private LinearLayout jf;
	private LinearLayout pc;
	private TextView qsno;
	private TextView jfCount;
	private TextView jfRecords;
	private TextView jfCode;
	private TextView pcno;
	private TextView pcCount;
	private TextView pcRecords;
	private TextView pcCode;
	private TextView count;
	private TextView record;
	private Button sure;
	
	private String uniqueKey;
	private String fhCode;
	private String jfNum;
	private String pcNum;
	
	private boolean isExist = false;
	
	private SoundPool soundPool;
	
	private ProgressDialog progress;

	private static final String TAG = CaptureActivity.class.getSimpleName();

	private static final long DEFAULT_INTENT_RESULT_DURATION_MS = 1500L;
	private static final long BULK_MODE_SCAN_DELAY_MS = 1000L;

	private static final String[] ZXING_URLS = {
			"http://zxing.appspot.com/scan", "zxing://scan/" };

	public static final int HISTORY_REQUEST_CODE = 0x0000bacc;

	private static final Collection<ResultMetadataType> DISPLAYABLE_METADATA_TYPES = EnumSet
			.of(ResultMetadataType.ISSUE_NUMBER,
					ResultMetadataType.SUGGESTED_PRICE,
					ResultMetadataType.ERROR_CORRECTION_LEVEL,
					ResultMetadataType.POSSIBLE_COUNTRY);

	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private Result savedResultToShow;
	private ViewfinderView viewfinderView;
	private TextView statusView;
	// private View resultView;
	private Result lastResult;
	private boolean hasSurface;
	private boolean copyToClipboard;
	private IntentSource source;
	private String sourceUrl;
	// private ScanFromWebPageManager scanFromWebPageManager;
	private Collection<BarcodeFormat> decodeFormats;
	private Map<DecodeHintType, ?> decodeHints;
	private String characterSet;
	private HistoryManager historyManager;
	private InactivityTimer inactivityTimer;
	private BeepManager beepManager;
	private AmbientLightManager ambientLightManager;
	
	private String pcId = "";

	ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	CameraManager getCameraManager() {
		return cameraManager;
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_capture);
		
		title = (TextView) this.findViewById(R.id.title);
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
//		ok = (Button) this.findViewById(R.id.ok);
//		ok.setText("查询");
//		ok.setVisibility(View.VISIBLE);
//		ok.setOnClickListener(this);
		
		linear = (LinearLayout) findViewById(R.id.out);
		jf = (LinearLayout) findViewById(R.id.jf);
		jf.setOnClickListener(this);
		pc = (LinearLayout) findViewById(R.id.pc);
		pc.setOnClickListener(this);
		
		qsno = (TextView) findViewById(R.id.qsno);
		jfCount = (TextView) findViewById(R.id.jfCount);
		jfRecords = (TextView) findViewById(R.id.jfRecords);
		jfCode = (TextView) findViewById(R.id.jfCode);
		pcno = (TextView) findViewById(R.id.pcno);
		pcCount = (TextView) findViewById(R.id.pcCount);
		pcRecords = (TextView) findViewById(R.id.pcRecords);
		pcCode = (TextView) findViewById(R.id.pcCode);
		count = (TextView) findViewById(R.id.count);
		record = (TextView) findViewById(R.id.record);
		sure = (Button) findViewById(R.id.sure);
		sure.setOnClickListener(this);
		
		soundPool = new SoundPool(4, AudioManager.STREAM_SYSTEM, 5);
		soundPool.load(this, R.raw.success, 1);
		soundPool.load(this, R.raw.done, 1);
		soundPool.load(this, R.raw.both, 1);
		soundPool.load(this, R.raw.failed, 1);
		
		TYPE = this.getIntent().getExtras().getInt("QRTYPE");
		switch (TYPE) {
		case 0:
			title.setText("扫描二维码出库");
			break;
		case 1:
			title.setText("扫描二维码入库");
			break;
		case 2:
			title.setText("扫描空白托运单");
			break;
		case 3:
			title.setText("扫描查询运单");
			break;
		case 4:
			title.setText("扫描进入货物确认");
			break;
		case 5:
			title.setText("扫描二维码添加货物");
			break;
		case 6:
			title.setText("货物出库");
			linear.setVisibility(View.VISIBLE);
			new NewTask().execute((Void)null);
			break;
		}
		
		hasSurface = false;
		historyManager = new HistoryManager(this);
		historyManager.trimHistory();
		inactivityTimer = new InactivityTimer(this);
		beepManager = new BeepManager(this);
		ambientLightManager = new AmbientLightManager(this);

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// CameraManager must be initialized here, not in onCreate(). This is
		// necessary because we don't
		// want to open the camera driver and measure the screen size if we're
		// going to show the help on
		// first launch. That led to bugs where the scanning rectangle was the
		// wrong size and partially
		// off screen.
		cameraManager = new CameraManager(getApplication());

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		viewfinderView.setCameraManager(cameraManager);

		// resultView = findViewById(R.id.result_view);
		statusView = (TextView) findViewById(R.id.status_view);

		handler = null;
		lastResult = null;

		resetStatusView();

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			// The activity was paused but not stopped, so the surface still
			// exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(surfaceHolder);
		} else {
			// Install the callback and wait for surfaceCreated() to init the
			// camera.
			surfaceHolder.addCallback(this);
		}

		beepManager.updatePrefs();
		ambientLightManager.start(cameraManager);

		inactivityTimer.onResume();

		Intent intent = getIntent();

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		copyToClipboard = prefs.getBoolean(
				PreferencesActivity.KEY_COPY_TO_CLIPBOARD, true)
				&& (intent == null || intent.getBooleanExtra(
						Intents.Scan.SAVE_HISTORY, true));

		source = IntentSource.NONE;
		decodeFormats = null;
		characterSet = null;

		if (intent != null) {

			String action = intent.getAction();
			String dataString = intent.getDataString();

			if (Intents.Scan.ACTION.equals(action)) {

				// Scan the formats the intent requested, and return the result
				// to the calling activity.
				source = IntentSource.NATIVE_APP_INTENT;
				decodeFormats = DecodeFormatManager.parseDecodeFormats(intent);
				decodeHints = DecodeHintManager.parseDecodeHints(intent);

				if (intent.hasExtra(Intents.Scan.WIDTH)
						&& intent.hasExtra(Intents.Scan.HEIGHT)) {
					int width = intent.getIntExtra(Intents.Scan.WIDTH, 0);
					int height = intent.getIntExtra(Intents.Scan.HEIGHT, 0);
					if (width > 0 && height > 0) {
						cameraManager.setManualFramingRect(width, height);
					}
				}

				String customPromptMessage = intent
						.getStringExtra(Intents.Scan.PROMPT_MESSAGE);
				if (customPromptMessage != null) {
					statusView.setText(customPromptMessage);
				}

			} else if (dataString != null
					&& dataString.contains("http://www.google")
					&& dataString.contains("/m/products/scan")) {

				// Scan only products and send the result to mobile Product
				// Search.
				source = IntentSource.PRODUCT_SEARCH_LINK;
				sourceUrl = dataString;
				decodeFormats = DecodeFormatManager.PRODUCT_FORMATS;

			} else if (isZXingURL(dataString)) {

				// Scan formats requested in query string (all formats if none
				// specified).
				// If a return URL is specified, send the results there.
				// Otherwise, handle it ourselves.
				source = IntentSource.ZXING_LINK;
				sourceUrl = dataString;
				Uri inputUri = Uri.parse(dataString);
				// scanFromWebPageManager = new
				// ScanFromWebPageManager(inputUri);
				decodeFormats = DecodeFormatManager
						.parseDecodeFormats(inputUri);
				// Allow a sub-set of the hints to be specified by the caller.
				decodeHints = DecodeHintManager.parseDecodeHints(inputUri);

			}

			characterSet = intent.getStringExtra(Intents.Scan.CHARACTER_SET);

		}
	}

	private static boolean isZXingURL(String dataString) {
		if (dataString == null) {
			return false;
		}
		for (String url : ZXING_URLS) {
			if (dataString.startsWith(url)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		ambientLightManager.stop();
		cameraManager.closeDriver();
		if (!hasSurface) {
			SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (source == IntentSource.NATIVE_APP_INTENT) {
				setResult(RESULT_CANCELED);
				finish();
				return true;
			}
			if ((source == IntentSource.NONE || source == IntentSource.ZXING_LINK)
					&& lastResult != null) {
				restartPreviewAfterDelay(0L);
				return true;
			}
			break;
		case KeyEvent.KEYCODE_FOCUS:
		case KeyEvent.KEYCODE_CAMERA:
			// Handle these events so they don't launch the Camera app
			return true;
			// Use volume up/down to turn on light
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			cameraManager.setTorch(false);
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			cameraManager.setTorch(true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater menuInflater = getMenuInflater();
//		// menuInflater.inflate(R.menu.capture, menu);
//		return super.onCreateOptionsMenu(menu);
//	}

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// Intent intent = new Intent(Intent.ACTION_VIEW);
	// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
	// switch (item.getItemId()) {
	// // case R.id.menu_share:
	// // intent.setClassName(this, ShareActivity.class.getName());
	// // startActivity(intent);
	// // break;
	// // case R.id.menu_history:
	// // intent.setClassName(this, HistoryActivity.class.getName());
	// // startActivityForResult(intent, HISTORY_REQUEST_CODE);
	// // break;
	// // case R.id.menu_settings:
	// // intent.setClassName(this, PreferencesActivity.class.getName());
	// // startActivity(intent);
	// // break;
	// // case R.id.menu_help:
	// // intent.setClassName(this, HelpActivity.class.getName());
	// // startActivity(intent);
	// // break;
	// default:
	// return super.onOptionsItemSelected(item);
	// }
	// return false;
	// }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK) {
			if (requestCode == HISTORY_REQUEST_CODE) {
				int itemNumber = intent.getIntExtra(
						Intents.History.ITEM_NUMBER, -1);
				if (itemNumber >= 0) {
					// HistoryItem historyItem =
					// historyManager.buildHistoryItem(itemNumber);
					// decodeOrStoreSavedBitmap(null, historyItem.getResult());
				}
			}
		}
		//PeiCheChuKuAdd again
		if (requestCode == 200) {
			if (resultCode == RESULT_OK) {
				pcId = intent.getStringExtra("pcid");
				Log.i("suxoyo", "3-pcid="+pcId);
			}
		}
	}

	private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
		// Bitmap isn't used yet -- will be used soon
		if (handler == null) {
			savedResultToShow = result;
		} else {
			if (result != null) {
				savedResultToShow = result;
			}
			if (savedResultToShow != null) {
				Message message = Message.obtain(handler,
						R.id.decode_succeeded, savedResultToShow);
				handler.sendMessage(message);
			}
			savedResultToShow = null;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG,
					"*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	/**
	 * A valid barcode has been found, so give an indication of success and show
	 * the results.
	 * 
	 * @param rawResult
	 *            The contents of the barcode.
	 * @param scaleFactor
	 *            amount by which thumbnail was scaled
	 * @param barcode
	 *            A greyscale bitmap of the camera data which was decoded.
	 */
	public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
		inactivityTimer.onActivity();
		lastResult = rawResult;
		// ResultHandler resultHandler =
		// ResultHandlerFactory.makeResultHandler(this, rawResult);
		ResultHandler resultHandler = new TextResultHandler(this,
				ResultParser.parseResult(rawResult), rawResult);

		boolean fromLiveScan = barcode != null;
		if (fromLiveScan) {
			historyManager.addHistoryItem(rawResult, resultHandler);
			// Then not from history, so beep/vibrate and we have an image to
			beepManager.playBeepSoundAndVibrate();
			
			switch (TYPE) {
			case 0:
				Intent i = new Intent(CaptureActivity.this, OutActivity.class);
				i.putExtra("from", 0);
				i.putExtra("pcId", pcId);
				i.putExtra("fhCode", rawResult.getText());
				this.startActivityForResult(i, 200);
				break;
			case 1:
				Intent i1 = new Intent(CaptureActivity.this, InActivity.class);
				i1.putExtra("fhCode", rawResult.getText());
				this.startActivity(i1);
				break;
			case 2:
				Intent i2 = new Intent(CaptureActivity.this, BlankActivity.class);
				i2.putExtra("fhCode", rawResult.getText());
				this.startActivity(i2);
				break;
			case 3:
				Intent i3 = new Intent(CaptureActivity.this, SearchActivity.class);
				i3.putExtra("number", rawResult.getText());
				this.startActivity(i3);
				break;
			case 4:
				Intent i4 = new Intent(CaptureActivity.this, OrderActivity.class);
				i4.putExtra("scan", true);
				i4.putExtra("fhCode", rawResult.getText());
				this.startActivity(i4);
				break;
			case 5:
				Intent i5 = new Intent();
				i5.putExtra("fhCode", rawResult.getText());
				setResult(RESULT_OK,i5);
				finish();
				break;
			case 6:
				if (isExist) {
					fhCode = rawResult.getText();
					new OutTask().execute((Void)null);
				}
				break;
			}
			// Log.i("suxoyo", "handleDecode="+rawResult.getText());
		}

	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG,
					"initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a
			// RuntimeException.
			if (handler == null) {
				handler = new CaptureActivityHandler(this, decodeFormats,
						decodeHints, characterSet, cameraManager);
			}
			decodeOrStoreSavedBitmap(null, null);
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializing camera", e);
			displayFrameworkBugMessageAndExit();
		}
	}

	private void displayFrameworkBugMessageAndExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage(getString(R.string.msg_camera_framework_bug));
		builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
		builder.setOnCancelListener(new FinishListener(this));
		builder.show();
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
		resetStatusView();
	}

	private void resetStatusView() {
		// resultView.setVisibility(View.GONE);
		statusView.setText(R.string.msg_default_status);
		statusView.setVisibility(View.VISIBLE);
		viewfinderView.setVisibility(View.VISIBLE);
		lastResult = null;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
//		case R.id.ok:
//			startActivity(new Intent(this, OutListActivity.class));
//			break;
		case R.id.jf:
			Intent i1 = new Intent(this, PreviewActivity.class);
			i1.putExtra("type", 1);
			i1.putExtra("keycode", jfNum);
			startActivity(i1);
			break;
		case R.id.pc:
			Intent i2 = new Intent(this, PreviewActivity.class);
			i2.putExtra("type", 2);
			i2.putExtra("keycode", pcNum);
			startActivity(i2);
			break;
		case R.id.sure:
			new EndTask().execute((Void)null);
			break;
		}
	}
	
	class NewTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		@Override
		protected void onPreExecute() {
			if (progress != null) {
				progress = null;
			}
			progress = ProgressDialog.show(CaptureActivity.this, null, "正在生成出库单号...", false, false);
			
			super.onPreExecute();
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, "ChuKuDanNew");
			rpc.addProperty("currentUserno", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + "ChuKuDanNew", envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("ChuKuDanNewResult"));
					JSONObject obj = (JSONObject) parser.nextValue();
					Log.i("suxoyo", obj.toString());
					if (obj.getBoolean("result")) {
						JSONObject data = obj.getJSONObject("data");
						jfNum = data.getString("qsno");
						pcNum = data.getString("pcno");
						uniqueKey = data.getString("uniqueKey");
						return true;
					} else {
						errMsg = obj.getString("msg");
						return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
//				errMsg = e.toString();
				errMsg = "操作失败，请稍候重试";
			}
			return false;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
			if (result) {
				qsno.setText(jfNum);
				pcno.setText(pcNum);
				isExist = true;
			} else {
				Toast.makeText(CaptureActivity.this, errMsg, Toast.LENGTH_LONG).show();
				if (errMsg.equals("请先登录")) {
					Intent intent = new Intent(CaptureActivity.this, LoginActivity.class);
					intent.putExtra("dir", 1);
					CaptureActivity.this.startActivity(intent);
				}
				CaptureActivity.this.finish();
			}
			
			super.onPostExecute(result);
		}
		
	}
	
	class OutTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		private boolean isJf;
		private boolean isPc;
		int strCount;
		int strRec;
		
		int strCount1;
		int strRec1;
		
		@Override
		protected void onPreExecute() {
			if (handler != null) {
				handler.quitSynchronously();
				handler = null;
			}
			inactivityTimer.onPause();
			ambientLightManager.stop();
			cameraManager.closeDriver();
			if (!hasSurface) {
				SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
				SurfaceHolder surfaceHolder = surfaceView.getHolder();
				surfaceHolder.removeCallback(CaptureActivity.this);
			}
			
			if (progress != null) {
				progress = null;
			}
			progress = ProgressDialog.show(CaptureActivity.this, null, "正在执行操作...", false, false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, "ChuKu");
			rpc.addProperty("currentUserno", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			rpc.addProperty("uniqueKey", uniqueKey);
			rpc.addProperty("fhcode", fhCode);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + "ChuKu", envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("ChuKuResult"));
					JSONObject obj = (JSONObject) parser.nextValue();
					Log.i("suxoyo", obj.toString());
					if (obj.getBoolean("result")) {
						JSONObject data = obj.getJSONObject("data");
						isJf = data.getBoolean("jf");
						isPc = data.getBoolean("pc");
						strCount = data.getInt("jfCount");
						strRec = data.getInt("jfRecords");
						strCount1 = data.getInt("pcCount");
						strRec1 = data.getInt("pcRecords");
						return true;
					} else {
						errMsg = obj.getString("msg");
						return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
//				errMsg = e.toString();
				errMsg = "操作失败，请稍候重试";
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
			if (result) {
				if (isJf && isPc) {
					soundPool.play(3,1, 1, 0, 0, 1);
					jfCode.setText(fhCode);
					jfCount.setText("数量："+strCount+"票");
					jfRecords.setText("共计"+strRec+"条货物记录");
					
					pcCode.setText(fhCode);
					pcCount.setText("数量："+strCount1+"票");
					pcRecords.setText("共计"+strRec1+"条货物记录");
					
					count.setText("合计："+(strCount+strCount1)+"票");
					record.setText("总货物记录数："+(strRec+strRec1)+"条");
					
					jf.setBackgroundDrawable(getResources().getDrawable(android.R.color.holo_orange_light));
					pc.setBackgroundDrawable(getResources().getDrawable(android.R.color.holo_orange_light));
				} else {
					if (isJf) {
						soundPool.play(1,1, 1, 0, 0, 1);
						jfCode.setText(fhCode);
						jfCount.setText("数量："+strCount+"票");
						jfRecords.setText("共计"+strRec+"条货物记录");
						
						count.setText("合计："+(strCount+strCount1)+"票");
						record.setText("总货物记录数："+(strRec+strRec1)+"条");
						
//						jf.setBackground(getResources().getDrawable(android.R.color.holo_green_light));
						jf.setBackgroundDrawable(getResources().getDrawable(android.R.color.holo_orange_light));
					}
					if (isPc) {
						soundPool.play(2,1, 1, 0, 0, 1);
						pcCode.setText(fhCode);
						pcCount.setText("数量："+strCount1+"票");
						pcRecords.setText("共计"+strRec1+"条货物记录");
						
						count.setText("合计："+(strCount+strCount1)+"票");
						record.setText("总货物记录数："+(strRec+strRec1)+"条");
						
						pc.setBackgroundDrawable(getResources().getDrawable(android.R.color.holo_orange_light));
					}
				}
//				new Handler().postDelayed(r, 3000);
				h.removeCallbacks(r);
				h.postDelayed(r, 3000);
				
			} else {
				Toast.makeText(CaptureActivity.this, errMsg, Toast.LENGTH_LONG).show();
				soundPool.play(4,1, 1, 0, 0, 1);
				if (errMsg.equals("请先登录")) {
					Intent intent = new Intent(CaptureActivity.this, LoginActivity.class);
					intent.putExtra("dir", 1);
					CaptureActivity.this.startActivity(intent);
				}
			}
			
			resetStatusView();
			handler = null;
			lastResult = null;

			resetStatusView();

			SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			if (hasSurface) {
				// The activity was paused but not stopped, so the surface still
				// exists. Therefore
				// surfaceCreated() won't be called, so init the camera here.
				initCamera(surfaceHolder);
			} else {
				// Install the callback and wait for surfaceCreated() to init the
				// camera.
				surfaceHolder.addCallback(CaptureActivity.this);
			}

			beepManager.updatePrefs();
			ambientLightManager.start(cameraManager);

			inactivityTimer.onResume();

			super.onPostExecute(result);
		}
		
	}
	
	Runnable r = new Runnable() {
		
		@Override
		public void run() {
			jf.setBackgroundDrawable(getResources().getDrawable(R.drawable.linearlayout));
			pc.setBackgroundDrawable(getResources().getDrawable(R.drawable.linearlayout));
		}
	};
	Handler h = new Handler();

	class EndTask extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected void onPreExecute() {
			if (progress != null) {
				progress = null;
			}
			progress = ProgressDialog.show(CaptureActivity.this, null, "正在执行操作...", false, false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, "ChuKuEnd");
			rpc.addProperty("currentUserno", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			rpc.addProperty("uniqueKey", uniqueKey);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + "ChuKuEnd", envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("ChuKuEndResult"));
					JSONObject obj = (JSONObject) parser.nextValue();
					Log.i("suxoyo", obj.toString());
					if (obj.getBoolean("result")) {
						return true;
					} else {
//						errMsg = obj.getString("msg");
						return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
//				errMsg = e.toString();
//				errMsg = "操作失败，请稍候重试";
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
			Toast.makeText(CaptureActivity.this, "操作完成", Toast.LENGTH_SHORT).show();
			CaptureActivity.this.finish();
			super.onPostExecute(result);
		}
		
	}
	
}
