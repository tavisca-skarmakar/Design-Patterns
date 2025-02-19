package patterns;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.management.RuntimeErrorException;

public class Singleton implements Serializable, Cloneable {
	//private static Singleton soleInstance = new Singleton();    //here object will be created during the class loading phase.
	
	private static volatile Singleton soleInstance = null;
	
	private Singleton() {
		if(soleInstance != null) {
			throw new RuntimeException("Cannot create, please use getInstance()");
		}
		
		// proceed with the creation
		System.out.println("Creating sole instance");
	}
	
	public static Singleton getInstance() {
		// multiple threads cause trouble only when you're lazily creating the object
		// double check locking 
//		if(soleInstance == null) {  // check 1
//			synchronized(Singleton.class) {
//				if(soleInstance == null) { // check 2
//					soleInstance = new Singleton();    // lazy initialization of object
//				}
//			}
//		}
//		return soleInstance;
		
		
		return Holder.INSTANCE;
	}
	
	// Singleton holder
	static class Holder {
		static final Singleton INSTANCE = new Singleton();
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		
		if(soleInstance != null) {
			throw new RuntimeException("Cannot clone, please use getInstance()");
		}
		
		return super.clone();
	}
	
	private Object readResolve() throws ObjectStreamException {
		System.out.println("..Read resolve..");
		return soleInstance;
	}
	
}

class TestClass {
	
	static void useSingleton() {
		Singleton singleton = Singleton.getInstance();
		print("Singleton ", singleton);
	}
	
	
	public static void main(String[] args) throws Exception {
				
		
//		Singleton s1 = Singleton.getInstance();
//		Singleton s2 = Singleton.getInstance();
//		
//		print("s1", s1);
//		print("s2", s2);
		
		
		//Reflection example
//		System.out.println("\nUsing Reflection");
//		Class clazz = Class.forName("patterns.Singleton"); 
//		Constructor<Singleton> ctor = clazz.getDeclaredConstructor(); 
//		ctor.setAccessible(true); 
//		Singleton s3 = ctor.newInstance();
//		print("s3", s3);
		
		//Serialization example
//		System.out.println("\nSerialization Example");
//		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("C:/Users/skarmakar/eclipse-workspace/DesignPatterns"
//				+ "/JavaDesignPatterns/src/patterns/tmp/s2.ser"));
//		oos.writeObject(s2);
//		
//		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("C:/Users/skarmakar/eclipse-workspace/DesignPatterns"
//				+ "/JavaDesignPatterns/src/patterns/tmp/s2.ser"));
//		Singleton s4 = (Singleton)ois.readObject();
//		
//		print("s4", s4);
		
		//Clone example
//		Singleton s5 = (Singleton) s2.clone();
//		print("s5", s5);
		
		//Multithreading example
		System.out.println("\nMultithreading example");
		ExecutorService service = Executors.newFixedThreadPool(2);
		service.submit(TestClass::useSingleton);
		service.submit(TestClass::useSingleton);
		
		service.shutdown();
		
	}

	static void print(String name, Singleton object) {
		System.out.println(String.format("Object : %s, Hashcode : %d", name, object.hashCode()));
	}
}
