import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FileOperationsTester {
  public static void main(String[] args) {
		String in;
		String[] temp;
		FileSystem fSystem;
		int rNum,count = 0,typeNo = 0,status = 0;
		//Used for Data Parsing, with whitespace delimited variables
		Pattern p = Pattern.compile(" ");
		try{
			//Directory Varies by User/OS
			BufferedReader reader = new BufferedReader(new FileReader("C:\\Resources\\fileoperations.txt"));
			in=reader.readLine();
			rNum = Integer.parseInt(in);
			Scanner scan= new Scanner(System.in);
			System.out.println("-----------------File Operations Tester----------");
			System.out.println("--Reads in Operations from FileOperation.txt-----");
			System.out.println("---Please enter the type of file system to use:---");
			System.out.println("---1 for Contiguous, 2 for Linked List, default is Contiguous---");
			try{
				typeNo = scan.nextInt();
			}catch(InputMismatchException e){
				//Default to Contiguous if Improper Input
				typeNo = 1;
			}
			scan.close();
			System.out.println("---Running Operations.....---");
			switch(typeNo){
				case 1:
					System.out.println("Selected Mode: Contiguous");
					fSystem = new FileSystem(FileSpace.BlockAlloc.continuous);
					while(count<rNum){
						in=reader.readLine();
						temp = p.split(in);
						System.out.print("File No:" + temp[0] + " Operation:" + temp[1] + " ");
						switch(temp[1]){
							case "R":
								status = fSystem.read(temp[0], FileSpace.BlockAlloc.continuous);
								break;
							case "W":
								status =  fSystem.write(temp[0], FileSpace.BlockAlloc.continuous);
								break;
							case "D":
								status = fSystem.Delete(temp[0], FileSpace.BlockAlloc.continuous);
						}
						switch(status){
						case 5:
							System.out.print("Status:Success\n");
							break;
						case 6:
							System.out.print("Status:Error:File not Found\n");
							break;
						case 7:
							System.out.print("Status:Error:Number Format\n"); 
							break;
						case 8:
							System.out.print("Status:Error:Disk Full\n");
						}
						count++;
					}
					fSystem.List(FileSpace.BlockAlloc.continuous);
					break;
				case 2:
					System.out.println("Selected Mode: Linked List");
					fSystem = new FileSystem(FileSpace.BlockAlloc.linked);
					while(count<rNum){
						in=reader.readLine();
						temp = p.split(in);
						System.out.print("File No:" + temp[0] + " Operation:" + temp[1] + " ");
						switch(temp[1]){
							case "R":
								status = fSystem.read(temp[0], FileSpace.BlockAlloc.linked);
								break;
							case "W":
								status =  fSystem.write(temp[0], FileSpace.BlockAlloc.linked);
								break;
							case "D":
								status = fSystem.Delete(temp[0], FileSpace.BlockAlloc.linked);
						}
						switch(status){
						case 5:
							System.out.print("Status:Success\n");
							break;
						case 6:
							System.out.print("Status:Error:File not Found\n");
							break;
						case 7:
							System.out.print("Status:Error:Number Format\n"); 
							break;
						case 8:
							System.out.print("Status:Error:Disk Full\n");
						}
						count++;
					}
					fSystem.List(FileSpace.BlockAlloc.linked);
			}
			
			System.out.println("-----------Operations Finished-------------");
			reader.close();
		}catch(IOException e) {
			System.out.println("---Exiting---");
			System.out.print("Error:fileoperations.txt not Found\n");
		}
		System.out.println("---Exiting---");
		System.exit(0);
	}
}
