package com.lsxy.framework.api.base;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.UUIDGenerator;

import java.io.Serializable;

/**
 * Created by liups on 2016/9/23.
 */
public class CustomUUIDGenerator extends UUIDGenerator {
    public Serializable generate(SessionImplementor session, Object obj) {
        if (obj instanceof IdEntity
                && (((IdEntity) obj).getId() != null
                && ((IdEntity) obj).getId().trim().length() > 0)) {
            return ((IdEntity) obj).getId();
        } else {
            Serializable a = super.generate(session, obj);
            System.out.println("===================ID============:"+a);
            return a;
        }
    }
}
