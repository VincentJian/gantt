package org.zkoss.sample;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.zkoss.json.JSONObject;
import org.zkoss.sample.data.GanttLink;
import org.zkoss.sample.data.GanttTask;
import org.zkoss.zk.au.out.AuInvoke;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;

public class GanttComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = -3594049519279780315L;

	@Wire
	private Div gantt;

	private List<GanttTask> taskList;
	private List<GanttLink> linkList;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		taskList = new ArrayList<GanttTask>();
		linkList = new ArrayList<GanttLink>();
		String[] ganttJSONString = initGanttTasks();

		Clients.response(new AuInvoke(gantt, "_init",
				"{\"data\": [" + ganttJSONString[0] + "], " +
				"\"collections\": {\"links\": [" + ganttJSONString[1] + "]}}"));
	}

	@Listen("onTaskAdd = #gantt")
	public void addTask(Event evt) throws ParseException {
		JSONObject obj = (JSONObject) evt.getData();
		GanttTask task = new GanttTask();
		Long parentId = Long.parseLong((String) obj.get("parent"));
		if (parentId == null)
			task.setParent(null);
		else {
			GanttTask parent = null;
			for (GanttTask t : taskList) {
				if (t.getId() == parentId) {
					parent = t;
					break;
				}
			}
			task.setParent(parent);
		}
		task.setId((Long) obj.get("id"));
		task.setStartDate(sdf.parse((String) obj.get("start_date")));
		task.setDuration((Integer) obj.get("duration"));
		task.setDescription((String) obj.get("text"));
		Object p = obj.get("progress");
		double progress = 0.0;
		if (p instanceof Integer)
			progress = (Integer) p * 1.0;
		else if (p instanceof Double)
			progress = (Double) p;
		task.setProgress(progress);
		taskList.add(task);
	}

	@Listen("onTaskUpdate = #gantt")
	public void updateTask(Event evt) throws ParseException {
		JSONObject obj = (JSONObject) evt.getData();
		GanttTask task = null;
		int index = 0;
		for (GanttTask t : taskList) {
			if (t.getId() == (Long)obj.get("id")) {
				task = t;
				index = taskList.indexOf(t);
				break;
			}
		}
		if (task != null) {
			task.setStartDate(sdf.parse((String) obj.get("start_date")));
			task.setDuration((Integer) obj.get("duration"));
			task.setDescription((String) obj.get("text"));
			Object p = obj.get("progress");
			double progress = 0.0;
			if (p instanceof Integer)
				progress = (Integer) p * 1.0;
			else if (p instanceof Double)
				progress = (Double) p;
			task.setProgress(progress);
			task.setSortOrder((Integer) obj.get("sortorder"));
			task.setOpen((Boolean) obj.get("open"));
		}
		//store to DB
		taskList.set(index, task);
	}

	@Listen("onTaskDelete = #gantt")
	public void deleteTask(Event evt) {
		JSONObject obj = (JSONObject) evt.getData();
		Long taskId = new Long((Integer) obj.get("id"));
		for (Iterator<GanttTask> it = taskList.iterator(); it.hasNext();) {
			GanttTask task = it.next();
			if (task.getId() == taskId)
				it.remove();
		}
		for (Iterator<GanttLink> it = linkList.iterator(); it.hasNext();) {
			GanttLink link = it.next();
			if (link.getSource().getId() == taskId || link.getTarget().getId() == taskId)
				it.remove();
		}
	}

	@Listen("onLinkAdd = #gantt")
	public void addLink(Event evt) {
		JSONObject obj = (JSONObject) evt.getData();
		GanttLink link = new GanttLink();
		link.setId((Long) obj.get("id"));
		Long sourceId = Long.parseLong((String) obj.get("source"));
		Long targetId = Long.parseLong((String) obj.get("target"));
		for (GanttTask task : taskList) {
			if (sourceId == task.getId()) {
				link.setSource(task);
				break;
			}
		}
		for (GanttTask task : taskList) {
			if (targetId == task.getId()) {
				link.setTarget(task);
				break;
			}
		}
		link.setType(Integer.parseInt((String) obj.get("type")));
		//store to DB
		linkList.add(link);
	}

	@Listen("onLinkDelete = #gantt")
	public void deleteLink(Event evt) {
		JSONObject obj = (JSONObject) evt.getData();
		Long linkId = new Long((Integer) obj.get("id"));
		for (Iterator<GanttLink> it = linkList.iterator(); it.hasNext();) {
			GanttLink link = it.next();
			if (link.getId() == linkId)
				it.remove();
		}
	}

	private String[] initGanttTasks() {
		Calendar cal = Calendar.getInstance();
		
		cal.set(2013, 3, 1);
		GanttTask parent = new GanttTask(1L, cal.getTime(), 5, "Project #1", 0.8, 20, null, true);
		taskList.add(parent);
		String taskJSONString = parent.toJSONString();
		
		cal.set(2013, 3, 6);
		GanttTask task1 = new GanttTask(2L, cal.getTime(), 4, "Task #1", 0.5, 10, parent, true);
		taskList.add(task1);
		taskJSONString += "," + task1.toJSONString();
		
		cal.set(2013, 3, 5);
		GanttTask task2 = new GanttTask(3L, cal.getTime(), 6, "Task #2", 0.7, 20, parent, true);
		taskList.add(task2);
		taskJSONString += "," + task2.toJSONString();
		
		cal.set(2013, 3, 7);
		GanttTask task3 = new GanttTask(4L, cal.getTime(), 2, "Task #3", 0, 30, parent, true);
		taskList.add(task3);
		taskJSONString += "," + task3.toJSONString();
		
		
		GanttLink link1 = new GanttLink(1L, parent, task1, 0);
		linkList.add(link1);
		String linkJSONString = link1.toJSONString();
		
		GanttLink link2 = new GanttLink(2L, parent, task2, 0);
		linkList.add(link2);
		linkJSONString += "," + link2.toJSONString();
		
		GanttLink link3 = new GanttLink(3L, parent, task3, 0);
		linkList.add(link3);
		linkJSONString += "," + link3.toJSONString();
		
		return new String[]{taskJSONString, linkJSONString};
	}

}
