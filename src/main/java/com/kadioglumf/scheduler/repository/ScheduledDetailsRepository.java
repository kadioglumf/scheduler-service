package com.kadioglumf.scheduler.repository;

import com.kadioglumf.scheduler.model.scheduler.ScheduledJobDetailModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface ScheduledDetailsRepository extends JpaRepository<ScheduledJobDetailModel, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM ScheduledJobDetailModel s WHERE s.creationDate <= :date")
    void deleteAllByCreationDateBefore(@Param("date") Date date);
}
