package com.lsxy.oc.user.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.PasswordUtil;
import com.lsxy.oc.api.exceptions.OcUserNotFoundException;
import com.lsxy.oc.api.user.model.OcUser;
import com.lsxy.oc.api.user.service.OcUserService;
import com.lsxy.oc.user.dao.OcUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/8/9.
 */
@Service
public class OcUserServiceImpl extends AbstractService<OcUser> implements OcUserService {
    @Autowired
    OcUserDao ocUserDao;
    @Override
    public BaseDaoInterface<OcUser, Serializable> getDao() {
        return this.ocUserDao;
    }

    @Override
    public OcUser findUserByLoginNameAndPassword(String userName, String password) {
        OcUser user = ocUserDao.findByUserNameAndStatus(userName, OcUser.STATUS_NORMAL);
        if(user != null) {
            if(!user.getPassword().equals(PasswordUtil.springSecurityPasswordEncode(password,user.getUserName()))){
                user = null;
            }
        }else{
            throw new OcUserNotFoundException("找不到账号");
        }
        return user;
    }

    @Override
    public OcUser findUserByLoginName(String userName) {
        return ocUserDao.findByUserNameAndStatus(userName, OcUser.STATUS_NORMAL);
    }

}
