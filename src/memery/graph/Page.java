package memery.graph;
import javax.swing.*;
import java.awt.*;



public class Page extends JPanel// implements Runnable
{
	
		public static int MAXINSTRUCTIONS=10;
		private  int PagNum=0;            
		public Instruction Instructions[]=new Instruction[MAXINSTRUCTIONS];//ÿ��ҳ��10��ָ��
		private JPanel ShowPage = new JPanel();
		private JLabel PageLabel ;
		public int CurrentPage=0;       //��ǰ��פ���ⲿҳ��
		public int ExistCount=0;    //��¼�����ڴ�ʱ��
		//��¼δ���ʵĴ���
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
	
	

    //������ҳʱ����
	public void ResetCount()
	{
		this.UnAllocCount=0;
		this.ExistCount=0;
		for(int j=0;j<this.Instructions.length;j++)
			this.Instructions[j].setUnCalled();

	}
	
 
	
}
