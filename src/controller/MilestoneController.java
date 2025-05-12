package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import config.DBConfig;
import model.MilestoneModel;

public class MilestoneController {
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

	public int insert(MilestoneModel dain) {
		int result = 0;
		String sql = "insert into pj_management.milestone (milestone_name,due_date,project_id,status_id) values(?,?,?,?)";

		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setString(1, dain.getName());
			ps.setString(2, dain.getDue_date());
			ps.setInt(3, dain.getPj_id());
			ps.setInt(4, dain.getSts_id());

			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public int update(MilestoneModel dain) {
		int result = 0;
		String sql = "update pj_management.milestone set milestone_name	=?,due_date=?,project_id=?,status_id=? where milestone_id=?";

		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setString(1, dain.getName());
			ps.setString(2, dain.getDue_date());
			ps.setInt(3, dain.getPj_id());
			ps.setInt(4, dain.getSts_id());
			ps.setInt(5, dain.getMilestone_id());

			System.out.println(ps);
			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public int delete(MilestoneModel dain) {
		// TODO Auto-generated method stub
		int result = 0;
		String sql = "delete from pj_management.milestone where milestone_id=?";
		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1, dain.getMilestone_id());
			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Delete Fail,Inter error", "Fail", JOptionPane.ERROR_MESSAGE);
		}
		return result;
	}

	public boolean isduplicate(MilestoneModel dain) throws SQLException {
		boolean duplicate = false;
		String sql = "select * from pj_management.milestone where milestone_name = ? and project_id != ?";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ps.setString(1, dain.getName());
		ps.setInt(2, dain.getPj_id());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			duplicate = true;
		} else {
			duplicate = false;
		}
		return duplicate;
	}

	public List<MilestoneModel> selectall() throws SQLException {
		List<MilestoneModel> list = new ArrayList<MilestoneModel>();
		String sql = "select * from pj_management.milestone order by milestone_id asc";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			MilestoneModel pm = new MilestoneModel();
			pm.setMilestone_id(rs.getInt("milestone_id"));
			pm.setName(rs.getString("milestone_name"));
			pm.setDue_date(rs.getString("due_date"));
			pm.setPj_id(rs.getInt("project_id"));
			pm.setSts_id(rs.getInt("status_id"));

			list.add(pm);
		}
		return list;
	}

	public List<MilestoneModel> selectone(MilestoneModel dain) throws SQLException {
		List<MilestoneModel> list = new ArrayList<MilestoneModel>();
		String sql = "select * from pj_management.milestone where milestone_name like ? order by milestone_id";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ps.setString(1, dain.getName() + "%");
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			MilestoneModel cs = new MilestoneModel();
			cs.setMilestone_id(rs.getInt("milestone_id"));
			cs.setName(rs.getString("milestone_name"));
			cs.setDue_date(rs.getString("due_date"));
			cs.setPj_id(rs.getInt("project_id"));
			cs.setSts_id(rs.getInt("status_id"));
			list.add(cs);
		}
		return list;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
