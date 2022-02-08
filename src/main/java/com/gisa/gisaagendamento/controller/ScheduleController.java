package com.gisa.gisaagendamento.controller;

import com.gisa.gisaagendamento.dto.SchedulingDTO;
import com.gisa.gisaagendamento.model.Scheduling;
import com.gisa.gisaagendamento.service.ScheduleService;
import com.gisa.gisacore.exception.InfraException;
import com.gisa.gisacore.util.JwtTokenUtil;
import com.gisa.gisacore.util.StringUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/agendamento")
public class ScheduleController {

    @Inject
    private ScheduleService scheduleService;

    @Inject
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping
    public ResponseEntity<List<SchedulingDTO>> findAll(@RequestHeader("Authorization") String authorization) {
        try {
            return ResponseEntity.ok(
                    scheduleService.findScheduling(getId(authorization)).stream()
                            .map(this::toDTO)
                            .collect(Collectors.toList()));
        } catch (InfraException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<SchedulingDTO> schedule(@RequestHeader("Authorization") String authorization, @RequestParam("resourceId") String resourceId,
                                                  @RequestParam("date") String date, @RequestParam("time") String time) {
        try {
            Scheduling scheduling = scheduleService.schedule(getId(authorization), resourceId, date, time);
            return ResponseEntity.ok(toDTO(scheduling));
        } catch (InfraException ie) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping
    public ResponseEntity cancel(@RequestHeader("Authorization") String authorization, @RequestParam("resourceId") String resourceId,
                                 @RequestParam("date") String date, @RequestParam("time") String time) {
        try {
            scheduleService.cancelSchedule(getId(authorization), resourceId, date, time);
            return ResponseEntity.ok().build();
        } catch (InfraException ie) {
            return ResponseEntity.badRequest().build();
        }
    }

    private String getId(String authHeader) {
        if (StringUtil.isBlank(authHeader)) {
            return null;
        }
        String jwt = jwtTokenUtil.getJwtToken(authHeader);
        return jwtTokenUtil.getSubject(jwt);
    }

    private SchedulingDTO toDTO(Scheduling scheduling) {
        return SchedulingDTO.builder()
                .resource(scheduling.getResourceId())
                .date(scheduling.getDate())
                .time(scheduling.getTime())
                .status(scheduling.getStatus())
                .build();
    }

}
