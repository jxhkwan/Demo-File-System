import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class FileOperationsCreator {
  
	public static void main(String[] args) {
		int rNum,fNum = 0,op,count,fcount = 0;
		String in;
		int opsNum[];
		boolean loop = true;
		
		//RNG using System Time as Seed
		Random rng = new Random(System.currentTimeMillis());
		try{
			System.out.println("------------------File Operations Creator--------------------");
			System.out.println("--Randomly Generate 1-5 R,W,D Operations for filelist.txt----");
			System.out.println("------------------Generating Operations....------------------");
			//Directory Varies by User/OS
			BufferedReader reader = new BufferedReader(new FileReader("C:\\Resources\\filelist.txt"));
			in=reader.readLine();
			rNum = Integer.parseInt(in);
			reader.close();
			//Randomly generate number of operations per file
			opsNum = new int[rNum];
			count = rNum;//Setup for initial run of generating loop
			for(int i=0;i<rNum;i++){
				opsNum[i] = rng.nextInt(5) + 1;
				fNum += opsNum[i];
			}
			//Directory Varies by User/OS
			BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Resources\\fileoperations.txt"));
			
			//Write the number of operations
			writer.write(fNum + "\n");
			
			//Generate Operations for the selected file
			while(loop){
				//Randomly generate a operation for a file
				fNum = rng.nextInt(rNum);
				op = rng.nextInt(3)+1;
				//If an operation can be generated
				if(opsNum[fNum] > 0){
					opsNum[fNum]--;
					fcount++;
					//Determine the operation
					switch(op){
					case 1:
						writer.write(fNum+1 + " " + "R"  +"\n");
						break;
					case 2:
						writer.write(fNum+1  + " " + "W"  +"\n");
						break;
					case 3:
						writer.write(fNum+1 + " " + "D"  +"\n");
					}
					System.out.println("File Operation " + fcount + " generated");
				}
				//Used for breaking out of loop
				for(int num:opsNum){
					if(num == 0){
						count--;
					}
				}
				//Simple Escape From Loop
				//Reset if each file hasnt
				//generated all its operations
				if(count==0)
					loop = false;
				else
					count = rNum;
			}
			writer.close();
			System.out.println("---------------Operations Geneated!------------------");
		} catch (IOException e) {
			System.out.println("--------------Operations Not Geneated!----------------");
			System.out.println("------------------Exiting.....------------------");
			System.exit(1);
		}
		System.out.println("------------------Exiting.....------------------");
		System.exit(0);
	}
}
