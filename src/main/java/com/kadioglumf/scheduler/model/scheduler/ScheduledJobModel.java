package com.kadioglumf.scheduler.model.scheduler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kadioglumf.scheduler.model.DeviceDetailedAbstractModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "scheduled_job")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ScheduledJobModel extends DeviceDetailedAbstractModel {

    @Column(unique = true)
    private String name;
    private String cronExpression;
    private String path;

    @Enumerated(value = EnumType.STRING)
    private HttpMethod httpMethod;
    @Enumerated(value = EnumType.STRING)
    private ApplicationServiceType applicationServiceType;

    @OneToMany(mappedBy = "scheduledJob", cascade = CascadeType.ALL)
    private List<ScheduledJobDetailModel> scheduledJobDetails;
}
