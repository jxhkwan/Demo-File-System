import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;

public class FileSystem {

  private ArrayList<FileSpace> fSys;
	private HashMap<String,String> fMaps,rMap;
	private int rNum;
	private Random rng;
	private final int MAX_BLOCKS = 400;
	private int currDiskUsed = 0;
	private boolean diskSpace[];
	//Status Codes, lets driver harness knows the status
	private final int SUCCESS = 5;
	private final int FNOTFOUND = 6;
	private final int NUMERROR = 7;
	private final int DISKFULL = 8;
	
	public FileSystem(FileSpace.BlockAlloc t){
		String in;
		String temp[];
		
		//Setup Arraylist Holding File Wrappers
		//And a Hashmap for getting file numbers
		//Reverse Hashmap for corrections when deleting
		//setup rng for file creation
		fSys = new ArrayList<FileSpace>();
		fMaps = new HashMap<String,String>();
		rMap = new HashMap<String,String>();
		//RNG using System Time as Seed
		rng = new Random(System.currentTimeMillis());
		
		//Used for Linked File Systems
		diskSpace = new boolean[MAX_BLOCKS];
		
		//Pattern for parsing in whitespace delimited records
		Pattern p = Pattern.compile(" ");
		try {
			
			//Directory Varies by User/OS
			BufferedReader reader = new BufferedReader(new FileReader("C:\\Resources\\filelist.txt"));
			
			//Reads in size of FileSystem and allocates table space as needed
			in=reader.readLine();
			rNum = Integer.parseInt(in);
			for(int i = 0;i < rNum; i++){
				
				//read in a record on a line
				//and strip out the space delimited information
				in = reader.readLine();
				temp = p.split(in);
				
				//Adds the file record to the array list.
				switch(t){
					case continuous:
						fSys.add(new FileSpace(temp[0],temp[1],temp[2],t,currDiskUsed,0));
						break;
					case linked:
						//Allocates Random Blocks for the file
						int index,fSLeft;
						fSLeft = Integer.parseInt(temp[1]);
						while(true){
							index = rng.nextInt(MAX_BLOCKS);
							if(!diskSpace[index]){
								diskSpace[index] = true;
								break;
							}
						}
						fSLeft--;
						FileSpace tempFile = new FileSpace(temp[0],temp[1],temp[2],t,0,index);
						while(fSLeft > 0){
							index = rng.nextInt(MAX_BLOCKS);
							if(!diskSpace[index]){
								diskSpace[index] = true;
								tempFile.addLinkedBlock(new LinkedBlock(index));
								fSLeft--;
							}
						}
						fSys.add(tempFile);
						
				}
				//Maps the fileNo to the ArrayList Index
				//Offset by -1 for Arraylist index
				fMaps.put(temp[0],String.valueOf(Integer.parseInt(temp[0])-1));
				rMap.put(String.valueOf(Integer.parseInt(temp[0])-1), temp[0]);
				
				//Increment disk space used for the file
				currDiskUsed += Integer.parseInt(temp[1]);
			}
			//Used to keep track of filno after instantiation
			//increment once to get next fileno
			rNum++;
			reader.close();
		//File I/O Errors
		}catch(IOException e){
			System.out.println("File List Read Error!! File List May be Corrupted");
			//File List Parsing Error CodeL 1
			System.exit(1);
		}catch(NumberFormatException e){
			System.out.println("File System Size Error! File May be Corrupted!!");
			//File Parsing Error Code: 2 
			System.exit(2);
		}
	}
	public void List(){
		FileSpace temp;
		System.out.println("File Number|   Blocks  |Size(Bytes)");
		System.out.println("-----------------------------------");
		
		for(int i = 0 ;i < fSys.size();i++){
			//Go through the list array and print each files information
			temp = fSys.get(i);
			temp.printInfo();
			
		}
	}
	public void List(FileSpace.BlockAlloc t){
		FileSpace temp;
		switch(t){
			case continuous:
				System.out.println("File Number|   Blocks  |Size(Bytes)|Starting Block");
				System.out.println("--------------------------------------------------");
				break;
			case linked:
				System.out.println("File Number|   Blocks  |Size(Bytes)|Starting Block|Last Block");
				System.out.println("-------------------------------------------------------------");
		}	
		for(int i = 0 ;i < fSys.size();i++){
			//Go through the list array and print each files information
			temp = fSys.get(i);
			temp.printInfo(t);
		}
	}
	public void Create(FileSpace.BlockAlloc type){
		int fBlockSize = rng.nextInt(39) + 1;
		int fByteSize = fBlockSize*512;
		
		//Maps the fileNo to the ArrayList Index
		//index value depends on size of arraylist
		fMaps.put(String.valueOf(rNum),String.valueOf(fSys.size()));
		rMap.put(String.valueOf(fSys.size()), String.valueOf(rNum));
		
		//Adds the file record to the array list.
		switch(type){
		case continuous:	
			fSys.add(new FileSpace(String.valueOf(rNum),String.valueOf(fBlockSize),String.valueOf(fByteSize),type,currDiskUsed,0));
			break;
		case linked:
			//Change the value of 0, depends on memory, later
			int index,fSLeft;
			fSLeft = fBlockSize;
			while(true){
				index = rng.nextInt(MAX_BLOCKS);
				if(!diskSpace[index]){
					diskSpace[index] = true;
					break;
				}
			}
			fSLeft--;
			FileSpace tempFile = new FileSpace(String.valueOf(rNum),String.valueOf(fBlockSize),String.valueOf(fByteSize),type,0,index);
			while(fSLeft > 0){
				index = rng.nextInt(MAX_BLOCKS);
				if(!diskSpace[index]){
					diskSpace[index] = true;
					tempFile.addLinkedBlock(new LinkedBlock(index));
					fSLeft--;
				}
			}
			fSys.add(tempFile);
		}
		currDiskUsed += fBlockSize;
		
		//Increment the file no
		rNum++;
	}
	public void Rename(String oldNum,String newNum){
		String index;
		try{
			//Used for error checking for improper user
			Integer.parseInt(oldNum);
			Integer.parseInt(newNum);
			
			//Change the Mapping for the file
			//if the fileNo exists
			if(fMaps.containsKey(oldNum)){
				//Get the index of the old mapping
				//Remove the old mapping
				//and add the new one with the old index
				//Then change the fileno of the file itself
				index = fMaps.get(oldNum);
				fMaps.remove(oldNum);
				fMaps.put(newNum,index);
				fSys.get(Integer.parseInt(index)).changeNum(newNum);
			}else{
				System.out.println("Warning:File Not Found!!");
			}
		}catch(NumberFormatException e){
			System.out.println("Usage: rename [fileno1] [file no2]");
		}
	}
	public int Delete(String fileToDelete,FileSpace.BlockAlloc t){
		String index;
		FileSpace temp;
		int tIndex,aIndex;
		try{
			//Error Checking
			Integer.parseInt(fileToDelete);
			//if fileno exists
			if(fMaps.containsKey(fileToDelete)){
				//get ArrayList Index
				//remove mapping
				//remove from ArrayList
				index = fMaps.get(fileToDelete);
				fMaps.remove(fileToDelete);
				rMap.remove(index);
				temp = fSys.remove(Integer.parseInt(index));
				tIndex = temp.getBlockSize();
				//Free allocated memory
				switch(t){
				case continuous:
					aIndex = Integer.parseInt(index);
					while(aIndex < fSys.size()){
						temp = fSys.get(aIndex);
						temp.shrinkStart(tIndex);
						aIndex++;
					}
					break;
				case linked:
					LinkedBlock tmp = temp.getFirstBlock();
					while(tmp!=null){
						diskSpace[tmp.getVal()] = false;
						tmp = tmp.getNextBlock();
					}
				}
				currDiskUsed -= temp.getBlockSize();
				//Fix Up mappings after the deleted file
				tIndex = Integer.parseInt(index)+1;
				String swap;
				//Dealing with index values on the reverse mapping
				//Prevent out of bounds since you shift
				//values from index n to n-1 over by 1
				while(tIndex < fSys.size()+1){
					swap  = rMap.get(String.valueOf(tIndex));
					rMap.remove(String.valueOf(tIndex));
					fMaps.remove(swap);
					fMaps.put(swap,String.valueOf(tIndex - 1));
					rMap.put(String.valueOf(tIndex - 1),swap);
					tIndex++;
				}
				return SUCCESS;
			}else{
				return FNOTFOUND;
			}
				
		}catch(NumberFormatException e){
			System.out.println("Usage: delete [fileno]");
			return NUMERROR;
		}
	}
	public void DiskUsage(){
		System.out.println("Disk Space Used:" + currDiskUsed + " Blocks " +  currDiskUsed*512 + " KB");
		System.out.println("Disk Space Free:" + (MAX_BLOCKS-currDiskUsed) + " Blocks " +  (MAX_BLOCKS-currDiskUsed)*512 + " KB");
		System.out.println("Total Disk Space:" + MAX_BLOCKS + " Blocks " +  MAX_BLOCKS*512 + " KB");
	}
	public int read(String fileNo,FileSpace.BlockAlloc t){
		String index;
		FileSpace temp;
		try{
			//Error Checking
			Integer.parseInt(fileNo);
			//if fileno exists
			if(fMaps.containsKey(fileNo)){
				//get ArrayList Index
				//remove mapping
				//remove from ArrayList
				index = fMaps.get(fileNo);
				temp = fSys.get(Integer.parseInt(index));
				switch(t){
				case continuous:
					System.out.print(" Starting Location:" + temp.getStart() + " Length:" + temp.getBlockSize() +" ");
					break;
				case linked:
					System.out.print(" Starting Location:" + temp.getFirstBlock().getVal() + " Last Block:" + temp.getLastBlock().getVal() +" ");
				}
				return SUCCESS;
			}else{
				return FNOTFOUND;
			}
		}catch(NumberFormatException e){
			System.out.println("Usage: read [fileno] [alloctype]");
			return NUMERROR;
		}
	}
	public int write(String fileNo,FileSpace.BlockAlloc t){
		String index;
		int aIndex,aSize,count;
		FileSpace temp;
		if(currDiskUsed + 5 < MAX_BLOCKS){
			try{
				//Error Checking
				Integer.parseInt(fileNo);
				//if fileno exists
				if(fMaps.containsKey(fileNo)){
					//get ArrayList Index
					//Perform write with random size
					index = fMaps.get(fileNo);
					temp = fSys.get(Integer.parseInt(index));
					aSize = rng.nextInt(5)+1;
					switch(t){
					case continuous:
						System.out.print(" Starting Location:" + temp.getStart() + " Length:"+ aSize + " ");
						temp.changeSize(aSize);
						aIndex = Integer.parseInt(index) + 1;
						while(aIndex < fSys.size()){
							temp = fSys.get(aIndex);
							temp.setStart(temp.getStart()+aSize);
							aIndex++;
						}
						currDiskUsed += aSize;
						break;
					case linked:
						count = 0;
						System.out.println(" Starting Location:" + temp.getLastBlock().getVal() + " Blocks Written:" + " ");
						while(count<aSize){
							aIndex = rng.nextInt(MAX_BLOCKS);
							if(!diskSpace[aIndex]){
								diskSpace[aIndex] = true;
								temp.addLinkedBlock(new LinkedBlock(aIndex));
								System.out.print(aIndex + ",");
								count++;
							}
						}
						currDiskUsed += aSize;
					}
					return SUCCESS;
				}else{
					return FNOTFOUND;
				}
			}catch(NumberFormatException e){
				System.out.println("Usage: write [fileno] [alloctype]");
				return NUMERROR;
			}
		}else{
			return DISKFULL;
		}
	}
}
