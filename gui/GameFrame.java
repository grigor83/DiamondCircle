package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import exceptions.DimensionException;
import exceptions.NumberOfHolesException;
import exceptions.NumberOfPlayersException;
import main.DiamondCircle;
import main.Game;
import main.Player;

public class GameFrame extends JFrame {
	public static List<Color> COLORS;
	{
		COLORS = new LinkedList<Color> ();	
		COLORS.add(Color.RED); COLORS.add(Color.GREEN); 
		COLORS.add(Color.BLUE); COLORS.add(Color.YELLOW);
	}
	
	private static final int MIN = 7, MAX = 10, MIN_PLAYERS = 2, MAX_PLAYERS = 4;
	private MatrixPanel matrixPanel;
	private RightPanel rightPanel;
	private NorthPanel northPanel;
	private LeftPanel leftPanel;
	private ArrayList<Player> players;
	private ArrayList<Card> cards;
	
	public GameFrame(String title) throws DimensionException, NumberFormatException, NumberOfHolesException {
		super(title);
		
		leftPanel=new LeftPanel(this);
		add(leftPanel,BorderLayout.WEST);
		loadDimensions();	
		loadPlayers();
		createCards();
		matrixPanel=new MatrixPanel();
		DiamondCircle.numberOfHoles = Integer.parseInt(JOptionPane.showInputDialog("Unesite broj rupa:"));
		if (DiamondCircle.numberOfHoles<0 || DiamondCircle.numberOfHoles>MatrixPanel.FINISH_FIELD)
			throw new NumberOfHolesException();
		add(matrixPanel, BorderLayout.CENTER);
		rightPanel=new RightPanel();
		add(rightPanel, BorderLayout.EAST);
		northPanel=new NorthPanel(this);
		add(northPanel, BorderLayout.NORTH);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void loadDimensions() throws DimensionException, NumberFormatException {
		DiamondCircle.dimensionOfMatrix = Integer.parseInt(JOptionPane.showInputDialog("Unesite dimenziju matrice:"));
		if (DiamondCircle.dimensionOfMatrix<MIN || DiamondCircle.dimensionOfMatrix>MAX)
			throw new DimensionException();
	}

	private void loadPlayers() {
		players = new ArrayList<>();
		int numberOfPlayers;
		
		while (true) {
			numberOfPlayers = Integer.parseInt(JOptionPane.showInputDialog("Unesite broj igrača:"));
			try {
				if (numberOfPlayers<MIN_PLAYERS || numberOfPlayers>MAX_PLAYERS)
					throw new NumberOfPlayersException();
				for (int i=0; i<numberOfPlayers; i++)
					players.add(new Player(leftPanel));
				break;
			}
			catch (NumberOfPlayersException e) {
				DiamondCircle.loger.log(Level.WARNING, e.fillInStackTrace().toString());
				JOptionPane.showMessageDialog(null, e);
			}
			catch (NumberFormatException ex) {
				DiamondCircle.loger.log(Level.WARNING, ex.fillInStackTrace().toString());
				System.exit(1);
			} 
		}
		//Redoslijed igrača se određuje na slučajan način i oni igraju jedan za drugim.
		//Za čuvanje redoslijeda igrača koristićemo već kreiran niz igrača players[]
		setOrderOfPlay();
	}
	
	private void createCards() {
		cards=new ArrayList<Card>();
		for (int i=1;i<5;i++)
			for (int j=0;j<10;j++)
				cards.add(new Card(i));
		for (int i=0;i<12;i++)
			cards.add(new Card(0));
		mixCards();
	}
	
	private void mixCards() {
		HashSet<Card> temp= new HashSet<>(cards);
		cards=new ArrayList<>(temp);
	}

	private void setOrderOfPlay() {
		HashSet<Player> temp= new HashSet<>(players);
		players=new ArrayList<>(temp);
	}
	
	public void startGame() {		
		Game game = new Game(matrixPanel, rightPanel, northPanel, players,cards);
		northPanel.setGame(game);
	}

	public MatrixPanel getPanelButtons() {
		return matrixPanel;
	}

}
