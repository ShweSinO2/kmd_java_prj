package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import config.DBConfig;
import model.AttachmentModel;
import model.ProjectModel;
import model.TeamMemberModel;

public class TeamMemberController {
	public static Connection con = null;
	static {
		DBConfig cls = new DBConfig();
		try {
			con = cls.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Fail,Inter error", "Fail", JOptionPane.ERROR_MESSAGE);
		}

	}
	
	public List<TeamMemberModel> selectall() throws SQLException {
		List<TeamMemberModel> list = new ArrayList<TeamMemberModel>();
		String sql = "select * from pj_management.team_member order by team_id asc";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			TeamMemberModel tmm = new TeamMemberModel();
			tmm.setTeam_id(rs.getInt("team_id"));
			tmm.setEmployee_id(rs.getString("employee_id"));
			tmm.setPosition(rs.getString("position"));
			list.add(tmm);
		}
		return list;
	}
	
	public int insert(TeamMemberModel dain) {
		int result = 0;
		String sql = "insert into pj_management.team_member (team_id,employee_id,position) values(?,?,?)";

		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1, dain.getTeam_id());
			ps.setString(2, dain.getEmployee_id());
			ps.setString(3, dain.getPosition());

			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public int update(TeamMemberModel dain, String employee_id) {
		int result = 0;
		String sql = "update pj_management.team_member set team_id =?,employee_id=?,position=? where employee_id=?";
		System.out.println("---");
		System.out.println(dain.getTeam_id());
		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1, dain.getTeam_id());
			ps.setString(2, dain.getEmployee_id());
			ps.setString(3, dain.getPosition());
			ps.setString(4, employee_id);
			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public int delete(TeamMemberModel dain, String employee_id) {
		// TODO Auto-generated method stub
		int result = 0;
		String sql = "delete from pj_management.team_member where employee_id=?";
		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setString(1, employee_id);
			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Delete Fail,Inter error", "Fail", JOptionPane.ERROR_MESSAGE);
		}
		return result;
	}

}
