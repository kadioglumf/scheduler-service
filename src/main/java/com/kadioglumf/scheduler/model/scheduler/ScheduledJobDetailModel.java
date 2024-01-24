package com.kadioglumf.scheduler.model.scheduler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kadioglumf.scheduler.model.AbstractModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "scheduled_job_detail")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ScheduledJobDetailModel extends AbstractModel {

    @Enumerated(value = EnumType.STRING)
    private ScheduledJobDetailsStatusType status;

    private long executionTime;
    private Date executionStartDate;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String errorMessage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scheduled_job_id", nullable = false)
    private ScheduledJobModel scheduledJob;
}
