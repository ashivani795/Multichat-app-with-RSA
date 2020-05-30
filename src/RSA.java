/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nabajyoti
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RSA {

    private BigInteger p;
    private BigInteger q;
    
    // making "N" public because it's a part of public key
    public  BigInteger N;
    private BigInteger phi;
    
    // making "e" public because it's a part of public key
    public  BigInteger e;
    private BigInteger d;
    
    private int bitlength = 1024;
    private int blocksize = 256;
    //blocksize in byte
    private Random r;

    public RSA() {
        //generating required variables for the RSA algorithim
        // when the instance of the class is created
        this.r = new Random();
        
        this.p = BigInteger.probablePrime(bitlength, r);
        this.q = BigInteger.probablePrime(bitlength, r);
        
        this.N = p.multiply(q);
        
        this.phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        this.e = BigInteger.probablePrime(bitlength / 2, r);
        
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0) {
            e.add(BigInteger.ONE);
        }
        this.d = e.modInverse(phi);
        
        System.out.println(e);
        System.out.println(N);
        System.out.println(d);
    }
    
    
 
    
    
    public RSA(BigInteger e, BigInteger d, BigInteger N) {
        this.e = e;
        this.d = d;
        this.N = N;     
    }
    
    
    public void generateKey(String name){
          try {
            File myObj = new File("C:\\Users\\acer\\Desktop\\Multichat-App-master\\multichatuser-master\\public keys",name+".txt");
            
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
                
                FileWriter myWriter = new FileWriter(myObj);
                myWriter.write(e + "\n" + N);
                myWriter.close();
            } else {
                System.out.println("File already exists.");
            } 

        } catch (IOException ex) {
            System.out.println("file error" + ex.getMessage());
        }
    }
    
    
    // converts the byte to string readable format
    /*
    This method is required only to view the encrypted bytes in string format.Used for debug process
    */
    public static String bytesToString(byte[] msg) {
        String test = "";
        for (byte b : msg) {
            test += Byte.toString(b);
        }
        return test;
    }

    //Encrypt method 
    /*public key(e,N)*/
    public byte[] encrypt(byte[] message, String name) {
        
      BigInteger[] publicKeys = new BigInteger[2];
      
      int i = 0;  
      
      try {
          File myObj = new File("C:\\Users\\acer\\Desktop\\Multichat-App-master\\multichatuser-master\\public keys",name);
           Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
            
              BigInteger val  = new BigInteger(data);
              publicKeys[i] = val;
              i++;
              System.out.println(val);
                
            }
            myReader.close();
            
      }catch (IOException ex) {
            System.out.println("file error" + ex.getMessage());
        }
            
        return ( new BigInteger(message) ).modPow(publicKeys[0], publicKeys[1]).toByteArray();
    }

    // Decrypt method
    /*private key(d,N)*/
    public byte[] decrypt(byte[] message) {
        
        byte[] msg = (new BigInteger(message)).modPow(d, N).toByteArray();
        
        return msg;
    }
}
