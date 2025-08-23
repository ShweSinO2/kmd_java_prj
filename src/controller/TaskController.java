package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
//			System.out.println("task id:"+ dain.getTask_id());
			ps.setInt(1, dain.getTask_id());
			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Delete Fail,Inter error", "Fail", JOptionPane.ERROR_MESSAGE);
		}
//		System.out.println(result);
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
		String sql = "select * from pj_management.task order by status_id asc";
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
	
	//get data for login user task only
	public List<TaskModel> selectsingle(String employeeid) throws SQLException {
		List<TaskModel> list = new ArrayList<TaskModel>();
		String sql = "select * from pj_management.task where assigned_by_id =? order by status_id asc";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ps.setString(1, employeeid);
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


	public String returnPjName(int milestoneId) {
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

//	public void updateTask(int status_id,int task_id)
//	{
//		String sql="update task set status_id=? where task_id=? ";
//		try { 
//			 PreparedStatement ps = con.prepareStatement(sql);
//			 ps.setInt(1,status_id);
//			 ps.setInt(2,task_id);
//			
//		}
//		catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//		System.out.println("Update successful! ");
//	}

	public List<TaskModel> selectone(TaskModel dain) throws SQLException {
		List<TaskModel> list = new ArrayList<TaskModel>();
		
		String sql = "";
		PreparedStatement ps;
		
		if(dain.getAssigned_id() != null && !dain.getAssigned_id().isEmpty() && dain.getStatus_id() > 0) {
			sql = "select * from pj_management.task where assigned_by_id like ? and status_id = ? order by status_id";
			ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setString(1, dain.getAssigned_id() + "%");
			ps.setInt(2, dain.getStatus_id());		
		} else if(dain.getAssigned_id() != null && !dain.getAssigned_id().isEmpty() && dain.getStatus_id() == 0) {
			sql = "select * from pj_management.task where assigned_by_id like ? order by status_id";
			ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setString(1, dain.getAssigned_id() + "%");
		} else {
			sql = "select * from pj_management.task where status_id = ? order by status_id";
			ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1, dain.getStatus_id());
		}
				
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			TaskModel cs = new TaskModel();
			cs.setTask_id(rs.getInt("task_id"));
			cs.setTask_name(rs.getString("task_name"));
			cs.setDescription(rs.getString("description"));
			cs.setStart_date(rs.getString("start_date"));
			cs.setEnd_date(rs.getString("end_date"));
			cs.setStatus_id(rs.getInt("status_id"));
			cs.setMilestone_id(rs.getInt("milestone_id"));
			cs.setAssigned_id(rs.getString("assigned_by_id"));
			cs.setPriority_id(rs.getInt("priority_id"));
			cs.setType_id(rs.getInt("type_id"));
			cs.setTask_id(rs.getInt("task_id"));
//			cs.setProject_id(rs.getInt("project_id"));
			list.add(cs);
		}
		return list;
	}
	
	public boolean isStartDateConflict(String assignedId, String startDate) {
	    boolean conflict = false;
	    String sql = "SELECT * FROM pj_management.task WHERE assigned_by_id = ? AND start_date = ?";
	    try {
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setString(1, assignedId);
	        ps.setString(2, startDate);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            conflict = true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return conflict;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
