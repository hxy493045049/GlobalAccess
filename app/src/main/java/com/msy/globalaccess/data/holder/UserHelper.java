package com.msy.globalaccess.data.holder;

import com.msy.globalaccess.base.App;
import com.msy.globalaccess.common.LoginType;
import com.msy.globalaccess.data.bean.user.User;
import com.msy.globalaccess.data.db.UserDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 获取用户信心
 * Created by chensh on 2017/2/9 0009.
 */

public class UserHelper {
    static User user;

    private UserHelper() {
    }

    public static UserHelper getInstance() {
        return Holder.instance;
    }

    public static class Holder {
        private static UserHelper instance = new UserHelper();
    }

    /**
     * 获取User状态
     * @return  false代表user等于空
     */
    public static boolean getUserState(){
        if(user == null){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 从数据库获取用户信息
     *
     * @return
     */
    public User getUser() {
        if (user == null) {
            try {
                UserDao userDao = App.getDaoSession().getUserDao();
                QueryBuilder<User> userList = userDao.queryBuilder().where(UserDao.Properties.UserLoginStatus.eq(1));
                if (userList.list() != null && userList.list().size() > 0) {
                    user = userList.list().get(0);
                }
            } catch (Exception e) {
            }
        }
        return user;
    }

    public void setUser(User user) {
        UserHelper.user = user;
    }

    /**
     * 添加用户信息,如果用户信息已经存在则更新用户信息
     *
     * @param user
     */
    public void insertUser(User user) {
        UserDao userDao = App.getDaoSession().getUserDao();
        QueryBuilder<User> qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.UserId.eq(user.getUserId()));
        if (qb.list() == null || qb.list().size() <= 0) {
            userDao.insert(user);
        } else {
            user.setId(qb.list().get(0).getId());
            updateUser(user);
        }
    }


    /**
     * 更新用户信息
     *
     * @param user
     */
    public void updateUser(User user) {
        UserDao userDao = App.getDaoSession().getUserDao();
        userDao.update(user);
    }

    /**
     * 把所有登录状态为1的都改成0
     */
    public void setUserListLogout(){
        List<User> users=null;
        try {
            UserDao userDao = App.getDaoSession().getUserDao();
            QueryBuilder<User> userList = userDao.queryBuilder().where(UserDao.Properties.UserLoginStatus.eq(LoginType.STATUS_LOGING));
            if (userList.list() != null && userList.list().size() > 0) {
                users = userList.list();
            }
        } catch (Exception e) {
        }
        if(users!=null&&users.size()>0){
            for(int i=0;i<users.size();i++){
                users.get(i).setUserLoginStatus(LoginType.STATUS_LOGOUT);
                updateUser(users.get(i));
            }
        }
    }

}
