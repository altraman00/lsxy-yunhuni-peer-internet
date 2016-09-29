package com.lsxy.app.oc.test;

import com.lsxy.framework.core.utils.PasswordUtil;
import com.lsxy.framework.core.utils.UUIDGenerator;
import org.junit.Test;

/**
 * Created by liups on 2016/9/29.
 */
public class PwdTest {

    @Test
    public void pwdTest(){
        String un = "";
        String ps = "";
        System.out.println(UUIDGenerator.uuid());
        String psssss = PasswordUtil.springSecurityPasswordEncode(ps, un);
        System.out.println(psssss);
        String pss = PasswordUtil.desencode(ps);
        System.out.println(pss);
    }

    @Test
    public void pwdTest2(){
        String psss = "";
        System.out.println(PasswordUtil.desdecode(psss));
    }
}
