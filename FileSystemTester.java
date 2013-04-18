import java.util.InputMismatchException;
import java.util.Scanner;

public class FileSystemTester {

  public static void main(String[] args) {
		int typeNo = 0;
		FileSystem test;
		Scanner scan= new Scanner(System.in);
		System.out.println("--------------------------File System Tester-------------------");
		System.out.println("---Tests Create, Rename, Delete and List Functions on filelist.txt----");
		System.out.println("---Please enter the type of file system to use:---");
		System.out.println("---1 for Contiguous, 2 for Linked List, default is Contiguous---");
		try{
			typeNo = scan.nextInt();
		}catch(InputMismatchException e){
			//Default to Contiguous if Improper Input
			typeNo = 1;
		}
		scan.close();
		switch(typeNo){
		case 1:
			System.out.println("Selected Mode: Contiguous");
			test = new FileSystem(FileSpace.BlockAlloc.continuous);
			test.List();
			test.Rename("8", "25");
			test.List();
			test.Rename("25", "8");
			test.List();
			test.Delete("8",FileSpace.BlockAlloc.continuous);
			test.List();
			test.Create(FileSpace.BlockAlloc.continuous);
			test.Create(FileSpace.BlockAlloc.continuous);
			test.List();
			test.Delete("12",FileSpace.BlockAlloc.continuous);
			test.List();
			test.DiskUsage();
			break;
		case 2:
			System.out.println("Selected Mode: Linked List");
			test = new FileSystem(FileSpace.BlockAlloc.linked);
			test.List();
			test.Rename("8", "25");
			test.List();
			test.Rename("25", "8");
			test.List();
			test.Delete("8",FileSpace.BlockAlloc.linked);
			test.List();
			test.Create(FileSpace.BlockAlloc.linked);
			test.Create(FileSpace.BlockAlloc.linked);
			test.List();
			test.Delete("12",FileSpace.BlockAlloc.linked);
			test.List();
			test.DiskUsage();
		}
		System.exit(0);
	}
}
