package tramcity.server.common;
import java.awt.Point;
import java.lang.Math;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;


public class DividePoint{

	// r : radius is the distance between two points

	static int k = 30;
	//w: divide 2 dimensions into squares of length w
	//maxPoint : Maximum number of points
	static Point[] grid ;
	static Object[] active;
	static List<Point> temp = new ArrayList<Point>();
	static int cols ;
	static int rows ;
	static int r;
	public DividePoint( )  {
		// TODO Auto-generated constructor stub

	}
	public static JSONObject getListPoint(int width ,  int height, int maxPoint, int r, double angleStart,double angleEnd){

		active = null;
		List<Point> tempTotal = new ArrayList<Point>();
		// TODO Auto-generated method stub		
		int w = (int) ((int) r/(Math.sqrt(2)));
//		System.out.println("Start Finding: "+width+"-"+height+"-"+maxPoint+"-"+angleStart+"-"+angleEnd+"-"+angleEnd+"-");
		try {
			//step 0	
			cols = (int) Math.floor(width/w);
			rows = (int) Math.floor(height/w);
			grid = new Point[cols*rows];
			for (int i = 0; i < cols*rows; i++) {
				grid[i] = new Point();		
			}
			//step 1
			int x = (int) width/2;
			int y = (int) height/2;
			// positon of x, y
			int i = (int) x/w;
			int j = (int) y/w;
			Point v0 = new Point(x, y);			
			grid[i+j*cols] = v0;
			if(active == null) {
				active = new Object[1];
				active[0] = v0;
			}else {
				active =  add(active, v0) ;
			}
			//Maximum number of points.number of point is is the length of active
			tempTotal.add(v0);
			JSONArray resPath= new JSONArray();
			temp.add(v0);
			int sizePoint = maxPoint;
			while(temp.size() > 0 && active.length< sizePoint) {
				//choose the next starting point
				int randomIndex = 0;
				if(temp.size() >1 && maxPoint >= 8 ) randomIndex= (int) new Random().nextInt(temp.size()-1);
				Point pos = (Point) temp.get(randomIndex);
				boolean found = false;
				for (int n = 0; n < k; n++) {
					double m = 1.1*r;
					//choose angle for point 
					double a = angleStart + ( new Random().nextDouble()*(angleEnd - angleStart));
					double offsetX = m*(Math.cos(a));
					double offsetY = m*(Math.sin(a));
					//vector
					Point  newPoint = new Point(0,0);
					newPoint = new Point((int)(pos.getX()+offsetX),(int)(pos.getY() + offsetY));				
					//coordonne
					int col = (int) (newPoint.getX() / w); 
					int row = (int) (newPoint.getY() / w); 
					boolean ok = true;
					//if the newest point is in the grid so check the distance of this point to the grid around
					if(isInsideEclips(newPoint.x, newPoint.y, width, height)) {							
						if(newPoint.x >= 0 && newPoint.y >=0 && newPoint.x < width && newPoint.y < height && (col+row*cols)>=0 && (col+row*cols) < cols*rows && isZero(grid[col+row*cols])) {  									
							for (int h = -1; h <= 1; h++) {
								for (int g = -1; g <= 1; g++) {
									if(((col+h)+(row+g)*cols)>=0 && ((col+h)+(row+g)*cols) <= cols*rows ) {  
										if((col+h)+(row+g)*cols >= 0 && (col+h)+(row+g)*cols<grid.length ) {
											Point neighbor =  grid[(col+h)+(row+g)*cols];	
											if(!isZero(grid[(col+h)+(row+g)*cols])){
												double d = dst(newPoint.getX(), newPoint.getY(), neighbor.getX(), neighbor.getY());	
												if(d<r) {
													ok = false;
												}
											}
										}
									}
								}
							}		
							if(ok) {
								grid[col+row*cols] = newPoint;
								active = add(active, newPoint);
								temp.add(newPoint);
								//	tempTotal.add(newPoint);
								found = true;
								// add path
								JSONObject pathObj = new JSONObject();
								pathObj.put("fromX", (int) pos.getX());
								pathObj.put("fromY", (int) pos.getY());
								pathObj.put("toX", (int) newPoint.getX());
								pathObj.put("toY", (int) newPoint.getY());
								pathObj.put("line", 0);
								resPath.put(pathObj);
								break;
							}						
						}	
					}
				}
				if(!found) {
					// the point can't find a next point so remove out of temporai
					temp.remove(randomIndex);
				}
			}
			JSONObject resJson =  new JSONObject();
			JSONArray resItems= new JSONArray();
			if( active.length == maxPoint) {
				for (int l = 0; l < active.length; l++) {
					//	System.out.println(active[l].toString());
					JSONObject aItem = new JSONObject();
					aItem.put("x", ((Point) active[l]).x);
					aItem.put("y", ((Point) active[l]).y);
					resItems.put(aItem);
				}		
				resJson.put("ListPoint", resItems );
				resJson.put("ListPath", renderPath(resPath,grid,active,cols,rows,height,width, r,w) );

				return resJson;
			}else
			{
				r=  r -200;
				return  getListPoint(width ,  height, maxPoint, r, angleStart,angleEnd);
			}
		} catch (Exception e) {
			// TODO: handle exception		
			e.printStackTrace();
			return null;
		}
	}
	private static boolean isZero (Point x) {
		return x.distance(new Point(0,0)) ==0;
	}
	private static double getRandomNumberInRange(double min, double max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}
		Random r = new Random();
		return (r.nextDouble())*(max - min) + min;
	}
	// < 1 : inside eclipse; = 0: on eclipse; >1: outside eclipse
	private static boolean isInsideEclips( int x, int y, double width, double height) 
	{ 
		double h = width/2;
		double k = height/2;
		double a = h;
		double b = k;
		// checking the equation of 
		// ellipse with the given point 
		double p = ((double)Math.pow((x - h), 2) / (double)Math.pow(a, 2)) 
				+ ((double)Math.pow((y - k), 2) / (double)Math.pow(b, 2)); 
		return p<=1?true:false; 
	} 
	public static Object[] add(Object[] arr, Object... elements){
		Object[] tempArr = new Object[arr.length+elements.length];
		System.arraycopy(arr, 0, tempArr, 0, arr.length);
		for(int i=0; i < elements.length; i++)
			tempArr[arr.length+i] = elements[i];
		return tempArr;
	}
	public static Object[] remove(Object[] arr, int index){
		if(arr.length >0) {
			Object[] tempArr = new Object[arr.length-1];
			for (int i = 0; i < arr.length-1; i++) {
				if (i!= index) {
					if(i<index) {
						tempArr[i] = arr[i];
					}
					else {
						tempArr[i] = arr[i+1];
					}
				}
			}
			return tempArr;
		}else
		{
			return arr;
		}
	}
	//distance of two point
	public static double dst (double d, double e, double f, double g) {
		final double x_d = f - d;
		final double y_d = g - e;
		return (double)Math.sqrt(x_d * x_d + y_d * y_d);
	}
	public static double dst (Point a, Point b) {
		final double x_d = b.getX() -  a.getX();
		final double y_d = b.getY() -  a.getY();
		return (double)Math.sqrt(x_d * x_d + y_d * y_d);
	}


	public static JSONArray renderPath(JSONArray pathCurrent, Point[] grid,Object[] active,int cols, int rows,int width, int height, int r, int w) throws JSONException {

		JSONArray resPath= new JSONArray();
		double distancePoint =2*r;
		int stepRun = cols>6?2:1;
		// render Horizontal path
		for (int i = 0; i < rows; i+=stepRun) {
			Point start = null;
			for (int j = 0; j < cols; j++) {
				Point end = null;
				boolean ok = false;
				if(!isZero(grid[i*cols + j])) {
					if(start == null)
					{
						start = grid[i*cols + j];
					}
					else {
						end  = grid[i*cols + j];
					}

				}
				else if((i>0)&&!isZero(grid[(i-1)*cols + j])) {
					if(start == null) {
						start = grid[(i-1)*cols + j];
					}
					else {
						end = grid[(i-1)*cols + j];
					}

				}
				if(start != null && end != null) {
					if(dst(start, end)<distancePoint) {
						JSONObject pathObj = new JSONObject();
						pathObj.put("fromX", start.getX());
						pathObj.put("fromY", start.getY());
						pathObj.put("toX", 	end.getX());
						pathObj.put("toY", end.getY());
						pathObj.put("line", 1);
						resPath.put(pathObj);start = end;	
					}

				}	
			}
		}
		// render vertical path
		for (int j = 0; j < cols; j+=stepRun) {
			Point start = null;
			for (int i = 0; i < rows; i++) {
				Point end = null;
				if(!isZero(grid[i*cols + j])) {
					if(start == null) {
						start = grid[i*cols + j];	
					}
					else {
						end  = grid[i*cols + j];
					}

				}
				else if((j>0)&&!isZero(grid[i*cols + j -1])) {
					if(start == null) {
						start = grid[i*cols + j -1];
					}
					else {
						end = grid[i*cols + j -1];
					}						
				}
				if(start != null && end != null ) {
					if(dst(start, end)<distancePoint) {
						JSONObject pathObj = new JSONObject();
						pathObj.put("fromX", start.getX());
						pathObj.put("fromY", start.getY());
						pathObj.put("toX", 	end.getX());
						pathObj.put("toY", end.getY());
						pathObj.put("line", 2);
						resPath.put(pathObj);
						start = end;
					}
				} 
			}
		}
		for (int i = 0; i < resPath.length(); i++) {
			JSONObject x = resPath.getJSONObject(i);
			pathCurrent.put(x);
		}
		return pathCurrent;
	}
}