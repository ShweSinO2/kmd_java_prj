package model;

public class TaskModel {
	private int task_id;
	private String task_name;
	private String description;
	private String start_date;
	private String end_date;
	private int project_id;
	private int status_id;
	private int milestone_id;
	private String assigned_id;
	private int priority_id;
	private int type_id;
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


	public String getTask_name() {
		return task_name;
	}


	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getStart_date() {
		return start_date;
	}


	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}


	public String getEnd_date() {
		return end_date;
	}


	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}


	public int getStatus_id() {
		return status_id;
	}


	public void setStatus_id(int status_id) {
		this.status_id = status_id;
	}


	public int getMilestone_id() {
		return milestone_id;
	}


	public void setMilestone_id(int milestone_id) {
		this.milestone_id = milestone_id;
	}


	public String getAssigned_id() {
		return assigned_id;
	}


	public void setAssigned_id(String assigned_id) {
		this.assigned_id = assigned_id;
	}


	public int getPriority_id() {
		return priority_id;
	}


	public void setPriority_id(int priority_id) {
		this.priority_id = priority_id;
	}


	public int getType_id() {
		return type_id;
	}


	public void setType_id(int type_id) {
		this.type_id = type_id;
	}


	public int getTask_id() {
		return task_id;
	}


	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}


	public int getProject_id() {
		return project_id;
	}


	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}


	
}
