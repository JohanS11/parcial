package edu.eci.arsw.moneylaundering;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;


public class MoneyLaunderingTool {


    private static boolean pause;

    public static boolean isPause() {
        return pause;
    }

    public static void main(String[] args)
    {
        //MoneyLaundering moneyLaundering1 = new MoneyLaundering();
        //Thread processingThread = new Thread(() -> moneyLaundering1.processTransactionData());

        //processingThread.start();
        MoneyLaundering moneyLaundering1 = new MoneyLaundering();
        int numHil = 5;
        MoneyLaundering[] threads = new MoneyLaundering[ numHil ];
        int start=0;
        AtomicInteger atomicH = new AtomicInteger(numHil);

        int div = moneyLaundering1.getAmountOfFilesTotal()/numHil;

        for (int i=0; i<numHil;i++){

            if(i==numHil-1){
                int residuo = moneyLaundering1.getAmountOfFilesTotal()%numHil;
                threads[i]=new MoneyLaundering(start,start+div+residuo,atomicH);
            }
            else{
                threads[i]=new MoneyLaundering(start,start+div,atomicH);
            }

            threads[i].start();
            start+=div;

        }
        /*
        for (int i=0; i<numHil;i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

        while(atomicH.get()>0)
        {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if(line.contains("exit")) {
                pause=true;
            }

            else{
                pause=false;
            }

            String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
            List<String> offendingAccounts = MoneyLaundering.getOffendingAccounts();
            String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
            message = String.format(message, MoneyLaundering.getAmountOfFilesProcessed().get(), MoneyLaundering.getAmountOfFilesTotal(), offendingAccounts.size(), suspectAccounts);
            System.out.println(message);
        }
    }



}
