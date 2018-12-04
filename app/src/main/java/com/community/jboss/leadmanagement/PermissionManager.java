package com.community.jboss.leadmanagement;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class PermissionManager implements ActivityCompat.OnRequestPermissionsResultCallback{

    private Context context;
    private Activity activity;

    public PermissionManager(Context context,Activity activity){

        this.context = context;
        this.activity = activity;
    }
    public boolean permissionStatus(String permission){
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(int constantInt,String permission){
        ActivityCompat.requestPermissions(activity,
                new String[]{permission},
                constantInt);
    }

    public int checkAndAskPermissions(String... permissions){

        // Bound: 32767 - Maximum signed 16 bit integer
        int requestId = new Random().nextInt(32767);

        List<String> nonGrantedPermission = new ArrayList<>();

        for(String permission: permissions){
            if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                nonGrantedPermission.add(permission);
            }
        }

        if(!nonGrantedPermission.isEmpty()){
            ActivityCompat.requestPermissions(activity, nonGrantedPermission.toArray(new String[nonGrantedPermission.size()]), requestId);
        }
        return requestId;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 512:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(context, "Permission Granted!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "No Permission Granted!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

}
