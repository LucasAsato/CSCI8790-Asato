package question0121;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;

public class DefineNewClass 
{
	static String workDir = System.getProperty("user.dir");
	static String outputDir = workDir + File.separator + "output";
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) 
	{
		String superClassName = "";
		String subClassArray[] = new String[2];
		String commonClassArray[] = new String[3];
		while (true) 
		{
			System.out.println("=============================================");
			System.out.print("Enter three classes (\"q\" to terminate)\n");
			String[] inputs = getInputs();
			if (inputs.length != 3) 
			{
				System.out.println("[WRN] Invalid Input");
			} 
			else 
			{
				int commonNum = 0;
				if (inputs[0].startsWith("Common")) 
				{
					commonClassArray[commonNum++] = inputs[0];
				}
				if (inputs[1].startsWith("Common")) 
				{
					commonClassArray[commonNum++] = inputs[1];
				}
				if (inputs[2].startsWith("Common")) 
				{
					commonClassArray[commonNum++] = inputs[2];
				}
				switch (commonNum) 
				{
				case 0:
					superClassName = inputs[0];
					subClassArray[0] = inputs[1];
					subClassArray[1] = inputs[2];
					break;
				case 1:
					int temp = 0;
					superClassName = commonClassArray[0];
					for (int i = 0; i <= 2; i++) 
					{
						if (!inputs[i].contentEquals(superClassName)) 
						{
							subClassArray[temp++] = inputs[i];
						}
					}
					break;
				case 2:
					if (commonClassArray[0].length() >= commonClassArray[1].length()) 
					{
						superClassName = commonClassArray[0];
					} 
					else 
					{
						superClassName = commonClassArray[1];
					}
					temp = 0;
					for (int i = 0; i <= 2; i++) 
					{
						if (!inputs[i].contentEquals(superClassName)) 
						{
							subClassArray[temp++] = inputs[i];
						}
					}
					break;
				case 3:
					if (commonClassArray[0].length() >= commonClassArray[1].length()
							&& commonClassArray[0].length() >= commonClassArray[2].length()) 
					{
						superClassName = commonClassArray[0];
					} 
					else if (commonClassArray[1].length() >= commonClassArray[0].length()
							&& commonClassArray[1].length() >= commonClassArray[2].length()) 
					{
						superClassName = commonClassArray[1];
					} 
					else 
					{
						superClassName = commonClassArray[2];
					}
					temp = 0;
					for (int i = 0; i <= 2; i++) 
					{
						if (!inputs[i].equals(superClassName)) 
						{
							subClassArray[temp++] = inputs[i];
						}
					}
					break;
				}
				try 
				{
					ClassPool pool = ClassPool.getDefault();

					CtClass cc1 = makeClass(pool, superClassName);
					cc1.writeFile(outputDir);
					System.out.println("[DBG] write output to: " + outputDir);

					CtClass cc2 = makeClass(pool, subClassArray[0]);
					cc2.writeFile(outputDir);
					System.out.println("[DBG] write output to: " + outputDir);
					cc2.defrost();
					cc2.setSuperclass(cc1);
					cc2.writeFile(outputDir);
					System.out.println("[DBG] write modified output to: " + outputDir);

					CtClass cc3 = makeClass(pool, subClassArray[1]);
					cc3.writeFile(outputDir);
					System.out.println("[DBG] write output to: " + outputDir);
					cc3.defrost();
					cc3.setSuperclass(cc1);
					cc3.writeFile(outputDir);
					System.out.println("[DBG] write modified output to: " + outputDir);
				}
				catch (CannotCompileException | IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}

	public static String[] getInputs() 
	{
		String input = sc.nextLine();
		if (input.trim().equalsIgnoreCase("q")) 
		{
			System.err.println("Terminated.");
			System.exit(0);
		}
		List<String> list = new ArrayList<String>();
		String[] inputArr = input.split(",");
		for (String iElem : inputArr) 
		{
			list.add(iElem.trim());
		}
		return list.toArray(new String[0]);
	}

	static CtClass makeClass(ClassPool pool, String newClassName) 
	{
		CtClass cc = pool.makeClass(newClassName);
		System.out.println("[DBG] make class: " + cc.getName());
		return cc;
	}
}
