//package cn.com.superLei.spear;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.ContextCompat;
//import android.util.Log;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//
//import cn.com.superLei.spear.annotation.Permission;
//import cn.com.superLei.spear.common.utils.PermissionUtils;
//
///**
// * Created by jerry on 2018/6/13.
// */
//
//@Aspect
//public class SysPermissionAspect {
//
//    @Around("execution(@com.heaton.baselibsample.aop.Permission * *(..)) && @annotation(permission)")
//    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, final Permission permission) throws Throwable {
//        Context context = null;
//        final Object object = joinPoint.getThis();
//        if (object instanceof Context) {
//            context = (Context) object;
//        } else if (object instanceof Fragment) {
//            context = ((Fragment) object).getActivity();
//        } else if (object instanceof android.support.v4.app.Fragment) {
//            context = ((android.support.v4.app.Fragment) object).getActivity();
//        }
//        if (context == null || permission == null) {
//            joinPoint.proceed();
//            return;
//        }
//        String[] permissions = permission.value();
//        int requestCode = permission.requestCode();
//        if (checkPermissions(context, permissions)) {
//            joinPoint.proceed();
//        }else {
//            PermissionUtils.INSTANCE.requestPermission(context, permission.value(), requestCode, "请求设备权限", new PermissionUtils.GrantedResult() {
//                @Override
//                public void onResult(boolean granted) {
//                    if (granted){
//                        try {
//                            joinPoint.proceed();//获得权限，执行原方法
//                        } catch (Throwable throwable) {
//                            throwable.printStackTrace();
//                        }
//                        Log.e("SysPermissionAspect", "onResult: true");
////                        context.setResult(Activity.RESULT_OK);
//                    }else {
//                        Log.e("SysPermissionAspect", "onResult: false");
////                        context.setResult(Activity.RESULT_CANCELED);
//                    }
//                }
//            });
//        }
//        /*new AlertDialog.Builder(context)
//                .setTitle("提示")
//                .setMessage("为了应用可以正常使用，请您点击确认申请权限。")
//                .setNegativeButton("取消", null)
//                .setPositiveButton("允许", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .create()
//                .show();*/
//    }
//
//    private boolean checkPermission(Context context, String permission) {
//        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        return false;
//    }
//
//    private boolean checkPermissions(Context context, String[] permissions) {
//        for(String permission : permissions) {
//            if (!checkPermission(context, permission)) {
//                return false;
//            }
//        }
//        return true;
//    }
//}
