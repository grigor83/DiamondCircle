package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.stream.Stream;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.DiamondCircle;

import java.awt.Color;

public class ResultsFrame extends JFrame implements ListSelectionListener {
	private JList<String> list;
	private DefaultListModel<String> l1;
	private Stream<Path> files;
	
	public ResultsFrame() {
		super("REZULTATI");
		loadFiles();
		JScrollPane sb=new JScrollPane(list);
		sb.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(sb);
		setSize(300,400);
		setLocationRelativeTo(null);
 		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 		setVisible(true);
	}

	private void loadFiles() {
		try {
			files = Files.list(DiamondCircle.dir.toPath());			
			Iterable<Path> iterable = files::iterator;
			l1 = new DefaultListModel<>();  
		    for (Path s : iterable) {
		    	l1.addElement(s.toString());
		    }
			list=new JList<>(l1);
			list.addListSelectionListener(this);
			list.setBackground(Color.gray);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readResultFile(String s) {		
		JTextArea text=new JTextArea();
		text.setEditable(false);
		try {
			BufferedReader bw=new BufferedReader(new FileReader(new File(s)));
			String s2=null;
			while((s2=bw.readLine())!=null)
				text.append(s2+"\n");
		} catch (FileNotFoundException e) {
			DiamondCircle.loger.log(Level.WARNING, e.fillInStackTrace().toString());
			JOptionPane.showMessageDialog(null, e);
		} catch (IOException e2) {
			DiamondCircle.loger.log(Level.WARNING, e2.fillInStackTrace().toString());
			JOptionPane.showMessageDialog(null, e2);
		}
		JScrollPane sp=new JScrollPane(text);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JFrame frame=new JFrame(s);
		frame.add(sp);
		frame.pack();
		frame.setLocationRelativeTo(null);
 		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 		frame.setVisible(true);
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			readResultFile(list.getSelectedValue().toString());
          }
	}

}
