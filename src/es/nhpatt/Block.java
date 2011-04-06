package es.nhpatt;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Block extends Activity {

	WebView browser;

	boolean mCorner0;
	boolean mCorner1;
	boolean mCorner2;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
				Window.PROGRESS_VISIBILITY_ON);
		final CookieSyncManager cookieSyncMngr = CookieSyncManager
				.createInstance(getApplicationContext());
		cookieSyncMngr.startSync();
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
				Window.PROGRESS_VISIBILITY_ON);
		browser = (WebView) findViewById(R.id.browser);

		// WebView.enablePlatformNotifications();
		browser.setWebViewClient(new BrowserViewClient());

		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		browser.getSettings().setJavaScriptEnabled(true);
		// browser.getSettings().setBuiltInZoomControls(true);
		browser.getSettings().setSaveFormData(Boolean.FALSE);
		browser.getSettings().setSavePassword(Boolean.FALSE);

		final Activity MyActivity = this;
		browser.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(final WebView view, final int progress) {
				MyActivity.setTitle("Cargando, por favor espere...");
				if (progress == 100) {
					MyActivity.setTitle(R.string.app_name);
				}
			}
		});

		final PackageManager pm = getPackageManager();

		final IntentFilter filter = new IntentFilter();
		filter.addCategory("android.intent.category.HOME");
		filter.addCategory("android.intent.category.DEFAULT");

		final Context context = getApplicationContext();
		final ComponentName component = new ComponentName(context
				.getPackageName(), Block.class.getName());

		final ComponentName[] components = new ComponentName[] {
				new ComponentName("com.android.launcher",
						"com.android.launcher.Launcher"), component };

		pm.clearPackagePreferredActivities("com.android.launcher");
		pm.addPreferredActivity(filter, IntentFilter.MATCH_CATEGORY_EMPTY,
				components, component);

		final CookieManager cookieManager = CookieManager.getInstance();
		final String cookie = cookieManager.getCookie("fiturcastillayleon.com");

		if (cookie == null || !cookie.startsWith("SESS")) {
			final String postData = "name=tablet&pass=tablet&form_id=user_login";
			browser
					.postUrl(
							"http://www.fiturcastillayleon.com/feriaturiscyl/user/login",
							EncodingUtils.getBytes(postData, "BASE64"));
			try {
				Thread.sleep(2000);
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			browser.loadUrl(getSharedPreferences("gamePrefs", MODE_PRIVATE)
					.getString(Settings.SETTING_URL,
							getResources().getString(R.string.defaultURL)));
		} else {
			browser.loadUrl(getSharedPreferences("gamePrefs", MODE_PRIVATE)
					.getString(Settings.SETTING_URL,
							getResources().getString(R.string.defaultURL)));
		}

	}

	@Override
	public void onResume() {
		CookieSyncManager.getInstance().stopSync();
		super.onResume();
	}

	@Override
	public void onPause() {
		CookieSyncManager.getInstance().stopSync();
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	private class BrowserViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(final WebView view,
				final String url) {
			view.loadUrl(url);
			if (url.contains(".pdf")) {
				browser.stopLoading();
				browser
						.loadUrl("http://docs.google.com/gview?embedded=true&url="
								+ url);
			}
			return true;
		}

		@Override
		public void onPageFinished(final WebView view, final String url) {
			super.onPageFinished(view, url);
			if (url.equals("about:blank")
					|| url
							.startsWith("http://static.ak.fbcdn.net/connect/xd_proxy.php#")) {

				if (!url
						.startsWith("http://static.ak.fbcdn.net/connect/xd_proxy.php#")) {
					@SuppressWarnings("unused")
					final CookieSyncManager cookieSyncMngr = CookieSyncManager
							.createInstance(getApplicationContext());
					final CookieManager cookieManager = CookieManager
							.getInstance();
					final String cookie = cookieManager
							.getCookie("fiturcastillayleon.com");
					Field campo;
					try {
						campo = CookieManager.class
								.getDeclaredField("mCookieMap");
						campo.setAccessible(true);
						final LinkedHashMap matriz = (LinkedHashMap) campo
								.get(cookieManager);
						matriz.clear();
						// matriz.remove("facebook.com");
						// final ArrayList cookies = (ArrayList) matriz
						// .get("fiturcastillayleon.com");
						// while (!cookies.isEmpty()) {
						// cookies.remove(0);
						// Log.v("DEV", "COOKIES");
						// }

					} catch (final Exception e) {
						e.printStackTrace();
					}

					// cookieManager.removeAllCookie();
					cookieManager.setCookie("fiturcastillayleon.com", cookie);
					CookieSyncManager.getInstance().sync();
				}
				browser = (WebView) findViewById(R.id.browser);
				browser.loadUrl(getSharedPreferences("gamePrefs", MODE_PRIVATE)
						.getString(Settings.SETTING_URL, "www.google.es"));
			}
		}

		@Override
		public void onReceivedSslError(final WebView view,
				final SslErrorHandler handler, final SslError error) {
			handler.proceed();
		}

	}

	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && browser.canGoBack()) {
			browser.goBack();
		} else {
			final Context context = getApplicationContext();
			final CharSequence text = "Esta tecla está bloqueada";
			final int duration = Toast.LENGTH_SHORT;
			Toast.makeText(context, text, duration).show();
		}
		return true;
		// return super.onKeyDown(keyCode, event);
	}

	private boolean isCloseTo(final MotionEvent paramMotionEvent,
			final int paramInt1, final int paramInt2) {
		final float x = paramMotionEvent.getX();
		final float y = paramMotionEvent.getY();
		return paramInt1 - 70 < x && paramInt1 + 70 > x && paramInt2 + 70 > y
				&& paramInt2 - 70 < y;
	}

	private void cornerNumber(final MotionEvent paramMotionEvent) {
		if (isCloseTo(paramMotionEvent, 0, 0)) {
			mCorner0 = true;
		} else if (isCloseTo(paramMotionEvent, browser.getWidth(), 0)) {
			mCorner1 = true;
		} else if (isCloseTo(paramMotionEvent, browser.getWidth(), browser
				.getHeight())) {
			mCorner2 = true;
		} else {
			mCorner0 = false;
			mCorner1 = false;
			mCorner2 = false;
		}
	}

	@Override
	public boolean dispatchTouchEvent(final MotionEvent paramMotionEvent) {
		if (paramMotionEvent.getAction() == MotionEvent.ACTION_DOWN) {
			cornerNumber(paramMotionEvent);
			if (mCorner0 && mCorner1 && mCorner2) {
				startActivity(new Intent(getApplicationContext(),
						Settings.class));
				finish();
			}
		}
		return super.dispatchTouchEvent(paramMotionEvent);
	}
	// final String name = url.substring(url.lastIndexOf("/") + 1);
	// try {
	//
	// final URL url1 = new URL(url);
	// final InputStream myInput = url1.openConnection()
	// .getInputStream();
	//
	// final FileOutputStream fos = openFileOutput(name,
	// Context.MODE_WORLD_READABLE);
	//
	// // transfer bytes from the input file to the output file
	// final byte[] buffer = new byte[8192];
	// int length;
	// while ((length = myInput.read(buffer)) > 0) {
	// fos.write(buffer, 0, length);
	// }
	// fos.close();
	//
	// // read from disk, and call intent
	//
	// openFileInput(name);
	// } catch (final Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } // will throw FileNotFoundException
	//
	// final File dir = getFilesDir(); // where files are stored
	// final File file = new File(dir, name); // new file with our
	// // name
	//
	// final Intent intent = new Intent(Intent.ACTION_VIEW, Uri
	// .fromFile(file));
	// final Uri path = Uri.fromFile(file);
	// intent.setDataAndType(path, "application/pdf");
	// intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	//
	// try {
	// startActivity(intent);
	// } catch (final ActivityNotFoundException e) {
	// Toast.makeText(Block.this,
	// "No Application Available to View PDF",
	// Toast.LENGTH_SHORT).show();
	// }

}