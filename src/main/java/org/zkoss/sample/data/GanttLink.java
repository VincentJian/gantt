package org.zkoss.sample.data;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.json.JSONAware;
import org.zkoss.json.JSONObject;

public class GanttLink implements JSONAware {

	private long id;
	private GanttTask source;
	private GanttTask target;
	private int type;

	public GanttLink() {}

	public GanttLink(long id, GanttTask source, GanttTask target, int type) {
		super();
		this.id = id;
		this.source = source;
		this.target = target;
		this.type = type;
	}

	public String toJSONString() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("source", source.getId());
		map.put("target", target.getId());
		map.put("type", type);
		return JSONObject.toJSONString(map);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public GanttTask getSource() {
		return source;
	}

	public void setSource(GanttTask source) {
		this.source = source;
	}

	public GanttTask getTarget() {
		return target;
	}

	public void setTarget(GanttTask target) {
		this.target = target;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
