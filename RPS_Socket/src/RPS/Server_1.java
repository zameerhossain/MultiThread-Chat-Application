/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RPS;

//import static RPS.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nayeem
 */

public class Server_1 {

    static ArrayList<Socket> RS = new ArrayList<Socket>(10);
    static Iterator<Socket> itr = RS.listIterator();
    public static void main(String[] args) {


        try {
            ServerSocket SS = new ServerSocket(8800);
            while (true) {

                Socket S = SS.accept();

                RS.add(S);
              //  plahandler pla_n =new plahandler(S);
                //normal run
              //  pla_n.start();
                req game =new req(S);
                game.start();

            }

           // String a [] = new String[2];


        } catch (IOException ex) {
            Logger.getLogger(Server_1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

class plahandler extends Thread {

    Socket S;
    static int n = 1;

    public plahandler(Socket S) {
        this.S = S;
    }

    @Override
    public void run() {
        //To change body of generated methods, choose Tools | Templates.
        try {
            BufferedReader R = new BufferedReader(new InputStreamReader(S.getInputStream()));
            PrintWriter W = new PrintWriter(S.getOutputStream(), true);
            if(n==1)
            {
                W.write(1);
                n++;
            }else {
                W.write(2);
            }
        }
        catch (IOException ex) {
            Logger.getLogger(Server_1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
class req extends Thread{

    Socket S;
    int req;
    public req(Socket S) {
        this.S = S;
    }

    @Override
    public void run() {
         //To change body of generated methods, choose Tools | Templates.
         System.out.println("player Connected");
         try {
            BufferedReader R = new BufferedReader(new InputStreamReader(S.getInputStream()));
            PrintWriter W = new PrintWriter(S.getOutputStream(),true);
            while(true)
            {
                req = Integer.parseInt(R.readLine());
                if(req==0)
                {
                    int result = Integer.parseInt(R.readLine());
                    if(Server_1.RS.get(0)==(S))
                    {
                        Socket s = Server_1.RS.get(1);
                        BufferedReader rr = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        PrintWriter ww = new PrintWriter(s.getOutputStream(),true);
                        ww.println("1");
                        ww.println(result);

                    }
                    else
                    {
                        Socket s = Server_1.RS.get(0);
                        BufferedReader rr = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        PrintWriter ww = new PrintWriter(s.getOutputStream(),true);
                        ww.println("1");
                        ww.println(result);
                    }
                }
                else
                {
                    int result = Integer.parseInt(R.readLine());
                    if(Server_1.RS.get(0)==(S))
                    {
                        Socket s = Server_1.RS.get(1);
                        BufferedReader rr = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        PrintWriter ww = new PrintWriter(s.getOutputStream(),true);
                        ww.println("0");
                        ww.println(result);

                    }
                    else
                    {
                        Socket s = Server_1.RS.get(0);
                        BufferedReader rr = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        PrintWriter ww = new PrintWriter(s.getOutputStream(),true);
                        ww.println("0");
                        ww.println(result);
                    }
                }


            }

        }
        catch (IOException ex) {
            Logger.getLogger(Server_1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



}
