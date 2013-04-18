import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class FileCreator {

  public static void main(String[] args) {
		
		//Block and Byte Size values for each file
		int fBlockSize;
		int fByteSize;
		
		//RNG using System Time as Seed
		Random rng = new Random(System.currentTimeMillis());
		
		//Add Console input asking user for filesystem size?
		int fNum;
		Scanner in = new Scanner(System.in);
		System.out.println("-----------------Random File Info Generator----------");
		System.out.println("--Generates X# of files between 1-40 Blocks(512 KB/block)--");
		System.out.println("-----Please enter the number files to generate:-------");
		System.out.println("-------------------10 files is default------------------");
		try{
			fNum = in.nextInt();
		}catch(InputMismatchException e){
			//Default to 10 Files if Improper Input
			fNum = 10;
		}
		in.close();
		System.out.println("Generating Files.....");
		try {
			//Directory Varies by User/OS
			BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Resources\\filelist.txt"));
			
			//Write the number of files generated on the first line of the file
			writer.write(fNum + "\n");
			
			//Generate fNum Files with Random Block Size
			for(int i = 1;i <= fNum;i++){
				System.out.println("Generating File:" + i);
				fBlockSize = rng.nextInt(40) + 1;
				fByteSize = fBlockSize*512;
				//Extra Newline at end of File, leave and account for in formatting??
				writer.write(String.valueOf(i) + " "+ fBlockSize +" " + fByteSize + "\n");
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("File IO Error! Files not Generated!");
			System.out.println("Exiting....");
			System.exit(1);
		}
		System.out.println("Sucess!! Files Generated!");
		System.out.println("Exiting....");
		System.exit(0);
	}

}
