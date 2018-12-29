package eu.mapidev.pi.htsystem.service;

import eu.mapidev.pi.htsystem.domain.Measurement;
import java.util.Date;
import java.util.List;

public interface MeasurementService {

    public static final int MEASUREMENTS_BUFFER_SIZE_LIMIT = 10;

    List<Measurement> getAllMeasurements();

    Measurement getLastMeasurement();

    Measurement createMeasurement(Measurement measurement);

    Measurement updateMeasurement(Date date, Measurement measurement);

    void removeFirstMeasurement();

    Measurement getMeasurementByDate(Date date);

    void removeMeasurement(Date date);
}
