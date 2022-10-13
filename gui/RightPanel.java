package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Color;

public class RightPanel extends JPanel implements ActionListener {
	private JButton button, buttonResults;
	private JTextField timeField, currentCardField;
	
	public RightPanel() {
		setLayout(new GridBagLayout());		
		setBorder(BorderFactory.createLineBorder(Color.black,3));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 0;		
		gbc.ipady=10;
		currentCardField=new JTextField("TRENUTNA KARTA");
		currentCardField.setBorder(BorderFactory.createLineBorder(Color.black,3));
		currentCardField.setHorizontalAlignment(JTextField.CENTER);
		currentCardField.setBackground(Color.lightGray);
		currentCardField.setEditable(false);
		add(currentCardField,gbc);
		
		button=new JButton("Igra još nije počela!");
		button.setPreferredSize(new Dimension(210,300));
		button.setBorder(BorderFactory.createLineBorder(Color.black,3));
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;	
		add(button,gbc);
		
		timeField=new JTextField("Vrijeme trajanja igre: ");
		timeField.setBorder(BorderFactory.createLineBorder(Color.black,3));
		timeField.setHorizontalAlignment(JTextField.CENTER);
		timeField.setEditable(false);
		timeField.setBackground(Color.lightGray);
		gbc.weighty = 0.5;
		gbc.ipady=10;
		gbc.gridx = 0;
		gbc.gridy = 2;	
		add(timeField,gbc);
		
		buttonResults=new JButton("REZULTATI");
		buttonResults.setBackground(Color.pink);
		buttonResults.setBorder(BorderFactory.createLineBorder(Color.black,3));
		buttonResults.setFont(new Font("SansSerif", Font.BOLD, 15));
		buttonResults.addActionListener(this);
		gbc.anchor = GridBagConstraints.PAGE_END;
		gbc.ipady=70;
		gbc.gridx = 0;
		gbc.gridy = 3;	
		add(buttonResults,gbc);
	}

	public JTextField getTimeField() {
		return timeField;
	}

	public void setTimeField(String s) {
		timeField.setText(s);;
	}

	public JButton getButton() {
		return button;
	}

	public void setButton(Card b) {
		remove(button);
		button = b;
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;		
		add(b,gbc);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ResultsFrame frame=new ResultsFrame();				
			}
		}).start();
	}
}
