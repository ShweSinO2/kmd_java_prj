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
		String sql = "select * from pj_management.team_member order by team_id desc";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			TeamMemberModel tmm = new TeamMemberModel();
			tmm.setAttachment_id(rs.getInt("attachment_id"));
			tmm.setFilename(rs.getString("filename"));
			tmm.setRelated_type(rs.getString("related_entity_type"));
			am.setRelated_id(rs.getInt("related_entity_id"));
			list.add(am);
		}
		return list;
	}

}
