package com.example.alertincservice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import android.os.Handler;
import java.util.Date;

public class CallReceiver extends PhonecallReceiver {
    Context context;

    @Override
    protected void onIncomingCallStarted(final Context ctx, String number, Date start)
    {
        Toast.makeText(ctx,"New Incoming Call"+ number,Toast.LENGTH_LONG).show();
        context =   ctx;

        showAlertDialog(ctx, number);
        Log.d("phoneNumber","" + number);

    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end)
    {
        final Intent intent = new Intent(ctx, ChatHeadService.class);
        ctx.stopService(intent);
        Toast.makeText(ctx,"Bye Bye"+ number,Toast.LENGTH_LONG).show();
    }


    private void showAlertDialog(Context context, String number) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Incoming Call");
        builder.setIcon(R.drawable.moleskin);
        builder.setMessage("You have an Incoming call! Pick Up." + number);
        builder.setPositiveButton("App Open", (dialog, which) -> {
// or you can replace **'this'** with your **ActivityName.this**
            Intent i = context.getPackageManager().getLaunchIntentForPackage("com.example.alertincservice");
            context.startActivity(i);
          /*  Intent openAppIntent = new Intent(context, MainActivity.class);
            openAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(openAppIntent);*/

        });
        builder.setNegativeButton("Decline", (dialog, which) -> {
            // Code to decline the call by the alert dialog box will be here
            dialog.dismiss();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        alertDialog.show();
    }

}