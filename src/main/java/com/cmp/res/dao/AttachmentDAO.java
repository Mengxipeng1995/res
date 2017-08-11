package com.cmp.res.dao;

import com.cmp.res.entity.NewBook;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.Attachment;

import java.util.List;

public interface AttachmentDAO  extends PagingAndSortingRepository<Attachment, Long>,JpaSpecificationExecutor<Attachment>{

    @Query
    public Iterable<Attachment> findAllByResouceId(Long bookid);

    @Query
    public Attachment findByResouceId(Long bookid);

}