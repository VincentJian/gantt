package org.zkoss.sample.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.json.JSONAware;
import org.zkoss.json.JSONObject;

public class GanttTask implements JSONAware {

	private long id;
	private Date startDate;
	private int duration;
	private String description;
	private double progress;
	private int sortOrder;
	private GanttTask parent;
	private boolean open;

	public GanttTask(long id, Date startDate, int duration,
			String description, double progress, int sortOrder,
			GanttTask parent, boolean open) {
		super();
		this.id = id;
		this.startDate = startDate;
		this.duration = duration;
		this.description = description;
		this.progress = progress;
		this.sortOrder = sortOrder;
		this.parent = parent;
		this.open = open;
	}

	public GanttTask() {}

	public String toJSONString() {
		DateFormat dft = new SimpleDateFormat("dd-MM-yyyy");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("start_date", dft.format(startDate));
		map.put("duration", duration);
		map.put("text", description);
		map.put("progress", progress);
		map.put("sortorder", sortOrder);
		map.put("parent", parent == null ? 0 : parent.getId());
		map.put("open", open);
		return JSONObject.toJSONString(map);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public GanttTask getParent() {
		return parent;
	}

	public void setParent(GanttTask parent) {
		this.parent = parent;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

}
