package com.lsxy.yunhuni.config.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.config.model.ApiGwRedBlankNum;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

/**
 * Created by liups on 2016/8/23.
 */
public interface ApiGwRedBlankNumDao extends BaseDaoInterface<ApiGwRedBlankNum, Serializable> {

    @Query(value = "SELECT * FROM db_lsxy_bi_yunhuni.tb_oc_config_num_redblacklist num WHERE num.deleted=0 AND num.type = :type AND num.status = :status AND :number REGEXP num.number", nativeQuery = true)
    ApiGwRedBlankNum findFirstByNumberAndTypeAndStatus(@Param("number") String number,@Param("type") int type,@Param("status") int status);
}
