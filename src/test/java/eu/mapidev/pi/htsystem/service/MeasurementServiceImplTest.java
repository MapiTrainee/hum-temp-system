package eu.mapidev.pi.htsystem.service;

import eu.mapidev.pi.htsystem.domain.Measurement;
import eu.mapidev.pi.htsystem.repository.MeasurementRepository;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class MeasurementServiceImplTest {

    @Autowired
    private MeasurementRepository repository;

    @Autowired
    private MeasurementService service;

    private final Measurement[] measurements = new Measurement[10];
    private static final long SAMPLE_DATE_AS_TIMESTAMP = 1474200000000L;

    @Before
    public void setUp() {
	for (int i = 0; i < measurements.length; i++) {
	    measurements[i] = new Measurement(new Date(SAMPLE_DATE_AS_TIMESTAMP + 1000 * (i + 1)), 20 + i + 1, 30 + i + 1);
	}
	repository.deleteAll();
	List<Measurement> asListMeasurements = Arrays.asList(measurements);
	repository.saveAll(asListMeasurements);
    }

    @Test
    public void whenFindAllMeasurements() {
	//given
	//setUp() inserts 10 measurements
	final int LENGTH = measurements.length;
	
	//when
	List<Measurement> allMeasurements = service.getAllMeasurements();

	//then
	assertThat(allMeasurements.size()).isEqualTo(LENGTH);
    }

    @Test
    public void whenFindLastMeasurement() {
	//given
	Measurement lastMeasurement = measurements[9];

	//when
	Measurement actualMeasurement = service.getLastMeasurement();

	//then
	assertThat(actualMeasurement).isEqualTo(lastMeasurement);
    }

    @Test
    public void whenAddNewMeasurementShouldPreventExceedTheBufferSizeLimitEqual() {
	//given
	final int LIMIT = MeasurementService.MEASUREMENTS_BUFFER_SIZE_LIMIT;
	Measurement newMeasurement = new Measurement(new Date(), 40, 40);

	//when
	service.createMeasurement(newMeasurement);
	Measurement lastMeasurement = service.getLastMeasurement();
	Measurement oldestMeasurement = measurements[0];
	List<Measurement> allMeasurements = service.getAllMeasurements();

	//then
	assertThat(allMeasurements.size()).isLessThanOrEqualTo(LIMIT);
	assertThat(lastMeasurement).isEqualTo(newMeasurement);
	assertThat(allMeasurements).asList().contains(lastMeasurement)
		.asList().doesNotContain(oldestMeasurement);
    }

    @Test
    public void whenAddNewMeasurementAndIsUnderBufferSizeLimit() {
	//given
	final int LIMIT = MeasurementService.MEASUREMENTS_BUFFER_SIZE_LIMIT;
	Measurement newMeasurement = new Measurement(new Date(), 40, 40);
	Measurement oldestMeasurement = measurements[0];
	deleteLastTwoMeasurements();

	//when
	service.createMeasurement(newMeasurement);
	Measurement lastMeasurement = service.getLastMeasurement();
	List<Measurement> allMeasurements = service.getAllMeasurements();

	//then
	assertThat(allMeasurements.size()).isLessThanOrEqualTo(LIMIT);
	assertThat(lastMeasurement).isEqualTo(newMeasurement);
	assertThat(allMeasurements).asList().contains(lastMeasurement, oldestMeasurement);
    }

    private void deleteLastTwoMeasurements() {
	repository.delete(measurements[8]);
	repository.delete(measurements[9]);
    }

    @Test
    public void whenDeleteThirdMeasurement() {
	//given
	final int LIMIT = MeasurementService.MEASUREMENTS_BUFFER_SIZE_LIMIT;
	Measurement thirdMeasurement = measurements[2];

	//when
	service.removeMeasurement(thirdMeasurement.getDate());
	List<Measurement> allMeasurements = service.getAllMeasurements();

	//then
	assertThat(allMeasurements.size()).isLessThanOrEqualTo(LIMIT);
	assertThat(allMeasurements).asList().doesNotContain(thirdMeasurement);
    }

    @Test
    public void whenDeleteOldestMeasurement() {
	//given
	final int LIMIT = MeasurementService.MEASUREMENTS_BUFFER_SIZE_LIMIT;
	Measurement oldestMeasurement = measurements[0];

	//when
	service.removeFirstMeasurement();
	List<Measurement> allMeasurements = service.getAllMeasurements();

	//then
	assertThat(allMeasurements.size()).isLessThanOrEqualTo(LIMIT);
	assertThat(allMeasurements).asList().doesNotContain(oldestMeasurement);
    }

    @Test
    public void whenUpdateFirstMeasurement() {
	//given
	final int LIMIT = MeasurementService.MEASUREMENTS_BUFFER_SIZE_LIMIT;
	Measurement newMeasurement = new Measurement(null, 0, 0);
	Measurement firstMeasurement = measurements[0];

	//when
	Measurement updatedMeasurement = service.updateMeasurement(firstMeasurement.getDate(), newMeasurement);
	List<Measurement> allMeasurements = service.getAllMeasurements();

	//then
	assertThat(allMeasurements.size()).isLessThanOrEqualTo(LIMIT);
	assertThat(updatedMeasurement.getHumidityPercentage()).isNotEqualTo(firstMeasurement.getHumidityPercentage());
	assertThat(updatedMeasurement.getTemperatureInCelcius()).isNotEqualTo(firstMeasurement.getTemperatureInCelcius());
	assertThat(allMeasurements).asList().contains(updatedMeasurement);
    }

}
