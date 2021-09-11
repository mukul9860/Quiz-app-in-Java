
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.rmi.RemoteException;
import org.junit.*;
import client.PlayerClient;
import client.SetUpClient;
import quiz.*;
import server.*;

public class SetUpClientPlayerClientTest {
	ServerLauncher launcher;
	SetUpClient setUpClient;
	PlayerClient playerClient;
	
	@Before
	public void buildUp(){
		launcher = new ServerLauncher();
		launcher.launch(); //launch the server on the registry
		setUpClient = new SetUpClient();
		setUpClient.connectToServer(); 
		playerClient = new PlayerClient();
		playerClient.connectToServer();
	}
	
	@Test
	public void testCurrentlyBeingPlayedQuizList(){
		setUpClient.createQuiz("test quiz");
		setUpClient.addQuestionToQuiz("Simplify (3+5-2)", new String[]{"6","8","2","1"},6);
		
		 try { //using reflection to get access to private field in playerClient - the remote server object
			Field field = PlayerClient.class.getDeclaredField("remoteServerObject");
			field.setAccessible(true);
			QuizRemoteInterface serverObject = (QuizRemoteInterface) field.get(playerClient);
			// use the remoteServerObject to see what the id for the next Quiz will be
			int idOfNewQuiz = serverObject.createQuizId(); 
			setUpClient.addQuizToServer(); //add the quiz to the server. The id will be idOfNewQuiz
			//add the quiz to currentlyBeingPlayedList
			serverObject.addCurrentlyBeingPlayedQuiz(idOfNewQuiz);
			//as the quiz is currently being played, it should not be possible to delete it
			assertEquals(false, playerClient.deleteQuiz(idOfNewQuiz));
			//remove the quiz from the currentlyBeingPlayed list
			serverObject.removeCurrentlyBeingPlayedQuiz(idOfNewQuiz);
			//now it should be possible to delete the quiz
			assertEquals(true, playerClient.deleteQuiz(idOfNewQuiz));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
