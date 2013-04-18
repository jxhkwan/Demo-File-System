public class FileSpace {
  //File Information
	private int fNum, fBlocks,fBytes;
	//Variable for block allocation methods
	private int startBlock;
	private LinkedBlock firstBlock,lastBlock;
	//Used to determine whether certain methods can be called
	private boolean contig;
	//Used to Determine Block Allocation
	public enum BlockAlloc{continuous,linked};
	
	//Single Constructor, all values of file must be provided at instantiation
	public FileSpace(String fNo,String fBlockSize,String fByteSize,BlockAlloc bType,int sBlock,int sBVal){
		fNum = Integer.parseInt(fNo);
		fBlocks = Integer.parseInt(fBlockSize);
		fBytes = Integer.parseInt(fByteSize);
		//Added,instantiate variables based on
		//which type of allocation is used
		switch(bType){
			case continuous:
				startBlock = sBlock;
				firstBlock = null;
				lastBlock = null;
				contig = true;
				break;
			case linked:
				startBlock = 0;
				firstBlock = new LinkedBlock(sBVal);
				lastBlock = firstBlock;
				contig = false;
		}
	}
	
	//Getters and Setters, Used for Read, Write Operations
	public void changeNum(String toNum){
		fNum = Integer.parseInt(toNum);
	}
	public int getFileNo(){
		return fNum;
	}
	public void changeSize(int bSize){
		fBlocks += bSize;
		fBytes  += bSize*512;
	}
	//Decrease starting pos
	public void shrinkStart(int bPos){
		startBlock -= bPos;
	}
	public int getBlockSize(){
		return fBlocks;
	}
	//Following methods will have no effect
	//depending on what mode the file system uses
	public int getStart(){
		if(contig)
			return startBlock;
		else
			return -1;
	}
	public void setStart(int newStart){
		if(contig)	
			startBlock = newStart;
	}
	public void addLinkedBlock(LinkedBlock add){
		if(!contig){
			lastBlock.addNextBlock(add);
			lastBlock = add;
		}
	}
	public LinkedBlock getFirstBlock(){
		if(!contig)
			return firstBlock;
		else
			return null;
	}
	public LinkedBlock getLastBlock(){
		if(!contig)
			return lastBlock;
		else
			return null;
	}
	//File Information is formatted and printed out on a line
	//Modify constant in calculation of space variable if
	//strings exceed 11 characters length
	public void printInfo(){
		int j,spacing;
		
		spacing = 11 - String.valueOf(fNum).length();
		for(j = 0; j <spacing;j++){
			System.out.print(" ");
		}
		System.out.print(fNum+ "|");
		
		spacing = 11 - String.valueOf(fBlocks).length();
		for(j = 0; j <spacing;j++){
			System.out.print(" ");
		}
		System.out.print(fBlocks + "|");
		
		spacing = 11 - String.valueOf(fBytes).length();
		for(j = 0; j <spacing;j++){
			System.out.print(" ");
		}
		System.out.print(fBytes + "\n");
	}
	public void printInfo(BlockAlloc t){
		int j,spacing;
		
		spacing = 11 - String.valueOf(fNum).length();
		for(j = 0; j <spacing;j++){
			System.out.print(" ");
		}
		System.out.print(fNum+ "|");
		
		spacing = 11 - String.valueOf(fBlocks).length();
		for(j = 0; j <spacing;j++){
			System.out.print(" ");
		}
		System.out.print(fBlocks + "|");
		
		spacing = 11 - String.valueOf(fBytes).length();
		for(j = 0; j <spacing;j++){
			System.out.print(" ");
		}
		
		System.out.print(fBytes + "|");
		
		switch(t){
			case continuous:
				spacing = 14 - String.valueOf(startBlock).length();
				for(j = 0; j <spacing;j++){
					System.out.print(" ");
				}
				System.out.print(startBlock + "\n");
				break;
			case linked:
				spacing = 14 - String.valueOf(firstBlock.getVal()).length();
				for(j = 0; j <spacing;j++){
					System.out.print(" ");
				}
				System.out.print(firstBlock.getVal() + "|");
				spacing = 10 - String.valueOf(lastBlock.getVal()).length();
				for(j = 0; j <spacing;j++){
					System.out.print(" ");
				}
				System.out.print(lastBlock.getVal() + "\n");
		}
		
	}

}
