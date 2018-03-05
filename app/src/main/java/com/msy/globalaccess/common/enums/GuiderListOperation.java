package com.msy.globalaccess.common.enums;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.msy.globalaccess.common.enums.GuiderListOperation.OPER_CANCEL;
import static com.msy.globalaccess.common.enums.GuiderListOperation.OPER_CANCEL_DELEGATE_INTERNET;
import static com.msy.globalaccess.common.enums.GuiderListOperation.OPER_CANCEL_DELEGATE_LOCAL;
import static com.msy.globalaccess.common.enums.GuiderListOperation.OPER_COMMIT;
import static com.msy.globalaccess.common.enums.GuiderListOperation.OPER_COMMIT_UNCHANGE;
import static com.msy.globalaccess.common.enums.GuiderListOperation.OPER_DELEGATE;
import static com.msy.globalaccess.common.enums.GuiderListOperation.OPER_DELETE_INTERNET;
import static com.msy.globalaccess.common.enums.GuiderListOperation.OPER_DELETE_LOCAL;
import static com.msy.globalaccess.common.enums.GuiderListOperation.OPER_INVALID;

/**
 * Created by shawn on 2017/4/10 0010.
 * <p>
 * description : 导游列表的一些操作枚举
 */
@IntDef({OPER_INVALID, OPER_COMMIT, OPER_CANCEL, OPER_DELETE_LOCAL, OPER_DELETE_INTERNET, OPER_CANCEL_DELEGATE_LOCAL,
        OPER_CANCEL_DELEGATE_INTERNET, OPER_DELEGATE,OPER_COMMIT_UNCHANGE})
@Retention(RetentionPolicy.SOURCE)
public @interface GuiderListOperation {
    int OPER_INVALID = 0;//不做操作
    int OPER_COMMIT = 1;//提交变更
    int OPER_CANCEL = 2;//取消变更
    int OPER_DELETE_LOCAL = 3;//删除本地导游
    int OPER_DELETE_INTERNET = 4;//删除网络导游
    int OPER_CANCEL_DELEGATE_LOCAL = 5;//取消本地委派
    int OPER_CANCEL_DELEGATE_INTERNET = 6;//取消网络委派
    int OPER_DELEGATE = 7;//委派操作
    int OPER_COMMIT_UNCHANGE = 8;//未做变更操作,直接点提交
}
