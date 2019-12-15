package com.gemini.jobcoin;

import com.gemini.jobcoin.service.JobCoinService;
import com.gemini.jobcoin.service.MixerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Scanner;

@EnableScheduling
@SpringBootApplication
public class JobcoinApplication implements CommandLineRunner {
    @Autowired
    JobCoinService jobCoinService;
    @Autowired
    MixerService mixerService;
    private boolean readyToDisburse = false;
    private ArrayList<String> addresses;

    public static void main(String[] args) {
        SpringApplication.run(JobcoinApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String depositAddress;
        int balance = 0;

        // START: PROVIDE SOURCE ADDRESS
        String sourceAddress = jobCoinService.obtainSourceAddress();

        // STEP 1: PROVIDE A LIST OF NEW ADDRESSES
        // Obtain fresh user addresses
        addresses = jobCoinService.obtainUserAddresses();

        // STEP 2: MIXER PROVIDES A DEPOSIT ADDRESS
        // Generate a random deposit address that the mixer owns to put the money into.
        depositAddress = mixerService.provideDepositAddress();
        System.out.println("Your deposit address is: " + depositAddress);

        boolean validDepositAccountEntered = false;

        // STEP 3: TRANSFER COINS TO DEPOSIT ADDRESS
        while (!validDepositAccountEntered) {
            String targetDepositAddress = jobCoinService.initiateDeposit();

            if (!targetDepositAddress.equals(depositAddress)) {
                System.err.println("Invalid target address");
            } else {
                validDepositAccountEntered = true;
                System.out.println("Processing deposit...");
                mixerService.depositFunds(sourceAddress, targetDepositAddress);
                System.out.println("Deposit processed.");
                readyToDisburse = true;
                System.out.println("Please wait while the house processes funds...");
            }
        }
    }

    // Check if funds are in deposit account and ready for disbursement...
    @Scheduled(fixedRate = 3000)
    public void checkIfReadyToDisburse() {
        if (readyToDisburse) {
            // Disburse the funds from the house account into the various addresses provided by the user.
            System.out.println("Funds being disbursed to target accounts");
            mixerService.disburseFunds("houseAccount", addresses);
            // Reset the flag...
            readyToDisburse = false;
            sendPromptToQuit();
        }
    }

    public void sendPromptToQuit() {
        System.out.println("Goodbye...");
        System.exit(0);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}

