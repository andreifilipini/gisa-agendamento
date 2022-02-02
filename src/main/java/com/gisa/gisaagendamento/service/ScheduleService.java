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

	public List<Scheduling> findScheduling(String login) {
		return isBlank(login)
				? Collections.emptyList()
				: repository.findByAssociateLogin(login);
	}

	public Scheduling schedule(String login, String resourceId, String date, String time) {
		Scheduling scheduling = new Scheduling(login, resourceId, date, time);

		repository.save(scheduling);

		schedulingProducer.send(scheduling.getId(), scheduling.getResourceId(), scheduling.getDate(), scheduling.getTime());

		return scheduling;
	}

	public void cancelSchedule(String login, String resourceId, String date, String time) {
		Scheduling scheduling = findByAssociateLoginAndResourceIdAndDateAndTime(login, resourceId, date, time);

		if (scheduling == null) {
			throw new InfraException("A agenda não foi encontrada.");
		}

		scheduling.cancel();

		repository.save(scheduling);

		cancelSchedulingProducer.send(scheduling.getId(), scheduling.getResourceId(), scheduling.getDate(), scheduling.getTime());
	}

	private Scheduling findByAssociateLoginAndResourceIdAndDateAndTime(String login, String resourceId, String date, String time) {
		return isNotBlank(resourceId) && isNotBlank(login) && isNotBlank(date) && isNotBlank(time)
				? repository.findByAssociateLoginAndResourceIdAndDateAndTime(login, resourceId, date, time)
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
