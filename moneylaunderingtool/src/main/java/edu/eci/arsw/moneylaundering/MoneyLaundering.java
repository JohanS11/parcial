package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoneyLaundering extends Thread {
    private static TransactionAnalyzer transactionAnalyzer;
    private TransactionReader transactionReader;
    private static int amountOfFilesTotal;

    public static AtomicInteger getAmountOfFilesProcessed() {
        return amountOfFilesProcessed;
    }

    private static AtomicInteger amountOfFilesProcessed;

    private int ini;

    private int fin;
    public static Object monitor = new Object();
    private boolean pause;
    private AtomicInteger num;

    //private boolean flag


    public MoneyLaundering(int inicio, int fin, AtomicInteger num) {


        transactionAnalyzer = new TransactionAnalyzer();
        transactionReader = new TransactionReader();
        amountOfFilesProcessed = new AtomicInteger();
        this.ini = inicio;
        this.fin = fin;
        this.num = num;
    }


    public MoneyLaundering() {

        transactionAnalyzer = new TransactionAnalyzer();
        transactionReader = new TransactionReader();
        amountOfFilesProcessed = new AtomicInteger();
    }


    public static int getAmountOfFilesTotal() {

        amountOfFilesProcessed.set(0);
        List<File> transactionFiles = getTransactionFileList();
        amountOfFilesTotal = transactionFiles.size();
        return amountOfFilesTotal;

    }

    @Override
    public void run() {

        synchronized (monitor) {
            if (MoneyLaunderingTool.isPause()) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

        }
        amountOfFilesProcessed.set(0);
        List<File> transactionFiles = getTransactionFileList();
        amountOfFilesTotal = transactionFiles.size();

        for (int i = ini; i < fin; i++) {
            List<Transaction> transactions = transactionReader.readTransactionsFromFile(transactionFiles.get(i));
            for (Transaction transaction : transactions) {
                transactionAnalyzer.addTransaction(transaction);
            }
            amountOfFilesProcessed.incrementAndGet();
        }
        num.decrementAndGet();
    }

    public void processTransactionData() {
        amountOfFilesProcessed.set(0);
        List<File> transactionFiles = getTransactionFileList();
        amountOfFilesTotal = transactionFiles.size();
        for (File transactionFile : transactionFiles) {
            List<Transaction> transactions = transactionReader.readTransactionsFromFile(transactionFile);
            for (Transaction transaction : transactions) {
                transactionAnalyzer.addTransaction(transaction);
            }
            amountOfFilesProcessed.incrementAndGet();
        }
    }

    public static List<String> getOffendingAccounts() {
        return transactionAnalyzer.listOffendingAccounts();
    }

    private static List<File> getTransactionFileList() {
        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/")).filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }

    private static String getBanner() {
        String banner = "\n";
        try {
            banner = String.join("\n", Files.readAllLines(Paths.get("src/main/resources/banner.ascii")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return banner;
    }

    private static String getHelp() {
        String help = "Type 'exit' to exit the program. Press 'Enter' to get a status update\n";
        return help;
    }
}