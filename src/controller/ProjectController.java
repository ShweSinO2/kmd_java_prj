package controller;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import config.DBConfig;
import model.MilestoneModel;
import model.ProjectModel;

public class ProjectController {
	public static Connection con = null;
	static {
		DBConfig cls = new DBConfig();
		try {
			con = cls.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Insert Fail,Inter error", "Fail", JOptionPane.ERROR_MESSAGE);
		}

	}

	public int insert(ProjectModel dain) {
		int result = 0;
		String sql = "insert into pj_management.project (project_name,description,start_date,end_date,status_id,team_id,client_id) values(?,?,?,?,?,?,?)";

		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setString(1, dain.getProject_name());
			ps.setString(2, dain.getDescription());
			ps.setString(3, dain.getStart_date());
			ps.setString(4, dain.getEnd_date());
			ps.setInt(5, dain.getStatus_id());
			ps.setInt(6, dain.getTeam_id());
			ps.setInt(7, dain.getClient_id());

			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public int update(ProjectModel dain) {
		int result = 0;
		String sql = "update pj_management.project set project_name	=?,description=?,start_date=?,end_date=?,status_id=?,team_id=?,client_id=? where project_id=?";

		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setString(1, dain.getProject_name());
			ps.setString(2, dain.getDescription());
			ps.setString(3, dain.getStart_date());
			ps.setString(4, dain.getEnd_date());
			ps.setInt(5, dain.getStatus_id());
			ps.setInt(6, dain.getTeam_id());
			ps.setInt(7, dain.getClient_id());
			ps.setInt(8, dain.getProject_id());

			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public int delete(ProjectModel dain) {
		// TODO Auto-generated method stub
		int result = 0;
		String sql = "delete from pj_management.project where project_id=?";
		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1, dain.getProject_id());
			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Delete Fail,Inter error", "Fail", JOptionPane.ERROR_MESSAGE);
		}
		return result;
	}

	public boolean isduplicate(ProjectModel dain) throws SQLException {
		boolean duplicate = false;
		String sql = "select * from pj_management.project where project_name = ? and project_id != ?";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ps.setString(1, dain.getProject_name());
		ps.setInt(2, dain.getProject_id());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			duplicate = true;
		} else {
			duplicate = false;
		}
		return duplicate;
	}

	public List<ProjectModel> selectall() throws SQLException {
		List<ProjectModel> list = new ArrayList<ProjectModel>();
		String sql = "select * from pj_management.project order by project_id desc";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ProjectModel pm = new ProjectModel();
			pm.setProject_id(rs.getInt("project_id"));
			pm.setProject_name(rs.getString("project_name"));
			pm.setDescription(rs.getString("description"));
			pm.setStart_date(rs.getString("start_date"));
			pm.setEnd_date(rs.getString("end_date"));
			pm.setStatus_id(rs.getInt("status_id"));
			pm.setTeam_id(rs.getInt("team_id"));
			pm.setClient_id(rs.getInt("client_id"));
			list.add(pm);
		}
		return list;
	}

	public List<ProjectModel> selectDeadlineProject() throws SQLException {
		List<ProjectModel> list = new ArrayList<ProjectModel>();
		String sql = "SELECT * FROM pj_management.project "
				+ "WHERE end_date BETWEEN CURRENT_DATE AND DATE_ADD(CURRENT_DATE, INTERVAL 10 DAY) "
				+ "ORDER BY project_id DESC";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ProjectModel pm = new ProjectModel();
			pm.setProject_id(rs.getInt("project_id"));
			pm.setProject_name(rs.getString("project_name"));
			pm.setDescription(rs.getString("description"));
			pm.setStart_date(rs.getString("start_date"));
			pm.setEnd_date(rs.getString("end_date"));
			pm.setStatus_id(rs.getInt("status_id"));
			pm.setTeam_id(rs.getInt("team_id"));
			pm.setClient_id(rs.getInt("client_id"));
			list.add(pm);
		}
		return list;
	}
	
	public List<ProjectModel> selectone(ProjectModel dain) throws SQLException {
		List<ProjectModel> list = new ArrayList<ProjectModel>();
		String sql = "select * from pj_management.project where project_name like ? order by project_id";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ps.setString(1, dain.getProject_name() + "%");
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			ProjectModel pm = new ProjectModel();
			pm.setProject_id(rs.getInt("project_id"));
			pm.setProject_name(rs.getString("project_name"));
			pm.setDescription(rs.getString("description"));
			pm.setStart_date(rs.getString("start_date"));
			pm.setEnd_date(rs.getString("end_date"));
			pm.setStatus_id(rs.getInt("status_id"));
			pm.setTeam_id(rs.getInt("team_id"));
			pm.setClient_id(rs.getInt("client_id"));
			list.add(pm);
		}
		return list;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
