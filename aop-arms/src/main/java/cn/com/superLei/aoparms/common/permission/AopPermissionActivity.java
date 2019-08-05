package cn.com.superLei.aoparms.common.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.com.superLei.aoparms.R;

/**
 * description $desc$
 * created by jerry on 2019/8/5.
 */
public class AopPermissionActivity extends Activity {
    private static IPermission permissionListener;
    private String[] permissions;
    private static final String PERMISSION_KEY = "permission_key";
    private static final String REQUEST_CODE = "request_code";
    private static final String REQUEST_RATIONALE = "request_rationale";
    private int requestCode;
    private String rationale;

    /**
     * 跳转到Activity申请权限
     *
     * @param context     Context
     * @param permissions Permission List
     * @param iPermission Interface
     */
    public static void PermissionRequest(Context context, String[] permissions, int requestCode, String rationale, IPermission iPermission) {
        permissionListener = iPermission;
        Intent intent = new Intent(context, AopPermissionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putStringArray(PERMISSION_KEY, permissions);
        bundle.putInt(REQUEST_CODE, requestCode);
        bundle.putString(REQUEST_RATIONALE, rationale);
        intent.putExtras(bundle);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            permissions = bundle.getStringArray(PERMISSION_KEY);
            requestCode = bundle.getInt(REQUEST_CODE, 0);
            rationale = bundle.getString(REQUEST_RATIONALE);
        }
        if (permissions == null || permissions.length <= 0) {
            finish();
            return;
        }
        requestPermission(permissions);
    }


    /**
     * 申请权限
     *
     * @param permissions permission list
     */
    private void requestPermission(String[] permissions) {

        if (AopPermissionUtils.hasSelfPermissions(this, permissions)) {
            //all permissions granted
            if (permissionListener != null) {
                permissionListener.permissionGranted();
                permissionListener = null;
            }
            finish();
            overridePendingTransition(0, 0);
        } else {
            boolean shouldShowRequestPermissionRationale = AopPermissionUtils
                    .shouldShowRequestPermissionRationale(this, permissions);
            if (TextUtils.isEmpty(rationale)){
                //request permissions
                ActivityCompat.requestPermissions(this, permissions, requestCode);
            }else {
                if (shouldShowRequestPermissionRationale){
                    showRequestPermissionRationale(this, permissions,rationale);
                }else {
                    //request permissions
                    ActivityCompat.requestPermissions(this, permissions, requestCode);
                }
            }
        }
    }

    public void showRequestPermissionRationale(Activity activity, String[] permissions, String rationale){
        new AlertDialog.Builder(activity)
                .setMessage(rationale)
                .setPositiveButton(R.string.btn_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(activity, permissions, requestCode);
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                        overridePendingTransition(0, 0);
                    }
                })
                .create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (AopPermissionUtils.verifyPermissions(grantResults)) {
            //所有权限都同意
            if (permissionListener != null) {
                permissionListener.permissionGranted();
            }
        } else {
            if (!AopPermissionUtils.shouldShowRequestPermissionRationale(this, permissions)) {
                //权限被拒绝并且选中不再提示
                if (permissions.length != grantResults.length) return;
                List<String> denyNoAskList = new ArrayList<>();
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        denyNoAskList.add(permissions[i]);
                    }
                }
                if (permissionListener != null) {
                    permissionListener.permissionNoAskDenied(requestCode, denyNoAskList);
                }
            } else {
                //权限被取消
                if (permissionListener != null) {
                    List<String> denyList = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            denyList.add(permissions[i]);
                        }
                    }
                    permissionListener.permissionDenied(requestCode, denyList);
                }
            }

        }
        permissionListener = null;
        finish();
        overridePendingTransition(0, 0);
    }
}
