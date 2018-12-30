package eu.mapidev.pi.htsystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.mapidev.pi.htsystem.domain.Measurement;
import eu.mapidev.pi.htsystem.service.MeasurementService;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.test.context.junit4.SpringRunner;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MeasurementController.class)
public class MeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeasurementService service;

    private Measurement measurement;
    private static final long SAMPLE_DATE_AS_TIMESTAMP = 1474200000000L;
    private static final String SAMPLE_DATE_AS_STRING = "2016-09-18T12:00:00.000+0000";

    @Before
    public void setUp() {
	measurement = new Measurement();
	measurement.setDate(new Date(SAMPLE_DATE_AS_TIMESTAMP));
	measurement.setHumidityPercentage(50);
	measurement.setTemperatureInCelsius(30);
    }

    @Test
    public void shouldGetAllMeasurements() throws Exception {
	List<Measurement> measurements = Collections.singletonList(measurement);
	given(service.getAllMeasurements()).willReturn(measurements);

	mockMvc.perform(get("/measurement"))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(1)))
		.andExpect(jsonPath("$[0].temperatureInCelsius", is(measurement.getTemperatureInCelsius())))
		.andExpect(jsonPath("$[0].humidityPercentage", is(measurement.getHumidityPercentage())))
		.andExpect(jsonPath("$[0].date", is(SAMPLE_DATE_AS_STRING)));
    }

    @Test
    public void shouldGetMeasurementForSampleTimestamp() throws Exception {
	given(service.getMeasurementByDate(measurement.getDate())).willReturn(measurement);

	mockMvc.perform(get("/measurement/" + SAMPLE_DATE_AS_TIMESTAMP))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andExpect(jsonPath("temperatureInCelsius", is(measurement.getTemperatureInCelsius())))
		.andExpect(jsonPath("humidityPercentage", is(measurement.getHumidityPercentage())))
		.andExpect(jsonPath("date", is(SAMPLE_DATE_AS_STRING)));
    }

    @Test
    public void shouldGetLastMeasurement() throws Exception {
	given(service.getLastMeasurement()).willReturn(measurement);

	mockMvc.perform(get("/measurement/last"))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andExpect(jsonPath("temperatureInCelsius", is(measurement.getTemperatureInCelsius())))
		.andExpect(jsonPath("humidityPercentage", is(measurement.getHumidityPercentage())))
		.andExpect(jsonPath("date", is(SAMPLE_DATE_AS_STRING)));
    }

    @Test
    public void shouldThrowExceptionWhenUsingNonexistantTimestamp() throws Exception {
	final long NONEXISTANT_TIMESTAMP = 0;
	given(service.getMeasurementByDate(new Date(NONEXISTANT_TIMESTAMP))).willThrow(IllegalArgumentException.class);

	mockMvc.perform(get("/measurement/" + NONEXISTANT_TIMESTAMP))
		.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldCreateMeasurementWhenPostRequestWithMeasurement() throws Exception {
	given(service.createMeasurement(measurement)).willReturn(measurement);

	mockMvc.perform(post("/measurement/").contentType(MediaType.APPLICATION_JSON)
		.content(parseMeasurement()).with(testUser()).with(csrf()))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("temperatureInCelsius", is(measurement.getTemperatureInCelsius())))
		.andExpect(jsonPath("humidityPercentage", is(measurement.getHumidityPercentage())))
		.andExpect(jsonPath("date", notNullValue()));
    }

    @Test
    public void shouldUpdateMeasurementWhenPutRequestWithSampleTimestampAndMeasurement() throws Exception {
	given(service.updateMeasurement(new Date(SAMPLE_DATE_AS_TIMESTAMP), measurement)).willReturn(measurement);

	mockMvc.perform(put("/measurement/" + SAMPLE_DATE_AS_TIMESTAMP).contentType(MediaType.APPLICATION_JSON)
		.content(parseMeasurement()).with(testUser()).with(csrf()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("temperatureInCelsius", is(measurement.getTemperatureInCelsius())))
		.andExpect(jsonPath("humidityPercentage", is(measurement.getHumidityPercentage())))
		.andExpect(jsonPath("date", is(SAMPLE_DATE_AS_STRING)));
    }

    @Test
    public void shouldDeleteMeasurementWhenDeleteRequestWithSampleTimestamp() throws Exception {
	willDoNothing().given(service).removeMeasurement(new Date(SAMPLE_DATE_AS_TIMESTAMP));

	mockMvc.perform(delete("/measurement/" + SAMPLE_DATE_AS_TIMESTAMP).with(testUser()).with(csrf()))
		.andExpect(status().isOk());
    }

    protected RequestPostProcessor testUser() {
	return user("admin").password("admin").roles("ADMIN");
    }

    protected String parseMeasurement() throws JsonProcessingException {
	return new ObjectMapper().writeValueAsString(measurement);
    }

}
