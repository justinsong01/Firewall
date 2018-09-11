#Illumio Coding Challenge 2018

First of all, I would like to thank Illumio for giving me an opportunity for Coding Assignment/challenge for internship opportunity.

=== Steps to run this assignment ===
1. In main method, please include the full file path of your csv file for Firewall constructor. 
2. Add any tests you would like to perform in main method. 
3. javac Firewall.java     // if you are just compiling it from cmd directly, please comment line 1 "package illumio"
4. java Firewall ./a.csv    //NOTE that as instructed on assignment, one needs to provide a full file path and this example is under the assumption that both your java file and csv file is in the same directory.


=== Submission Guideline Question 2 ===
 
 a. I have tested my codes using public static void main as I have spent most of the time programming a right solution to the problem. Nonetheless, I have tested several edge & extreme cases which verified that my code works as intended. 
 
 b. I have decided to use a nested Hashmap that is optimized for finding whether there exists a record that follows the rule from csv. At the end, I have also used HashSet for list of IP address for the specified port, protocol and direction. Structure of nested HashMap was chosen rather than using ArrayList, as it is faster to find an element through the use of nested Hashmap. As for IP address comparison, I have decided to convert ip address to one long number for a better comparison such that given 111.111.111.111-121.122.122.22, any ip address (ex. 111.111.111.112) within the range can be accepted without a problem. 
 
 c. If I had more time, I would have written jUnit code to throughly test my code. Nonetheless, I believe I got most of crucial edge cases correct.  Also I could have organized my code structure better. 

 d. I just would like to thank a reviewer for taking his/her time to review this coding assignment. This coding assignment was interesting enough for me and this assignment was manageable within 60-90 minutes of the time frame.  There is no dependency needed for this assignment, but Java 8 is recommended instead of Java 7 to run this assignment. 
 
=== Submission Guideline Question 3 ===
  
I would like to work for platform team the most, as I have various experience working with API and library (ex. Ignite Caching Layer for NoSQL databases (ex. MongoDB & Cassandra), DataStax, Java (including Spring), Scala)

My ranking is as follows; 
1. Platform
2. Policy
3. Data

Thank you very much again!