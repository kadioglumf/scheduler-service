package com.kadioglumf.scheduler.model;

import com.kadioglumf.scheduler.model.generator.LoggedUserIpGenerator;
import com.kadioglumf.scheduler.model.generator.OriginGenerator;
import com.kadioglumf.scheduler.model.generator.OriginIpGenerator;
import com.kadioglumf.scheduler.model.generator.UserAgentGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GeneratorType;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDetailedAbstractModel extends AbstractModel {

    @Column(updatable = false)
    @GeneratorType(
            type = LoggedUserIpGenerator.class,
            when = GenerationTime.INSERT
    )
    private String createdByIpAddr;

    @LastModifiedBy
    @GeneratorType(
            type = LoggedUserIpGenerator.class,
            when = GenerationTime.ALWAYS
    )
    private String updatedByIpAddr;

    @Column(updatable = false)
    @GeneratorType(
            type = OriginIpGenerator.class,
            when = GenerationTime.INSERT
    )
    private String originIpAddr;

    @GeneratorType(
            type = OriginGenerator.class,
            when = GenerationTime.ALWAYS
    )
    private String origin;

    @GeneratorType(
            type = UserAgentGenerator.class,
            when = GenerationTime.ALWAYS
    )
    private String userAgent;
}
