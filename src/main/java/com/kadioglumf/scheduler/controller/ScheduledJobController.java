package com.kadioglumf.scheduler.controller;

import com.kadioglumf.scheduler.payload.request.CreateScheduledJobRequest;
import com.kadioglumf.scheduler.payload.request.UpdateScheduledJobRequest;
import com.kadioglumf.scheduler.payload.request.search.SearchRequest;
import com.kadioglumf.scheduler.payload.response.ScheduledJobResponse;
import com.kadioglumf.scheduler.service.scheduled.ScheduledService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Scheduler Service Scheduled Controller Documentation")
@RestController
@RequestMapping("/api/scheduler/scheduled/admin")
@RequiredArgsConstructor
@Validated
public class ScheduledJobController {


    private final ScheduledService scheduledService;

    @PostMapping("/create")
    @ApiOperation(value = "Create Scheduled Job", authorizations = {@Authorization(value = "JWT_LOGIN")})
    public ResponseEntity<?> create(@Valid @RequestBody CreateScheduledJobRequest request) {
        scheduledService.create(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update")
    @ApiOperation(value = "Update Scheduled Job", authorizations = {@Authorization(value = "JWT_LOGIN")})
    public ResponseEntity<?> update(@Valid @RequestBody UpdateScheduledJobRequest request) {
        scheduledService.update(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "Delete Scheduled Job", authorizations = {@Authorization(value = "JWT_LOGIN")})
    public ResponseEntity<?> delete(@RequestParam Long id) {
        scheduledService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/scheduledJobs")
    @ApiOperation(value = "Scheduled Job List", authorizations = {@Authorization(value = "JWT_LOGIN")})
    public ResponseEntity<Page<ScheduledJobResponse>> createCampaign(@Valid @RequestBody SearchRequest request) {
        return new ResponseEntity<>(scheduledService.getScheduledJobs(request), HttpStatus.OK);
    }

    @PostMapping("/restartAllJobs")
    @ApiOperation(value = "Restart All Scheduled Jobs", authorizations = {@Authorization(value = "JWT_LOGIN")})
    public ResponseEntity<?> restartAllJobs() {
        scheduledService.restartAllJobs();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
