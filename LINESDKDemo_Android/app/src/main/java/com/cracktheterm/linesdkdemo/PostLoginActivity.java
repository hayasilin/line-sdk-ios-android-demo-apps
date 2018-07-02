package com.cracktheterm.linesdkdemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.linecorp.linesdk.LineApiResponse;
import com.linecorp.linesdk.LineCredential;
import com.linecorp.linesdk.LineProfile;
import com.linecorp.linesdk.api.LineApiClient;
import com.linecorp.linesdk.api.LineApiClientBuilder;

import org.w3c.dom.Text;

public class PostLoginActivity extends AppCompatActivity {

    private LineApiClient lineApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        LineApiClientBuilder apiClientBuilder = new LineApiClientBuilder(getApplicationContext(), Constants.CHANNEL_ID);
        lineApiClient = apiClientBuilder.build();

        final Button profileButton = (Button)findViewById(R.id.getProfileButton);
        profileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new GetProfileTask().execute();
            }
        });

        Intent intent = getIntent();
        LineProfile intentProfile = intent.getParcelableExtra("line_profile");
        LineCredential intentCredential = intent.getParcelableExtra("line_credential");

        TextView profileText;
        profileText = (TextView)findViewById(R.id.nameTextView);
        profileText.setText(intentProfile.getDisplayName());

        profileText = (TextView)findViewById(R.id.userIDTextView);
        profileText.setText(intentProfile.getUserId());

        profileText = (TextView)findViewById(R.id.statusTextView);
        profileText.setText(intentProfile.getStatusMessage());

        profileText = (TextView)findViewById(R.id.accessTokenTextView);
        profileText.setText(intentCredential.getAccessToken().getAccessToken());

        profileText = (TextView)findViewById(R.id.scopeTextView);
        profileText.setText(intentCredential.getPermission().toString());
    }


    public static class ProfileDialogFragment extends DialogFragment {
        private LineProfile profileInfo;

        public LineProfile getProfileInfo() {
            return profileInfo;
        }

        public void setProfileInfo(LineProfile profileInfo) {
            this.profileInfo = profileInfo;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.profile_dialog, null);

            TextView textView = (TextView)view.findViewById(R.id.profileName);
            textView.setText(profileInfo.getDisplayName());
            textView = (TextView)view.findViewById(R.id.profileMessage);
            textView.setText(profileInfo.getStatusMessage());
            textView = (TextView)view.findViewById(R.id.profileMid);
            textView.setText(profileInfo.getUserId());
            Uri pictureUrl = profileInfo.getPictureUrl();
            textView = (TextView)view.findViewById(R.id.profileImageUrl);

            if (pictureUrl != null) {
                textView.setText(profileInfo.getPictureUrl().toString());
            } else {
                textView.setText("Profile image not set");
            }

            view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            view.findViewById(R.id.profileContent).setVisibility(View.VISIBLE);

            builder.setView(view);
            builder.setPositiveButton("OK", null);
            return builder.create();
        }
    }

    public class GetProfileTask extends AsyncTask<Void, Void, LineApiResponse<LineProfile>> {

        private ProfileDialogFragment fragment;

        final static String TAG = "GetProfileTask";

        protected void onPreExecute() {

        }

        protected LineApiResponse<LineProfile> doInBackground(Void... params) {
            return lineApiClient.getProfile();
        }

        @Override
        protected void onPostExecute(LineApiResponse<LineProfile> apiResponse) {
            super.onPostExecute(apiResponse);

            if (apiResponse.isSuccess()) {
                ProfileDialogFragment newFragment = new ProfileDialogFragment();
                newFragment.setProfileInfo(apiResponse.getResponseData());
                newFragment.show(getFragmentManager(), null);
            } else {
                Toast.makeText(getApplicationContext(), "Failed to get profile", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
