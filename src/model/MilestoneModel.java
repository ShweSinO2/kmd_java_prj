package model;

public class MilestoneModel {
	
	private int milestone_id;
	private String name;
	private String due_date;
	private int pj_id;
	private int sts_id;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public int getMilestone_id() {
		return milestone_id;
	}

	public void setMilestone_id(int milestone_id) {
		this.milestone_id = milestone_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDue_date() {
		return due_date;
	}

	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}

	public int getPj_id() {
		return pj_id;
	}

	public void setPj_id(int pj_id) {
		this.pj_id = pj_id;
	}

	public int getSts_id() {
		return sts_id;
	}

	public void setSts_id(int sts_id) {
		this.sts_id = sts_id;
	}

}
