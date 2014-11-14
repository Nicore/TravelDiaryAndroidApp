package domain;

import java.sql.Timestamp;

public class Event {
	private Integer poiId;
	private Double latitude;
	private Double longitude;
	private Timestamp dtime;
	private Timestamp updatetime;
	private String name;
	private String description;
	private Integer tripId;
	private String flag;

	public Event() {
	}

	public Event(Integer poiId, Double latitude, Double longitude,
			Timestamp dtime, String name, String description, Integer tripId,
			Timestamp updatetime, String flag) {
		super();
		this.poiId = poiId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.dtime = dtime;
		this.updatetime = updatetime;
		this.name = name;
		this.description = description;
		this.tripId = tripId;
		this.flag = flag;
	}

	public Timestamp getDtime() {
		return dtime;
	}

	public void setDtime(Timestamp dtime) {
		this.dtime = dtime;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Integer getPoiId() {
		return poiId;
	}

	public void setPoiId(Integer poiId) {
		this.poiId = poiId;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getTripId() {
		return tripId;
	}

	public void setTripId(Integer tripId) {
		this.tripId = tripId;
	}

	@Override
	public String toString() {
		return "Event [poiId=" + poiId + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", dtime=" + dtime
				+ ", updatetime=" + updatetime + ", name=" + name
				+ ", description=" + description + ", tripId=" + tripId
				+ ", flag=" + flag + "]";
	}

}
