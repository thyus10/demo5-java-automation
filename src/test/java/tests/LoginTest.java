package tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import pages.LoginPage;

public class LoginTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    static void setupAll() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @AfterAll
    static void tearDownAll() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    void setup() {
        context = browser.newContext();
        page = context.newPage();
        page.navigate("https://demo5.cybersoft.edu.vn/login"); // Navigate to the login page
    }

    @AfterEach
    void tearDown() {
        context.close();
    }

    @Test
    void testLoginTC01() {
        LoginPage loginPage = new LoginPage(page);
        String email = "jisoo@yopmail.com";
        String password = "jisookim";
        loginPage.doLogin(email, password);
    }
} 
