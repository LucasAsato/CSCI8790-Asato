package ex04.toclass;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.Loader;
import javassist.NotFoundException;

public class ToClassAsato 
{
	static Scanner sc = new Scanner(System.in);
	private static final String PKG_NAME = "target.";
	
	public static void main(String[] args) throws ClassNotFoundException
	{
		while (true) 
		{
			System.out.println("=============================================");
			System.out.print("Enter three class (e.g., target.CommonServiceA,idA,nameA or target.CommonComponentB,idB,nameB).\n"
					+ "(\"q\" to terminate)\n");
		    String[] inputs = getInputs();
		    if (inputs.length != 3)
		    {
		    	System.out.println("[WRN] Invalid Input");
		    }
		    else
		    {
		    	process(inputs[0], inputs[1], inputs[2]);
		    }
		}
	}
	static void process(String clazz, String field1, String field2) throws ClassNotFoundException
	{
		ClassPool cp = ClassPool.getDefault();
	    
	      try {
	         CtClass cc = cp.get(clazz);
	         cc.defrost();

	         CtConstructor declaredConstructor = cc.getDeclaredConstructor(new CtClass[0]);
	         String block1 = "{ " //
	               + "System.out.println(\"" + field1 + " : \" + " + field1 + "); }";
	         System.out.println("[DBG] BLOCK1: " + block1);
	         declaredConstructor.insertAfter(block1);

	         String block2 = "{ " //
	               + "System.out.println(\"" + field2 + ": \" + " + field2 + "); }";
	         System.out.println("[DBG] BLOCK2: " + block2);
	         declaredConstructor.insertAfter(block2);
	         Loader cl = new Loader(cp);
	 	     Class<?> c = cl.loadClass(clazz);
	         c.newInstance();
	         
	      } catch (NotFoundException | CannotCompileException | //
	            InstantiationException | IllegalAccessException e) {
	         System.out.println(e.toString());
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
