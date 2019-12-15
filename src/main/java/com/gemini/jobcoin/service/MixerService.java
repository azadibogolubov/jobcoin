package com.gemini.jobcoin.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MixerService {
    @Autowired
    private GeminiAPIService geminiAPIService;

    ArrayList<Integer> depositsList = new ArrayList<>();

    // send the funds from the source address to the deposit address
    public void depositFunds(String sourceAddress, String depositAddress) {
        int balance = getBalanceOfAccount(sourceAddress);
        // Post the transaction to the deposit address.
        geminiAPIService.postTransaction(sourceAddress, depositAddress, balance);
        System.out.println("Deposit registered in amount of: " + balance);
        depositsList.add(balance);
        System.out.println("Please wait while mixing service picks up your record of deposit.");
    }

    // STEP 4: DETECT DEPOSIT AND TRANSFER TO HOUSE THROUGH POLLING EVERY 2 SECONDS...
    // Poll to see if deposit has happened...
    @Scheduled(fixedRate = 2000)
    public void checkForDepositAndSendToHouse() {
        if (depositsList.size() > 0) {
            int balance = depositsList.get(0);
            System.out.println("Deposit detected in the amount of " + balance + " jobcoins. Processing...");
            depositsList = new ArrayList<>();
            geminiAPIService.postTransaction("deposit_buxvkn", "houseAccount", balance);
            System.out.println("Deposit posted to house account");
        }
    }

    // Create a random deposit address
    public String provideDepositAddress() {
        // NOTE: Started this to random generate, but started getting tons of addresses as I coded,
        // so returned hardcoded instead, for sake of brevity on the UI.
//        String validChars = "abcdefghijklmnopqrstuvxyz";
//
//        // create 6 character string after deposit_
//        StringBuilder depositAccountStr = new StringBuilder("deposit_");
//
//        for (int i = 0; i < 6; i++) {
//            int index = (int) (validChars.length() * Math.random());
//            depositAccountStr.append(validChars.charAt(index));
//        }
//
//        depositAddress = depositAccountStr.toString();
//        return depositAddress;
        return "deposit_buxvkn";
    }

    // STEP 6: HOUSE ACCOUNT TO DOLE OUT COINS TO THE ADDRESSES PROVIDED IN RANDOM DENOMINATIONS.
    // Take the funds from the deposit and disburse it back to the source addresses.
    public void disburseFunds(String houseAccount, ArrayList<String> addresses) {
        int currBalance = getBalanceOfAccount("deposit_buxvkn");

        for (int i = 0; i < 3; i++) {
            Random denominationRandomizer = new Random();
            int amountToDistribute;

            // If we are on source address 3, we want to distribute whatever is remaining...
            if (i == 2) {
                amountToDistribute = currBalance;
            }
            // Otherwise distribute a random amount from the total...
            else {
                amountToDistribute = denominationRandomizer.nextInt(currBalance);
                currBalance -= amountToDistribute;
            }

            geminiAPIService.postTransaction(houseAccount, addresses.get(i), amountToDistribute);
        }

        // Print the balances...
        System.out.println("The balances of your source addresses are listed below:");
        for (int i = 0; i < 3; i++) {
            System.out.println("Address name: " + addresses.get(i) + ", balance = " + getBalanceOfAccount(addresses.get(i)));
        }
    }

    // Get the balance for the given source address provided.
    public Integer getBalanceOfAccount(String depositAddress) {
        JSONObject addressInfo = geminiAPIService.getAddress(depositAddress);

        // Return the value of the JSON property balance.
        return Integer.parseInt(addressInfo.getString("balance"));
    }
}
