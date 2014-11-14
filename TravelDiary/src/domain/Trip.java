package domain;

import java.sql.Timestamp;

public class Trip {
	private Integer tripId;
	private String name;
	private String description;
	private Timestamp updatetime;
	private String flag;

	public Trip() {

	}

	public Trip(Integer id, String name, String desc, Timestamp time,
			String flag) {
		this.tripId = id;
		this.name = name;
		this.description = desc;
		this.updatetime = time;
		this.flag = flag;
	}

	public Integer getTripId() {
		return tripId;
	}

	public void setTripId(Integer tripId) {
		this.tripId = tripId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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

	@Override
	public String toString() {
		return "Trip [tripId=" + tripId + ", name=" + name + ", description="
				+ description + ", updatetime=" + updatetime + ", flag=" + flag
				+ "]";
	}

}
