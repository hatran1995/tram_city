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
	public DividePoint( )  {
		// TODO Auto-generated constructor stub

	}
	public static JSONObject getListPoint(int width ,  int height, int maxPoint, int r, double angleStart,double angleEnd){
		
		active = null;
		List<Point> tempTotal = new ArrayList<Point>();
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

			if(active == null) {
				active = new Object[1];
				active[0] = v0;
			}else {
				active =  add(active, v0) ;
			}
			//Maximum number of points.number of point is is the length of active
			tempTotal.add(v0);
			JSONArray resPath= new JSONArray();
//			for (int kz = 0; kz < 5; kz++) {
//				int sizePoint = (kz+1) * maxPoint/4;
//				angleStart = kz*Math.PI/2;
//				angleEnd = (kz+1)*Math.PI/2;
//
//				temp = new ArrayList<Point>();
//				temp.add(v0);
//				
//				if(kz==4) {
//					angleStart = 0;
//					angleEnd = 2*Math.PI;
//					sizePoint = maxPoint;
//					temp = tempTotal;
//				}			
//
//			}
			temp.add(v0);
			int sizePoint = maxPoint;
			while(temp.size() > 0 && active.length< sizePoint) {
				//System.out.println(+ temp.size()+ "="+ active.length+"="+maxPoint);
				//random the next starting point
				int randomIndex = 0;
				if(temp.size() >1 && maxPoint >= 8 ) randomIndex= (int) new Random().nextInt(temp.size()-1);
				Point pos = (Point) temp.get(randomIndex);
				boolean found = false;
				ArrayList<JSONObject> listPathMore = new ArrayList<JSONObject> ();
				for (int n = 0; n < k; n++) {

					//random radius from r to 2r
					//double m = getRandomNumberInRange(r,2*r);
					double m = 1.1*r;
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
					if(isInsideEclips(newPoint.x, newPoint.y, width, height)) {							
						if(newPoint.x >= 0 && newPoint.y >=0 && newPoint.x < width && newPoint.y < height && (col+row*cols)>=0 && (col+row*cols) < cols*rows && isZero(grid[col+row*cols])) {  									
							for (int h = -1; h <= 1; h++) {
								for (int g = -1; g <= 1; g++) {
									if(((col+h)+(row+g)*cols)>=0 && ((col+h)+(row+g)*cols) <= cols*rows ) {  
										if((col+h)+(row+g)*cols >= 0 && (col+h)+(row+g)*cols<grid.length ) {
											Point neighbor =  grid[(col+h)+(row+g)*cols];	
											if(!isZero(grid[(col+h)+(row+g)*cols])){
												double d = dst(newPoint.getX(), newPoint.getY(), neighbor.getX(), neighbor.getY());	
												//	System.out.println("kc:"+d+"-"+r);
												if(d<r) {
													ok = false;
												}else {			
//													JSONObject pathObj = new JSONObject();
//													pathObj.put("fromX", (int) newPoint.getX());
//													pathObj.put("fromY", (int) newPoint.getY());
//													pathObj.put("toX", (int) neighbor.getX());
//													pathObj.put("toY", (int) neighbor.getY());
//													pathObj.put("line", 0);										
//													listPathMore.add(pathObj);
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
//								for (int kc = 0; kc < listPathMore.size(); kc++) {
//									resPath.put(listPathMore.get(kc));
//								}
								break;
							}
						}	
					}
				}
				if(!found) {
					// the point can't find a next point so remove out of temporai
					//System.out.println("drop point:"+randomIndex);
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
				resJson.put("ListPath", resPath );
				
				return resJson;
			}else
			{
				if(maxPoint <8 )
					r=  r -100;
				else
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

	public static double dst (double d, double e, double f, double g) {
		final double x_d = f - d;
		final double y_d = g - e;
		return (double)Math.sqrt(x_d * x_d + y_d * y_d);
	}
}