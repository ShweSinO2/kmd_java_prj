package model;

public class AttachmentModel {
	
	private int attachment_id;
	private String filename;
	private String filepath;
	private int related_id;
	private String related_type;

	public int getAttachment_id() {
		return attachment_id;
	}

	public void setAttachment_id(int attachment_id) {
		this.attachment_id = attachment_id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getRelated_type() {
		return related_type;
	}

	public void setRelated_type(String related_type) {
		this.related_type = related_type;
	}

	public int getRelated_id() {
		return related_id;
	}

	public void setRelated_id(int related_id) {
		this.related_id = related_id;
	}

}
