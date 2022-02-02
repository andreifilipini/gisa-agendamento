package com.gisa.gisaagendamento.repository;

import com.gisa.gisaagendamento.model.Scheduling;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchedulingRepository extends JpaRepository<Scheduling, Long> {

    List<Scheduling> findByAssociateLogin(String login);

    Scheduling findByAssociateLoginAndResourceIdAndDateAndTime(String associateId, String resourceId, String date, String time);
}
