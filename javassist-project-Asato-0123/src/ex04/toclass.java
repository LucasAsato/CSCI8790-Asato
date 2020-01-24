package ex04;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class toclass
{
	static Scanner sc = new Scanner(System.in);
	private static final String PKG_NAME = "target" + ".";
	
	public static void main(String[] args)
	{
		while (true) 
		{
			System.out.println("=============================================");
			System.out.print("Enter one class (\"q\" to terminate)\n");
		    String[] inputs = getInputs();
		    if (inputs.length != 1)
		    {
		    	System.out.println("[WRN] Invalid Input");
		    }
		    else
		    {
		    	try
		    	{
		    		ClassPool cp = ClassPool.getDefault();
			        CtClass cc = cp.get(PKG_NAME + inputs[0]);
		    	}
		    	catch (NotFoundException | CannotCompileException | //
		    	            InstantiationException | IllegalAccessException | //
		    	            NoSuchMethodException | SecurityException | //
		    	            IllegalArgumentException | InvocationTargetException e) 
		    	{
		    		System.out.println(e.toString());
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
}