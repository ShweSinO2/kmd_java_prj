package model;

public class TeamMemberModel {
	private int team_id;
	private String employee_id;
	private String position;

	public int getTeam_id() {
		return team_id;
	}

	public void setTeam_id(int team_id) {
		this.team_id = team_id;
	}

	public String getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}
