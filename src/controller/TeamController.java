package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import config.DBConfig;
//import controller.EmployeController;
//import model.CustomerModel;
import model.TeamModel;

public class TeamController {
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

	public int insert(TeamModel dain) {
		int result = 0;
		String sql = "insert into pj_management.team (team_id,team_name) values(?,?)";

		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1,dain.getTeam_id());
			ps.setString(2, dain.getTeam_name());
			
			
			

			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public int update(TeamModel dain) {
		int result = 0;
		String sql = "update pj_management.team set team_name=?where team_id=? ";
		
		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			
			ps.setString(1, dain.getTeam_name());
			ps.setInt(2, dain.getTeam_id());
			
			
			
			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
		
	

	public boolean isduplicate(TeamModel dain) throws SQLException {
		boolean duplicate = false;
		String sql = "select * from pj_management.team where team_name = ? and team_id != ?";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ps.setString(1, dain.getTeam_name());
		ps.setInt(2, dain.getTeam_id());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			duplicate = true;
		} else {
			duplicate = false;
		}
		return duplicate;
	}

	public List<TeamModel> selectall() throws SQLException {
		List<TeamModel> list = new ArrayList<TeamModel>();
		String sql = "select * from pj_management.team order by team_id asc";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			TeamModel pm = new TeamModel();
			pm.setTeam_id(rs.getInt("team_id"));
			pm.setTeam_name(rs.getString("team_name"));
			
			
			
			list.add(pm);
		}
		return list;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
