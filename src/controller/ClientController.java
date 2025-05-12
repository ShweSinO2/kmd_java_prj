package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import config.DBConfig;
//import controller.EmployeController;
//import model.CustomerModel;
import model.ClientModel;
import model.EmployeeModel;

public class ClientController {
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

	public int insert(ClientModel dain) {
		int result = 0;
		String sql = "insert into pj_management.client (client_id,name,phone,email,company_name,company_address) values(?,?,?,?,?,?)";

		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1, dain.getClient_id());
			ps.setString(2, dain.getName());
			ps.setString(3, dain.getPhone());
			ps.setString(4, dain.getEmail());
			ps.setString(5, dain.getCompany_name());
			ps.setString(6, dain.getCompany_address());

			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public int update(ClientModel dain) {
		int result = 0;
		String sql = "update pj_management.client set name=?,phone=?,email=?,company_name=?,company_address=? where client_id=? ";

		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);

			ps.setString(1, dain.getName());
			ps.setString(2, dain.getPhone());
			ps.setString(3, dain.getEmail());
			ps.setString(4, dain.getCompany_name());
			ps.setString(5, dain.getCompany_address());
			ps.setInt(6, dain.getClient_id());

			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public int delete(ClientModel dain) {
		// TODO Auto-generated method stub
		int result = 0;
		String sql = "delete from pj_management.client where client_id=?";
		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1, dain.getClient_id());
			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Delete Fail,Inter error", "Fail", JOptionPane.ERROR_MESSAGE);
		}
		return result;
	}

	public int getIDbyName(String name) throws SQLException {
		int id = 0;
		String sql = "select client_id from pj_management.client where name = ?";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			id = rs.getInt("client_id");
		}

		return id;
	}

	public boolean isduplicate(ClientModel dain) throws SQLException {
		boolean duplicate = false;
		String sql = "select * from pj_management.client where name = ? and client_id != ?";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ps.setString(1, dain.getName());
		ps.setInt(2, dain.getClient_id());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			duplicate = true;
		} else {
			duplicate = false;
		}
		return duplicate;
	}

	public List<ClientModel> selectall() throws SQLException {
		List<ClientModel> list = new ArrayList<ClientModel>();
		String sql = "select * from pj_management.client order by client_id asc";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ClientModel pm = new ClientModel();
			pm.setClient_id(rs.getInt("client_id"));
			pm.setName(rs.getString("name"));
			pm.setPhone(rs.getString("phone"));
			pm.setEmail(rs.getString("email"));
			pm.setCompany_name(rs.getString("company_name"));
			pm.setCompany_address(rs.getString("company_address"));

			list.add(pm);
		}
		return list;
	}

	public List<ClientModel> selectone(ClientModel dain) throws SQLException {
		List<ClientModel> list = new ArrayList<ClientModel>();
		String sql = "select * from pj_management.client where name like ? order by client_id ";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ps.setString(1, "%" + dain.getName() + "%");
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			ClientModel cs = new ClientModel();
			cs.setClient_id(rs.getInt("client_id"));
			cs.setName(rs.getString("name"));
			cs.setCompany_name(rs.getString("company_name"));
			cs.setCompany_address(rs.getString("company_address"));
			cs.setPhone(rs.getString("phone"));
			cs.setEmail(rs.getString("email"));
			list.add(cs);
		}
		return list;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
