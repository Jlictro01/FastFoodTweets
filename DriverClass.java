//John Lictro
package Twitter;
//import the necessary twitter and java classes
//import the many necessary classes for the word cloud API
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import twitter4j.IDs;
import twitter4j.Status;
import twitter4j.TwitterException;


public class DriverClass //declare the driverClass
{
  
   
      public static void main (String []args) throws IOException, TwitterException  //declare main
      {
           ArrayList<Status> retweet = new ArrayList<Status>();
           TwitterCode T = new TwitterCode(); //create a new TwitterCode object
         try
         {
             
                T.unTweet();
                ArrayList<String> followingIDs = T.getFollowers();
         
                System.out.println(followingIDs.size());
                for(int i = 0; i < followingIDs.size(); i++)
                {
                  System.out.println(followingIDs.get(i));
                  ArrayList<Status> statuses =  T.queryHandle(followingIDs.get(i));
                  for(int j = 0; j < statuses.size(); j++)
                  {
                      retweet.add(statuses.get(j));
                  }
                }
                T.doRetweet(retweet);
         }
         catch (TwitterException TE)
         {
                System.out.println("Twitter Exception. Removing All Tweets.");
                T.unTweet();
         }
                 
        
         
        



         
      
      }       
            
}
