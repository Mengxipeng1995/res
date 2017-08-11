package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.Subject;

public interface SubjectDAO  extends PagingAndSortingRepository<Subject, Long>,JpaSpecificationExecutor<Subject>{


}
