package eu.mapidev.pi.htsystem.domain;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Measurement {

    @Id
    @Column(nullable = false, unique = true, name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(nullable = false, name = "temp")
    private int temperatureInCelcius;

    @Column(nullable = false, name = "hum")
    private int humidityPercentage;

    public Measurement() {
    }

    public Measurement(Date date, int temperatureInCelcius, int humidityPercentage) {
	this.date = date;
	this.temperatureInCelcius = temperatureInCelcius;
	this.humidityPercentage = humidityPercentage;
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public int getTemperatureInCelcius() {
	return temperatureInCelcius;
    }

    public void setTemperatureInCelcius(int temperatureInCelcius) {
	this.temperatureInCelcius = temperatureInCelcius;
    }

    public int getHumidityPercentage() {
	return humidityPercentage;
    }

    public void setHumidityPercentage(int humidityPercentage) {
	this.humidityPercentage = humidityPercentage;
    }

    @Override
    public int hashCode() {
	int hash = 5;
	hash = 37 * hash + Objects.hashCode(this.date);
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final Measurement other = (Measurement) obj;
	if (!Objects.equals(this.date.getTime(), other.date.getTime())) {
	    return false;
	}
	return true;
    }

    public String toString() {
	return "Measurement{" + "date=" + date.getTime() + ", temperatureInCelcius=" + temperatureInCelcius + ", humidityPercentage=" + humidityPercentage + '}';
    }

}
