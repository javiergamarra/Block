package es.nhpatt;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends Activity {

	public static final String SETTING_URL = "URL";

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		final EditText urlText = (EditText) findViewById(R.id.editText);
		urlText.setText(getSharedPreferences("gamePrefs", MODE_PRIVATE)
				.getString(SETTING_URL,
						getResources().getString(R.string.defaultURL)));

		final Button go = (Button) findViewById(R.id.go);
		go.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				final EditText urlText = (EditText) findViewById(R.id.editText);
				getSharedPreferences("gamePrefs", MODE_PRIVATE).edit()
						.putString(SETTING_URL, urlText.getText().toString())
						.commit();
				startActivity(new Intent(getApplicationContext(), Block.class));
				finish();
			}
		});
		final Activity myActivity = this;
		final Button eliminar = (Button) findViewById(R.id.eliminar);
		eliminar.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				trimCache();
				final CookieManager cookieManager = CookieManager.getInstance();
				CookieSyncManager.getInstance();
				cookieManager.removeAllCookie();
				Toast.makeText(myActivity, "Eliminados", Toast.LENGTH_SHORT)
						.show();
			}
		});

		final Button salir = (Button) findViewById(R.id.salir);
		salir.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				final PackageManager pm = getPackageManager();
				pm.clearPackagePreferredActivities(getResources().getString(
						R.string.packages));
				finish();
			}
		});

	}

	public void trimCache() {
		try {
			final File dir = getApplication().getCacheDir();
			if (dir != null && dir.isDirectory()) {
				deleteDir(dir);
			}
		} catch (final Exception e) {
		}
	}

	public boolean deleteDir(final File dir) {
		if (dir != null && dir.isDirectory()) {
			final String[] children = dir.list();
			for (final String element : children) {
				final boolean success = deleteDir(new File(dir, element));
				if (!success) {
					return false;
				}
			}
		}
		// The directory is now empty so delete it
		return dir.delete();
	}
}
