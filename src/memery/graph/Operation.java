package memery.graph;

public class Operation {
	
	public static void main(String arg[])
	{
		MainFrame MyMemory=new MainFrame();
		Thread Memory =new Thread(MyMemory);
		Memory.start();
		
	}

}
