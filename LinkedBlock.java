//Node Class for Linked List
public class LinkedBlock{
  private int val;
	private LinkedBlock nextBlock;
	public LinkedBlock(int val){
		this.val = val;
		nextBlock = null;
	}
	//Getter & Setter Methods
	public int getVal()
	{
		return val;
	}
	public void setVal(int val){
		this.val = val;
	}
	public LinkedBlock getNextBlock(){
		return nextBlock;
	}
	public void addNextBlock(LinkedBlock nextBlock){
		this.nextBlock = nextBlock;
	}

}
