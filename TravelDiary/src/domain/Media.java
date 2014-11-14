package domain;

import java.sql.Timestamp;
import java.util.Arrays;

public class Media {
	private Integer mediaId;
	private String name;
	private String type;
	private String description;
	private byte[] blob;
	private Integer poiId;
	private Timestamp updatetime;
	private String flag;

	public Media(Integer mediaId, String name, String description, String type,
			byte[] blob, Integer poiId, Timestamp time, String flag) {
		this.mediaId = mediaId;
		this.name = name;
		this.type = type;
		this.description = description;
		this.blob = blob;
		this.poiId = poiId;
		this.updatetime = time;
		this.flag = flag;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	public Media() {
	}

	public Integer getMediaId() {
		return mediaId;
	}

	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getBlob() {
		return blob;
	}

	public void setBlob(byte[] blob) {
		this.blob = blob;
	}

	public Integer getPoiId() {
		return poiId;
	}

	public void setPoiId(Integer poiId) {
		this.poiId = poiId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Media [mediaId=" + mediaId + ", name=" + name + ", type="
				+ type + ", description=" + description + ", blob="
				+ Arrays.toString(blob) + ", poiId=" + poiId + ", updatetime="
				+ updatetime + ", flag=" + flag + "]";
	}

}
