/**
 * Tag object
 * 
 * @author Nick Comer
 */
package domain;

import java.sql.Timestamp;

public class Tag {
	private Integer tagid; // the id of the parent object ie event/trip/media
	private Timestamp updatetime;
	private String text;
	private String type;
	private String flag;

	public Tag() {
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param text
	 * @param type
	 */
	public Tag(Integer id, String text, String type, Timestamp updatetime,
			String flag) {
		this.tagid = id;
		// this.dateTime = dateTime;
		this.text = text;
		this.type = type;
		this.updatetime = updatetime;
		this.flag = flag;
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

	/*
	 * Access and set methods
	 */
	public Integer getTagId() {
		return tagid;
	}

	public void setTagId(Integer tagid) {
		this.tagid = tagid;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Tag [tagid=" + tagid + ", updatetime=" + updatetime + ", text="
				+ text + ", type=" + type + ", flag=" + flag + "]";
	}

}
