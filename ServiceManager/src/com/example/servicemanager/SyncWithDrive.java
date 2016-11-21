package com.example.servicemanager;

import static com.example.servicemanager.Constants.*;
import static com.example.servicemanager.Utils.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Calendar;

import org.apache.commons.io.IOUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class SyncWithDrive extends Activity implements ConnectionCallbacks, OnConnectionFailedListener {

	private static final String TAG = "Google Drive Activity";
	private static final int REQUEST_CODE_RESOLUTION = 1;
	private static final int REQUEST_CODE_OPENER = 2;
	private GoogleApiClient mGoogleApiClient;
	private boolean fileOperation = false;
	DBHelper db = new DBHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	/**
	 * Called when the activity will start interacting with the user. At this
	 * point your activity is at the top of the activity stack, with user input
	 * going to it.
	 */

	@Override
	protected void onResume() {
		super.onResume();
		if (mGoogleApiClient == null) {

			/**
			 * Create the API client and bind it to an instance variable. We use
			 * this instance as the callback for connection and connection
			 * failures. Since no account name is passed, the user is prompted
			 * to choose.
			 */
			mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Drive.API).addScope(Drive.SCOPE_FILE)
					.addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
		}

		mGoogleApiClient.connect();

	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient != null) {

			// disconnect Google API client connection
			mGoogleApiClient.disconnect();
		}
		super.onPause();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {

		// Called whenever the API client fails to connect.
		Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());

		if (!result.hasResolution()) {

			// show the localized error dialog.
			GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
			return;
		}

		/**
		 * The failure has a resolution. Resolve it. Called typically when the
		 * app is not yet authorized, and an authorization dialog is displayed
		 * to the user.
		 */

		try {

			result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);

		} catch (SendIntentException e) {

			Log.e(TAG, "Exception while starting resolution activity", e);
		}
	}

	/**
	 * It invoked when Google API client connected
	 * 
	 * @param connectionHint
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		createFileOnDrive();
	}

	/**
	 * It invoked when connection suspend
	 * 
	 * @param cause
	 */
	@Override
	public void onConnectionSuspended(int cause) {

		Log.i(TAG, "GoogleApiClient connection suspended");
	}

	public void createFileOnDrive() {
		fileOperation = true;

		// create new contents resource
		Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(driveContentsCallback);

	}

	public void onClickOpenFile() {
		fileOperation = false;

		// create new contents resource
		Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(driveContentsCallback);
	}

	/**
	 * Open list of folder and file of the Google Drive
	 */
	public void OpenFileFromGoogleDrive() {

		IntentSender intentSender = Drive.DriveApi.newOpenFileActivityBuilder()
				.setMimeType(new String[] { "text/plain", "text/html" }).build(mGoogleApiClient);
		try {
			startIntentSenderForResult(

					intentSender, REQUEST_CODE_OPENER, null, 0, 0, 0);

		} catch (SendIntentException e) {

			Log.w(TAG, "Unable to send intent", e);
		}

	}

	/**
	 * This is Result result handler of Drive contents. this callback method
	 * call CreateFileOnGoogleDrive() method and also call
	 * OpenFileFromGoogleDrive() method, send intent onActivityResult() method
	 * to handle result.
	 */
	final ResultCallback<DriveContentsResult> driveContentsCallback = new ResultCallback<DriveContentsResult>() {
		@Override
		public void onResult(DriveContentsResult result) {

			if (result.getStatus().isSuccess()) {

				if (fileOperation == true) {

					try {
						CreateFileOnGoogleDrive(result);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {

					OpenFileFromGoogleDrive();

				}
			}

		}
	};

	/**
	 * Create a file in root folder using MetadataChangeSet object.
	 * 
	 * @param result
	 * @throws IOException 
	 */
	public void CreateFileOnGoogleDrive(DriveContentsResult result) throws IOException {

		try {
			backupDatabase(getApplicationContext());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
		
		final DriveContents driveContents = result.getDriveContents();
		InputStream mInput = new FileInputStream(getExternalFolderPath() + "/" + DB_NAME);
		OutputStream outputStream = driveContents.getOutputStream();
		Writer writer = new OutputStreamWriter(outputStream);
		IOUtils.copy(mInput,outputStream);
		
		MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
				.setTitle(FILE_NAME + getDateString(Calendar.getInstance())).setMimeType("text/plain").setStarred(true)
				.build();

		// create a file in root folder
		Drive.DriveApi.getRootFolder(mGoogleApiClient).createFile(mGoogleApiClient, changeSet, driveContents)
				.setResultCallback(fileCallback);
	}

	/**
	 * Handle result of Created file
	 */
	final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new ResultCallback<DriveFolder.DriveFileResult>() {
		@Override
		public void onResult(DriveFolder.DriveFileResult result) {
			if (result.getStatus().isSuccess()) {

				Toast.makeText(getApplicationContext(), "file created: " + "" + result.getDriveFile().getDriveId(),
						Toast.LENGTH_LONG).show();

			}
			finish();
			moveTaskToBack(true);
			System.exit(0);
			return;

		}
	};

	public static void backupDatabase(Context context) throws IOException {
		// Open your local db as the input stream
		String inFileName = Utils.getDatabasePath(context.getApplicationInfo().dataDir, context.getPackageName()) + "/" +DB_NAME_WITH_EXTENTION;
		File dbFile = new File(inFileName);
		FileInputStream fis = new FileInputStream(dbFile);
		File dir = new File(getExternalFolderPath());
		dir.mkdirs();
		String outFileName =  dir + "/" + DB_NAME;
		// Open the empty db as the output stream
		OutputStream output = new FileOutputStream(outFileName);
		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = fis.read(buffer)) > 0) {
			output.write(buffer, 0, length);
		}
		// Close the streams
		output.flush();
		output.close();
		fis.close();
	}
}