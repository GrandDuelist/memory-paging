package memery.graph;
import javax.swing.*;
import java.awt.*;



public class Page extends JPanel// implements Runnable
{
	
		public static int MAXINSTRUCTIONS=10;
		private  int PagNum=0;            
		public Instruction Instructions[]=new Instruction[MAXINSTRUCTIONS];//每个页面10条指令
		private JPanel ShowPage = new JPanel();
		private JLabel PageLabel ;
		public int CurrentPage=0;       //当前所驻的外部页面
		public int ExistCount=0;    //记录进入内存时间
		//记录未访问的次数
		public int UnAllocCount=0;
	public Page(int num)
	{
		super();
		this.setLayout(new GridLayout(2,1));
		PagNum= num;
		this.setPageLabel();
		this.addInstruction();
	    
		
	}
	
	
	public void addInstruction()
	{
		this.ShowPage.setLayout(new GridLayout(1,10));
		
		for(int i=0;i<Page.MAXINSTRUCTIONS ;i++)
		{
			this.Instructions[i]=new  Instruction(i+1);
			this.ShowPage.add(Instructions[i]);
			
			
		}
		this.add(this.ShowPage);
		
	}
	
	public void setPageLabel()
	{
		PageLabel=new JLabel("       PAGE:"+0);
		PageLabel.setFont(new Font("Serif",Font.PLAIN,14));
		this.add(PageLabel);
	}
	public void changePage(int num)
	{
		PageLabel.setText("       PAGE:"+num);
	}
	public void showCurrentPage()
	{
		PageLabel.setText("       PAGE:"+this.CurrentPage);
	}
	
	

    //调入新页时清零
	public void ResetCount()
	{
		this.UnAllocCount=0;
		this.ExistCount=0;
		for(int j=0;j<this.Instructions.length;j++)
			this.Instructions[j].setUnCalled();

	}
	
 
	
}
