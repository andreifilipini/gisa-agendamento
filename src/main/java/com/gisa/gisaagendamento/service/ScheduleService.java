package com.gisa.gisaagendamento.service;

import com.gisa.gisaagendamento.messages.CancelSchedulingProducer;
import com.gisa.gisaagendamento.messages.SchedulingProducer;
import com.gisa.gisaagendamento.model.Scheduling;
import com.gisa.gisaagendamento.repository.SchedulingRepository;
import com.gisa.gisacore.exception.InfraException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.gisa.gisacore.util.StringUtil.isBlank;
import static com.gisa.gisacore.util.StringUtil.isNotBlank;

@Service
public class ScheduleService {

	@Inject
	private SchedulingRepository repository;

	@Inject
	private SchedulingProducer schedulingProducer;

	@Inject
	private CancelSchedulingProducer cancelSchedulingProducer;

	public List<Scheduling> findScheduling(String associateId) {
		return isBlank(associateId)
				? Collections.emptyList()
				: repository.findByAssociateId(associateId);
	}

	public Scheduling schedule(String associateId, String resourceId, String date, String time) {
		Scheduling scheduling = new Scheduling(associateId, resourceId, date, time);

		repository.save(scheduling);

		schedulingProducer.send(scheduling.getId(), scheduling.getResourceId(), scheduling.getDate(), scheduling.getTime());

		return scheduling;
	}

	public void cancelSchedule(String associateId, String resourceId, String date, String time) {
		Scheduling scheduling = findByAssociateIdAndResourceIdAndDateAndTime(associateId, resourceId, date, time);

		if (scheduling == null) {
			throw new InfraException("A agenda não foi encontrada.");
		}

		scheduling.cancel();

		repository.save(scheduling);

		cancelSchedulingProducer.send(scheduling.getId(), scheduling.getResourceId(), scheduling.getDate(), scheduling.getTime());
	}

	private Scheduling findByAssociateIdAndResourceIdAndDateAndTime(String associateId, String resourceId, String date, String time) {
		return isNotBlank(resourceId) && isNotBlank(associateId) && isNotBlank(date) && isNotBlank(time)
				? repository.findByAssociateIdAndResourceIdAndDateAndTime(associateId, resourceId, date, time)
				: null;
	}

	public void confirm(Long id) {
		Optional<Scheduling> optScheduling = repository.findById(id);
		if (optScheduling.isPresent()) {
			Scheduling scheduling = optScheduling.get();
			scheduling.confirm();
			repository.save(scheduling);
		}
	}

	public void cancel(Long id) {
		Optional<Scheduling> optScheduling = repository.findById(id);
		if (optScheduling.isPresent()) {
			Scheduling scheduling = optScheduling.get();
			scheduling.cancel();
			repository.save(scheduling);
		}
	}
}
