package controller;

import java.io.File;
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

public class AttachmentController {
	
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
	public List<AttachmentModel> selectall() throws SQLException {
		List<AttachmentModel> list = new ArrayList<AttachmentModel>();
		String sql = "select * from pj_management.attachment order by attachment_id desc";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			AttachmentModel am = new AttachmentModel();
			am.setAttachment_id(rs.getInt("attachment_id"));
			am.setFilename(rs.getString("filename"));
			am.setRelated_type(rs.getString("related_entity_type"));
			am.setRelated_id(rs.getInt("related_entity_id"));
			list.add(am);
		}
		return list;
	}
	
	public int delete(AttachmentModel dain) {
		// TODO Auto-generated method stub
		int result = 0;
	    String tempFileName = null;
	    String sqlSelect = "SELECT temp_filename FROM pj_management.attachment WHERE attachment_id=?";
		String sqlDelete = "delete from pj_management.attachment where attachment_id=?";
		
		try {
		    // Get temp_file name
	        PreparedStatement psSelect = con.prepareStatement(sqlSelect);
	        psSelect.setInt(1, dain.getAttachment_id());
	        ResultSet rs = psSelect.executeQuery();
	        if (rs.next()) {
	            tempFileName = rs.getString("temp_filename");
	        }
	        rs.close();
	        psSelect.close();
			
	        // Delete file from disk
	        if (tempFileName != null) {
	            File file = new File("upload_dir/" + tempFileName);
	            if (file.exists()) {
	                if (!file.delete()) {
	                    JOptionPane.showMessageDialog(null, "Failed to delete the file: " + tempFileName, "Warning", JOptionPane.WARNING_MESSAGE);
	                }
	            }
	        }
	        
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sqlDelete);
			ps.setInt(1, dain.getAttachment_id());
			result = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Delete Fail,Inter error", "Fail", JOptionPane.ERROR_MESSAGE);
		}
		return result;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
