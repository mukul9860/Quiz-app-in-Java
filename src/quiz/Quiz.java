package quiz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Quiz implements Serializable {
	private String quizName;
	private int quizId;
	private ArrayList<Question> quizQuestions = new ArrayList<Question>(); //the questions for this Quiz
	private ArrayList<PlayerAttempt> highScores = new ArrayList<PlayerAttempt>(); //records of playerAttempts for this quiz
	
	public Quiz(String name){
		this.quizName = name;
	}
	
	/**
	 *  sets the id for the quiz
	 */
	public void setId(int id){
		this.quizId = id;
	}
	
	/**
	 * adds newQuestion to the quiz
	 * @param newQuestion
	 */
	public void addQuestion(Question newQuestion){
		this.quizQuestions.add(newQuestion);
	}
	/*
	 * gets all the questions for a Quiz
	 */
	public ArrayList<Question> getQuestions(){
		return this.quizQuestions;
	}	
	/**
	 * returns the number of Questions in a quiz
	 */
	public int getNumberOfQuestions(){
		return this.quizQuestions.size();
	}
	/**
	 * returns the id of a quiz
	 * @return - the id for a quiz
	 */
	public int getQuizId(){
		return this.quizId;
	}
	/**
	 * Adds a playerAttempt to a quiz
	 * @param playerAttempt - the playerAttempt to add
	 */
	public void addPlayerAttempt(PlayerAttempt playerAttempt){
		this.highScores.add(playerAttempt);
		Collections.sort(highScores); //sorts the highscores from highest to lowest
	}
	/**
	 * returns the name of a quiz
	 */
	public String getName(){
		return this.quizName;
	}
	/**
	 * returns all the playerAttempts for a quiz
	 */
	public ArrayList<PlayerAttempt>getPlayerAttempts(){
		return highScores;
	}
}
