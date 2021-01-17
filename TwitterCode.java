//John Lictro
package Twitter;
import twitter4j.*; //import all of the twitter4j classes
//also import useful java classes
import java.util.ArrayList;
import java.io.*;
import java.util.Collections;
import java.util.Date;


public class TwitterCode implements Comparable //create the TwitterCode class
   {
   
      private Twitter twitterInstance; //declare a private Twitter instance
      private ArrayList<Status> arrListStat; //declare a private ArrayList that will hold the Statuses, the Status data type is provided by the twitter4j library
      private Date newDate = new Date();
     
      public TwitterCode() //the constructor for TwitterCode
      {
        
         twitterInstance = TwitterFactory.getSingleton();  //a single instance of twitter thats able to use twitters methods to return different things
         arrListStat = new ArrayList<Status>();    //an arraylist to contain Statuses
      }
      public ArrayList getFollowers() throws TwitterException
      {
          PagableResponseList<User> followerlist;
          followerlist = twitterInstance.getFriendsList("fastfoodsales", -11, 200);
          ArrayList<String> userNames = new ArrayList<String>();
          for(int i = 0; i < followerlist.size(); i++)
          {
              userNames.add(followerlist.get(i).getScreenName());
          }
          return userNames;
      }
      public void unTweet() throws TwitterException
      {
          ArrayList<Status> retweets = new ArrayList<Status>();
          Paging page = new Paging(1, 200);
          for(int i = 1; i <= 10; i++)
         {
             page.setPage(i);
             retweets.addAll(twitterInstance.getUserTimeline("fastfoodsales",page));
         }
          for(int i = 0; i < retweets.size(); i++)
          {
              twitterInstance.destroyStatus(retweets.get(i).getId());
          }
      }
      //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     public ArrayList queryHandle(String handle) throws TwitterException, IOException
     {
         String[] keyWords = {"sale", "%", "free", "off", "half", "buy", "one", "deal", "limited", "steal", "bogo", "just", "offer", "expires", "today", "until", "deals", "only", "now", "$1", "%5", "code", "weekend", "time","app", "each", "mobile", "new", "purchase" };
         int[] keyPoints = { 15,      15,      15,   15,    11,     8,      5,    10,       11,       5,      15,      5,      15,      15,        3,       5,      10,       7,     4,    10,    5,    15,       4,        4,    8,      3,      5,       5,       5 };
         int dealornodeal;
         ArrayList<Status> sickDeals = new ArrayList<Status>();
         ArrayList<Status> promotions = new ArrayList<Status>();
         arrListStat.clear();
         fetchTweets(handle);
         for(int i = arrListStat.size()-1; i > 0; i--)
         {
             
             String tweet = arrListStat.get(i).getText();
             if(!tweet.substring(0, 1).equals("@"))
             {
                 if((arrListStat.get(i).getCreatedAt().getMonth() == newDate.getMonth() -1 || arrListStat.get(i).getCreatedAt().getMonth() == newDate.getMonth()) && (arrListStat.get(i).getCreatedAt().getYear() == newDate.getYear())  )
                 {
                    
                      sickDeals.add(arrListStat.get(i));
                 }
                
                
               
             }
           
            
         }
          for(int j = 0; j < sickDeals.size(); j++)
                 {
                     dealornodeal = 0;
                     String advert = sickDeals.get(j).getText();
                     String[] splits = advert.split(" ");
                     for(int k = 0; k < splits.length; k++)
                     {
                         for(int x = 0; x < keyWords.length; x++)
                         {
                            if(splits[k].equalsIgnoreCase(keyWords[x]))
                            {
                                dealornodeal += keyPoints[x];
                                break;
                                
                             
                            }
                         }
                     }
                     if(dealornodeal >= 21  )
                     {
                         
                             promotions.add(sickDeals.get(j));
                       
                     }
                 }
         return promotions;
     
         
     }
     
     
     public void doRetweet(ArrayList<Status> promotions)
     {
         ArrayList<Status> toSort = promotions;
         for(int k = 0; k < toSort.size(); k++)
          {
              for(int h = 1; h < toSort.size(); h++)
              {
                  if(toSort.get(h-1).getCreatedAt().before(toSort.get(h).getCreatedAt()))
                  {
                      
                      Collections.swap(toSort, h-1, h);
                      //System.out.println(promotions.get(k));
                      
                  }
              }
          }
          for(int h = toSort.size()-1; h >= 0; h--)
          {
               try
              {
                 
                  twitterInstance.retweetStatus(toSort.get(h).getId());
              }
              catch(TwitterException TE)
              {
                 System.out.println("SKIPPING!");
                 System.out.println(TE);
              }
          }
     }
     
     private void fetchTweets(String handle) throws TwitterException, IOException
     {
         Paging page = new Paging(1, 200);
         for(int i = 1; i <= 10; i++)
         {
             page.setPage(i);
             arrListStat.addAll(twitterInstance.getUserTimeline(handle,page));
         }
     }
     
     //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~UNRELATED TO FAST FOOD DEALS, SIMPLY WILL PUT TWEETS UNDER A CERTAIN HASHTAG IN A FILE
      public void searchHashtag(String hashtag, int numtoFind) //method to search for tweets with a hashtag, takes in the hashtag and the number of tweets to find
      {
          Query query = new Query(hashtag); //create a new twitter query with the input hashtag
        int numbTweets = numtoFind; //set the number of tweets equal to numtoFind
       
         ArrayList<Status>tweets = new ArrayList<Status>(); //create an arraylist of statuses called tweets
         while(tweets.size() < numbTweets) //while the size of the arraylist is less than the number of tweets to get
         {
         
                 query.setCount(numbTweets); //set the query to search for numbTweets amount of tweets
          
             try //try the following code
             {
                 QueryResult result = twitterInstance.search(query); //create a QueryResult object and set it equal to a twitter search of the query
                 tweets.addAll(result.getTweets()); //add all of the tweets returned to the arraylist
              
             }
             catch(TwitterException te) //catch a possible twitter exception
             {
                 System.out.println("Couldn't connect: "+te);
             }
         }
         File tweetFile = new File("C:\\Users\\owner\\Desktop\\tweets.txt"); //create a file object for the file to store the tweets in
             try //attempt the following code
             {
                PrintWriter output = new PrintWriter(tweetFile); //create a printWriter object
              
                for(int i = 0; i < tweets.size(); i++) //for every status in the arraylist
                {
                    Status t = (Status)tweets.get(i); //get the status
                    
                    String msg = t.getText(); //get the text of that status
                 
                   
                   
                    output.print(i+": "+msg+"\n"); //print it to the file
                   
                 
                }
                output.close(); //close the printwriter
             }
             catch(FileNotFoundException f) //catch the possible exception
             {
                 System.out.println("File not found.");
             }
             
             
      }
      public void tweetOut(String message) throws TwitterException 
      {
          Status status = twitterInstance.updateStatus(message);
      }

   
   
   }  
