package server;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import quiz.*;

/**
 * 
 * @author punesh12
 * 
 * This is the central Quiz Server
 * 
 * 
 */
public class QuizServer extends UnicastRemoteObject implements QuizRemoteInterface  {
	
	private HashMap<Integer, Quiz> quizList = new HashMap<Integer, Quiz>(); //all the quizzes added by users
	private ArrayList<Integer> quizzesCurrentlyBeingPlayed = new ArrayList<Integer>(); //the id's of quizzes currently being played
	
	/**
	 * The QuizServer constructor. Reads from a file to get all the quizzes that have been added to the server by users. 
	 * @throws RemoteException
	 */
	public QuizServer() throws RemoteException{
		super();
		File storageFile = new File("QuizStorage.txt");
		if (storageFile.exists()){
			try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(storageFile))){
				quizList = (HashMap<Integer, Quiz>) in.readObject();
				System.out.println("Read the QuizList from File");
			} catch(IOException e){
				e.printStackTrace();
			} catch (ClassNotFoundException ex){
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Adds a quiz to the quizList and flushes the new quizList to a file
	 */
	public boolean addQuiz(Quiz quizToAdd, int id){
		quizList.put(id, quizToAdd);
		flush();
		System.out.println("Added quiz " + id + " to server");
		return true;
	}
	 /**
	  *  @param - the id of the requested quiz
	  *  @return - the requested Quiz
	  */
	public Quiz getQuiz(int id){
		return (Quiz) quizList.get(id);
	}
	/**
	 *  Checks whether a quiz is currently being played. 
	 *  
	 */
	public boolean deleteQuiz(int id){
		if (isQuizCurrentlyBeingPlayed(id)){
			return false;
		}
		else{
			quizList.remove(id);
			flush();
			System.out.println("Deleted quiz " + id + " from server");
			return true;
		}
	}
	
	private boolean isQuizCurrentlyBeingPlayed(Integer id){
		for (Integer quizBeingPlayedId : quizzesCurrentlyBeingPlayed){
			if (id.equals(quizBeingPlayedId)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Creates a unique ID to assign to a new Quiz created by the user. Iterates over the existing
	 * ID's and finds the smallest number which isn't already being used as an ID
	 */
	public synchronized int createQuizId(){
		Set<Integer> keySet = quizList.keySet();
		int iD = 1;
		boolean idFound = false;
		while (!idFound){
			if (keySet.contains(iD)){
				iD++;
			}
			else{
				idFound = true;
			}
		}
		System.out.println("id of new quiz: " + iD);
		return iD;
	}
	
	/**
	 * Creates a String array where each Quiz in the Quiz Server 
	 * is represented by a string consisting of its QuizName and QuizId
	 * It is used by the playerClient to display the available quizzes in a JList
	 */
	public String[] getEachQuizString(){
		int position = 0;
		String[] result = new String[quizList.size()];
			for (Quiz quiz: quizList.values()){
				result[position] = new String("[Quiz id]: " + quiz.getQuizId() + " [Quiz name]: " + quiz.getName());
				position++;
			}
		return result;
	}
	
	/**
	 * Adds the record of a player Attempt to a quiz
	 * @param - highScore - the data about a Player Attempt to be added
	 * @param id - the id for the quiz that was played
	 */
	public void addHighScore(PlayerAttempt highScore, int id){
		quizList.get(id).addPlayerAttempt(highScore);
		flush();
	}
	/**
	 * Adds the id of a quiz to list of quizzes currently being played
	 */
	public void addCurrentlyBeingPlayedQuiz(Integer id){
		this.quizzesCurrentlyBeingPlayed.add((Integer) id);
		System.out.println("Added quiz + " + id + " to currently being played quizlist");
	}
	/**
	 * removes an id for a quiz from the list of quizzes currently being played once a user has finished playing
	 */
	public void removeCurrentlyBeingPlayedQuiz(Integer id){
		this.quizzesCurrentlyBeingPlayed.remove((Integer)id);
		System.out.println("Removed quiz + " + id + " from currently being played quizlist");
	}
	/**
	 * Writes the QuizList to a file
	 */
	private void flush(){
		File storageFile = new File("QuizStorage.txt");
		if (storageFile.exists()){ //if the file already exists, overwrite it with updated data
			storageFile.delete();
		}
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(storageFile))){
			out.writeObject(quizList);
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}
