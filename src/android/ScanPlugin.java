package ScanPlugin;

import java.util.HashMap;
import java.util.Map;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.seuic.scanner.DecodeCallback;
import com.seuic.scanner.Scanner;
import com.seuic.scanner.ScannerFactory;
import com.seuic.scanner.ScannerKey;

/**
 * This class echoes a string called from JavaScript.
 */
public class ScanPlugin extends CordovaPlugin implements DecodeCallback {
	public static final String TAG = "ScanPlugin";
	private Scanner mScanner;
	private Context mContext;
	private Activity mActivity;
	private CallbackContext mCallback = null;
	private InputMethodManager imm;
	private SoundPool soundPool;
	private Map<String, Integer> map = new HashMap<String, Integer>();
	private Uri uri;
	private Ringtone rt;

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		if (action.equals("getCode")) {
			this.mCallback = callbackContext;
			return true;
		} else if (action.equals("closeScan")) {
			this.mCallback = null;
			this.mCallback.success();
			return true;
		} else if (action.equals("closeKeyboard")) {
			if (imm != null) {
				imm.hideSoftInputFromWindow(mActivity.getWindow()
						.getCurrentFocus().getWindowToken(), 0);
				return true;
			}
		} else if (action.equals("playOk")) {
			soundPool.play(map.get("ok"), 1.0f, 1.0f, 0, 0, 1);
			return true;
		} else if (action.equals("playError")) {
			soundPool.play(map.get("error"), 1.0f, 1.0f, 0, 0, 1);
			return true;
		} else if (action.equals("playSys")) {
			rt.play();
			return true;
		}
		return false;
	}

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		// TODO Auto-generated method stub
		super.initialize(cordova, webView);
		mActivity = cordova.getActivity();
		mContext = cordova.getActivity().getApplicationContext();
		soundPool = new SoundPool(2, AudioManager.STREAM_NOTIFICATION, 100);
		map.put("ok", soundPool.load(mActivity,mActivity.getResources().getIdentifier("ok", "raw",mActivity.getPackageName()), 1));
		map.put("error", soundPool.load(mActivity,mActivity.getResources().getIdentifier("error", "raw",mActivity.getPackageName()), 1));
		uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);// 绯荤粺鑷甫鎻愮ず闊�
		rt = RingtoneManager.getRingtone(mActivity, uri);
		mScanner = ScannerFactory.getScanner(mContext);
		mScanner.open();
		imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		new Thread(runnable).start();
		mScanner.setDecodeCallBack(new DecodeCallback() {
			@Override
			public void onDecodeComplete(String arg0) {
				if (mCallback != null) {
					PluginResult result = new PluginResult(
							PluginResult.Status.OK, arg0);
					result.setKeepCallback(true);
					mCallback.sendPluginResult(result);
				}
			}

		});
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			int ret1 = ScannerKey.open();
			if (ret1 > -1) {
				while (true) {
					int ret = ScannerKey.getKeyEvent();
					if (ret > -1) {
						switch (ret) {
						case ScannerKey.KEY_DOWN:
							mScanner.startScan();
							break;
						}
					}
				}
			}
		}
	};

	@Override
	public void onDecodeComplete(String arg0) {
		Toast.makeText(mContext, "2:" + arg0, Toast.LENGTH_SHORT).show();
		// barcode = arg0;

	}

}
