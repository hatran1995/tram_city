package tramcity.test;

import tramcity.server.common.City;
import tramcity.server.connection.DataSource;

import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;

import tramcity.server.common.ApiResponse;

public class TestConnectionPool extends  Thread  {	
	private Thread t;
	private String threadName;
	
	TestConnectionPool( String name){
	       threadName = name;
	       System.out.println("Creating " +  threadName );
	   }
	public void run() {
	    System.out.println("Running " +  threadName );
	    try {
	       for(int i = 1; i <= 4; i++) {
	          System.out.println("Thread: " + threadName + "-" + i + " start get list all city");
	          ApiResponse resItem =  City.getAllCity();

				//compare resItem.body.success with testItem.output 
				if( resItem.getBody().getBoolean("success") == true) {
					JSONArray resList = resItem.getBody().getJSONArray("data");
					//test ok
					if(resList.length() > 0) {
							System.out.println("Thread: " + threadName + "-" + i +" JsonListCity: " + resList.toString() + "");
					}else {
						//test fail
						System.err.println("Test false");

					}
				}
	          // Let the thread sleep for a while.
	          Thread.sleep(50);
	       }
	   } catch (InterruptedException e) {
	       System.out.println("Thread " +  threadName + " interrupted.");
	   } catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   System.out.println("Thread " +  threadName + " exiting.");
	 }
	public void start ()
	   {
	      System.out.println("Starting " +  threadName );
	      if (t == null)
	      {
	         t = new Thread (this, threadName);
	         t.start ();
	      }
	   }
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		DataSource dst = new DataSource();
		TestConnectionPool T1 = new TestConnectionPool( "Thread-a");
	    T1.start();
	    TestConnectionPool T2 = new TestConnectionPool( "Thread-b");
		T2.start();
	      
	}
}