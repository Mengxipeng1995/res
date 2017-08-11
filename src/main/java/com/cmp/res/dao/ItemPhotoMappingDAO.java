package com.cmp.res.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.ItemPhotoMapping;

public interface ItemPhotoMappingDAO  extends PagingAndSortingRepository<ItemPhotoMapping, Long>,JpaSpecificationExecutor<ItemPhotoMapping>{

}
