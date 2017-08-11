package com.cmp.res.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.KeyWord;

public interface KeyWordDAO extends PagingAndSortingRepository<KeyWord, Long>,JpaSpecificationExecutor<KeyWord>{

}
