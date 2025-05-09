package config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class MySqlQuery {
	public static Connection con = null;
	Map<String, Integer> teamMap = new HashMap<>();

	static {
		DBConfig cls = new DBConfig();
		try {
			con = cls.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Fail, Inter error","Fail", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
//	public static void addCoboBox(String tableName,String columnName,JComboBox<String> comboBox) {
//		String sql = "select " + columnName + " from " + tableName;
//		try {
//			PreparedStatement ps = (PreparedStatement)con.prepareStatement(sql);
//			ResultSet rs = ps.executeQuery();
//			comboBox.removeAllItems();
//			comboBox.addItem("-Select-");
//			while(rs.next()) {
//				String value = rs.getString(columnName);
//				
//				comboBox.addItem(value);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public static void addCoboBox(String tableName, String columnName1, String columnName2,JComboBox<String> comboBox, Map<String, Integer> dataMap) {
	    String columnName = columnName1 +", "+ columnName2;
		String sql = "SELECT " + columnName + " FROM "+ tableName;
	    try {
	        PreparedStatement ps = con.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();
	        comboBox.removeAllItems();
	        comboBox.addItem("-Select-");
	        dataMap.clear();

	        while (rs.next()) {
	            int id = rs.getInt(columnName1);
	            String name = rs.getString(columnName2);

	            comboBox.addItem(name);
	            dataMap.put(name, id);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void addCoboBoxEmployee(String tableName, String columnName1, String columnName2,JComboBox<String> comboBox, Map<String, String> dataMap) {
	    String columnName = columnName1 +", "+ columnName2;
		String sql = "SELECT " + columnName + " FROM "+ tableName;
	    try {
	        PreparedStatement ps = con.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();
	        comboBox.removeAllItems();
	        comboBox.addItem("-Select-");
	        dataMap.clear();

	        while (rs.next()) {
	            String id = rs.getString(columnName1);
	            String name = rs.getString(columnName2);

	            comboBox.addItem(name);
	            dataMap.put(name, id);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public static void getComboData(String tableName, String columnName1, String columnName2, Map<String, Integer> dataMap) {
	    String columnName = columnName1 +", "+ columnName2;
		String sql = "SELECT " + columnName + " FROM "+ tableName;
	    try {
	        PreparedStatement ps = con.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();
	        dataMap.clear();

	        while (rs.next()) {
	            int id = rs.getInt(columnName1);
	            String name = rs.getString(columnName2);
	            dataMap.put(name, id);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void addCoboBoxV2(JComboBox<String> comboBox, Map<String, String> dataMap,int milestone_id) {
//	    String columnName = columnName1 +", "+ columnName2;
		String sql = "SELECT * FROM employee e "
				+ " JOIN team_member tm ON tm.employee_id = e.employee_id "
	             + "JOIN project p ON p.team_id = tm.team_id "
	             + "JOIN milestone m ON  m.project_id = p.project_id  "
	             + "WHERE m.milestone_id = ?" ;
	    try {
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setInt(1, milestone_id);
	        ResultSet rs = ps.executeQuery();
	        comboBox.removeAllItems();
	        comboBox.addItem("-Select-");
	        dataMap.clear();

	        while (rs.next()) {
	            String id = rs.getString("e.employee_id");
	            String name = rs.getString("e.name");

	            comboBox.addItem(name);
	            dataMap.put(name, id);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void addCoboBoxV1(JComboBox<String> comboBox, Map<String, Integer> dataMap,int project_id) {
//	    String columnName = columnName1 +", "+ columnName2;
		String sql = "SELECT  *  FROM milestone WHERE project_id = ?" ;
				
	    try {
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setInt(1, project_id);
	        ResultSet rs = ps.executeQuery();
	        comboBox.removeAllItems();
	        comboBox.addItem("-Select-");
	        dataMap.clear();

	        while (rs.next()) {
	            int id = rs.getInt("milestone_id");
	            String name = rs.getString("milestone_name");

	            comboBox.addItem(name);
	            dataMap.put(name, id);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}