package com.lsxy.framework.core.persistence;

import java.io.Serializable;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;


@NoRepositoryBean
public interface BaseDaoInterface<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {

	 
}
