package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class ProfilePage {
    private final Page page;
    private final Locator updateIcon;
    private final Locator email;
    private final Locator phone;
    private final Locator name;
    private final Locator birthday;
    private final Locator birthdayInvalidFormatError;
    private final Locator birthdayInvalidCharsError;
    private final Locator phoneInvalidCharsError;
    private final Locator phoneTooShortError;
    private final Locator phoneTooLongError;
    private final Locator certification;
    private final Locator skill;
    private final Locator save;
    private final Locator cancel;
    private final Locator profileModal;

    public ProfilePage(Page page) {
        this.page = page;
        this.updateIcon = page.locator("(//button[contains(@class, 'edit')])[2]");
        this.email = page.locator("//input[@id='email']");
        this.phone = page.locator("//input[@name='phone']");
        this.name = page.locator("//input[@id='name']");
        this.birthday = page.locator("//input[@name='birthday']");
        this.birthdayInvalidFormatError = page.locator("#birthday-error-message");
        this.birthdayInvalidCharsError = page.locator("#birthday-error-message");
        this.phoneInvalidCharsError = page.locator("#phone-error-message");
        this.phoneTooShortError = page.locator("#phone-error-message");
        this.phoneTooLongError = page.locator("#phone-error-message");
        this.certification = page.locator("//input[@id='certification']");
        this.skill = page.locator("//input[@id='skill']");
        this.save = page.locator("//button[normalize-space()='Save']");
        this.cancel = page.locator("//button[normalize-space()='Cancel']");
        this.profileModal = page.locator("//h2[@id='responsive-dialog-title']");
    }

    public Locator getEditIcon() {
        return updateIcon;
    }

    public void clickUpdateIcon() {
        updateIcon.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        updateIcon.click();
    }

    public ProfilePage enterEmail(String mail) {
        email.fill(mail);
        return this;
    }

    public ProfilePage enterPhone(String phoneValue) {
        phone.fill(phoneValue);
        return this;
    }

    public ProfilePage enterName(String nameValue) {
        name.fill(nameValue);
        return this;
    }

    public ProfilePage enterBirthday(String birthdayValue) {
        birthday.fill(birthdayValue);
        return this;
    }

    public ProfilePage selectGender(String gender) {
        page.locator("input[name='gender'][value='" + gender.toLowerCase() + "']").check();
        return this;
    }

    public ProfilePage enterCertification(String certificationValue) {
        certification.fill(certificationValue);
        return this;
    }

    public ProfilePage enterSkill(String skillValue) {
        skill.fill(skillValue);
        skill.press("Enter");
        return this;
    }

    public void enterProfileModal(String phone, String name, String birthday, String gender, String certification, String skill) {
        enterPhone(phone)
            .enterName(name)
            .enterBirthday(birthday)
            .selectGender(gender)
            .enterCertification(certification)
            .enterSkill(skill);
    }

    public Locator getSaveButton() {
        return save;
    }

    public void clickSaveButton() {
        save.click();
    }

    public Locator getCancelButton() {
        return cancel;
    }

    public void clickCancelButton() {
        cancel.click();
    }

    public Locator getUpdateProfileModal() {
        return profileModal;
    }

    public String getNameValue() {
        return name.inputValue();
    }
    public String getBirthdayValue() {
        return birthday.inputValue();
    }
    public String getPhoneValue() {
        return phone.inputValue();
    }
    public String getEmailValue() {
        return email.inputValue();
    }
    public String getCertificationValue() {
        return certification.inputValue();
    }
    public String getSkillValue() {
        return skill.inputValue();
    }
    public boolean isGenderChecked(String gender) {
        return page.locator("input[name='gender'][value='" + gender.toLowerCase() + "']").isChecked();
    }
    public boolean isUpdateProfileModalVisible() {
        return profileModal.isVisible();
    }
} 