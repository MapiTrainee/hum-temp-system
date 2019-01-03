package eu.mapidev.pi.htsystem.service;

import eu.mapidev.pi.htsystem.domain.Measurement;
import eu.mapidev.pi.htsystem.repository.MeasurementRepository;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeasurementServiceImpl implements MeasurementService {

    @Autowired
    private MeasurementRepository repository;

    @Override
    public List<Measurement> getAllMeasurements() {
	return repository.findAllByOrderByDateAsc();
    }

    @Override
    public Measurement getLastMeasurement() {
	return repository.findFirstByOrderByDateDesc().get(0);
    }

    @Override
    public Measurement createMeasurement(Measurement measurement) {
	if (measurement.getDate() == null) {
	    measurement.setDate(new Date());
	}
	Measurement savedMeasurement = repository.save(measurement);
	if (repository.count() > MEASUREMENTS_BUFFER_SIZE_LIMIT) {
	    removeFirstMeasurement();
	}
	return savedMeasurement;
    }

    @Override
    public Measurement getMeasurementByDate(Date date) {
	return repository.findById(date).orElseThrow(() -> {
	    return new IllegalArgumentException("Measurement for date  " + date + " has not been found!");
	});
    }

    @Override
    public Measurement updateMeasurement(Date date, Measurement measurement) {
	Measurement updatedMeasurement = getMeasurementByDate(date);
	updatedMeasurement.setHumidityPercentage(measurement.getHumidityPercentage());
	updatedMeasurement.setTemperatureInCelsius(measurement.getTemperatureInCelsius());
	return repository.save(updatedMeasurement);
    }

    @Override
    public void removeFirstMeasurement() {
	Measurement measurement = repository.findFirstByOrderByDateAsc().get(0);
	repository.delete(measurement);
    }

    @Override
    public void removeMeasurement(Date date) {
	Measurement measurement = getMeasurementByDate(date);
	repository.delete(measurement);
    }

}
