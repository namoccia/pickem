package com.feedthewolf.nhlpickem;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Intent intent = getIntent();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setVersionTextField();

        findViewById(R.id.feedbackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setVersionTextField() {
        String versionName = "";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView appVersionTextView = (TextView)findViewById(R.id.appVersionTextView);
        appVersionTextView.setText(String.format("%s v%s", getString(R.string.app_name), versionName));
    }

    //Start a new activity for sending a feedback email
    private void sendFeedback() {
        final Intent _Intent = new Intent(android.content.Intent.ACTION_SEND);
        _Intent.setType("text/html");
        _Intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ getString(R.string.dev_email) });
        _Intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.send_feedback_email_subject));
        _Intent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.send_feedback_email_body));
        startActivity(Intent.createChooser(_Intent, getString(R.string.send_feedback_title)));
    }
}
