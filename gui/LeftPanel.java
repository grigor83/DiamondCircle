package gui;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;

import figures.Figure;
import main.DiamondCircle;

public class LeftPanel extends JPanel implements MouseListener {
	private JScrollPane sp; 
	private JTextArea title;
	private ArrayList<Figure> figures;
	private GameFrame gameBoard;
	private int i;
	
	public LeftPanel(GameFrame gameBoard) {
		super();
		this.gameBoard=gameBoard;
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createLineBorder(Color.black,3));
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.BOTH;
		cons.weightx=1; cons.weighty=1;
		
		figures=new ArrayList<Figure>();
		title=new JTextArea("\n Dvostrukim klikom na figuru \n prikazuje se novi prozor "
				+ "\n u kojem se iscrtava \n njena putanja ");
		title.addMouseListener(this);
		title.setBackground(Color.lightGray);
		sp= new JScrollPane(title);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(sp,cons);
	}
	
	public void addFigure(Figure f) {
		figures.add(f);
		title.append("\n      Figura_"+i++);
	}

	@Override
	public void mouseClicked(java.awt.event.MouseEvent e) {
		if (e.getClickCount() == 2 && !e.isConsumed()) {
            e.consume(); 
            int offset= title.getSelectionStart();
			int rowStart;
            int rowEnd;
			try {
				rowStart = Utilities.getRowStart(title, offset);
				rowEnd = Utilities.getRowEnd(title, offset);
				title.setSelectionStart(rowStart); title.setSelectionEnd(rowEnd);
                String line=title.getText(rowStart, rowEnd-rowStart);
                Figure f=figures.get(Integer.parseInt(line.split("_")[1]));
//////////////////////////////////////////////////////////////////////////////////////////////////////   
                new Thread(new Runnable() {
					@Override
					public void run() {
						synchronized (f.getPredjenaPolja()) {
							FigureFrame frame=new FigureFrame(gameBoard,f);
							while(true) {
								try {
									f.getPredjenaPolja().wait();
									frame.drawPath();
									frame.repaint();
								} catch (InterruptedException e) {
									DiamondCircle.loger.log(Level.WARNING, e.fillInStackTrace().toString());
									JOptionPane.showMessageDialog(null, e);
								}
							}
						} 
					}
				}).start();
			} catch (BadLocationException e2) {
				e2.printStackTrace();
			}                   
		}	
	}

	@Override
	public void mousePressed(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
