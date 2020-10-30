package tramcity.server.common;

import java.awt.Point;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.JSONObject;
import org.json.JSONArray;

//import com.mysql.cj.xdevapi.JsonArray;
//import com.mysql.cj.xdevapi.JsonValue;

public class DividePoint{
	//	public static void main(String[] args) {
	//	
	//		//double degs = Math.toDegrees(Math.atan(width/height));		
	//		getListPoint();
	//		
	//	}
	// r : radius is the shortest distance between two points

	static int k = 30;
	//w: divide 2 dimensions into squares of length w
	//maxPoint : Maximum number of points
	static Point[] grid ;
	static Object[] active;
	static List<Point> temp = new ArrayList<Point>();
	static int cols ;
	static int rows ;
	static int r;
	static int width;
	static int height;
	static int maxPoint;
	static double angleStart;
	static double angleEnd;
	public DividePoint(int width ,  int height, int maxPoint,int r , double angleStart, double angleEnd )  {
		// TODO Auto-generated constructor stub
		this.width= width;
		this.height= height;
		this.r= r;
		this.maxPoint= maxPoint;
		this.angleStart= angleStart;
		this.angleEnd= angleEnd;
	}
	public static JSONObject getListPoint(){

		active = null;
		temp = new ArrayList<Point>();
		// TODO Auto-generated method stub
		// canvas : 600x400			
		int w = (int) ((int) r/(Math.sqrt(2)));
		System.out.println("Start Finding: "+width+"-"+height+"-"+maxPoint+"-"+angleStart+"-"+angleEnd+"-"+angleEnd+"-");

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
			// int x = new Random().nextInt(width);
			// int y = new Random().nextInt(height);
			// positon of x, y
			int i = (int) x/w;
			int j = (int) y/w;
			Point v0 = new Point(x, y);			
			grid[i+j*cols] = v0;
			temp.add(v0);
			if(active == null) {
				active = new Object[1];
				active[0] = v0;
			}else {
				active =  add(active, v0) ;
			}
			//Maximum number of points.number of point is is the length of active
			JSONArray resPath= new JSONArray();
			//tramway in a line = (Math.min(width, height)/(2*r)
			//min line is 4. 
			int linecount = Math.max(4,  maxPoint /(Math.min(width, height)/(2*r)-1));
			for (int kz = 0; kz < linecount; kz++) {
				angleStart = (2*Math.PI/(linecount+1))*kz;
				angleEnd = angleStart + Math.min(2*Math.PI/(linecount+1), Math.PI/4);//35+90*(kz+1);

				int sizePoint = ((maxPoint - 1)/linecount )*(kz+1)+1;
				temp =  new ArrayList<Point>();
				temp.add(v0);
				if(maxPoint > 10) sizePoint += 1;
				if(kz == linecount-1) sizePoint = maxPoint;
				while(temp.size() > 0 && active.length< sizePoint) {
					int randomIndex =0;
					if(temp.size() >1) randomIndex= (int) new Random().nextInt(temp.size()-1);
					Point pos = (Point) temp.get(randomIndex);
					boolean found = false;
					for (int n = 0; n < k; n++) {

						//random radius from r to 2r
						//double m = getRandomNumberInRange(r,2*r);
						double m = 1*r;
						//random angle for point 
						double a = angleStart + ( new Random().nextDouble()*(angleEnd - angleStart));
						double offsetX = m*(Math.cos(a));
						double offsetY = m*(Math.sin(a));
						//vector
						Point  newPoint = new Point(0,0);
						newPoint = new Point((int)(pos.getX()+offsetX),(int)(pos.getY() + offsetY));				
						// coordonne
						int col = (int) (newPoint.getX() / w); 
						int row = (int) (newPoint.getY() / w); 
						boolean ok = true;
						//if the newest random point is in the grid so check the distance of this point to the grid around
						if(newPoint.x >= 0 && newPoint.y >=0 && newPoint.x < width && newPoint.y < height && (col+row*cols)>=0 && (col+row*cols) < cols*rows   && isInsideEclips(newPoint.x, newPoint.y))
							if( isZero(grid[col+row*cols])) {  									
								for (int h = -1; h <= 1; h++) {
									for (int g = -1; g <= 1; g++) {
										if(((col+h)+(row+g)*cols)>=0 && ((col+h)+(row+g)*cols) <= cols*rows ) {  
											if((col+h)+(row+g)*cols >= 0 && (col+h)+(row+g)*cols<grid.length ) {
												Point neighbor =  grid[(col+h)+(row+g)*cols];	
												if(!isZero(grid[(col+h)+(row+g)*cols])){
													double d = dst(newPoint.getX(), newPoint.getY(), neighbor.getX(), neighbor.getY());	
													//	System.out.println("kc:"+d+"-"+r);
													if(d< Math.sin(Math.PI/(linecount)) * r ) {
														ok = false;
														System.out.println(kz + ":" + newPoint.getX() +" - "+ newPoint.getY() + "  false");
													}
												}
											}
										}
									}
								}	
								//System.out.println(ok);
								if(ok) {
									grid[col+row*cols] = newPoint;
									active = add(active, newPoint);
									temp.add(newPoint);
									found = true;
									System.out.println(kz + ":" + newPoint.getX() +" - "+ newPoint.getY());
									// add path
									JSONObject pathObj = new JSONObject();
									pathObj.put("fromX", (int) pos.getX());
									pathObj.put("fromY", (int) pos.getY());
									pathObj.put("toX", (int) newPoint.getX());
									pathObj.put("toY", (int) newPoint.getY());
									pathObj.put("line", kz);
									resPath.put(pathObj);
									temp.remove(randomIndex);
									break;
								}
							}
//							else {
//								newPoint = grid[col+row*cols];
//								grid[col+row*cols] = newPoint;
//								temp.add(newPoint);
//								found = true;
//								System.out.println( newPoint.getX() +" - "+ newPoint.getY());
//								// add path
//								JSONObject pathObj = new JSONObject();
//								pathObj.put("fromX", (int) pos.getX());
//								pathObj.put("fromY", (int) pos.getY());
//								pathObj.put("toX", (int) newPoint.getX());
//								pathObj.put("toY", (int) newPoint.getY());
//								pathObj.put("line", kz);
//								resPath.put(pathObj);
//							//	temp.remove(randomIndex);
//								break;
//							}
					}
					if(!found) {
						// the point can't find a next point so remove out of temporai
						//System.out.println("drop point:"+randomIndex);
						temp.remove(randomIndex);
					}
				}
			}				
			JSONObject resJson =  new JSONObject();
			JSONArray resItems= new JSONArray();
			for (int l = 0; l < active.length; l++) {
				//	System.out.println(active[l].toString());
				JSONObject aItem = new JSONObject();
				aItem.put("x", ((Point) active[l]).x);
				aItem.put("y", ((Point) active[l]).y);
				resItems.put(aItem);
			}			
			resJson.put("ListPoint", resItems );
			resJson.put("ListPath", resPath );
			return resJson;
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
	private static boolean isInsideEclips( int x, int y) 
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
	//distance between two points 1:(d,e) 2:(f,g)
	public static double dst (double d, double e, double f, double g) {
		final double x_d = f - d;
		final double y_d = g - e;
		return (double)Math.sqrt(x_d * x_d + y_d * y_d);
	}
}