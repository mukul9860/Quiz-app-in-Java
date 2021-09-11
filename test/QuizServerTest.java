
import static org.junit.Assert.*;
import java.rmi.RemoteException;
import org.junit.Before;
import org.junit.Test;
import server.QuizServer;
import quiz.*;

public class QuizServerTest {
	private Quiz testerQuiz1;
	private Quiz testerQuiz2;
	private QuizServer serverTester;

	@Before
	public void buildUp(){
		try {
			serverTester = new QuizServer();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		testerQuiz1 = new Quiz("TestQuiz1");
		testerQuiz2 = new Quiz("TestQuiz2");
		Question question1 = new Question("what is the capital of India?", new String[] {"Mumbai", "Delhi","Chennai", "Bangalore"} ,1);
		Question question2 = new Question("Who is the founder of Microsoft?", new String[] {"Elon Musk", "Mark Zuckerburg","Larry paige", "Bill Gates"} ,3);
		Question question3 = new Question("What is Android owned by Google?", new String[] {"Operating System", "Browser","Payment system", "Website"} ,0);
		Question question4 = new Question("What is speed of light in air?", new String[] {"3*10^8 m/s", "5*10^8 m/s","7*10^8 m/s", "8*10^8 m/s"} ,0);
		testerQuiz1.addQuestion(question1);
		testerQuiz1.addQuestion(question2);
		testerQuiz2.addQuestion(question3);
		testerQuiz2.addQuestion(question4);
	}
	
	
	@Test  //tests addQuiz, getQuiz and DeleteQuiz
	public void testAddGetDeleteQuiz() {
		int x = serverTester.createQuizId(); //test createQuizId
		serverTester.addQuiz(testerQuiz1, x);
		assertEquals(testerQuiz1, serverTester.getQuiz(x));
		assertEquals(serverTester.deleteQuiz(x), true);
	}
	
	@Test //tests that a quiz cannot be deleted when in the currentlyBeingPlayedList and can be deleted when not being played
	public void testDeleteCurrentlyBeingPlayedQuizBySingleUser(){
		//a quiz cannot be deleted when it is currently being played
		serverTester.addQuiz(testerQuiz1, 100);
		serverTester.addCurrentlyBeingPlayedQuiz(100);
		assertEquals(false, serverTester.deleteQuiz(100));
		//a quiz can be deleted when it is not being played currently
		serverTester.removeCurrentlyBeingPlayedQuiz(100);
		assertEquals(true, serverTester.deleteQuiz(100));
		
	}
        
        
        @Test
        public void testDeleteCurrentlyBeingPlayedQuizByMultipleUser(){
            //if the same quiz is being played by 2 players and 1 player finishes, the quiz should not be able to be deleted as one player is still playing it
		serverTester.addQuiz(testerQuiz1, 100);
                
                //2 players playing the quiz
		serverTester.addCurrentlyBeingPlayedQuiz(100); //player 1
		serverTester.addCurrentlyBeingPlayedQuiz(100);// player 2
                
		serverTester.removeCurrentlyBeingPlayedQuiz(100); //1 player finishes playing
		assertEquals(false, serverTester.deleteQuiz(100)); //quiz cannot be deleted
		serverTester.removeCurrentlyBeingPlayedQuiz(100);
		
//the quiz can now be deleted as both players have finished playing
		assertEquals(true, serverTester.deleteQuiz(100));
            
        }
	
        
	//tests whether addHighScore successfully adds a PlayerAttempt to a quiz
	@Test
	public void testAddHighScore(){
		serverTester.addQuiz(testerQuiz2, 100);
		PlayerAttempt attempt = new PlayerAttempt("Mukul");
		serverTester.addHighScore(attempt, 100);
		assertEquals(attempt, serverTester.getQuiz(100).getPlayerAttempts().get(0));
		assertEquals(true, serverTester.deleteQuiz(100)); //delete the quiz at the end to tidy up 
	}
}
