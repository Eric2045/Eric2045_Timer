package application;

import java.net.URL;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Deze class betreft de controller van de applicatie, deze class verbonden met de Scene Builder tool via de 
 * controller menu in de Scene Builder.
 * De objecten die gemaakt worden in de Scene Builder tool (AnchorPane, GridPane, Button...et cetera) 
 * worden van de Scene Builder gekopieerd via (menu -> view -> Show Sample Controller Skeleton) en hier geplakt voor bewerking.
 *   
 * @author Eric Cordero Castillo
 */

public class timerController extends Thread implements Initializable{
	 @FXML
	 private AnchorPane timerPane;

	 @FXML 
	 private Text whiteHoursTimer;

	 @FXML 
	 private Text whiteMinutesTimer;

	 @FXML 
	 private Text whiteSecondsTimer;

	 @FXML
	 private Text blackHoursTimer;

	 @FXML
	 private Text blackMinutesTimer;

	 @FXML
	 private Text blackSecondsTimer;

	 @FXML
	private AnchorPane menuPane;

	 @FXML
	 private ComboBox<Integer> hoursInput;

	 @FXML
	 private ComboBox<Integer> minutesInput;

	 @FXML
	 private ComboBox<Integer> secondsInput;

	 @FXML
	 private Button setButton;
	 
	 @FXML
	 private GridPane wClockPane;
	 
	 @FXML
	 private GridPane bClockPane;	 
	
	 @FXML
	 private GridPane blackControl;
	  
	 /**
	  * Instance variabelen, Map numberMap word gebruikt om de keys en de waardes de ordenen, een LinkedList en niet 
	  * een ArrayList word gebruikt dit omdat LinkedList geeft direct toegang tot toevoegen en verwijdenren van data 
	  * en dat is in dit geval beter voor de manipulatie van de tijd, de uren, minuten en secondes worden telkens opgeslagen 
	  * bij iedere iteratie van de while loop in de inner classes.  
	  * Een pauseWhite pauseBlack flag om de threads te pauzeren en twee nieuwe objecten van de inner classes. 
	  */	
	 private Map<Integer, String> numberMap;
	 private LinkedList<Integer> currHmsWhite;
	 private LinkedList<Integer> currHmsBlack;
	 private Integer currSecondsWhite;
	 private Integer currSecondsBlack;	 	 
	 private boolean pauseBlack = false;
	 private boolean pauseWhite = true;
	 WhiteThread wt = new WhiteThread();
	 BlackThread bt = new BlackThread();
	 
	 
	 /**
	  * De setTimer methode zet de tijd van de applicatie, hierbij word gebruik gemaakt van de methode hmsToSeconds,
	  * hmsToseconds berekent de totale secondes voor de tijd die word gezet aan het beging van de applicatie, die waarde 
	  * word dan in een Integer variabele currSecondsWhite en currSecondsBlack opgeslagen, die weer als input gelden voor 
	  * de twee verschillende timers via de setOutput methode die word opgeroepen met twee nieuwe objecten wt. en bt. voor de 
	  * verschillende inner classes. De White timer begint altijd in een schaak spel dus aan het beging 
	  * moet de focus van de KeyEvent aan de White gegeven worden met WClocking.setDisable(false).setFocusTraversable(true).
	  * requestFocus(), er word dan weer een nieuwe object van de WhiteThread gemaakt die word dan gestart met nwt.start() en de 
	  * run() methode van de outer class (timerController) word aangeroepen.
	  * Als de timer wordt getoond en begint te lopen worden de hoursInput, minutesInput en 
	  * secondsInput van het menu scherm weer op nul gezet met setValue(0).   
	  * 
	  * @param event
	  */	 
	 @FXML
	 void setTimer(ActionEvent event) {
		 currSecondsWhite = hmsToSeconds(hoursInput.getValue(), minutesInput.getValue(), secondsInput.getValue());
		 currSecondsBlack = hmsToSeconds(hoursInput.getValue(), minutesInput.getValue(), secondsInput.getValue());
		 scrollUp();
		 wt.setOutputW(currSecondsWhite);
		 bt.setOutputB(currSecondsBlack);
		 hoursInput.setValue(0);
		 minutesInput.setValue(0);
		 secondsInput.setValue(0);		 						 		 			  
		 wClockPane.setDisable(false);
		 wClockPane.setFocusTraversable(true);
		 wClockPane.requestFocus();
		 Thread nwt = new WhiteThread();
		 nwt.start();		 
		 run();			
	 }
	 
	 
	 /**
	  * In de whiteThread inner class wordt eerst de flag boolean pauseWhite op true gezet, zodat de while loop van white kan lopen 
	  * het kan zijn dat deze op false staat, dit zou het geval zijn als de tijd voor Black afgelopen is,
	  * of als Black op de cancel knop heeft gedrukt (als Black loopt is pauseWhite op false en andersom).
	  * (wit begint altijd)
	  * setOutput wordt aangeroepen die methode secondsToHms aanroept die berekent de uren, minuten en secondes en zet die in een LinkedList,
	  * via setText(numberMap.get(currHmsWhite.get(0) wordt bijvoorbeeld het uur opgehaald en getoond op de applicatie,
	  * er wordt één seconde 'gewacht' met WhiteThread.sleep(1000) en de secondes voor White worden telkens
	  * met 1 verlaagd, als het op 0 komt dan is de tijd voorbij en het hooftmenu wordt weer getoond via scrollDown(),
	  * met setDisable(true) en setFocusTraversable(false) wordt de KeyEvent voor deze speler uitgeschakeld zodat deze niet meer 
	  * op SPACE kan drukken, want dan zou de overgebleven tijd van de andere speler op de achtergrond weer gaan lopen.
	  * en de Thread wordt gestopt met this.stop().
	  * Dezelfde implementatie is ook voor de BlackThread inner class, zonder de flag statement.
	  * 
	  * @author Eric
	  */	 
	 private class WhiteThread extends Thread {		
		 @SuppressWarnings("deprecation")
		 public void run() { 			
			pauseWhite = true;		  			
			 try {
				 while (pauseWhite) { 
					 setOutputW(currSecondsWhite);				
					 WhiteThread.sleep(1000);				 
					 if (currSecondsWhite == 0) {
						 System.out.println("White's time is finished");						   
						 scrollDown();						
						 wClockPane.setDisable(true);
						 wClockPane.setFocusTraversable(false);
						 this.stop();
					 }
					 currSecondsWhite -= 1;
				 }								
			 }
			 catch (InterruptedException e) {					
					e.printStackTrace();
			}
		 }
		 
		 void setOutputW(Integer sec) {			 
			 currHmsWhite = secondsToHms(sec);
			 whiteHoursTimer.setText(numberMap.get(currHmsWhite.get(0)));
			 whiteMinutesTimer.setText(numberMap.get(currHmsWhite.get(1))); 
			 whiteSecondsTimer.setText(numberMap.get(currHmsWhite.get(2)));
		 }
	 } 
	 
	 private class BlackThread extends Thread {			 
		 @SuppressWarnings("deprecation")
		 public void run() {			 
			 //System.out.println(bt.getState()+ " " + "Black Thread");
			 try {				 
			 	 while (pauseBlack) {			 		 
			 		 setOutputB(currSecondsBlack);			 
			 		 BlackThread.sleep(1000);
			 		 if (currSecondsBlack == 0) {			 			 			 			 
			 			 pauseBlack = true;
			 			 System.out.println("Black's time is finished");
			 			 scrollDown();
			 			 blackControl.setDisable(true);
	    		 		 blackControl.setFocusTraversable(false);
			 			 this.stop();		 						 				
			 		 }
			 		 currSecondsBlack -= 1;
			 	 } 
			 }
			 catch (InterruptedException e) {				 
				 e.printStackTrace();
			 }				 				
		 }
		 		 
		 private void setOutputB(Integer sec) {
			 currHmsBlack = secondsToHms(sec);
			 blackHoursTimer.setText(numberMap.get(currHmsBlack.get(0)));
			 blackMinutesTimer.setText(numberMap.get(currHmsBlack.get(1)));
			 blackSecondsTimer.setText(numberMap.get(currHmsBlack.get(2)));
		 }
	 }	
	 
	 
	 /**
	  * In de run() methode van de timerController class worden de KeyEvents behandeld, er worden twee objecten gemaakt van de inner classes 
	  * om ze vanaf hier aan te roepen als er een KeyEvent gebeurt. Controle wordt aan eerst aan white gegeven (wit begint),
	  * met de Focus en setOnKeyPressed EventHandler<KeyEvent>, binnen de handle(event) methode wordt een switch statement gebruikt om te controleren 
	  * welke Event getriggert wordt, met event.getCode() wordt gekeken of het gelijk is aan de case SPACE of C, als SPACE dan de flag methode pauseW()
	  * wordt aangeroepen om zo de boolean flag pauseWhite op false te zetten zodat de while loop van de white thread 'pauzeer', continueTB() zorgt dat 
	  * de boolean flag pauseBlack dan op true wordt gezet zodat de while loop van Black kan beginnen of verder gaan met de tijd dat is opgeslagen 
	  * in de LinkedList currHmsBlack. via wClockPane.setDisable(true) en setFocusTraversable(false) wordt de Focus (controle) van White weggehaald
	  * en weer aan Black gegeven, dit omdat slechts één component tegelijk in het venstersysteem de keyboardfocus kan hebben. 
	  * de BlackThread wordt dan gestart met nBt.start(). Als op C gedrukt wordt gaat eerst de flag pauseWhite met pauseW() op false, dit is nodig 
	  * anders blijft de while loop lopen tot dat de timer op 0 is, de Node wordt ook disable en setFocusTraversable gaat op false, 
	  * dit om te voorkomen dat wanneer de C key is gedrukt en we zitten in het hoofdmenu, dat er niet meer op de SPACE toets gedrukt kan worden 
	  * anders zou de while loop van de ander speler weer beginnen te lopen op de achtergrond.
	  * met scrollDown() word weer het hooftmenu getoond.
	  * 
	  * Dezelfde handeling geld ook voor de blackControl.setOnKeyPressed methode.
	  */	 
	 @Override
	 public void run() {		 
		 wClockPane.setFocusTraversable(true); 
		 wClockPane.requestFocus();
		 wClockPane.setOnKeyPressed(new EventHandler<KeyEvent>() {			 
			@SuppressWarnings("incomplete-switch")
			@Override
			 public void handle(KeyEvent event) {			 
				 switch (event.getCode()) {				 
					case SPACE:
						pauseW();
						continueTB();
						wClockPane.setDisable(true);
						wClockPane.setFocusTraversable(false);
						blackControl.setDisable(false);						
						blackControl.setFocusTraversable(true);
						blackControl.requestFocus();		
						Thread nBt = new BlackThread();
						nBt.start();
						break;
					case C:
						pauseW();
						wClockPane.setDisable(true);
						wClockPane.setFocusTraversable(false);
						scrollDown();
						System.out.println("White has cancelled the timer");
				 }
			}								 
		 });
		 
	     blackControl.setOnKeyPressed(new EventHandler<KeyEvent>() {
	    	@SuppressWarnings("incomplete-switch")
			@Override
		     public void handle(KeyEvent event) {			 
	    		 switch (event.getCode()) {			 
	    		 	case SPACE:
	    		 		pauseB();
	    		 		continueTW();
	    		 		blackControl.setDisable(true);
	    		 		blackControl.setFocusTraversable(false);
	    		 		wClockPane.setDisable(false);
	    		 		wClockPane.setFocusTraversable(true);
	    		 		wClockPane.requestFocus();
	    		 		Thread nWt = new WhiteThread();
	    		 		nWt.start();
	    		 		break;
	    		 	case C:
	    		 		pauseB();
	    		 		blackControl.setDisable(true);
	    		 		blackControl.setFocusTraversable(false);	    		 		    		 		
	    		 		scrollDown();
	    		 		System.out.println("Black has cancelled the timer");
	    		 }
	    	 }		 
	     });		 		 		 	 		
	 }				 		 	 
	 
	 
	 /**
	  * pauseW(), pauseB(), continueTB() en continueTW() zijn methodes die worden aangeroepen om de booleans pauseWhite/pauseBlack op true/false te zetten. 
	  */
	 public void pauseW() {		 	 
	     pauseWhite = false;
	 }
	 
	 public void pauseB() {			 		 
		 pauseBlack = false;
	 }
	 
	 public void continueTB() { 		 
	     pauseBlack = true;	    
	 }
	 
	 public void continueTW() {    
	     pauseWhite = true;		    
	 }
	 
	 
	 /**
	  * Deze methode berekent en retourneer de totale secondes van de tijd dat aan het beging van de applicatie in het hoofdmenu wordt gezet.
	  * @param hours
	  * @param minutes
	  * @param seconds
	  * @return Integer
	  */	 
	 Integer hmsToSeconds(Integer hours, Integer minutes, Integer seconds) {		 
		 Integer hoursToSeconds = hours * 3600;
		 Integer minutesToSeconds = minutes * 60;
		 Integer total = hoursToSeconds + minutesToSeconds + seconds;
		 return total;
	 }
	 
	 
	 /**
	  * Deze methode berekent en retourneert in een LinkedList telkens weer de totale tijd die nog over is terwijl de while loop in één van de inner classes loopt
	  * en currSecondsWhite of currSecondsBlack wordt telkens met één seconde verlaagd bij elke iteratie.
	  * Deze methode wordt aangeroepen via de setOutput() methode met de huidige secondes,
	  * hours wordt berekend van de huidige currSecond en sinds we met Integers werken wordt truncation toegepast.
	  * 
	  * Bijvoorbeeld:	
	  *   
	  * 3uur = 10800sec / ((60sec * 60) = 3600sec)  
	  * 2.999722222 = 2uur 59min 59sec = 10799sec / 3600 = 2 (truncation)
	  * 
	  * dan worden de minuten berekent met modulo (klokrekenen) 
	  * 10799sec / 3600 = 2.999722222 = 2 (truncation)  
	  * 10799sec % 3600 = 10799sec - (3600 * 2) = 3599
	  * 3599sec / 60 = 59.98333333 = 59 (truncation)
	  *  	   	 
	  * en secondes = currSecond
	  * hour, minutes, en secondes worden dan in een LinkedList gezet, daarna worden ze opgehaald via een Map numberMap om vervolgens als tekst 
	  * toonbaar te worden in de applicatie. 
	  * @param currSecond
	  * @return Integer LinkedList
	  */	
	 LinkedList<Integer> secondsToHms(Integer currSecond) {
		 Integer hours = currSecond / 3600;
		 currSecond = currSecond % 3600;
		 Integer minutes = currSecond / 60;
		 currSecond = currSecond % 60; 
		 Integer seconds = currSecond;
		 LinkedList<Integer> answerList = new LinkedList<>(); 
		 answerList.add(hours);
		 answerList.add(minutes);
		 answerList.add(seconds);
		 return answerList;
	 }
	 
	 
	 /**
	  * Met scrollUp() wordt een object gemaakt van TranslateTransition, de Duration op 0,1 secondes gezet (dit is de duur van de applicatie voor een beweging).
	  * De begin waarde van de Y-as wordt hetzelfde gezet als de lengte van de GUI tt.setToY(-350) en met de setNode wordt de target gezet (het hoofdmenu).
	  * De timer display wordt op een positieve waarde (350) met setFromY() en setToY() op 0 gezet. Met ParallelTransition pt.play() wordt de animatie van het 
	  * hoofdmenu getoond, als op de setTimer knop wordt gedrukt dan scroll de applicatie vervolgens naar 'boven' (de timer display wordt getoond).  
	  * Via de X-as wordt niet bewogen dus deze hebben de waardes 0.
	  * 
	  * De scrollDown() methode doet hetzelfde van boven naar beneden. 
	  */
	 public void scrollUp() {
		 TranslateTransition tt = new TranslateTransition();
		 tt.setDuration(Duration.millis(100));
		 tt.setToX(0);
		 tt.setToY(-350);
		 tt.setNode(menuPane);
		 TranslateTransition tt2 = new TranslateTransition();
		 tt2.setDuration(Duration.millis(100));
		 tt2.setFromX(0);
		 tt2.setFromY(350);
		 tt2.setToX(0);
		 tt2.setToY(0);
		 tt2.setNode(timerPane);
		 ParallelTransition pt = new ParallelTransition(tt, tt2);
		 pt.play();
	 }
	 
	 public void scrollDown() {
		 TranslateTransition tt = new TranslateTransition();
		 tt.setDuration(Duration.millis(100));
		 tt.setToX(0);
		 tt.setToY(350);
		 tt.setNode(timerPane);
		 TranslateTransition tt2 = new TranslateTransition();
		 tt2.setDuration(Duration.millis(100));
		 tt2.setFromX(0);
		 tt2.setFromY(-350);
		 tt2.setToX(0);
		 tt2.setToY(0);
		 tt2.setNode(menuPane);
		 ParallelTransition pt = new ParallelTransition(tt, tt2);
		 pt.play();		 
	 }
	 
	 
	 /**
	  * Deze methode initialiseer de controller. Twee ObservableList worden gebruikt voor de hoursList en minutesAndSecondsList,
	  * via FXCollections wordt een observableArrayList gemaakt.
	  * In de eerste for loop voor hoursList en minutesAndSecondsList worden de waardes toegevoegd, deze waardes is wat gekozen kan worden
	  * om de tijd in te stellen in de comboBox van het hoofdmenu. Met setItems() worden deze items toegevoegt, de spelers kunnen dan een tijd kiezen 
	  * tot 24 uur, 60 minuten en 60 secondes. Met setValue(0) wordt standaard een 0 getoond op de comboBoxen in het hoofdmenu wanneer er nog 
	  * geen tijd is gekozen.
	  * In de tweede for loop wordt een TreeMap gemaakt, de Integers waardes worden tot string gemaakt en er wordt een "0" toegevoegd (concatenation)
	  * aan waardes van 0 tot en met 9. Wanneer we dit niet doen zien we maar één getal telkens als de timer loopt tot dat het de 9 passeert, namelijk ( 9) in plaats van (09).  
	  * TreeMap wordt gebruikt om de keys en de waardes te ordenen.
	  */
	 @Override
	 public void initialize(URL location, ResourceBundle resources) {		
		 ObservableList<Integer> hoursList = FXCollections.observableArrayList();
		 ObservableList<Integer> minutesAndSecondsList = FXCollections.observableArrayList();
		
		 for (int i = 0; i <= 60; i++) {
			 if (0 <= i && i <= 24) {
				 hoursList.add(new Integer(i));
			 }			 
			 minutesAndSecondsList.add(new Integer(i));
		 }
		 hoursInput.setItems(hoursList);
		 hoursInput.setValue(0);
		
		 minutesInput.setItems(minutesAndSecondsList);
		 minutesInput.setValue(0);
		
		 secondsInput.setItems(minutesAndSecondsList);
		 secondsInput.setValue(0);
		
		 numberMap = new TreeMap<Integer, String>();
		 for (Integer i = 0; i <= 60; i++) {
			 if (0 <= i && i <= 9) {				 
				 numberMap.put(i, "0" + i.toString());
			 } 
			 else {
				numberMap.put(i, i.toString());
			}
		}
	}					
}	

