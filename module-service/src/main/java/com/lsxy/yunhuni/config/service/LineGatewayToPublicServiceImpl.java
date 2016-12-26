package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.model.LineGatewayToPublic;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.config.service.LineGatewayToPublicService;
import com.lsxy.yunhuni.config.dao.LineGatewayToPublicDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxb on 2016/10/25.
 */
@Service
public class LineGatewayToPublicServiceImpl extends AbstractService<LineGatewayToPublic> implements LineGatewayToPublicService {
    @Autowired
    LineGatewayToPublicDao lineGatewayToPublicDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    LineGatewayService lineGatewayService;
    @Override
    public BaseDaoInterface<LineGatewayToPublic, Serializable> getDao() {
        return this.lineGatewayToPublicDao;
    }

    @Override
    public LineGatewayToPublic findById(String id) {
        return getDao().findOne(id);
    }

    @Override
    public int getMaxPriority() {
        String sql = " SELECT IFNULL(MAX(priority),0) FROM db_lsxy_bi_yunhuni.tb_oc_linegateway_to_public WHERE deleted=0 ";
        int result = jdbcTemplate.queryForObject(sql,Integer.class);
        return result;
    }

    @Override
    public int findByLindId(String lindId) {
        String sql = " SELECT COUNT(id) FROM db_lsxy_bi_yunhuni.tb_oc_linegateway_to_public where deleted=0 AND line_id='"+lindId+"'";
        int result = jdbcTemplate.queryForObject(sql,Integer.class);
        return result;
    }

    @Override
    public Page<LineGatewayToPublic> getPage(Integer pageNo, Integer pageSize, String operator, String isThrough, String status, String isPublicLine, String order) {
        String hql = " FROM LineGatewayToPublic obj WHERE obj.deleted=0  ";
        if(StringUtils.isNotEmpty(operator)){
            hql += " AND obj.lineGateway.operator = '"+operator+"' ";
        }
        if(StringUtils.isNotEmpty(isThrough)){
            hql += " AND obj.lineGateway.isThrough = '"+isThrough+"' ";
        }
        if(StringUtils.isNotEmpty(status)){
            hql += " AND obj.lineGateway.status = '"+status+"' ";
        }
        if(StringUtils.isNotEmpty(isPublicLine)){
            hql += " AND obj.lineGateway.isPublicLine = '"+isPublicLine+"' ";
        }
        if(StringUtils.isNotEmpty(order)){
            String temp = " ORDER BY ";
            String[] o = order.split(";");
            for(int i=0;i<o.length;i++){
                String[] oo = o[i].split(":");
                if("1".equals(oo[1])){
                    if("priority".equals(oo[0])){
                        temp += " obj." + oo[0] + " DESC ";
                    }else {
                        temp += " obj.lineGateway." + oo[0] + " DESC ";
                    }
                }else if("0".equals(oo[1])){
                    if("priority".equals(oo[0])){
                        temp += " obj." + oo[0] + " ";
                    }else {
                        temp += " obj.lineGateway." + oo[0] + " ";
                    }
                }
                if(i!=o.length-1){
                    temp += " , ";
                }
            }
            hql += temp;
        }else{
            hql += " ORDER BY obj.priority  ";
        }
        Page page = this.pageList(hql,pageNo,pageSize);
        return page;
    }

    @Override
    public List<LineGateway> findAllLineGatewayByAreaId(String areaId) {
        List<LineGateway> lineGateways = new ArrayList<>();
        List<LineGatewayToPublic> ltps = lineGatewayToPublicDao.findByLineGateway_AreaId(areaId);
        for (LineGatewayToPublic ltp:ltps){
            LineGateway lineGateway = ltp.getLineGateway();
            //TODO 判断线路是否可用
            if(LineGateway.STATUS_USABLE.equals(lineGateway.getStatus())){
                lineGateway.setPriority(ltp.getPriority());
                lineGateways.add(lineGateway);
            }
        }
        return lineGateways;
    }

    @Override
    public void deleteLine(String line) {
        String sql =  " update  db_lsxy_bi_yunhuni.tb_oc_linegateway_to_public  set deleted=1 where deleted=0 and line_id='"+line+"'  ";
        jdbcTemplate.update(sql);
    }

    @Override
    public void addPublic(String id) {
        LineGateway lineGateway = lineGatewayService.findById(id);
        //获取当前最大编号
        int re2 = this.getMaxPriority();
        re2++;
        //新建关系
        LineGatewayToPublic lineGatewayToPublic = new LineGatewayToPublic();
        lineGatewayToPublic.setLineGateway(lineGateway);
        lineGatewayToPublic.setPriority(re2);
        this.save(lineGatewayToPublic);
        //修改线路状态标识为加入全局
        lineGateway.setIsPublicLine(LineGateway.ISPUBLICLINE_TRUE);
        lineGatewayService.save(lineGateway);
    }

    @Override
    public void removePublic(String id) {
        int o3 = this.getMaxPriority();
        LineGatewayToPublic lineGatewayToPublic = this.findById(id);
        //删除线路关系
        try {
            this.delete(lineGatewayToPublic);
        } catch (Exception e) {
            throw new RuntimeException("删除失败");
        }
        //修改对应线路关系
        LineGateway lineGateway = lineGatewayToPublic.getLineGateway();
        if(lineGateway!=null){
            lineGateway.setIsPublicLine(LineGateway.ISPUBLICLINE_FALSE);
            lineGatewayService.save(lineGateway);
        }
        //修正优先级
        if(o3!=lineGatewayToPublic.getPriority()){
            int re = upPriority(lineGatewayToPublic.getPriority(),o3,null);
            if(re==-1){
                throw new RuntimeException("删除成功，修正失败，请手动修正");
            }
        }
    }
    @Override
    public int upPriority(int o1,int o2,String line){
        String flag = "+1";
        int begin = -1;
        int end = -1;
        if(o1<o2){
            begin = o1+1;
            end = o2;
            flag = "-1";
        }else{
            begin = o2;
            end = o1-1;
            flag = "+1";
        }
        String[] sql = new String[2];
        sql[0] = " UPDATE db_lsxy_bi_yunhuni.tb_oc_linegateway_to_public SET priority=priority"+flag+" WHERE deleted=0 AND priority BETWEEN   "+begin+" AND "+end+" ";
        if(StringUtils.isNotEmpty(line)) {
            sql[1] = " UPDATE db_lsxy_bi_yunhuni.tb_oc_linegateway_to_public SET priority=" + o2 + " WHERE deleted=0 AND id ='" + line + "' ";
        }
        int re = lineGatewayService.batchModify(sql);
        return re;
    }
}
