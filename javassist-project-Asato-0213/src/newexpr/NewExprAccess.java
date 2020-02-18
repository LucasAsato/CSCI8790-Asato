package newexpr;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import util.UtilMenu;

public class NewExprAccess extends ClassLoader {
   static final String WORK_DIR = System.getProperty("user.dir");
   static final String CLASS_PATH = WORK_DIR + File.separator + "classfiles";
   static final String OUT_PATH = WORK_DIR + File.separator + "output";
   static final String TARGET_MY_APP2 = "target.MyAppField";
   static String _L_ = System.lineSeparator();
   static String[] inputs = null;

   public static void main(String[] args) throws Throwable {
	   while (true) {
			System.out.print("Enter two inputs: (1) an application class name,\n "
					+ "(2) number of the member fields to be analyzed and displayed.\n"
					+ "(e.g, target.ComponentApp,1 or target.ServiceApp,100)");
			inputs = UtilMenu.getArguments();
			if (inputs.length == 2) 
			{
				NewExprAccess s = new NewExprAccess();
				Class<?> c = s.loadClass(inputs[0]);
			    Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
			    mainMethod.invoke(null, new Object[] { args });
			    System.out.println("------------------------");
				
			}
			else {
				System.out.println("[WRN] Invalid number of inputs.");
			}
	   }
      
   }

   private ClassPool pool;

   public NewExprAccess() throws NotFoundException {
      pool = new ClassPool();
      pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
      pool.insertClassPath(CLASS_PATH); // TARGET must be there.
   }

   /*
    * Finds a specified class. The bytecode for that class can be modified.
    */
   protected Class<?> findClass(String name) throws ClassNotFoundException {
      CtClass cc = null;
      
      try {
         cc = pool.get(name);
         cc.instrument(new ExprEditor() {
            public void edit(NewExpr newExpr) throws CannotCompileException {
               try {
                  String longName = newExpr.getConstructor().getLongName();
                  if (longName.startsWith("java.")) {
                     return;
                  }
               } catch (NotFoundException e) {
                  e.printStackTrace();
               }
               int nFields = Integer.parseInt(inputs[1]);
               CtField fields[] = newExpr.getEnclosingClass().getDeclaredFields();
               if(fields.length < nFields)
               {
            	   nFields = fields.length;
               }
               String block1 = "{ " + _L_ //
	                     + "$_ = $proceed($$);" + _L_;//
               String log = String.format("[Edited by ClassLoader] new expr: %s, " //
	                     + "line: %d, signature: %s", newExpr.getEnclosingClass().getName(), //
	                     newExpr.getLineNumber(), newExpr.getSignature());
          	   System.out.println(log);
               for(int i = 0; i < nFields; i++)
               {
            	   try {
            	   String fieldName = fields[i].getName();
            	   String fieldType = fields[i].getType().getName();
            	   
	               block1 += 
	                      "	{"+ _L_//
	                     +"String cName = $_.getClass().getName();" + _L_//
	                     +"String fName = $_.getClass().getDeclaredFields()[" + i + "].getName();" + _L_//
	                     +"String fieldFullName = cName + \".\"  + fName ;" + _L_//
	                     + fieldType + " fieldValue = $_." + fieldName + ";" + _L_//
	                     +"System.out.print(\" [Instrument] \" + fieldFullName + \": \" + fieldValue );" + _L_ //
	               // + " System.out.println($type);" + _L_
	                     + "	}" + _L_;
	          
            	   }
            	   
            	   catch (NotFoundException e) {
                       e.printStackTrace();
                    }   
               }
               block1 += "}";
               System.out.println(block1);
        	   newExpr.replace(block1);
               
            }
         });
         cc.debugWriteFile(OUT_PATH);
         byte[] b = cc.toBytecode();
         return defineClass(name, b, 0, b.length);
      } catch (NotFoundException e) {
         throw new ClassNotFoundException();
      } catch (IOException e) {
         throw new ClassNotFoundException();
      } catch (CannotCompileException e) {
         e.printStackTrace();
         throw new ClassNotFoundException();
      }
   }
}