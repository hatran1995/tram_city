package tramcity_server_common;

public class City {

	int ID;
	String Name;
	double Height ; 
	double Width;
	private void contructor() {
		// TODO Auto-generated method stub

	}
	

	public City(int iD, String name, double height, double width) {
		super();
		this.ID = iD;
		this.Name = name;
		this.Height = height;
		this.Width = width;
	}

	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		this.ID = iD;
	}
	public String getName() {
		return Name;
	}

	public void setName(int iD) {
		this.Name = Name;
	}
	public double getHeight() {
		return Height;
	}
	public void setHeight(int height) {
		this.Height = height;
	}
	public double getWidth() {
		return Height;
	}
	public void setWidth(int width) {
		this.Width = width;
	}
}
