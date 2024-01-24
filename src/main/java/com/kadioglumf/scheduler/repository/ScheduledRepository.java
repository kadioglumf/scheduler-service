package com.kadioglumf.scheduler.repository;

import com.kadioglumf.scheduler.model.scheduler.ScheduledJobModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ScheduledRepository extends JpaRepository<ScheduledJobModel, Long>, JpaSpecificationExecutor<ScheduledJobModel> {

    boolean existsByName(String name);
}
