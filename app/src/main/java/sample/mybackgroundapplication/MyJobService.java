package sample.mybackgroundapplication;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by manikantad on 21-12-2017.
 */

public class MyJobService extends JobService implements LocationListener{
    LocationManager locationManager;
    private static String url = "https://api.androidhive.info/contacts/";
    JobParameters paramsParameters;

    @Override
    public boolean onStartJob(JobParameters params) {
        System.out.println("Job running");
        doSampleJob(params);
        paramsParameters = params;
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    public void doSampleJob(JobParameters params) {
         locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,this,null);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        new GetContacts().execute();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            jobFinished(paramsParameters, false);
            Schedule.scheduleJob(MyJobService.this);
        }

    }
}
