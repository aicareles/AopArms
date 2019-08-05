package cn.com.superLei.aoparms.common.permission;

import java.util.List;

/**
 * description $desc$
 * created by jerry on 2019/8/5.
 */
public interface IPermission {
    //同意权限
    void permissionGranted();

    //拒绝权限并且选中不再提示
    void permissionNoAskDenied(int requestCode, List<String> denyNoAskList);

    //取消权限
    void permissionDenied(int requestCode, List<String> denyList);
}
