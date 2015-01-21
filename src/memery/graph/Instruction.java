package memery.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;

public class Instruction extends JButton{
	
	   private boolean IsCalled=false;
	   private int  InstructionNum=0;
	    
		public Instruction(int num)
		{
			
			super();
			
			
		    		
			this.InstructionNum = num;
			this.setPreferredSize(new Dimension(2,4));
			this.setEnabled(false);
			this.setUnCalled();
			
			
		}
		public void setUnCalled()
		{
			this.setBackground(Color.red);
		}
		public void setCalled()
		{
		    this.setBackground(Color.yellow);
		}
		
		public void setCalledSymbol(boolean is)
		{
			this.IsCalled=is;
		}
		
		public boolean isCalledSymbol ()
		{
			return this.IsCalled;
		}


}
