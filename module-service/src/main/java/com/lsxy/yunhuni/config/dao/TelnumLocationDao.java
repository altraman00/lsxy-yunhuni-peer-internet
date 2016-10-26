package com.lsxy.yunhuni.config.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.config.model.TelnumLocation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

/**
 * Created by liups on 2016/10/13.
 */
public interface TelnumLocationDao extends BaseDaoInterface<TelnumLocation, Serializable> {
    @Query(value = "SELECT t.area_code FROM db_lsxy_bi_yunhuni.tb_oc_config_telnum_location t WHERE :telephone LIKE CONCAT(t.area_code,'%') LIMIT 1",nativeQuery = true)
    String findAreaCodeByTelephone(@Param("telephone") String telephone);

    TelnumLocation findFirstByMobile(String mobile);
}
