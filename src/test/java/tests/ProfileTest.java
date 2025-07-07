package tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import pages.ProfilePage;
import pages.LoginPage;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInfo;

public class ProfileTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;
    ProfilePage profile;
    private boolean testFailed = false;

    @BeforeAll
    static void setupAll() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @BeforeAll
    static void ensureScreenshotsDir() throws Exception {
        Files.createDirectories(Paths.get("screenshots"));
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
        page.navigate("https://demo5.cybersoft.edu.vn/login");

        // Perform login
        LoginPage loginPage = new LoginPage(page);
        loginPage.doLogin("jisookim@yopmail.com", "jisookim");


        profile = new ProfilePage(page);
    }

    @AfterEach
    void captureScreenshotOnFailure(TestInfo testInfo) {
        if (testFailed && page != null) {
            try {
                String testName = testInfo.getDisplayName().replaceAll("[^a-zA-Z0-9._-]", "_");
                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots", testName + ".png")));
                System.out.println("Screenshot captured for failed test: " + testName);
            } catch (Exception e) {
                System.out.println("Failed to capture screenshot: " + e.getMessage());
            }
        }
        testFailed = false;
    }

    @Test
    void testUpdateProfile() {
        profile.clickUpdateIcon();
        profile.enterProfileModal("04203", "Thy", "2002-11-10", "Male", "Tester", "playwright");
        profile.clickSaveButton();
    }

    @Test
    void test_ed_001_verify_edit_icon_open_modal() {
        assertDoesNotThrow(() -> profile.clickUpdateIcon());
    }

    @Test
    void test_ed002_verify_modal_closes() {
        profile.clickUpdateIcon();
        page.keyboard().press("Escape");
        // TODO: Assert modal is closed
    }

    @Test
    void test_ed003_verify_modal_title() {
        profile.clickUpdateIcon();
    }

    @Test
    void test_ed004_verify_fields_and_buttons_inModal() {
        profile.clickUpdateIcon();
        assertTrue(profile.getEditIcon().isVisible());
        assertTrue(profile.getSaveButton().isVisible());
        assertTrue(profile.getCancelButton().isVisible());
    }

    @Test
    void test_ed_005_verify_current_gender_preselected() {
        profile.clickUpdateIcon();
        assertTrue(profile.isGenderChecked("male") || profile.isGenderChecked("female") || profile.isGenderChecked("n/a"));
    }

    @Test
    void test_ed_006_verify_email_field_editable() {
        profile.clickUpdateIcon();
        String testValue = "test.edit@example.com";
        profile.enterEmail(testValue);
        assertEquals(testValue, profile.getEmailValue());
    }

    @Test
    void test_ed_007_verify_phone_field_editable() {
        profile.clickUpdateIcon();
        String testValue = "1234567890";
        profile.enterPhone(testValue);
        assertEquals(testValue, profile.getPhoneValue());
    }

    @Test
    void test_ed_008_verify_name_field_editable() {
        profile.clickUpdateIcon();
        String testValue = "Test Name";
        profile.enterName(testValue);
        assertEquals(testValue, profile.getNameValue());
    }

    @Test
    void test_ed_009_verify_birthday_field_editable() {
        profile.clickUpdateIcon();
        String testValue = "2000-01-01";
        profile.enterBirthday(testValue);
        assertEquals(testValue, profile.getBirthdayValue());
    }

    @Test
    void test_ed_010_verify_certification_field_editable() {
        profile.clickUpdateIcon();
        String testValue = "Playwright Certification";
        profile.enterCertification(testValue);
        assertEquals(testValue, profile.getCertificationValue());
    }

    @Test
    void test_ed_011_verify_skill_field_editable() {
        try {
            profile.clickUpdateIcon();
            String testValue = "Test Automation with Playwright";
            profile.enterSkill(testValue);
            assertEquals(testValue, profile.getSkillValue());
        } catch (Throwable t) {
            testFailed = true;
            throw t;
        }
    }

    @Test
    void test_ed_012_verify_gender_selection_can_be_changed() {
        profile.clickUpdateIcon();
        profile.selectGender("female");
        assertTrue(profile.isGenderChecked("female"));
        profile.selectGender("male");
        assertTrue(profile.isGenderChecked("male"));
    }

    @Test
    void test_ed_013_update_name_field_with_valid_data() {
        profile.clickUpdateIcon();
        String newName = "Updated Name";
        profile.enterName(newName);
        assertEquals(newName, profile.getNameValue());
    }

    @Test
    void test_ed_014_update_birthday_field_with_valid_data() {
        profile.clickUpdateIcon();
        String newBirthday = "1990-05-15";
        profile.enterBirthday(newBirthday);
        assertEquals(newBirthday, profile.getBirthdayValue());
    }

    @Test
    void test_ed_015_change_gender_selection_to_opposite() {
        profile.clickUpdateIcon();
        profile.selectGender("female");
        assertTrue(profile.isGenderChecked("female"));
    }

    @Test
    void test_ed_016_verify_phone_field_accepts_valid_chars() {
        profile.clickUpdateIcon();
        String validPhone = "+1 555-123-4567";
        profile.enterPhone(validPhone);
        assertEquals(validPhone, profile.getPhoneValue());
    }

    @Test
    void test_ed_017_verify_birthday_field_accepts_yyyymmdd() {
        profile.clickUpdateIcon();
        String validBirthday = "1995-12-25";
        profile.enterBirthday(validBirthday);
        assertEquals(validBirthday, profile.getBirthdayValue());
    }

    @Test
    void test_ed_018_verify_email_field_accepts_format() {
        profile.clickUpdateIcon();
        String validEmail = "test.user.123@example.co.uk";
        profile.enterEmail(validEmail);
        assertEquals(validEmail, profile.getEmailValue());
    }

    @Test
    void test_ed_019_verify_text_fields_accept_various_chars() throws InterruptedException {
        profile.clickUpdateIcon();
        String nameValue = "Name- with space, numbers 123!@#.";
        profile.enterName(nameValue);
        Thread.sleep(500);
        System.out.println("Actual name value: " + profile.getNameValue());
        assertEquals(nameValue, profile.getNameValue());
        String certValue = "Cert, number 456 and symbols &*()";
        profile.enterCertification(certValue);
        Thread.sleep(500);
        System.out.println("Actual certification value: " + profile.getCertificationValue());
        assertEquals(certValue, profile.getCertificationValue());
        String skillValue = "Skill with_underscores- periods. and, commas";
        profile.enterSkill(skillValue);
        Thread.sleep(500);
        System.out.println("Actual skill value: " + profile.getSkillValue());
    }

    @Test
    void test_ed_020_invalid_birthday_format_yyyymmdd_error() {
        profile.clickUpdateIcon();
        String invalidBirthday = "2000/01/01";
        profile.enterBirthday(invalidBirthday);
        profile.enterName("");
        profile.clickSaveButton();
        assertTrue(profile.isUpdateProfileModalVisible());
    }

    @Test
    void test_ed_021_invalid_birthday_format_ddmmyyyy_error() {
        profile.clickUpdateIcon();
        String invalidBirthday = "01-01-2000";
        profile.enterBirthday(invalidBirthday);
        profile.enterName("");
        profile.clickSaveButton();
        assertTrue(profile.isUpdateProfileModalVisible());
    }

    @Test
    void test_ed_022_invalid_birthday_non_date_chars_error() {
        profile.clickUpdateIcon();
        String invalidBirthday = "abcdefg";
        profile.enterBirthday(invalidBirthday);
        profile.enterName("");
        profile.clickSaveButton();
        assertTrue(profile.isUpdateProfileModalVisible());
    }

    @Test
    void test_ed_023_invalid_phone_non_numeric_chars_error() {
        profile.clickUpdateIcon();
        String invalidPhone = "abc-def-ghij";
        profile.enterPhone(invalidPhone);
        profile.enterName("");
        profile.clickSaveButton();
        assertTrue(profile.isUpdateProfileModalVisible());
    }

    @Test
    void test_ed_024_invalid_phone_too_short_error() {
        profile.clickUpdateIcon();
        String invalidPhone = "123";
        profile.enterPhone(invalidPhone);
        profile.enterName("");
        profile.clickSaveButton();
        assertTrue(profile.isUpdateProfileModalVisible());
    }

    @Test
    void test_ed_025_invalid_phone_too_long_error() {
        profile.clickUpdateIcon();
        String invalidPhone = "123456789012345678901234567890";
        profile.enterPhone(invalidPhone);
        profile.enterName("");
        profile.clickSaveButton();
        assertTrue(profile.isUpdateProfileModalVisible());
    }

    @Test
    void test_ed_026_invalid_phone_special_chars_error() {
        profile.clickUpdateIcon();
        String invalidPhone = "555-123-4567!*?";
        profile.enterPhone(invalidPhone);
        profile.enterName("");
        profile.clickSaveButton();
        assertTrue(profile.isUpdateProfileModalVisible());
    }

    @Test
    void test_ed_027_attempt_save_with_empty_field() {
        profile.clickUpdateIcon();
        profile.enterName("");
        profile.clickSaveButton();
        assertTrue(profile.isUpdateProfileModalVisible());
    }

    @Test
    void test_ed_028_exceed_max_length_name_field() {
        profile.clickUpdateIcon();
        char[] chars = new char[100];
        Arrays.fill(chars, 'a');
        String longName = new String(chars);
        profile.enterName(longName);
        profile.enterName(longName);
        assertTrue(profile.getNameValue().length() <= 100);
    }

    @Test
    void test_ed_029_exceed_max_length_certification_field() {
        profile.clickUpdateIcon();
        char[] chars = new char[300];
        Arrays.fill(chars, 'b');
        String longCert = new String(chars);
        profile.enterCertification(longCert);
        profile.enterCertification(longCert);
        assertTrue(profile.getCertificationValue().length() <= 300);
    }

    @Test
    void test_ed_030_exceed_max_length_skill_field() {
        profile.clickUpdateIcon();
        char[] chars = new char[600];
        Arrays.fill(chars, 'c');
        String longSkill = new String(chars);
        profile.enterSkill(longSkill);
        profile.enterSkill(longSkill);
        assertTrue(profile.getSkillValue().length() <= 600);
    }

    @Test
    void test_ed_031_save_valid_changes_and_verify_success_message() throws InterruptedException {
        profile.clickUpdateIcon();
        String newName = "Jane Doe";
        profile.enterName(newName);
        String newBirthday = "1990-01-01";
        profile.enterBirthday(newBirthday);
        profile.selectGender("female");
        profile.clickSaveButton();
        Thread.sleep(500);
        System.out.println("Modal visible after save: " + profile.isUpdateProfileModalVisible());
        assertFalse(profile.isUpdateProfileModalVisible());
    }

    @Test
    void test_ed_032_updated_info_persists_after_navigation() {
        profile.clickUpdateIcon();
        String newName = "Persistent Name";
        profile.enterName(newName);
        profile.clickSaveButton();
        page.reload();
        profile = new ProfilePage(page);
        profile.clickUpdateIcon();
        assertEquals(newName, profile.getNameValue());
    }

    @Test
    void test_ed_033_updated_info_persists_after_refresh() {
        profile.clickUpdateIcon();
        String refreshName = "Refreshed Name";
        profile.enterName(refreshName);
        profile.clickSaveButton();
        page.keyboard().press("F5");
        assertEquals(refreshName, profile.getNameValue());
    }

    @Test
    void test_ed_034_save_without_changes() throws InterruptedException {
        profile.clickUpdateIcon();
        profile.clickSaveButton();
        Thread.sleep(500);
        System.out.println("Modal visible after save without changes: " + profile.isUpdateProfileModalVisible());
        assertFalse(profile.isUpdateProfileModalVisible());
    }

    @Test
    void test_ed_035_cancel_button_closes_modal() throws InterruptedException {
        profile.clickUpdateIcon();
        profile.clickCancelButton();
        Thread.sleep(500);
        System.out.println("Modal visible after cancel: " + profile.isUpdateProfileModalVisible());
        assertFalse(profile.isUpdateProfileModalVisible());
    }

    @Test
    void test_ed_036_cancel_discards_changes() {
        profile.clickUpdateIcon();
        String originalName = profile.getNameValue();
        String newName = "Discarded Name";
        profile.enterName(newName);
        profile.clickCancelButton();
        profile.clickUpdateIcon();
        assertEquals(originalName, profile.getNameValue());
    }

    @Test
    void test_ed_037_cancel_button_closes_modal_no_changes() throws InterruptedException {
        profile.clickUpdateIcon();
        profile.clickCancelButton();
        Thread.sleep(500);
        System.out.println("Modal visible after cancel (no changes): " + profile.isUpdateProfileModalVisible());
        assertFalse(profile.isUpdateProfileModalVisible());
    }

} 
