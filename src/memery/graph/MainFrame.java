package memery.graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.Border;


public class MainFrame extends JFrame implements Runnable{
	
	public static int MAXINPAGE=4;
	public static int MAXOUTPAGE=32;
	public static int MAXINSTRUCTION=320;
	private Page Pages[]=new Page[MAXINPAGE];
	private OutPage WaitPage[]= new OutPage[MAXOUTPAGE];
	private JButton BeginButton =new JButton("��ʼ��ʾ");
	private JButton EndButton = new JButton("������ʾ");
	
	
	
	private JPanel AllPagePanel = new JPanel();
	private JPanel OutPagePanel  =new JPanel();
	//�㷨ѡ���־
	private int AllocMethod=-1;
	private int FIFO =-1;
	private int LRU =1;
	
	//���Ʒ�����־
	private int ControlMethod =-1;
	public static final int StepControl=0;
	public static final int AutoControl=-1;
	public static final int OutControl=1;
    //����ִ��ʱ�Ƿ����
	private boolean isContinued=false;
   //����һ��ѡ����
	private MenuBar Select;
	//�����㷨��ѡ��
	private Menu AllocAlgorithmSelect =new Menu("�����㷨") ;
	private MenuItem FIFOSelect =new MenuItem("FIFO");
	private MenuItem LRUSelect =new MenuItem("LRU");
	//���з�ʽ��ѡ��
	private Menu ExecuteWaySelect = new Menu("���з�ʽ");
	private MenuItem SequenceSelect =new MenuItem("˳������");
	private MenuItem SkipSelect =new MenuItem("Skip����");
	private MenuItem RandomSelect =new MenuItem("�������");
	//���Ʒ�ʽ��ѡ��
	private Menu ControlSelect = new Menu("���Ʒ�ʽ");
	private MenuItem StepControlSelect =new MenuItem("�ֶ�ִ��");
	private MenuItem AutoControlSelect = new MenuItem("�Զ�ִ��");
	private MenuItem FastControlSelect = new MenuItem("������");
	//��һ����ǩ��ʾѡ����㷨
	private JLabel ShowCurrentAlgorithm = new JLabel();
	//��һ����ǩ��ʾѡ�� 
	private JLabel ShowCurrentControl = new JLabel();
	//��һ����ǩ��ʾ���з�ʽ
	private JLabel ShowCurrentExecute =new JLabel();
	
	//����һ��ȱҳ�������Label
	private JLabel LackPageRateLabel =new JLabel();
	//����һ�����ȱҳ�ʵ�Label
	private JLabel LackPageNumLabel =new JLabel();
	//����һ���ı�����ʾÿһ�����������
	private JTextArea ShowStepExecuteText ;
	 JScrollPane scroll;


	//Thread[] PageInMemory =new Thread[MAXINPAGE];
	
	private int ExecuteWay=3;
	public static final int SequeceWay=1;
	public static final int RandomWay=2;
	public static final int SkipWay=3;
	
	//��ʼ���ͽ�����
	private boolean isBegin=false;
	private boolean isInterupted=false;
	
	
	private int LackPageNum=0;
	private float LackPageRate=0;
	
	//��ǰ����ִ�е�ָ��
	private int CurrentInstruction=0;
	
	public MainFrame(){
		
		super("Memory_1152703_��־��");
		
		
		
		try
		{
			
			Init();
		}
		catch(Exception exception)
		{

			exception.printStackTrace();
	 	}
	
	
	}
	
	
  public void setMyMenuBar()
  {
	  Select= new MenuBar();
	  //�������з�ʽ
	  this.ExecuteWaySelect.add(this.SkipSelect);
	  this.ExecuteWaySelect.add(this.SequenceSelect);
	  this.ExecuteWaySelect.add(this.RandomSelect);
	  this.Select.add(this.ExecuteWaySelect);
	  
	  this.SkipSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				  ExecuteWay=SkipWay;
				  setShowCurrentExecute();
				}	
			});
	  this.SequenceSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				  ExecuteWay=SequeceWay;
				  setShowCurrentExecute();
				}	
			});
	  this.RandomSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				  ExecuteWay=RandomWay;
				  setShowCurrentExecute();
				}	
			});
	  
	 
	  //�����㷨ѡ��
	  
	  this.AllocAlgorithmSelect.add(this.FIFOSelect);
	  this.AllocAlgorithmSelect.add(this.LRUSelect);
	  this.Select.add(this.AllocAlgorithmSelect);
	  
	  this.FIFOSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				  AllocMethod=FIFO;
				  setShowCurrentAlgorithm();
				}	
			});
	  this.LRUSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				  AllocMethod=LRU;
				  setShowCurrentAlgorithm();
				}	
			});
	  
	  //���ÿ��Ʒ�ʽ
	  
	  this.ControlSelect.add(this.StepControlSelect);
	  this.ControlSelect.add(this.AutoControlSelect);
	  this.ControlSelect.add(this.FastControlSelect);
	  
	  this.StepControlSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ControlMethod=StepControl;
				  setShowCurrentControl();
				}	
			});
	  this.AutoControlSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ControlMethod=AutoControl;
				  setShowCurrentControl();
				}	
			});
	  this.FastControlSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ControlMethod=OutControl;
				  setShowCurrentControl();
				}	
			});
	  
	  this.Select.add(this.ControlSelect);
	  
	  this.setMenuBar(this.Select);
	  
	  
	  
	  
  }

  
  //����ȱҳ�������Panel
 public void  setOutPanel()
  {
	   JPanel OutPanel =new JPanel();
	   OutPanel.setBackground(Color.yellow);
	   OutPanel.setLayout(null);
	   JLabel RateHint = new JLabel("ȱҳ��:");
	   JLabel NumHint =new JLabel("ȱҳ����");
	   ClearOutLabel();
	   
	   OutPanel.add(NumHint);
	   OutPanel.add(RateHint);
	   OutPanel.add(this.LackPageNumLabel);
	   OutPanel.add(this.LackPageRateLabel);
	   
	   NumHint.setBounds(0,50,100,50);
	   RateHint.setBounds(0,150,100,50);
	   this.LackPageNumLabel.setBounds(40, 100, 100, 50);
	   this.LackPageRateLabel.setBounds(40, 200, 100, 50);
	   
	   this.LackPageRateLabel.setFont(new Font("Serif",Font.ROMAN_BASELINE,18));
	   this.LackPageNumLabel.setFont(new Font("Serif",Font.ROMAN_BASELINE,18));
	   RateHint.setFont(new Font("Serif",Font.ROMAN_BASELINE,18));
	   NumHint.setFont(new Font("Serif",Font.ROMAN_BASELINE,18));
	   Border b = BorderFactory.createEtchedBorder(); //��
	    OutPanel.setBorder(b); //Ϊ�����ʾ�����ϱ߿�
	   OutPanel.setBounds(460,92, 130, 320);
	   
	   this.add(OutPanel);
	   
	   
  }
 
 //һ������ϴ��������ĺ���
 public void ClearOutLabel()
 
 {
	 this.LackPageNum=0;
	 this.LackPageRateLabel.setText("");
	 this.LackPageNumLabel.setText("");
 }
//��ӡȱҳ�ʺͽ��
 public void OutputLackRateAndNum()
 {
	 this.LackPageRateLabel.setText(this.LackPageRate+" %");
	 this.LackPageNumLabel.setText(this.LackPageNum+"");
 }
	//����
private void Init()throws Exception{
	
	this.setResizable(false);
	
	this.setSize(900,600);
	this.setLayout(null);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.addPage();
	this.setOutPagePanel();
	this.setControlButton();
	this.setMyMenuBar();
	this.setShowLabels();
	this.setOutPanel();
	this.setTextArea();
	this.setVisible(true);	
}
//��ʾ��ǰ��ʾ���㷨�����Ʒ�ʽ�������㷨
public void setShowLabels()
{
	this.setShowCurrentAlgorithm();
	this.setShowCurrentControl();
	this.setShowCurrentExecute();
	
	JPanel LabelPanel = new JPanel();
	LabelPanel.setLayout(new GridLayout(1,3));
	LabelPanel.setBackground(Color.pink);
	
	LabelPanel.setBounds(20, 30, 570, 60);
	Border b = BorderFactory.createEtchedBorder(); //�߿�

    LabelPanel.setBorder(b); //Ϊ�����ʾ�����ϱ߿�
	LabelPanel.add(this.ShowCurrentExecute);
	LabelPanel.add(this.ShowCurrentAlgorithm);
	LabelPanel.add(this.ShowCurrentControl);
	this.ShowCurrentExecute.setFont(new Font("Serif",Font.ROMAN_BASELINE,18));
	this.ShowCurrentAlgorithm.setFont(new Font("Serif",Font.ROMAN_BASELINE,18));
	this.ShowCurrentControl.setFont(new Font("Serif",Font.ROMAN_BASELINE,18));
	this.add(LabelPanel);
	
	
}
//��ʾ�㷨
public void setShowCurrentAlgorithm()
{
	if(this.AllocMethod==LRU)this.ShowCurrentAlgorithm.setText("��ǰ�㷨��LRU");
	if(this.AllocMethod==FIFO)this.ShowCurrentAlgorithm.setText("��ǰ�㷨��FIFO");
}
//��ʾִ�з�ʽ
public void setShowCurrentExecute()
{
	if(this.ExecuteWay==SequeceWay)this.ShowCurrentExecute.setText("��ǰִ�У�˳��ִ��");
	if(this.ExecuteWay==RandomWay)this.ShowCurrentExecute.setText("��ǰִ�У����ִ��");
	if(this.ExecuteWay==SkipWay)this.ShowCurrentExecute.setText("��ǰִ�У�Skipִ��");
}
//��ʾ���Ʒ�ʽ
public void setShowCurrentControl()
{
	if(this.ControlMethod==StepControl)this.ShowCurrentControl.setText("��ǰ���ƣ���������");
	if(this.ControlMethod==AutoControl)this.ShowCurrentControl.setText("��ǰ���ƣ��Զ�����");
	if(this.ControlMethod==OutControl)this.ShowCurrentControl.setText("��ǰ���ƣ�������");	
}


//�����ڲ�����ҳ��
     public void addPage()
     {
    	this.AllPagePanel.setLayout(new GridLayout(1,MAXINPAGE,40,0));
    	
    	 for(int i=0;i<MAXINPAGE;i++)
    	 {
    		 this.Pages[i]=new Page(i+1); 
    		this.AllPagePanel.add(this.Pages[i]);
    		 
    	 }
    	 

    	this.AllPagePanel.setBounds(20,420,550,100);
    	this.add(this.AllPagePanel);
    	
    	Border b = BorderFactory.createEtchedBorder(); //�߿�

		this.AllPagePanel.setBorder(b); //Ϊ�����ʾ�����ϱ߿�
		
		
		JLabel PagePanelLabel =new JLabel("�ڴ����ҳ��");
		
		PagePanelLabel.setFont(new Font("Serif",Font.PLAIN,18));
		PagePanelLabel.setBounds(20,360,550, 80);
		
    	this.add(PagePanelLabel);
    	
    	 
     }
   //�����ⲿ�ȴ���ҳ��  
     public void setOutPagePanel()
     {
    	 this.OutPagePanel.setLayout(new GridLayout(8,4,12,12));   
    	 
    	 this.OutPagePanel.setBackground(Color.black);
    	 for(int i=0;i<this.WaitPage.length;i++)
    	 {
    		 this.WaitPage[i]=new OutPage(i+1);
    		 this.OutPagePanel.add(WaitPage[i]);
    		 
    		 
    	 }
   
    	 
    	 this.OutPagePanel.setBounds(610,60,180,360);
    	 
    	Border b = BorderFactory.createEtchedBorder();; //�߿�

    	this.OutPagePanel.setBorder(b); //Ϊ�����ʾ�����ϱ߿�

    	 this.add(OutPagePanel);
    	 
    	 JLabel OutPagePanelLabel =new JLabel("�ⲿ�ȴ�ҳ��");
 		
 		OutPagePanelLabel.setFont(new Font("Serif",Font.PLAIN,18));
 		OutPagePanelLabel.setBounds(610,0,550, 80);
 		
     	this.add(OutPagePanelLabel); 
    	  	 
     }
     //���ÿ�ʼ�ͽ�����ť
     public void setControlButton()
     {
    	 
    	 this.BeginButton.setBounds(590,450,120,50);
    	 this.BeginButton.setFont(new Font("Serif",Font.PLAIN,15));
    	 this.BeginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
			  isBegin=true;
			  isContinued=true;
			  isInterupted=false;
			}	
		});
         this.add(this.BeginButton);
         this.EndButton.setBounds(730,450,120,50);
    	 this.EndButton.setFont(new Font("Serif",Font.PLAIN,15));
    	 this.EndButton.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent event) {
 			  isBegin=false;
 			  isContinued=false;
 			  isInterupted=true;
 			}	
 		});
         this.add(this.EndButton);

     }
     
     public void setTextArea()
     {
    	 JPanel TextPanel =new JPanel();
         TextPanel.setBounds(20,92 , 435, 292);
         TextPanel.setBackground(Color.GRAY);
         TextPanel.setLayout(new BorderLayout());
         this.add(TextPanel);
         Border b = BorderFactory.createEtchedBorder();; //�߿�
         TextPanel.setBorder(b); //Ϊ�����ʾ�����ϱ߿�
    	 this.ShowStepExecuteText=new JTextArea();
    	 
    	 scroll = new JScrollPane(this.ShowStepExecuteText); 
    	 this.ShowStepExecuteText.setWrapStyleWord(true);
    	 TextPanel.add(this.scroll);
    	
    	 //this.ShowStepExecuteText.setEnabled(false);
    	 
    	//��һ��Panel����ͷ
    	 JPanel Title =new JPanel();
    	 Title.setLayout(new GridLayout(1,5));
    	 Title.add(new JButton("���"));
    	 Title.add(new JButton("ָ��"));
    	 Title.add(new JButton("����ҳ"));
    	 Title.add(new JButton("�滻ҳ"));
    	 Title.add(new JButton("�Ƿ�ȱҳ"));
    	 TextPanel.add(Title,BorderLayout.NORTH);
    	// this.ShowStepExecuteText.setBounds(x, y, width, height)
     }
     
     public void run()
     {
    	 while(true)
    	 {
    		 if(isBegin) 
    			 {
    			   this.ShowStepExecuteText.setText("");
    			   this.ClearOutLabel();
    			   if(ExecuteWay==SequeceWay)this.sequenceExecute();
    			   if(ExecuteWay==SkipWay)this.skipExecute();
    			   if(ExecuteWay==RandomWay)this.randomExecute();
    			   
    			 }
    		 
    		 isBegin=false;
    		 for(int i=0;i<MAXINPAGE;i++)
    		 {
    			 this.Pages[i].CurrentPage=0;
    			 this.Pages[i].showCurrentPage();
    			
    		 }
    		
    		 this.clearPages();
    		 try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	 }
     }
     
     
     public int getPageOfInstruction(int instruction)
     {
    	 int m=(int)instruction%10;
    	 if(m==0)
    	 return (int)instruction/10;
    	 else return ((int)instruction/10+1);
    	 
     }
     
     public int getPositionOfInstruction(int instruction)
     {
    	 int m=(int )instruction%10;
    	 if(m==0)return 10;
    	 else
    	 return m;
    	 
     }
     
     
     //���û�ҳ�ĺ��� ��ڲ���Ϊָ��ͻ����λ��
     public void showPageChanged(int instruction, int inPage)
     {
    	//this.Pages[inPage-1].calledAction(getPositionOfInstruction(instruction));
    	//��ҳ
    	this.Pages[inPage-1].changePage(getPageOfInstruction(instruction));
    	//�ı��ڴ�ҳ��ĵ�ǰҳ
    	this.Pages[inPage-1].CurrentPage=getPageOfInstruction(instruction);
    	//ʱ������͵��ü�������
    	this.Pages[inPage-1].ResetCount();
     }
     
     
      
     //������Ĳ�����������ָ��
     public int getRandomInstruction()
     {
    	 return (int)((Math.random())*320)+1;
    	 
     }
     //skipָ�����ָ��
     public int getSkipInstruction()
     {
    	 int a= (int)(Math.random()*100);
    	 //�ٷ�֮50�ļ���˳��ִ��
    	 if(a>=0&&a<50&&CurrentInstruction<MAXINSTRUCTION)return(this.CurrentInstruction+1);
    	 else{
    	 //�ٷ�֮25�ļ���ǰ��ִַ��
    	 if(a>=50&&a<75)
    	 {    
    		 int b;
    		 
    			 b= (int)(Math.random()*CurrentInstruction)+1;
    		 return b;
   
    	 }
    	 else
    	 {
    	 //�ٷ�֮25�ļ��ʺ��ִַ��
    	 if(a>=75&&a<100) 
    	 {    
    		 
    		 
    		 return (int)(((1-(Math.random()))*(MAXINSTRUCTION-CurrentInstruction))+CurrentInstruction);
    		  }
    	 else return 1;
    	 }
    	 }
    	 
    	
     }
     
     //˳��ָ�����
     public int getSequenceInstruction()
     {
    	 if(CurrentInstruction<MAXINSTRUCTION)
    	 return CurrentInstruction+1;
    	 else return 0;
     }
     
     //�ж��γɵ�ָ���Ƿ����ڴ���
     public int isInMemory(int CurrentInstruction)
     {
    	 int CurrentPage=-1;
    	 for(int i=0;i<this.Pages.length;i++)
    	 {
    		 if(this.Pages[i].CurrentPage==this.getPageOfInstruction(CurrentInstruction))
    			 {
    			 CurrentPage=i+1;
    			 
    			 }
    	 }
    	 return CurrentPage;
     }
     
     
     //˳��ִ��
     public void sequenceExecute()
     {
    	 this.LackPageNum=0;
    	 this.LackPageRate=0;
    	for(int m=0;m<MAXINSTRUCTION;m++)
    	{
    		if(this.isInterupted)break;
    	 CurrentInstruction=this.getSequenceInstruction();
    	 this.ExecuteInstruction(m+1);
    	
    	}
    	if(!this.isInterupted)
    	{
    	this.LackPageRate=((float)this.LackPageNum/(float)MAXINSTRUCTION)*100;
    	
    	this.OutputLackRateAndNum();
    	}
    	CurrentInstruction=0;
    	 
    	    	 
     }
     
     //skipִ��
     public void skipExecute()
     {
    	 this.LackPageNum=0;
    	 this.LackPageRate=0;
    	 CurrentInstruction=1;
    	 this.ExecuteInstruction(1);
    	for(int m=1;m<MAXINSTRUCTION;m++)
    	{
    		if(this.isInterupted)break;
    	 CurrentInstruction=this.getSkipInstruction();
    	 this.ExecuteInstruction(m+1);
    	
    	}
    	if(!this.isInterupted)
    	{
    	this.LackPageRate=((float)this.LackPageNum/(float)MAXINSTRUCTION)*100;
    	
    	this.OutputLackRateAndNum();
    	}
    	CurrentInstruction=0;
     }
     
     //���ִ��
     public void randomExecute()
     {
    	 this.LackPageNum=0;
    	 this.LackPageRate=0;
    	 CurrentInstruction=1;
    	 this.ExecuteInstruction(1);
    	for(int m=1;m<MAXINSTRUCTION;m++)
    	{
    		if(this.isInterupted)break;
    	 CurrentInstruction=this.getRandomInstruction();
    	 this.ExecuteInstruction(m+1);
    	
    	}
    	if(!this.isInterupted)
    	{
    	this.LackPageRate=((float)this.LackPageNum/(float)MAXINSTRUCTION)*100;
    	
    	this.OutputLackRateAndNum();
    	}
    	CurrentInstruction=0;
    	
     }
     public void clearPages()
     {
    	 for(int i=0;i<this.Pages.length;i++)
    		 this.Pages[i].ResetCount();
    	 for(int i=0;i<this.WaitPage.length;i++)
    		 this.WaitPage[i].setUnCalledOutPage();
    	 this.ShowStepExecuteText.cut();
     
     }

     
     
 	//����ָ����õĶ���
 	public void calledAction(int instruction,int page)
 	{
 	
 		while(this.ControlMethod==StepControl&&(!isContinued))
 		{
 			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 		}
 		this.Pages[page-1].Instructions[instruction-1].setCalled();
 		//this.Pages[page-1].Instructions[instruction-1].setBackground(Color.yellow);
 		if(this.ControlMethod==AutoControl||this.ControlMethod==StepControl)
 		{
 			for(int i=0;i<50;i++)   //������ѭ��С˯�߶�����sleep��500�����Լ�������
 				                    // ��ֹ��ʱ��˯�ߵ��¼���������ͬ��
 			{
 		try {
 			Thread.sleep(10);
 		} catch (InterruptedException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 			}
 		}
 		if(this.ControlMethod==OutControl)
 		{
 			try {
 	 			Thread.sleep(30);
 	 		} catch (InterruptedException e) {
 	 			// TODO Auto-generated catch block
 	 			e.printStackTrace();
 	 		}
 			
 		}
		this.Pages[page-1].Instructions[instruction-1].setUnCalled();
 		this.Pages[page-1].Instructions[instruction-1].setBackground(Color.red);
 		isContinued=false;
	}

     
     //ָ�����
     public void ExecuteInstruction(int step)
     {
    	 int Page = 0;
    	//����ҳ�����ʱ��+1
    	 for(int i=0;i<this.Pages.length;i++)
     	 {
     		 this.Pages[i].ExistCount++;
     		 this.Pages[i].UnAllocCount++;
     	 }
    	 
     	//ָ���ڵ�ǰҳ
     	 if((Page=this.isInMemory(CurrentInstruction))!=-1)
     		 {
     		 this.Pages[Page-1].UnAllocCount=0;
     		 
     		 this.calledAction(this.getPositionOfInstruction(CurrentInstruction),Page);
     		 this.ShowStepExecuteText.append("            "+step+"\t" +CurrentInstruction+"\t"+this.getPageOfInstruction(CurrentInstruction)
			                 +"\t"+"--"+"\t"+"��"+'\n');
     		 scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
     		 }
     	 //���ڵ�ǰҳ����ҳ
     	 else 
     		 
     	 
     		
     		 {
     		     
     		     this.LackPageNum++;
     		     int PageByFIFONum;
     		     int PageByLRUNum;
     		     if(AllocMethod==FIFO)
     		    	 {
     		    	    //�õ��û�����ҳ��
     		    	    PageByFIFONum=getOutPageByFIFO();
     		    	    int LastPage=this.Pages[PageByFIFONum-1].CurrentPage;
     		    	    if(this.Pages[PageByFIFONum-1].CurrentPage!=0)
     		    	    this.WaitPage[this.Pages[PageByFIFONum-1].CurrentPage-1].setUnCalledOutPage();
     		    	     //�ı����״̬
     		    	    
     		    	    this.showPageChanged(CurrentInstruction,PageByFIFONum);
     		    	   
     		   
     		    	   this.ShowStepExecuteText.append("            "+step+"\t" +CurrentInstruction+"\t"+this.getPageOfInstruction(CurrentInstruction)
  			                 +"\t"+LastPage+"\t"+"��"+'\n');
     		    	  //scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
     		    	 this.WaitPage[this.Pages[ PageByFIFONum-1].CurrentPage-1].setCalledOutPage();
     		    	//����ҳ�ոձ����ʣ�δ����Ϊ0
     		    	    this.Pages[PageByFIFONum-1].UnAllocCount=0;
     		    	   //����ҳ�Ĵ��ڴ���Ϊ0
     		    	    this.Pages[PageByFIFONum-1].ExistCount=0;
     		    	    
     		    	   this.calledAction(this.getPositionOfInstruction(CurrentInstruction),PageByFIFONum);
     		    	    
     		    	 }
     		     if(AllocMethod==LRU)
     		    	 {
     		    	     PageByLRUNum=getOutPageByLRU();
     		    	    int LastPage=this.Pages[PageByLRUNum-1].CurrentPage;
     		    	    if(this.Pages[PageByLRUNum-1].CurrentPage!=0)
     		    	    this.WaitPage[this.Pages[PageByLRUNum-1].CurrentPage-1].setUnCalledOutPage();
     		    	    
     		    	     this.showPageChanged(CurrentInstruction, PageByLRUNum);
     		    	    this.ShowStepExecuteText.append("            "+step+"\t" +CurrentInstruction+"\t"+this.getPageOfInstruction(CurrentInstruction)
     			                 +"\t"+LastPage+"\t"+"��"+'\n');
        		    	//  scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
     		    	     
     		    	    this.WaitPage[this.Pages[PageByLRUNum-1].CurrentPage-1].setCalledOutPage();
     		    	    //����ҳ�ոձ����ʣ�δ����Ϊ0
     		    	     this.Pages[PageByLRUNum-1].UnAllocCount=0;
     		    	     //����ҳ�Ĵ��ڴ���Ϊ0
     		    	    this.Pages[PageByLRUNum-1].ExistCount=0;
      		    	     this.calledAction(this.getPositionOfInstruction(CurrentInstruction),PageByLRUNum);
     		    	 }
     		     
     		 }
            
     	 
     	 }
     
     
     
     
     //FIFO�ĵ����㷨,�ҵ�Ҫ������ҳ��
     public int getOutPageByFIFO()
     {
    	 int temp=this.Pages[0].ExistCount;
    	 int page=1;
    	 for(int i=0;i<this.Pages.length;i++)
    	 {
    		 if(this.Pages[i].ExistCount>temp)
    			 {
    			    temp=this.Pages[i].ExistCount;
    			    page=i+1;
    			 }
    	 }
    	 return page;
     }
     
     //LRU�ĵ����㷨���ҵ�Ҫ������ҳ��
     public int getOutPageByLRU()
     {
    	 int temp=this.Pages[0].UnAllocCount;
    	 int page=1;
    	 for(int i=0;i<this.Pages.length;i++)
    	 {
    		 if(this.Pages[i].UnAllocCount>temp)
    			 {
    			    temp=this.Pages[i].UnAllocCount;
    			    page=i+1;
    			 }
    	 }
    	 return page;
     }
     

}
