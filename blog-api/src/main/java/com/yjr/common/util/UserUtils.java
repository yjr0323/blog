package com.yjr.common.util;


import com.yjr.common.constant.Base;
import com.yjr.entity.User;
import org.apache.shiro.SecurityUtils;

/**
 * @author shimh
 * <p>
 * 2018年1月25日
 */
public class UserUtils {

    public static User getCurrentUser() {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute(Base.CURRENT_USER);
        return user;
    }
}
