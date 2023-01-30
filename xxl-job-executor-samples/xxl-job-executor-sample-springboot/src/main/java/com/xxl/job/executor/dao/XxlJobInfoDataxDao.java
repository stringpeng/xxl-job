package com.xxl.job.executor.dao;

import com.xxl.job.executor.core.model.XxlJobInfoDataxExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * job info
 * @author xuxueli 2016-1-12 18:03:45
 */
@Mapper
public interface XxlJobInfoDataxDao {

	public int save(XxlJobInfoDataxExt info);

	public int update(XxlJobInfoDataxExt xxlJobInfo);

	public int delete(@Param("id") long id);

	public XxlJobInfoDataxExt loadById(@Param("id") long id);
}
