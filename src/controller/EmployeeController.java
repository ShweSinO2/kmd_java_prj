package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import config.DBConfig;
import controller.EmployeeController;
//import model.CustomerModel;
import model.EmployeeModel;
import model.ProjectModel;

public class EmployeeController {
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

	public int insert(EmployeeModel dain) {
		int result = 0;
		String sql = "insert into pj_management.employee (employee_id,name,phone,email,password,status,role_id) values(?,?,?,?,?,?,?)";

		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setString(1, dain.getEmployee_id());
			ps.setString(2, dain.getEmployee_name());
			ps.setString(3, dain.getPhone());
			ps.setString(4, dain.getEmail());
			ps.setString(5, dain.getPassword());
			ps.setString(6, "In Active");
			ps.setInt(7, dain.getRole_id());

			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public int update(EmployeeModel dain) {
		int result = 0;
		String sql = "update pj_management.employee set name=?,phone=?,email=?,password=?,status=?,role_id=? where employee_id=? ";

		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);

			ps.setString(1, dain.getEmployee_name());
			ps.setString(2, dain.getPhone());
			ps.setString(3, dain.getEmail());
			ps.setString(4, dain.getPassword());
			ps.setString(5, "Active");
			ps.setInt(6, dain.getRole_id());
			ps.setString(7, dain.getEmployee_id());

			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public int delete(EmployeeModel dain) {
		// TODO Auto-generated method stub
		int result = 0;
		String sql = "delete from pj_management.employee where employee_id=?";
		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setString(1, dain.getEmployee_id());
			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Delete Fail,Inter error", "Fail", JOptionPane.ERROR_MESSAGE);
		}
		return result;
	}

	public String getPswbyId(String id) throws SQLException {
		String psw = "";
		String sql = "select password from pj_management.employee where employee_id = ?";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ps.setString(1, id);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			psw = rs.getString("password");
		}

		return psw;
	}

	public boolean isduplicate(EmployeeModel dain) throws SQLException {
		boolean duplicate = false;
		String sql = "select * from pj_management.employee where name = ? ";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ps.setString(1, dain.getEmployee_name());
//		ps.setString(2, dain.getEmployee_id());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			duplicate = true;
		} else {
			duplicate = false;
		}
		return duplicate;
	}

	public boolean isduplicate1(EmployeeModel dain) throws SQLException {
		boolean duplicate = false;
		String sql = "select * from pj_management.employee where name = ? and employee_id != ?";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ps.setString(1, dain.getEmployee_name());
		ps.setString(2, dain.getEmployee_id());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			duplicate = true;
		} else {
			duplicate = false;
		}
		return duplicate;
	}

	public List<EmployeeModel> selectall() throws SQLException {
		List<EmployeeModel> list = new ArrayList<EmployeeModel>();
		String sql = "select * from pj_management.employee order by employee_id desc";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			EmployeeModel pm = new EmployeeModel();
			pm.setEmployee_id(rs.getString("employee_id"));
			pm.setEmployee_name(rs.getString("name"));
			pm.setPhone(rs.getString("phone"));
			pm.setEmail(rs.getString("email"));
			pm.setPassword(rs.getString("password"));
			pm.setStatus(rs.getString("status"));
			pm.setRole_id(rs.getInt("role_id"));

			list.add(pm);
		}
		return list;
	}

	public boolean loginState(EmployeeModel dain) throws SQLException {
		boolean duplicate = false;
		String sql = "select * from pj_management.employee where name=? and password=?";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ps.setString(1, dain.getEmployee_name());
		ps.setString(2, dain.getPassword());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			duplicate = true;
		} else {
			duplicate = false;
		}
		return duplicate;
	}

	public List<EmployeeModel> selectone(EmployeeModel dain) throws SQLException {
		List<EmployeeModel> list = new ArrayList<EmployeeModel>();
		String sql = "select * from pj_management.employee where name like ? order by employee_id desc";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ps.setString(1, dain.getEmployee_name() + "%");
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			EmployeeModel cs = new EmployeeModel();
			cs.setEmployee_id(rs.getString("employee_id"));
			cs.setEmployee_name(rs.getString("name"));
			cs.setPassword(rs.getString("password"));
			cs.setPhone(rs.getString("phone"));
			cs.setEmail(rs.getString("email"));
			cs.setStatus(rs.getString("status"));
			cs.setRole_id(rs.getInt("role_id"));
			list.add(cs);
		}
		return list;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
