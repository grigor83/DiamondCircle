package gui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.DiamondCircle;
import main.Game;
import main.Player;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;

public class NorthPanel extends JPanel implements ActionListener {
	private JTextField playedGames, title;
	private JButton stopButton;
	private JLabel[] playersName;
	private ArrayList<Player> players;
	private GameFrame gameFrame;
	private Game game;
	private int numberOfGames;
	private boolean paused, ended;
	private GridBagConstraints cons;
	private static Object lock=new Object();
	
	public NorthPanel(GameFrame gameFrame) {
		super();
		this.gameFrame=gameFrame;
		Stream<Path> files;
		try {
			files = Files.list(DiamondCircle.dir.toPath());
			numberOfGames=(int) files.count();
		} catch (IOException e) {
			DiamondCircle.loger.log(Level.WARNING, e.fillInStackTrace().toString());
			JOptionPane.showMessageDialog(null, e);
		}
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createLineBorder(Color.black,3));
		
		JPanel titlePanel=new JPanel();
		titlePanel.setLayout(new GridBagLayout());
		cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.BOTH;
		
		playedGames=new JTextField("Trenutni broj odigranih igara: "+numberOfGames);
		playedGames.setHorizontalAlignment(JTextField.CENTER);
		playedGames.setEnabled(false);
		playedGames.setDisabledTextColor(Color.black);
		playedGames.setBorder(BorderFactory.createLineBorder(Color.black));
		cons.weightx=0.5; cons.weighty=1;
		cons.gridx=0; cons.gridy=0;
		titlePanel.add(playedGames,cons); 
		
		title=new JTextField("DIAMOND CIRCLE");
		title.setFont(new Font("SansSerif", Font.BOLD, 20));
		title.setHorizontalAlignment(JTextField.CENTER);
		title.setEnabled(false);
		title.setDisabledTextColor(Color.black);
		title.setBorder(BorderFactory.createLineBorder(Color.black));
		cons.weightx=1; cons.weighty=1;
		cons.gridx=1;
		cons.gridwidth=2;
		cons.ipady=20;
		titlePanel.add(title,cons);

		stopButton=new JButton("PAUSE/STOP");
		stopButton.setBackground(Color.PINK);
		stopButton.setFont(new Font("SansSerif", Font.BOLD, 15));
		stopButton.addActionListener(this);
		cons.weightx=0.5; cons.weighty=1;
		cons.gridwidth=1; cons.gridheight=2;
		cons.gridy=0;  cons.gridx=3;
		stopButton.setBorder(BorderFactory.createLineBorder(Color.black));
		titlePanel.add(stopButton,cons);
		
		cons.weightx=1; cons.weighty=1;
		cons.gridx=0; cons.gridy=0;
		add(titlePanel,cons);		
	}
	
	public JButton getStopButton() {
		return stopButton;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
		setPlayersLabel();
	}

	public void setEnded(boolean ended) {
		this.ended = ended;
	}
	
	public Object getLock() {
		return lock;
	}
	
	private void setPlayersLabel() {
		JPanel bottomPanel=new JPanel();
		bottomPanel.setLayout(new GridLayout(1,DiamondCircle.dimensionOfMatrix));
		players=game.getPlayers();
		playersName=new JLabel[players.size()];
		for (int i=0;i<players.size();i++) {
			playersName[i]=new JLabel(players.get(i).getName());
			playersName[i].setForeground(players.get(i).getColor());
			playersName[i].setHorizontalAlignment(JTextField.CENTER);
			playersName[i].setBackground(Color.gray);
			playersName[i].setOpaque(true);
			bottomPanel.add(playersName[i]);
		}
		
		cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.BOTH;
		cons.gridx=0; cons.gridy=2;
		add(bottomPanel,cons);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(ended) {
			DiamondCircle.startNewGame();
			gameFrame.dispose();
			return;
		}
		if(paused) {
			game.setWait(false);
			synchronized (lock) {
				lock.notifyAll();
			}
			paused=false;
		}
		else {
			game.setWait(true);
			paused=true;
		}
	}

}
