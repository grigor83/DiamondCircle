package gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;

import main.DiamondCircle;

public class Card extends JButton {
	private int number;
	
	public Card(int number) {
		super(Integer.toString(number));
		setFont(new Font("SansSerif", Font.BOLD, 20));
		this.number=number;
		setPreferredSize(new Dimension(210,300));
		setBorder(BorderFactory.createLineBorder(Color.black,3));
		setBackground(Color.gray);
		if(number==0)
			setIcon(DiamondCircle.SPECIAL_CARD_IMAGE);	
		else
			setIcon(DiamondCircle.CARD_IMAGE);
		setHorizontalTextPosition(Card.CENTER);
		setVerticalTextPosition(Card.CENTER);
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}
