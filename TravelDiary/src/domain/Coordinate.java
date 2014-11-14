package domain;

import java.sql.Timestamp;

public class Coordinate {
	private Double lat;
	private Double longi;
	private Timestamp datetime;
	private Integer tripId;
	private String flag;

	public Coordinate() {
		super();
	}

	public Coordinate(Double lat, Double longi, Timestamp datetime,
			Integer tripId, String flag) {
		super();
		this.lat = lat;
		this.longi = longi;
		this.datetime = datetime;
		this.tripId = tripId;
		this.flag = flag;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLongi() {
		return longi;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public void setLongi(Double longi) {
		this.longi = longi;
	}

	public Timestamp getDatetime() {
		return datetime;
	}

	public void setDatetime(Timestamp datetime) {
		this.datetime = datetime;
	}

	public Integer getTripId() {
		return tripId;
	}

	public void setTripId(Integer tripId) {
		this.tripId = tripId;
	}

	@Override
	public String toString() {
		return "Coordinate [lat=" + lat + ", longi=" + longi + ", datetime="
				+ datetime + ", tripId=" + tripId + ", flag=" + flag + "]";
	}

}
