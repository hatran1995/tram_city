
package tramcity_server_test;

import tramcity_server_common.City;
import tramcity_server_common.ApiResponse;

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
	       for(int i = 4; i > 0; i--) {
	          System.out.println("Thread: " + threadName + "-" + i + " start get list all city");
	  
			City resItem = City.getCityByID(13);
			//compare resItem.body.success with testItem.output 
			if( resItem != null) {
				if(resItem.getID() > 0) {
					System.out.println("Thread: " + threadName + "-" + i +" test Success: Name City - "+ resItem.getName());
					
				}else {
					//test fail
					System.err.println("Thread: " + threadName + "-" + i +" test false");
				}
			}
	          // Let the thread sleep for a while.
	          Thread.sleep(50);
	       }
	   } catch (InterruptedException e) {
	       System.out.println("Thread " +  threadName + " interrupted.");
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
	
	public static void main(String[] args) {
		
		TestConnectionPool T1 = new TestConnectionPool( "Thread-1");
	      T1.start();
			TestConnectionPool T2 = new TestConnectionPool( "Thread-2");
		      T2.start();
	      
	}
}