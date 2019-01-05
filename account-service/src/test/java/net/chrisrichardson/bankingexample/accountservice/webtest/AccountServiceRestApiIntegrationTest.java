package net.chrisrichardson.bankingexample.accountservice.webtest;

import net.chrisrichardson.bankingexample.accountservice.backend.AccountMother;
import net.chrisrichardson.bankingexample.accountservice.common.AccountInfo;
import net.chrisrichardson.bankingexample.accountservice.common.GetAccountResponse;
import net.chrisrichardson.bankingexample.accountservice.common.CreateAccountResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AccountServiceRestApiIntegrationTestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountServiceRestApiIntegrationTest {


  @Value("${local.server.port}")
  private int port;

  @Autowired
  private RestTemplate restTemplate;

  public String baseUrl(String path) {
    return "http://localhost:" + port + "/api" + path;
  }

  @Test
  public void shouldSaveAndLoadAccount() {

    AccountInfo accountInfo = AccountMother.makeAccount();


    CreateAccountResponse accountResponse = restTemplate.postForEntity(baseUrl("/accounts"),
            accountInfo,
            CreateAccountResponse.class).getBody();



    GetAccountResponse loadedAccountInfo = restTemplate.getForEntity(baseUrl("/accounts/" + accountResponse.getId()),
            GetAccountResponse.class).getBody();

    assertNotNull(loadedAccountInfo);

    assertEquals(accountInfo, loadedAccountInfo.getAccountInfo());
  }
}