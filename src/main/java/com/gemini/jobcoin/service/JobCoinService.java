package com.gemini.jobcoin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class JobCoinService {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    GeminiAPIService geminiAPIService;

    ArrayList<String> addresses = new ArrayList<>();

    // Obtain a source address from the user.
    public String obtainSourceAddress() {
        System.out.println("Please provide the source address where your JobCoins reside: ");
        Scanner addressScanner = new Scanner(System.in);
        String sourceAddress = addressScanner.next();
        return sourceAddress;
    }

    // Obtain the user addresses in which we want to put the funds.
    public ArrayList<String> obtainUserAddresses() {
        addresses = new ArrayList<>();
        System.out.println("Please provide 3 addresses where you can receive your mixed coins: ");
        Scanner addressScanner = new Scanner(System.in);

        while (addresses.size() < 3) {
            addresses.add(addressScanner.next());
        }
        return addresses;
    }

    // Prompt user to initiate deposit.
    public String initiateDeposit() {
        System.out.println("Please enter your deposit address to mix funds: ");
        Scanner depositScanner = new Scanner(System.in);

        return depositScanner.next();
    }
}