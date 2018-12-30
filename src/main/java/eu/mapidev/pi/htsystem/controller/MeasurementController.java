package eu.mapidev.pi.htsystem.controller;

import eu.mapidev.pi.htsystem.domain.Measurement;
import eu.mapidev.pi.htsystem.service.MeasurementService;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MeasurementController {

    @Autowired
    private MeasurementService measurementService;

    @GetMapping("/measurement")
    @ResponseBody
    public List<Measurement> listAllMeasurements() {
	return measurementService.getAllMeasurements();
    }

    @GetMapping("/measurement/{timestamp}")
    @ResponseBody
    public Measurement listMeasurement(@PathVariable("timestamp") Long timestamp) {
	return measurementService.getMeasurementByDate(new Date(timestamp));
    }
    
    @GetMapping("/measurement/last")
    @ResponseBody
    public Measurement listLastMeasurement() {
	return measurementService.getLastMeasurement();
    }

    @PostMapping("/measurement")
    public ResponseEntity<?> createMeasurement(@RequestBody Measurement measurement) {
	return new ResponseEntity<>(measurementService.createMeasurement(measurement), HttpStatus.CREATED);
    }

    @PutMapping("/measurement/{timestamp}")
    public ResponseEntity<?> updateMeasurement(@PathVariable("timestamp") Long timestamp, @RequestBody Measurement measurement) {
	return new ResponseEntity<>(measurementService.updateMeasurement(new Date(timestamp), measurement), HttpStatus.OK);
    }

    @DeleteMapping("/measurement/{timestamp}")
    public ResponseEntity deleteMeasurement(@PathVariable("timestamp") Long timestamp) {
	measurementService.removeMeasurement(new Date(timestamp));
	return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    void handleBadRequests(HttpServletResponse response) throws IOException {
	response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
