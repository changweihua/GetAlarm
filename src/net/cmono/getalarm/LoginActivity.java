package net.cmono.getalarm;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import net.cmono.consts.ConstValue;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Android login screen Activity
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {

	private static final String DUMMY_CREDENTIALS = "changweihua@outlook.com:password";

	private UserLoginTask userLoginTask = null;
	private View loginFormView;
	private View progressView;
	private AutoCompleteTextView emailTextView;
	private EditText passwordTextView;
	private TextView signUpTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		CrashReport.setUserSceneTag(LoginActivity.this, 3329); // 上报后的Crash会显示该标签
		emailTextView = (AutoCompleteTextView) findViewById(R.id.email);
		loadAutoComplete();
		 Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		 emailTextView.startAnimation(shake);
		passwordTextView = (EditText) findViewById(R.id.password);
		passwordTextView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == EditorInfo.IME_NULL) {
							initLogin();
							return true;
						}
						return false;
					}
				});

		Button loginButton = (Button) findViewById(R.id.email_sign_in_button);
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				try {
					initLogin();
					// ...
					// throw new Exception("测试异常标签");
				} catch (Throwable thr) {
					CrashReport.postCatchedException(thr); // bugly会将这个throwable上报
				}
				// initLogin();
			}
		});

		SharedPreferences sharedPreferences = getSharedPreferences(
				ConstValue.LOGON_PF, MODE_PRIVATE);
		String userName = sharedPreferences.getString("USER_NAME", "");
		String userPwd = sharedPreferences.getString("PASSWORD", "");

		emailTextView.setText(userName);
		passwordTextView.setText(userPwd);

		loginFormView = findViewById(R.id.login_form);
		progressView = findViewById(R.id.login_progress);

		// adding underline and link to signup textview
		signUpTextView = (TextView) findViewById(R.id.signUpTextView);
		signUpTextView.setPaintFlags(signUpTextView.getPaintFlags()
				| Paint.UNDERLINE_TEXT_FLAG);
		Linkify.addLinks(signUpTextView, Linkify.ALL);

		signUpTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("LoginActivity", "Sign Up Activity activated.");
				// this is where you should start the signup Activity
				// LoginActivity.this.startActivity(new
				// Intent(LoginActivity.this, SignupActivity.class));
			}
		});
	}

	private void loadAutoComplete() {
		// getLoaderManager().initLoader(0, null, this);
	}

	/**
	 * Validate Login form and authenticate.
	 */
	public void initLogin() {
		if (userLoginTask != null) {
			return;
		}

		emailTextView.setError(null);
		passwordTextView.setError(null);

		String email = emailTextView.getText().toString();
		String password = passwordTextView.getText().toString();

		boolean cancelLogin = false;
		View focusView = null;

		if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
			passwordTextView.setError(getString(R.string.invalid_password));
			focusView = passwordTextView;
			cancelLogin = true;
		}

		if (TextUtils.isEmpty(email)) {
			emailTextView.setError(getString(R.string.field_required));
			focusView = emailTextView;
			cancelLogin = true;
		} else if (!isEmailValid(email)) {
			emailTextView.setError(getString(R.string.invalid_email));
			focusView = emailTextView;
			cancelLogin = true;
		}

		if (cancelLogin) {
			// error in login
			focusView.requestFocus();
		} else {
			// show progress spinner, and start background task to login
			showProgress(true);
			userLoginTask = new UserLoginTask(email, password, true, false);
			userLoginTask.execute((Void) null);
		}
	}

	private boolean isEmailValid(String email) {
		// add your own logic
		return email.contains("@");
	}

	private boolean isPasswordValid(String password) {
		// add your own logic
		return password.length() > 4;
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			loginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							loginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});

			progressView.setVisibility(show ? View.VISIBLE : View.GONE);
			progressView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							progressView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			progressView.setVisibility(show ? View.VISIBLE : View.GONE);
			loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		return new CursorLoader(this,
				// Retrieve data rows for the device user's 'profile' contact.
				Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
						ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
				ProfileQuery.PROJECTION,

				// Select only email addresses.
				ContactsContract.Contacts.Data.MIMETYPE + " = ?",
				new String[] { ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE },

				// Show primary email addresses first. Note that there won't be
				// a primary email address if the user hasn't specified one.
				ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		List<String> emails = new ArrayList<String>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			emails.add(cursor.getString(ProfileQuery.ADDRESS));
			cursor.moveToNext();
		}

		addEmailsToAutoComplete(emails);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursorLoader) {

	}

	private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
		// Create adapter to tell the AutoCompleteTextView what to show in its
		// dropdown list.
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				LoginActivity.this,
				android.R.layout.simple_dropdown_item_1line,
				emailAddressCollection);

		emailTextView.setAdapter(adapter);
	}

	private interface ProfileQuery {
		String[] PROJECTION = { ContactsContract.CommonDataKinds.Email.ADDRESS,
				ContactsContract.CommonDataKinds.Email.IS_PRIMARY, };

		int ADDRESS = 0;
		int IS_PRIMARY = 1;
	}

	/**
	 * Async Login Task to authenticate
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

		private final String emailStr;
		private final String passwordStr;
		private final boolean isRemember;
		private final boolean isAutoLogin;

		UserLoginTask(String email, String password, boolean remember,
				boolean autoLogin) {
			emailStr = email;
			passwordStr = password;
			isRemember = remember;
			isAutoLogin = autoLogin;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// this is where you should write your authentication code
			// or call external service
			// following try-catch just simulates network access
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}

			// using a local dummy credentials store to authenticate
			String[] pieces = DUMMY_CREDENTIALS.split(":");
			if (pieces[0].equals(emailStr) && pieces[1].equals(passwordStr)) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			userLoginTask = null;
			// stop the progress spinner
			showProgress(false);

			if (success) {
				// login success and move to main Activity here.
				SharedPreferences sharedPreferences = getSharedPreferences(
						ConstValue.LOGON_PF, MODE_PRIVATE);

				sharedPreferences.edit().putString("USER_NAME", emailStr)
						.commit();
				sharedPreferences.edit().putString("PASSWORD", passwordStr)
						.commit();
				sharedPreferences.edit().putBoolean("Remember", isRemember)
						.commit();
				sharedPreferences.edit().putBoolean("AutoLogin", isAutoLogin)
						.commit();

				GetAlarmApplication.putSession(true);
				Intent mIntent = new Intent();
				Bundle b = new Bundle();
				b.putBoolean("LoignResult", true);
				mIntent.putExtras(b);
				mIntent.setClass(LoginActivity.this, SplashActivity.class);
				LoginActivity.this.startActivity(mIntent);
				LoginActivity.this.finish();
			} else {
				// login failure
				passwordTextView
						.setError(getString(R.string.incorrect_password));
				passwordTextView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			userLoginTask = null;
			showProgress(false);
		}
	}
}
