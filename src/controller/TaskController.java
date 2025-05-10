package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import config.DBConfig;
import model.ProjectModel;
import model.TaskModel;

public class TaskController {
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

	public int insert(TaskModel dain) {
		int result = 0;
		String sql = "insert into pj_management.task (task_name,description,start_date,end_date,status_id,milestone_id,assigned_by_id,priority_id,type_id) values(?,?,?,?,?,?,?,?,?)";

		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setString(1, dain.getTask_name());
			ps.setString(2, dain.getDescription());
			ps.setString(3, dain.getStart_date());
			ps.setString(4, dain.getEnd_date());
			ps.setInt(5, dain.getStatus_id());
			ps.setInt(6, dain.getMilestone_id());
			ps.setString(7, dain.getAssigned_id());
			ps.setInt(8, dain.getPriority_id());
			ps.setInt(9, dain.getType_id());

			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public int update(TaskModel dain) {
		int result = 0;
		String sql = "update pj_management.task set task_name=?,description=?,start_date=?,end_date=?,status_id=?,milestone_id=?,assigned_by_id=?,priority_id=?,type_id=? where task_id=?";

		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setString(1, dain.getTask_name());
			ps.setString(2, dain.getDescription());
			ps.setString(3, dain.getStart_date());
			ps.setString(4, dain.getEnd_date());
			ps.setInt(5, dain.getStatus_id());
			ps.setInt(6, dain.getMilestone_id());
			ps.setString(7, dain.getAssigned_id());
			ps.setInt(8, dain.getPriority_id());
			ps.setInt(9, dain.getType_id());
			ps.setInt(10, dain.getTask_id());

			System.out.println(ps);
			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public int delete(TaskModel dain) {
		// TODO Auto-generated method stub
		int result = 0;
		String sql = "delete from pj_management.task where task_id=?";
		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1, dain.getTask_id());
			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Delete Fail,Inter error", "Fail", JOptionPane.ERROR_MESSAGE);
		}
		return result;
	}

	public boolean isduplicate(TaskModel dain) throws SQLException {
		boolean duplicate = false;
		String sql = "select * from pj_management.task where task_name = ?";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ps.setString(1, dain.getTask_name());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			duplicate = true;
		} else {
			duplicate = false;
		}
		return duplicate;
	}

	public List<TaskModel> selectall() throws SQLException {
		List<TaskModel> list = new ArrayList<TaskModel>();
		String sql = "select * from pj_management.task order by task_id desc";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			TaskModel pm = new TaskModel();
			pm.setTask_id(rs.getInt("task_id"));
			pm.setTask_name(rs.getString("task_name"));
			pm.setDescription(rs.getString("description"));
			pm.setStart_date(rs.getString("start_date"));
			pm.setEnd_date(rs.getString("end_date"));
			pm.setStatus_id(rs.getInt("status_id"));
			pm.setMilestone_id(rs.getInt("milestone_id"));
			pm.setAssigned_id(rs.getString("assigned_by_id"));
			pm.setPriority_id(rs.getInt("priority_id"));
			pm.setType_id(rs.getInt("type_id"));
			list.add(pm);
		}
		return list;
	}
	
	public String returnPjName(int milestoneId)
	{
		String sql = "select * from project j join milestone m on j.project_id = m.project_id where m.milestone_id=?";
		String name = null;
		try {
		        PreparedStatement ps = con.prepareStatement(sql);
		        ps.setInt(1, milestoneId);
		        ResultSet rs = ps.executeQuery();
//		        comboBox.removeAllItems();
//		        comboBox.addItem("-Select-");
//		        dataMap.clear();
		        
		        while (rs.next()) {
		             name = rs.getString("j.project_name");
		            

		        }
		        
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		return name;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
