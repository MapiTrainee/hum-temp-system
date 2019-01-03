package eu.mapidev.pi.htsystem.repository;

import eu.mapidev.pi.htsystem.domain.Measurement;
import java.util.Date;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementRepository extends CrudRepository<Measurement, Date> {

    public List<Measurement> findAllByOrderByDateAsc();

    public List<Measurement> findFirstByOrderByDateDesc();
    
    public List<Measurement> findFirstByOrderByDateAsc();

}
