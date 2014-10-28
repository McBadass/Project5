//This program takes a URl and finds all URLs in <a> tags.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class Spider extends JFrame implements ActionListener {

	public final int MAXSITES = 20;
	//public static final String DEFAULT_URL = "faculty.fortlewis.edu";
	public static final String DEFAULT_URL = "grizzly.fortlewis.edu";
	private JTextField url;			//textfield to enter the remote machine
	private JTextField url_file;	//textfield to enter the remote file name
	private JTextArea result;		//textarea to display everything the server returns
	private ArrayList<String> urls;
	private int current_url = 0;
	private boolean debug = false;	//turns on error messages
	private int siteCount = 0;			//sites visited

	public static void main(String[] cmd) {
		Spider bsr = new Spider();
	}//main

	public Spider() {
		urls = new ArrayList<String>();
		final int NROWS	= 32;			//rows in the textarea
		final int NCOLS = 100;			//columns in the textarea
		Container cp = getContentPane();//Frame's content pane
		JPanel url_pane = new JPanel();	//panel for content pane's North area
		url = new JTextField(35);
		url.setText(DEFAULT_URL);
		url_pane.add(new JLabel("Site:"));
		url_pane.add(url);
		url_file = new JTextField("",24);
		//url_file.setText("gordon_a/browserTest.html");
		url_file.setText("testing/testone.html");
		url_pane.add(new JLabel("file:"));
		url_pane.add(url_file);
		JButton go = new JButton("Go");	//button to activate the retrieval
		go.addActionListener(this);
		go.setBackground(Color.red);
		url_pane.add(go);
		cp.add(url_pane,BorderLayout.NORTH);

		result	= new JTextArea(NROWS, NCOLS);
		JScrollPane jsp = new JScrollPane(result);	//place textarea in a scrollpane
		JPanel pane = new JPanel();			//panel to place in content pane's center
		pane.add(jsp);
		cp.add(pane, BorderLayout.CENTER);

		result.setBackground(Color.cyan);
		setBounds(50,150,1400,700);
		setVisible(true);
	}//constructor

	public void actionPerformed(ActionEvent e) {
		result.setText("");
		String site = url.getText();
		String file = url_file.getText();
		urls.add(site);

		Page p;
		try {
			p = new Page("http://" + site+"/"+file);
		} catch (Exception ee) {
			msg("Spider:actionPerformed: page is null\n");
			return;
		}
		if (p.pageDone()) 	msg("Spider:actionPerformed: pageDone\n");
		while (p != null && !p.pageDone()) {
			while (p != null && !p.pageDone()) {
				String line = p.getLine();
				if (debug) msg(">> " + line + "\n");
			}
			ArrayList<String> links = p.getLinks();
			for (int i = 0; i < links.size(); i++) {
				result.append(links.get(i) + "\n");
				urls.add(links.get(i));
			}
			if (++current_url >= urls.size())	p = null;
			else {
				do {
					try {
						p = new Page(urls.get(current_url));
					} catch (Exception eee) { //bad url - just ignore
						current_url++;
					}
				} while (current_url < urls.size() && p == null);
				msg("Spider:actionPerformed: next page is [" + p.getName() + "]");
			}
			if (++siteCount >= MAXSITES)  break;
		}
	}//actionPerformed

	public void msg(String words) {
		System.out.println(words);
	}

}//class