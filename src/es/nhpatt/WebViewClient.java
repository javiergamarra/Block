package es.nhpatt;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

public class WebViewClient {

	public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
		return false;
	}

	public void onPageStarted(final WebView view, final String url,
			final Bitmap favicon) {
	}

	public void onPageFinished(final WebView view, final String url) {
	}

	public void onLoadResource(final WebView view, final String url) {
	}

	public void onTooManyRedirects(final WebView view, final Message cancelMsg,
			final Message continueMsg) {
		cancelMsg.sendToTarget();
	}

	public static final int ERROR_UNKNOWN = -1;
	public static final int ERROR_HOST_LOOKUP = -2;
	public static final int ERROR_UNSUPPORTED_AUTH_SCHEME = -3;
	public static final int ERROR_AUTHENTICATION = -4;
	public static final int ERROR_PROXY_AUTHENTICATION = -5;
	public static final int ERROR_CONNECT = -6;
	public static final int ERROR_IO = -7;
	public static final int ERROR_TIMEOUT = -8;
	public static final int ERROR_REDIRECT_LOOP = -9;
	public static final int ERROR_UNSUPPORTED_SCHEME = -10;
	public static final int ERROR_FAILED_SSL_HANDSHAKE = -11;
	public static final int ERROR_BAD_URL = -12;
	public static final int ERROR_FILE = -13;
	public static final int ERROR_FILE_NOT_FOUND = -14;
	public static final int ERROR_TOO_MANY_REQUESTS = -15;

	public void onReceivedError(final WebView view, final int errorCode,
			final String description, final String failingUrl) {
	}

	public void onFormResubmission(final WebView view,
			final Message dontResend, final Message resend) {
		dontResend.sendToTarget();
	}

	public void doUpdateVisitedHistory(final WebView view, final String url,
			final boolean isReload) {
	}

	public void onReceivedSslError(final WebView view,
			final SslErrorHandler handler, final SslError error) {
		handler.cancel();
	}

	public void onReceivedHttpAuthRequest(final WebView view,
			final HttpAuthHandler handler, final String host, final String realm) {
		handler.cancel();
	}

	public boolean shouldOverrideKeyEvent(final WebView view,
			final KeyEvent event) {
		return false;
	}

	public void onUnhandledKeyEvent(final WebView view, final KeyEvent event) {
	}

	public void onScaleChanged(final WebView view, final float oldScale,
			final float newScale) {
	}
}
