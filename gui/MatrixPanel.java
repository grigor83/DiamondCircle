package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import main.DiamondCircle;

public class MatrixPanel extends JPanel {
	//START_FIELD, FINISH_FIELD sadrže pozicije polja(indekse dugmadi u listi) na stazi
	public static int START_FIELD=0, FINISH_FIELD;
	private MyButton[] buttons;
	private ArrayList<MyButton> fields;
	private JTextPane descriptionField;
	private GridBagConstraints cons;
	
	public MatrixPanel() {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createLineBorder(Color.black,3));
		cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.BOTH;
		addButtons();
		createPath();
		addDescriptionField();
	}

	public void addButtons() {
		buttons = new MyButton [DiamondCircle.dimensionOfMatrix*DiamondCircle.dimensionOfMatrix];
		JPanel centralPanel=new JPanel();
		centralPanel.setLayout(new GridLayout(0,DiamondCircle.dimensionOfMatrix));	
		centralPanel.setPreferredSize(new Dimension(520,520));
		centralPanel.setMaximumSize(new Dimension(520,520));
		centralPanel.setMinimumSize(new Dimension(520,520));
		MyButton button;
		for (int i=0, k=1; i<DiamondCircle.dimensionOfMatrix; i++)
			for (int j=0; j<DiamondCircle.dimensionOfMatrix; j++) {
				button = new MyButton(""+k);
				button.setBackground(Color.pink);
				centralPanel.add(button);
				buttons[k++ -1] = button;
			}
		cons.gridx=0; cons.gridy=0;
		cons.weightx=1; cons.weighty=1;
		add(centralPanel,cons);
	}
	
	private void createPath() {
		fields= new ArrayList<>();
		int middle = (int) Math.round(DiamondCircle.dimensionOfMatrix/2.0);
		int anker = middle;
		
		for (int j=2; j<DiamondCircle.dimensionOfMatrix; j=j+2) {
			int step = DiamondCircle.dimensionOfMatrix +1;
			for (int i=0; i<2*middle-j; i++) {
				if (i==(2*middle-j)/2)
					step = step-2;
				anker = anker + step;
				fields.add(getButton(anker)); 
			}
			
			step = step+2;
			for (int i=0; i<2*middle-j; i++) {
				if (i==(2*middle-j)/2)
					step = step-2;
				anker = anker - step;
				if (j==2 && i==2*middle-j-1) {
					fields.add(0,getButton(anker));
				}
				else if (i!=2*middle-j-1) {
					fields.add(getButton(anker));
				}
			}			
			anker=anker+DiamondCircle.dimensionOfMatrix;
			fields.add(getButton(anker));
		}
		
		for (MyButton myButton : fields) {
			//System.out.print(myButton.getText()+" ");
			myButton.setIcon(DiamondCircle.DEFAULT_IMAGE);
			myButton.setHorizontalTextPosition(MyButton.CENTER);
			myButton.setVerticalTextPosition(MyButton.CENTER);
		}
		//System.out.println();
		FINISH_FIELD=fields.size()-1;
	}
	
	private void addDescriptionField() {
		descriptionField=new JTextPane();
		descriptionField.setBorder(BorderFactory.createLineBorder(Color.black,3));
		descriptionField.setEditable(false);
		descriptionField.setFont(new Font("SansSerif", Font.BOLD, 12));
		descriptionField.setPreferredSize(new Dimension(100,110));
		descriptionField.setMaximumSize(new Dimension(100,110));
		descriptionField.setMinimumSize(new Dimension(100,110));
		StyledDocument  doc=(StyledDocument) descriptionField.getDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		cons.gridx=0; cons.gridy=1;
		cons.weightx=1; cons.weighty=1;
		//cons.ipady=100;
		add(descriptionField,cons);
	}

	public void clearLastField() {
		MyButton button=getField(FINISH_FIELD);
		synchronized (button) {
			if(button.getName().equals("buisy")) {
				button.setName("free");
				button.setIcon(DiamondCircle.DEFAULT_IMAGE);
				button.getFigures().clear();
				refreshPanel();
			}
		}
	}

	public MyButton[] getButtons() {
		return buttons;
	}
	
	private MyButton getButton(int index) {
		return buttons[index-1];
	}
	
	public ArrayList<MyButton> getFields() {
		return fields;
	}
	
	public synchronized MyButton getField(int index) {
		return fields.get(index);
	}

	public JTextPane getDescriptionField() {
		return descriptionField;
	}
	
	public synchronized void refreshPanel() {
		revalidate();
		repaint();
	}
}
