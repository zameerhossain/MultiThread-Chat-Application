package Server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.PrimitiveIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Zameer
 */
public class Server {

    static ArrayList<Socket> SO = new ArrayList<Socket>(10);
    static ArrayList<String> NA = new ArrayList<String>(10);
    static int ssr = 0;
    static BufferedReader fr;
    static PrintWriter fw;

    public static void main(String[] args) {

        try {
            ServerSocket SSS = new ServerSocket(8900);
            fr = new BufferedReader(new FileReader("Login.txt"));
            fw = new PrintWriter(new FileWriter("Login.txt"), true);
            while (true) {
                Socket S = SSS.accept();
                ssr++;
                SO.add(S);
                BufferedReader R = new BufferedReader(new InputStreamReader(S.getInputStream()));
                PrintWriter W = new PrintWriter(S.getOutputStream(), true);
                Cre_Log T = new Cre_Log(S);
                T.start();
            }

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

class Cre_Log extends Thread {

    Socket S;

    public Cre_Log(Socket S) {
        this.S = S;
    }

    @Override
    public void run() {

        try {
            BufferedReader R = new BufferedReader(new InputStreamReader(S.getInputStream()));
            PrintWriter W = new PrintWriter(S.getOutputStream(), true);

            while (true) {
                int a = Integer.parseInt(R.readLine());
                int flg = -1;
                switch (a) {

                    case 0:
                        String s = R.readLine();
                        Server.fw.append("").print(s);
                        s = R.readLine();
                        Server.fw.append(" ").print(s);
                        s = R.readLine();
                        Server.fw.append(" ").print(s);
                        s = R.readLine();
                        Server.fw.append(" ").print(s);
                        s = R.readLine();
                        Server.fw.append(" ").println(s);

                        break;

                    case 1:
                        s = R.readLine();
                        String s1 = R.readLine();
                        String s2 = Server.fr.readLine();

                        while (s2 != null) {
                            String sp[] = s2.split(" ");
                            if (sp[0].equals(s) && sp[1].equals(s1)) {
                                flg = 1;
                                break;
                            } else {
                                flg = -1;
                            }
                            s2 = Server.fr.readLine();
                        }
                        Server.fr = new BufferedReader(new FileReader("Login.txt"));
                        W.println("" + flg);

                        break;

                }
                if (flg == 1) {

                    break;
                }
            }
            String name = R.readLine();
            rd_wr rw = new rd_wr(S, name);
            rw.start();

        } catch (IOException ex) {
            Logger.getLogger(Cre_Log.class.getName()).log(Level.SEVERE, null, ex);

        }

    }
}

class rd_wr extends Thread {

    Socket s;
    String name;

    public rd_wr(Socket s, String name) {
        this.s = s;
        this.name = name;
        Server.NA.add(name);
    }

    @Override
    public void run() {
        try {
            //To change body of generated methods, choose Tools | Templates.
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(s.getInputStream())
            );
            int flag = -1;
            nam();
            for (Socket x : Server.SO) {
                if (x != s) {
                    Socket ss = x;
                    PrintWriter uut = new PrintWriter(ss.getOutputStream(), true);
                    uut.println(name + " is ONLINE");
                }
            }
            for(Socket x : Server.SO)
            {
                PrintWriter uut = new PrintWriter(x.getOutputStream(), true);
                uut.println("update");
                for(String a:Server.NA)
                {
                    uut.println(a);
                }
                uut.println("");
            }
            while (true) {
                String clc = "&&$";
                String a = in.readLine();
                if (a.contains(clc)) {
                    String[] sp = a.split(" ");
                    flag = checkCL(sp[1]);
                    if (flag == -1) {
                        out.println("SERVER : YOUR MS WILL SEND TO ALL");
                    } else if (flag == -2) {
                        out.println("SERVER : CLIENT NOT CONNECTED");
                    } else {
                        out.println("SERVER : YOUR MS WILL SEND TO "+sp[1]+"ONLY");
                    }
                } else if (flag != -1) {
                    Socket ss = Server.SO.get(flag);
                    PrintWriter uut = new PrintWriter(ss.getOutputStream(), true);
                    uut.println(name + ": " + a);
                } else {
                    for (Socket x : Server.SO) {
                        if (x != s) {
                            Socket ss = x;
                            PrintWriter uut = new PrintWriter(ss.getOutputStream(), true);
                            uut.println(name + ": " + a);
                        }
                    }
                }
                /* if (SErver.SO.get(0).equals(s)) {
                    Socket ss = SErver.SO.get(1);
                    PrintWriter uut = new PrintWriter(ss.getOutputStream(), true);

                    uut.println(a);
                } else {
                    Socket ss = SErver.SO.get(0);
                    PrintWriter uut = new PrintWriter(ss.getOutputStream(), true);

                    uut.println(a);
                }*/

            }
        } catch (IOException ex) {
            Logger.getLogger(rd_wr.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void nam() {
        try {
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            out.println(this.name);
        } catch (IOException ex) {
            Logger.getLogger(rd_wr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int checkCL(String a) {
        int count = 0, check = -1;
        if (a.toLowerCase().equals("all")) {
            check = -1;
        } else {
            for (String x : Server.NA) {
                count++;
                if (x.equals(a)) {
                    check = count - 1;
                    break;
                }
            }
            if (check == -1) {
                check = -2;
            }
        }
        return check;
    }

}
