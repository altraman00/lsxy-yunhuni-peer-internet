package com.lsxy.msg.service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.msg.api.model.MsgConstant;
import com.lsxy.msg.api.model.MsgSupplier;
import com.lsxy.msg.api.service.MsgSupplierService;
import com.lsxy.msg.dao.MsgSupplierDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liups on 2017/3/1.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class MsgSupplierServiceImpl extends AbstractService<MsgSupplier> implements MsgSupplierService {
    @Autowired
    MsgSupplierDao msgSupplierDao;
    @Override
    public BaseDaoInterface<MsgSupplier, Serializable> getDao() {
        return this.msgSupplierDao;
    }
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public MsgSupplier findByCode(String code) {
        return msgSupplierDao.findByCode(code);
    }

    @Override
    public List<MsgSupplier> findByCodeAndOperatorAndSendTypeAndIsMassAndOrder(List<String> code, String operator, String sendType, int isMass, String order) {
        String hql = "SELECT * FROM db_lsxy_bi_yunhuni.tb_bi_msg_supplier obj WHERE obj.enabled=1 and obj.code in ("+ StringUtil.sqlIn(code)+") AND obj.operator like "+StringUtil.sqlLike(operator)+" ";
        if(MsgConstant.MSG_USSD.equals(sendType)){
            hql += " AND obj.is_ussd = 1 and obj.ussd_send_type in('"+isMass+"','3') ";
        }else if(MsgConstant.MSG_SMS.equals(sendType)){
            hql += " AND ( (obj.is_sms = 1 and obj.sms_send_type in( '"+isMass+"', '3' ) )or (obj.is_template = 1 and obj.template_send_type in('"+isMass+"','3')) ) ";
        }
        if(StringUtils.isNotEmpty(order)) {
            hql += " order by " + order;
        }
        List<MsgSupplier> list = jdbcTemplate.query(hql,
                new Object[] { },
                new BeanPropertyRowMapper<MsgSupplier>(MsgSupplier.class));
        return list;
    }
}
