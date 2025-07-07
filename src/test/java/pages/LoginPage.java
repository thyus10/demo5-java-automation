package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LoginPage {
    private final Page page;
    private final Locator email;
    private final Locator password;
    private final Locator loginBtn;
    private final Locator alertMessage;

    public LoginPage(Page page) {
        this.page = page;

        this.email = page.getByPlaceholder("Email");
        this.password = page.getByPlaceholder("Password");
      
        this.loginBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"));
        this.alertMessage = page.locator("//div[contains(text(),'Email hoặc mật khẩu không đúng !')]");
    }

    public void enterEmail(String mail) {
        email.fill(mail);
    }

    public void enterPassword(String password) {
        this.password.fill(password);
    }

    public Locator getLoginButton() {
        return loginBtn;
    }

    public void clickLogin() {
        loginBtn.click();
    }

    public void doLogin(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLogin();
    }

    public Locator getErrorMessageLocator() {
        return alertMessage;
    }
} 
