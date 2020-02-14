package ex09.substitutemethodbody;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import util.UtilMenu;

public class SubstituteMethodBody extends ClassLoader {
   static final String WORK_DIR = System.getProperty("user.dir");
   static final String INPUT_PATH = WORK_DIR + File.separator + "classfiles";

   static String _L_ = System.lineSeparator();
   static String[] inputs;
   static ArrayList<String> modified = new ArrayList<String>();
   
   public static void main(String[] args) throws Throwable {
	   while (true) {
			System.out.print("Enter three inputs: (1) an application class name, "
					+ "(2) a method name, (3) the index of the method parameter,\n "
					+ "and (4) the value to be assigned into the specific method parameter.\n"
					+ "(e.g. ComponentApp,move,1,0 or ServiceApp,fill,2,10)");
			inputs = UtilMenu.getArguments();
			if (inputs.length == 4) {
				if (!modified.contains(inputs[0] + "," + inputs[1])) {
				      SubstituteMethodBody s = new SubstituteMethodBody();
				      Class<?> c = s.loadClass("target." + inputs[0]);
				      modified.add(inputs[0] + "," + inputs[1]);
				      Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
				      mainMethod.invoke(null, new Object[] { args });
				}
				else
				{
					System.out.println("[WRN] This method" + inputs[1] + "has been modified!!");
				}
			}
			else {
				System.out.println("[WRN] Invalid number of inputs.");
			}
	   }
   }

   private ClassPool pool;

   public SubstituteMethodBody() throws NotFoundException {
      pool = new ClassPool();
      pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
      pool.insertClassPath(INPUT_PATH); // "target" must be there.
      System.out.println("[DBG] Class Pathes: " + pool.toString());
   }

   /*
    * Finds a specified class. The bytecode for that class can be modified.
    */
   protected Class<?> findClass(String name) throws ClassNotFoundException {
      CtClass cc = null;
      try {
         cc = pool.get(name);
         if (!cc.getName().equals("target." + inputs[0])) {
            return defineClass(name, cc.toBytecode(), 0, cc.toBytecode().length);
         }

         cc.instrument(new ExprEditor() {
            public void edit(MethodCall call) throws CannotCompileException {
               String className = call.getClassName();
               String methodName = call.getMethodName();

              if (className.equals("target." + inputs[0]) && methodName.equals(inputs[1])) {
                  System.out.println("[Edited by ClassLoader] method name: " + methodName + ", line: " + call.getLineNumber());
                  String block2 = "{" + _L_ //
                        + "System.out.println(\"\tReset param to zero.\"); " + _L_ //
                        + "$" + inputs[2] + "=" + inputs[3] + "; " + _L_ //
                        + "$proceed($$); " + _L_ //
                        + "}";
                  System.out.println("[DBG] BLOCK2: " + block2);
                  System.out.println("------------------------");
                  call.replace(block2);
               }
            }
         });
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
