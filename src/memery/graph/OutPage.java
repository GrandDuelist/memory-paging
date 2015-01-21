package memery.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;


public class OutPage extends JPanel{

	
	private boolean isCallIn= false;
	private int    OutPageNum =0;
	
	public OutPage(int num)
	{
		
		super();
		JLabel temp= new JLabel(""+num);
		temp.setFont(new Font("Serif",Font.ROMAN_BASELINE,18));
		temp.setForeground(Color.white);
		this.add(temp);
		this.OutPageNum=num;
		setUnCalledOutPage();
		
		this.setFont(new Font("Serif",Font.PLAIN,12));
		this.setPreferredSize(new Dimension(100,100));
		this.setEnabled(false);
	}
	
	
	public void setUnCalledOutPage()
	{
		this.setBackground(Color.green);
	}
	public void setCalledOutPage()
	{
		this.setBackground(Color.red);
	}
}
