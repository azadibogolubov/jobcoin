package com.gemini.jobcoin;

import com.gemini.jobcoin.service.GeminiAPIService;
import com.gemini.jobcoin.service.JobCoinService;
import com.gemini.jobcoin.service.MixerService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class JobcoinApplicationTests {
    @Autowired
    private MixerService mixerService;
    @Autowired
    private GeminiAPIService geminiAPIService;

    @Test
    public void testGetAddress() {
        JSONObject returnObj = geminiAPIService.getAddress("nonsense");
        Assert.isTrue((returnObj != JSONObject.NULL && returnObj.length() != 0), "Should return non-null, non empty JSON Object");
    }

    @Test
    public void testGenerateRandomDepositAccount() {
        String depositAccount = mixerService.provideDepositAddress();
        Assert.isTrue(depositAccount.equals("deposit_buxvkn"), "Should be string deposit_buxvkn");
    }

    @Test
    public void testGetBalanceOfAccount() {
        String accountName = "nonsense";
        int balance = mixerService.getBalanceOfAccount(accountName);
        Assert.isTrue(balance == 0, "Balance should be zero");
    }

}
