///@author: Bertan Berker
///
/// This is a musicMaker application that creates a chord progression
/// Based on the given major chord. Using a backtracking algorithm, this program
/// Finds the chords that has to be played for the chord progression
/// We have basic scales in a text file which we will read
/// We will store all of the scales in a hashmap data structure 
/// The chord progression we will create is I-V-VI-IV

/// All the Scales are from the website: https://www.pianoscales.org/major.html


import java.util.Scanner;  /// Importing the Scanner class for getting user input
import java.util.ArrayList;
import java.util.HashMap; /// Importing HashMap to store the scales
import java.io.File;  // Importing the File class
import java.io.FileNotFoundException;  // Importing this class to handle errors
import java.util.LinkedList; // Importing LinkedList to store the scales in a chain
import java.util.Map.Entry;


public class musicMaker {

    /// The Possible Chords/ Notes
    public static String arr = "CDEFGAB";


    /// This is the chord progression we are going to print at the end
    public static String progression = ""; 


    /// Hashmap that contains all the scales that we will use to get spaces
    /// (Key: String Note, value: LinkedList Scale)
    
    public static HashMap<String, LinkedList> scales = new HashMap<String, LinkedList>();


    /// HashMap that contains the steps (space) between notes (Key: Note-Pair, value: int Space)

    public static HashMap<String, Double> spaces = new HashMap<String, Double>();


    /// ArrayList is used for the order of the keys in spaces,
    /// because HashMap does not maintain our insertion order
    
    public static ArrayList<String> inOrder = new ArrayList<String>();


    /// This is the recursive function that creates the chord Progression
    /// :param chord: the initial chord (I)
    /// :param place: the place we are putting the chord at
    /// :return: true if there is a chord progression

    public static boolean chordProgression(String chord, int place) {
        
        /// Base case: The string consists of 4 chords

        if (place == 4) {
            return true;
        }

        // Recursion (there are 8 notes in a scale so that is why c < 8)

        for(int c = 0; c < 8; c++) {

            /// Checking if the chord is the correct one

            String addition = scales.get(chord).get(c).toString();

            if (isNextChord(chord, addition, place)){

                progression += addition;
;
                if(chordProgression(chord, place+1)) {
                    return true;
                }
            }
            
        }
        
        return false;
    }


    /// This function checks whether a chord should be added for the progression or not
    /// :param chord: it is the I in the progression
    /// :param add: the chord we are checking
    /// :param place: where are we trtying to place the chord
    
    public static boolean isNextChord(String chord, String add, int place) {
        
        /// Calculating the sum of steps between notes

        double sum = 0.0;

        for(String pair: inOrder) {
            
            sum += spaces.get(pair);

            int index = 3;

            if (pair.charAt(1) == '-') {index = 2;}

            if (pair.substring(index).equals(add)) {
                break;
            }
        }

        /// Checking if the steps are appropriate for specific spaces

        if (place == 1) {  /// V (3.5 Gap between notes/chords)

            if (sum == 3.5) {
                return true;
            }

            return false;
        }
        
        else if (place == 2) { /// VI (4.5 Gap notes/chords)

            if (sum == 4.5) {
                return true;
            }

            return false;
        }
        
        else if (place == 3) { /// IV (2.5 Gap notes/chords)
            
            if (sum == 2.5) {
                return true;
            }

            return false;
        }
        
        return false;
    }


    /// This method prints the instructions on how the user should provide an input

    public static void helper(){
        System.out.println("You can provide a chord based on the following format:");
        System.out.println("Ex: C");
        System.out.println("Ex: G#");
        System.out.println("Ex: Db");
    }


    /// This method reads the data from the basic-scales.txt file and stores the data in a HashMap
    /// :param chord: it is the I in the progression

    public static void loadDataFromFile(String chord) {
         
        /// Reading the file and loading the appropriate data from the file
        /// Only the necessary scale

         try {
            File file = new File("basic-scales.txt");
            
            Scanner myReader = new Scanner(file);
            
            while (myReader.hasNextLine()) {
              String str = myReader.nextLine();
            
                /// Storing the data into the HashMap
                
                String[] data = str.split(": ");

                if (data[0].equals(chord)) {
                    
                    String[] scalesLinked = data[1].split("-");

                    LinkedList linked = new LinkedList<String>();

                    for (int i = 0; i < scalesLinked.length; i++) {
                        linked.add(scalesLinked[i]);
                    }

                    scales.put(data[0], linked);
                }
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }


    /// We load the spaces between notes into the HashMap spaces
    /// based on the scales HashMap

    public static void loadSpaces() {

        for(Entry<String, LinkedList> entry: scales.entrySet()) {
            
            boolean isWholeNote;

            int counter = 0;

            LinkedList val = entry.getValue();

            for (int i = 0; i < val.size() - 1; i++) {

                /// When is there a help gap (if the counter is 2 and 6)

                if (counter == 2 || counter == 6) {
                    isWholeNote = false;
                }

                else {
                    isWholeNote = true;
                }

                /// Putting the pair into the spaces hashmap

                String keySpace = val.get(i) + "-" + val.get(i+1); 

                double step = 0.5;

                if (isWholeNote) {
                    step = 1;
                }
                
                inOrder.add(keySpace);
            
                spaces.put(keySpace, step);

                counter++;
            } 
        }

    }


    /// This method properly prints the chord progression result

    public static void printProgression() {
        
        for (int i = 0; i < progression.length(); i++) {
            if (i != progression.length()-1) {

                if (progression.charAt(i) != '#' && progression.charAt(i) != 'b') {
                    
                    if (progression.charAt(i+1) != '#' && progression.charAt(i+1) != 'b') {
                        System.out.print(progression.charAt(i));
    
                        if (progression.length() != i + 1) {
                            System.out.print("-");
                        }
                    }
                    else {
                        System.out.print(progression.substring(i,i+2));
    
                        if (progression.length() != i + 2) {
                            System.out.print("-");
                        }
                    }
                }
            }
            else {

                if (progression.charAt(progression.length()-1) != '#' && progression.charAt(progression.length()-1) != 'b'){
                    System.out.print(progression.charAt(progression.length()-1));
                }
            }
        }
    }


    /// This method asks for user input
    /// Stores the data from a file into a hashmap (scales)
    /// Loads the spaces HashMap
    /// Calls the backtracking algorithm to find the chord progression
    /// Prints the chord progression based on the given user's chord

    public static void main(String[] args){
        
        /// Welcome Message

        System.out.println("Welcome to musicMaker!");
        System.out.println("This is a music program that generates a chord progression" + 
        "based on a major chord that you provide");
        helper();

        /// User Input

        Scanner myObj = new Scanner(System.in);  /// Create a Scanner object
        System.out.println("Please provide a chord: ");
        String chord = myObj.nextLine();  /// Reads user input
        
        if ((chord.length() != 1 && chord.length() != 2) || Character.isLowerCase(chord.charAt(0))) {
            System.out.println("Wrong input! Please follow the format below and" +
            "restart the program to try again!");
            helper();
        }

        else if (chord.length() == 2 && (chord.charAt(1) != 'b' && chord.charAt(1) != '#')) {
            System.out.println("Wrong input! Please follow the format below and" +
            "restart the program to try again!");
            helper();
        }
       
        else if (!(arr.contains(String.valueOf(chord.charAt(0))))) {
            System.out.println("Wrong input! Please follow the format below and" +
            "restart the program to try again!");
            helper();
        }

        else {
            
            /// Loading the data

            loadDataFromFile(chord);
            
            /// Loading the spaces
            loadSpaces();

            /// Processing the chord and creating the progression...
            
            System.out.println("");
            System.out.println("Loading...");
            System.out.println("");

            /// Since our Progression is I-V-VI-IV, the I is the chord the user provides as input

            progression += chord; 

            boolean value = chordProgression(chord, 1);
 
            if (!value) {
                System.out.println("Couldn't create a chord progression from " + chord);
            }

            else {
                printProgression();
                System.out.println("");
                System.out.println("");
            }
        }
    }
}