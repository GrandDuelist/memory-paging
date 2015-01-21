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
	private JButton BeginButton =new JButton("开始演示");
	private JButton EndButton = new JButton("结束演示");
	
	
	
	private JPanel AllPagePanel = new JPanel();
	private JPanel OutPagePanel  =new JPanel();
	//算法选择标志
	private int AllocMethod=-1;
	private int FIFO =-1;
	private int LRU =1;
	
	//控制方法标志
	private int ControlMethod =-1;
	public static final int StepControl=0;
	public static final int AutoControl=-1;
	public static final int OutControl=1;
    //单步执行时是否继续
	private boolean isContinued=false;
   //设置一个选择栏
	private MenuBar Select;
	//调度算法的选择
	private Menu AllocAlgorithmSelect =new Menu("调度算法") ;
	private MenuItem FIFOSelect =new MenuItem("FIFO");
	private MenuItem LRUSelect =new MenuItem("LRU");
	//运行方式的选择
	private Menu ExecuteWaySelect = new Menu("运行方式");
	private MenuItem SequenceSelect =new MenuItem("顺序运行");
	private MenuItem SkipSelect =new MenuItem("Skip运行");
	private MenuItem RandomSelect =new MenuItem("随机运行");
	//控制方式的选择
	private Menu ControlSelect = new Menu("控制方式");
	private MenuItem StepControlSelect =new MenuItem("手动执行");
	private MenuItem AutoControlSelect = new MenuItem("自动执行");
	private MenuItem FastControlSelect = new MenuItem("结果输出");
	//用一个标签显示选择的算法
	private JLabel ShowCurrentAlgorithm = new JLabel();
	//用一个标签显示选择 
	private JLabel ShowCurrentControl = new JLabel();
	//用一个标签显示运行方式
	private JLabel ShowCurrentExecute =new JLabel();
	
	//设置一个缺页率输出的Label
	private JLabel LackPageRateLabel =new JLabel();
	//设置一个输出缺页率的Label
	private JLabel LackPageNumLabel =new JLabel();
	//增加一个文本域显示每一步的运行情况
	private JTextArea ShowStepExecuteText ;
	 JScrollPane scroll;


	//Thread[] PageInMemory =new Thread[MAXINPAGE];
	
	private int ExecuteWay=3;
	public static final int SequeceWay=1;
	public static final int RandomWay=2;
	public static final int SkipWay=3;
	
	//开始建和结束健
	private boolean isBegin=false;
	private boolean isInterupted=false;
	
	
	private int LackPageNum=0;
	private float LackPageRate=0;
	
	//当前正在执行的指令
	private int CurrentInstruction=0;
	
	public MainFrame(){
		
		super("Memory_1152703_方志晗");
		
		
		
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
	  //设置运行方式
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
	  
	 
	  //设置算法选择
	  
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
	  
	  //设置控制方式
	  
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

  
  //设置缺页率输出的Panel
 public void  setOutPanel()
  {
	   JPanel OutPanel =new JPanel();
	   OutPanel.setBackground(Color.yellow);
	   OutPanel.setLayout(null);
	   JLabel RateHint = new JLabel("缺页率:");
	   JLabel NumHint =new JLabel("缺页数：");
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
	   Border b = BorderFactory.createEtchedBorder(); //边
	    OutPanel.setBorder(b); //为这个显示区加上边框
	   OutPanel.setBounds(460,92, 130, 320);
	   
	   this.add(OutPanel);
	   
	   
  }
 
 //一个清空上次输出结果的函数
 public void ClearOutLabel()
 
 {
	 this.LackPageNum=0;
	 this.LackPageRateLabel.setText("");
	 this.LackPageNumLabel.setText("");
 }
//打印缺页率和结果
 public void OutputLackRateAndNum()
 {
	 this.LackPageRateLabel.setText(this.LackPageRate+" %");
	 this.LackPageNumLabel.setText(this.LackPageNum+"");
 }
	//启动
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
//显示当前显示的算法，控制方式，调度算法
public void setShowLabels()
{
	this.setShowCurrentAlgorithm();
	this.setShowCurrentControl();
	this.setShowCurrentExecute();
	
	JPanel LabelPanel = new JPanel();
	LabelPanel.setLayout(new GridLayout(1,3));
	LabelPanel.setBackground(Color.pink);
	
	LabelPanel.setBounds(20, 30, 570, 60);
	Border b = BorderFactory.createEtchedBorder(); //边框

    LabelPanel.setBorder(b); //为这个显示区加上边框
	LabelPanel.add(this.ShowCurrentExecute);
	LabelPanel.add(this.ShowCurrentAlgorithm);
	LabelPanel.add(this.ShowCurrentControl);
	this.ShowCurrentExecute.setFont(new Font("Serif",Font.ROMAN_BASELINE,18));
	this.ShowCurrentAlgorithm.setFont(new Font("Serif",Font.ROMAN_BASELINE,18));
	this.ShowCurrentControl.setFont(new Font("Serif",Font.ROMAN_BASELINE,18));
	this.add(LabelPanel);
	
	
}
//显示算法
public void setShowCurrentAlgorithm()
{
	if(this.AllocMethod==LRU)this.ShowCurrentAlgorithm.setText("当前算法：LRU");
	if(this.AllocMethod==FIFO)this.ShowCurrentAlgorithm.setText("当前算法：FIFO");
}
//显示执行方式
public void setShowCurrentExecute()
{
	if(this.ExecuteWay==SequeceWay)this.ShowCurrentExecute.setText("当前执行：顺序执行");
	if(this.ExecuteWay==RandomWay)this.ShowCurrentExecute.setText("当前执行：随机执行");
	if(this.ExecuteWay==SkipWay)this.ShowCurrentExecute.setText("当前执行：Skip执行");
}
//显示控制方式
public void setShowCurrentControl()
{
	if(this.ControlMethod==StepControl)this.ShowCurrentControl.setText("当前控制：单步运行");
	if(this.ControlMethod==AutoControl)this.ShowCurrentControl.setText("当前控制：自动运行");
	if(this.ControlMethod==OutControl)this.ShowCurrentControl.setText("当前控制：结果输出");	
}


//设置内部调入页面
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
    	
    	Border b = BorderFactory.createEtchedBorder(); //边框

		this.AllPagePanel.setBorder(b); //为这个显示区加上边框
		
		
		JLabel PagePanelLabel =new JLabel("内存调入页面");
		
		PagePanelLabel.setFont(new Font("Serif",Font.PLAIN,18));
		PagePanelLabel.setBounds(20,360,550, 80);
		
    	this.add(PagePanelLabel);
    	
    	 
     }
   //设置外部等待的页面  
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
    	 
    	Border b = BorderFactory.createEtchedBorder();; //边框

    	this.OutPagePanel.setBorder(b); //为这个显示区加上边框

    	 this.add(OutPagePanel);
    	 
    	 JLabel OutPagePanelLabel =new JLabel("外部等待页面");
 		
 		OutPagePanelLabel.setFont(new Font("Serif",Font.PLAIN,18));
 		OutPagePanelLabel.setBounds(610,0,550, 80);
 		
     	this.add(OutPagePanelLabel); 
    	  	 
     }
     //设置开始和结束按钮
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
         Border b = BorderFactory.createEtchedBorder();; //边框
         TextPanel.setBorder(b); //为这个显示区加上边框
    	 this.ShowStepExecuteText=new JTextArea();
    	 
    	 scroll = new JScrollPane(this.ShowStepExecuteText); 
    	 this.ShowStepExecuteText.setWrapStyleWord(true);
    	 TextPanel.add(this.scroll);
    	
    	 //this.ShowStepExecuteText.setEnabled(false);
    	 
    	//加一个Panel做表头
    	 JPanel Title =new JPanel();
    	 Title.setLayout(new GridLayout(1,5));
    	 Title.add(new JButton("序号"));
    	 Title.add(new JButton("指令"));
    	 Title.add(new JButton("所在页"));
    	 Title.add(new JButton("替换页"));
    	 Title.add(new JButton("是否缺页"));
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
     
     
     //设置换页的函数 入口参数为指令和换入的位置
     public void showPageChanged(int instruction, int inPage)
     {
    	//this.Pages[inPage-1].calledAction(getPositionOfInstruction(instruction));
    	//换页
    	this.Pages[inPage-1].changePage(getPageOfInstruction(instruction));
    	//改变内存页面的当前页
    	this.Pages[inPage-1].CurrentPage=getPageOfInstruction(instruction);
    	//时间计数和调用计数清零
    	this.Pages[inPage-1].ResetCount();
     }
     
     
      
     //随机数的产生函数产生指令
     public int getRandomInstruction()
     {
    	 return (int)((Math.random())*320)+1;
    	 
     }
     //skip指令产生指令
     public int getSkipInstruction()
     {
    	 int a= (int)(Math.random()*100);
    	 //百分之50的几率顺序执行
    	 if(a>=0&&a<50&&CurrentInstruction<MAXINSTRUCTION)return(this.CurrentInstruction+1);
    	 else{
    	 //百分之25的几率前地址执行
    	 if(a>=50&&a<75)
    	 {    
    		 int b;
    		 
    			 b= (int)(Math.random()*CurrentInstruction)+1;
    		 return b;
   
    	 }
    	 else
    	 {
    	 //百分之25的几率后地址执行
    	 if(a>=75&&a<100) 
    	 {    
    		 
    		 
    		 return (int)(((1-(Math.random()))*(MAXINSTRUCTION-CurrentInstruction))+CurrentInstruction);
    		  }
    	 else return 1;
    	 }
    	 }
    	 
    	
     }
     
     //顺序指令产生
     public int getSequenceInstruction()
     {
    	 if(CurrentInstruction<MAXINSTRUCTION)
    	 return CurrentInstruction+1;
    	 else return 0;
     }
     
     //判断形成的指令是否在内存中
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
     
     
     //顺序执行
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
     
     //skip执行
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
     
     //随机执行
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

     
     
 	//设置指令被调用的动画
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
 			for(int i=0;i<50;i++)   //这里用循环小睡眠而不用sleep（500）可以监听任务，
 				                    // 防止长时间睡眠导致几个动画不同步
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

     
     //指令调用
     public void ExecuteInstruction(int step)
     {
    	 int Page = 0;
    	//所有页面调入时间+1
    	 for(int i=0;i<this.Pages.length;i++)
     	 {
     		 this.Pages[i].ExistCount++;
     		 this.Pages[i].UnAllocCount++;
     	 }
    	 
     	//指令在当前页
     	 if((Page=this.isInMemory(CurrentInstruction))!=-1)
     		 {
     		 this.Pages[Page-1].UnAllocCount=0;
     		 
     		 this.calledAction(this.getPositionOfInstruction(CurrentInstruction),Page);
     		 this.ShowStepExecuteText.append("            "+step+"\t" +CurrentInstruction+"\t"+this.getPageOfInstruction(CurrentInstruction)
			                 +"\t"+"--"+"\t"+"否"+'\n');
     		 scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
     		 }
     	 //不在当前页，则换页
     	 else 
     		 
     	 
     		
     		 {
     		     
     		     this.LackPageNum++;
     		     int PageByFIFONum;
     		     int PageByLRUNum;
     		     if(AllocMethod==FIFO)
     		    	 {
     		    	    //得到置换出的页面
     		    	    PageByFIFONum=getOutPageByFIFO();
     		    	    int LastPage=this.Pages[PageByFIFONum-1].CurrentPage;
     		    	    if(this.Pages[PageByFIFONum-1].CurrentPage!=0)
     		    	    this.WaitPage[this.Pages[PageByFIFONum-1].CurrentPage-1].setUnCalledOutPage();
     		    	     //改变相关状态
     		    	    
     		    	    this.showPageChanged(CurrentInstruction,PageByFIFONum);
     		    	   
     		   
     		    	   this.ShowStepExecuteText.append("            "+step+"\t" +CurrentInstruction+"\t"+this.getPageOfInstruction(CurrentInstruction)
  			                 +"\t"+LastPage+"\t"+"是"+'\n');
     		    	  //scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
     		    	 this.WaitPage[this.Pages[ PageByFIFONum-1].CurrentPage-1].setCalledOutPage();
     		    	//换入页刚刚被访问，未访问为0
     		    	    this.Pages[PageByFIFONum-1].UnAllocCount=0;
     		    	   //换入页的存在次数为0
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
     			                 +"\t"+LastPage+"\t"+"是"+'\n');
        		    	//  scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
     		    	     
     		    	    this.WaitPage[this.Pages[PageByLRUNum-1].CurrentPage-1].setCalledOutPage();
     		    	    //换入页刚刚被访问，未访问为0
     		    	     this.Pages[PageByLRUNum-1].UnAllocCount=0;
     		    	     //换入页的存在次数为0
     		    	    this.Pages[PageByLRUNum-1].ExistCount=0;
      		    	     this.calledAction(this.getPositionOfInstruction(CurrentInstruction),PageByLRUNum);
     		    	 }
     		     
     		 }
            
     	 
     	 }
     
     
     
     
     //FIFO的调度算法,找到要换出的页面
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
     
     //LRU的调度算法，找到要换出的页面
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
