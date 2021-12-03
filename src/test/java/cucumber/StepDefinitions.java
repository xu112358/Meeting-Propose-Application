package cucumber;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Instant;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Step definitions for Cucumber tests.
 */
public class StepDefinitions {
    private static final String ROOT_URL = "https://localhost:8443";

    private static int num_userevents  = 0;
    private static int num_events  = 0;
    private static int new_events  = 0;
    private static int rejected_events  = 0;
    private static int confirmed_events  = 0;
    private static long temp_time = 0;


    ChromeOptions handlingSSL = new ChromeOptions().setAcceptInsecureCerts(true);
    private final WebDriver driver = new ChromeDriver(handlingSSL);


    @Given("I am on the index page")
    public void i_am_on_the_index_page() {
        driver.get(ROOT_URL);
    }

    @When("I click the sign up button")
    public void I_click_the_sign_up_button() {
        driver.findElement(By.xpath("//*[@id=\"signup\"]")).click();
    }

    @Then("I should be on the sign up page")
    public void I_should_be_on_the_sign_up_page() {
        assertTrue(driver.getCurrentUrl().equalsIgnoreCase(ROOT_URL + "/signup"));
    }

    @Given("I am on the Sign Up page")
    public void i_am_on_the_Sign_Up_page() {
        driver.get(ROOT_URL + "/signup");
    }

    @When("I enter {string} in Username field")
    public void i_enter_in_Username_field(String string) {
        driver.findElement(By.cssSelector("#username")).sendKeys(string);
    }
    @When("I enter {string} in Password field")
    public void i_enter_in_Password_field(String string) {
        driver.findElement(By.cssSelector("#password")).sendKeys(string);
    }
    @When("I enter {string} in Re-Password field")
    public void i_enter_in_Re_Password_field(String string) {
        driver.findElement(By.cssSelector("#re_password")).sendKeys(string);
    }
    @When("I enter {string} in First Name field")
    public void i_enter_in_First_Name_field(String string) {
        driver.findElement(By.cssSelector("#fname")).sendKeys(string);
    }
    @When("I enter {string} in Last Name field")
    public void i_enter_in_Last_Name_field(String string) {
        driver.findElement(By.cssSelector("#lname")).sendKeys(string);
    }
    @When("I press create account button")
    public void i_press_create_account_button() {
        driver.findElement(By.cssSelector("#signup")).click();
    }
    @Then("I should sign up unsuccesfully")
    public void i_should_sign_up_unsuccesfully() {
        String text = driver.findElement(By.cssSelector("#warning")).getText();
        assertEquals("Username is taken. Try another one.", text);
    }
    @Then("I should sign up succesfully")
    public void i_should_sign_up_succesfully() {
        String text = driver.findElement(By.cssSelector("#success")).getText();
        assertEquals("Sign Up Successfully!", text);
    }
    @Then("Signup Password Mismatch")
    public void password_mismatch() {
        String text = driver.findElement(By.cssSelector("#warning")).getText();
        assertEquals("Two passwords are not matching!", text);
    }
    @Given("I am on the sign in page")
    public void i_am_on_the_sign_in_page() {
        driver.get(ROOT_URL + "/signin");
    }
    @When("I click sign in button")
    public void i_click_sign_in_button() {
        driver.findElement(By.cssSelector("#signin")).click();
    }
    @When("I click Log Out button")
    public void click_logout_button() {
        //driver.findElement(By.linkText("../logout")).click();
        driver.findElement(By.xpath("//*[@id=\"navbarNav\"]/ul/li[4]/a")).click();
    }
    @Then("I should log in successfully")
    public void i_should_log_in_successfully() {
        String text = driver.findElement(By.cssSelector("#add-button")).getText();
        assertEquals("Add Event", text);
    }
    @Then("username and password do not match log in unsuccessful")
    public void username_and_password_do_not_match_log_in_unsuccessful() {
        String text = driver.findElement(By.cssSelector("#warning")).getText();
        assertEquals("Username and password do not match!", text);
    }
    @Then("username or password is empty")
    public void username_or_password_is_empty() {
        String text = driver.findElement(By.cssSelector("#warning")).getText();
        assertEquals("Username or Password is empty!", text);
    }
    @Then("Fill up all inputs")
    public void fill_all_inputs() {
        String text = driver.findElement(By.cssSelector("#warning")).getText();
        assertEquals("You need to fill up all the inputs!", text);
    }
    @Then("I am on the logout page")
    public void on_logout_page() {
        String text = driver.findElement(By.cssSelector("#warning")).getText();
        assertEquals("You need to log in first!", text);
    }

    @Given("I am on the Search Events page")
    public void i_am_on_the_Search_Events_page() {
        // Write code here that turns the phrase above into concrete actions
        driver.get(ROOT_URL + "/SearchEvents");
    }

    @When("I click the targeted keyword field")
    public void i_click_the_targeted_keyword_field() {
        // Write code here that turns the phrase above into concrete actions
        String text = driver.findElement(By.cssSelector("#check_mark")).getText();
//      assertEquals("Add Event", text);
    }
    @When("I enter start date in the start date field")
    public void i_enter_start_date_in_the_start_date_field() {
        // Write code here that turns the phrase above into concrete actions
        String text = driver.findElement(By.cssSelector("#input-group date form_date")).getText();
    }
    @When("I enter end date in the end date field")
    public void i_enter_end_date_in_the_end_date_field() {
        // Write code here that turns the phrase above into concrete actions
        String text = driver.findElement(By.cssSelector("#input-group date form_date")).getText();
    }
    @Then("I will be listed all the events that satisfied my above requirement")
    public void i_will_be_listed_all_the_events_that_satisfied_my_above_requirement() {
        // Write code here that turns the phrase above into concrete actions
        String text = driver.findElement(By.className("event")).getText();
        assertNotNull(text);
    }

    //homepage acceptance tests
    @Given("I am on the homepage and signed in successfully")
    public void i_am_on_the_homepage_and_signed_in_successfully() {
        // Write code here that turns the phrase above into concrete actions
        driver.get(ROOT_URL + "/home");
        driver.findElement(By.cssSelector("#username")).sendKeys("root");
        driver.findElement(By.cssSelector("#password")).sendKeys("123");
        driver.findElement(By.cssSelector("#signin")).click();
    }
    @When("I click the date of the event I want to create")
    public void i_click_the_date_of_the_event_I_want_to_create() {
        // Write code here that turns the phrase above into concrete actions
        String text = driver.findElement(By.className("calendar-container")).getText();
        assertNotNull(text);
    }
    @When("click the add event button")
    public void click_the_add_event_button() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.xpath("//*[@id=\"add-button\"]")).click();
    }
    @Then("I should see a form to let me enter event details")
    public void i_should_see_a_form_to_let_me_enter_event_details() {
        // Write code here that turns the phrase above into concrete actions
        String text = driver.findElement(By.className("dialog-header")).getText();
        assertEquals("Add New Event", text);
    }

    @Given("I am on the homepage and the event form has poped up and signed in successfully")
    public void i_am_on_the_homepage_and_the_event_form_has_poped_up_and_signed_in_successfully() {
        // Write code here that turns the phrase above into concrete actions
        driver.get(ROOT_URL + "/home");
        driver.findElement(By.cssSelector("#username")).sendKeys("root");
        driver.findElement(By.cssSelector("#password")).sendKeys("123");
        driver.findElement(By.cssSelector("#signin")).click();
        driver.findElement(By.xpath("//*[@id=\"add-button\"]")).click();
    }
    @When("I enter the name of the event {string}")
    public void i_enter_the_name_of_the_event(String string) {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.cssSelector("#name")).sendKeys(string);
    }

    @When("I enter the {int} people I'd like to invite")
    public void i_enter_the_people_I_d_like_to_invite(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.cssSelector("#count")).sendKeys(int1.toString());
    }
    @When("I enter OK")
    public void i_enter_OK() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.cssSelector("#ok-button")).click();
    }
    @Then("I should see a event info about {string} with {int} people invited")
    public void i_should_see_a_event_info_about_with_people_invited(String string, Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        String text = driver.findElement(By.className("event-name")).getText();
        assertEquals(string+":", text);
        String text2 = driver.findElement(By.className("event-count")).getText();
        assertEquals(int1.toString()+" Invited", text2);
    }

    @When("I enter the cancel button")
    public void i_enter_the_cancel_button() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.cssSelector("#cancel-button")).click();
    }

    @Then("I should see a event info unchanged")
    public void i_should_see_a_event_info_unchanged() {
        String text2 = driver.findElement(By.className("event-card")).getText();
        assertNotNull(text2);
    }

    @Given("I am on the propose event page")
    public void i_am_on_the_propose_event_page() {
        // Write code here that turns the phrase above into concrete actions
        temp_time = Instant.now().getEpochSecond();
        driver.get(ROOT_URL + "/home");
        driver.findElement(By.cssSelector("#username")).sendKeys("root");
        driver.findElement(By.cssSelector("#password")).sendKeys("123");
        driver.findElement(By.cssSelector("#signin")).click();
        driver.get(ROOT_URL + "/proposeEvent");
    }

    @When("I enter {string} in Username search field")
    public void i_enter_in_the_username_search_field(String string) {
        driver.findElement(By.cssSelector("#searchusername")).sendKeys(string);
    }

    @When("I enter {string} in the Keyword field")
    public void i_enter_in_the_keyword_field(String string) {

        driver.findElement(By.cssSelector("#keyword")).sendKeys(string);
    }

    @When("I enter {string} in the Location field")
    public void i_enter_in_the_location_field(String string) {
        driver.findElement(By.cssSelector("#city-name")).sendKeys(string);
    }

    @When("I enter {string} in the GroupDate name field")
    public void i_enter_in_the_groupdate_name_field(String string) {
        driver.findElement(By.cssSelector("#groupDate_name")).sendKeys(string + String.valueOf(temp_time));
    }

    @When("I select a date")
    public void i_select_a_date() {
        driver.findElement(By.cssSelector("#choose-date")).sendKeys("01232019");
    }

    @When("I click the Username Add button")
    public void i_click_the_username_add_button() {
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.findElement(By.cssSelector("#username_add")).click();

    }

    @When("I click the Username Add button twice")
    public void i_click_the_username_add_button_twice() {
        driver.findElement(By.cssSelector("#username_add")).click();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.findElement(By.cssSelector("#username_add")).click();
    }

    @When("I click event search button")
    public void i_click_event_event_search_button() {
        driver.findElement(By.cssSelector("#searchbutton")).click();
    }

    @When("I click the propose event button")
    public void i_click_the_propose_event_button() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        driver.findElement(By.cssSelector("#propose-events")).click();
    }

    @When("I click add event")
    public void i_click_add_event() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver,10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".add-event")));
        TimeUnit.SECONDS.sleep(2);
        driver.findElement(By.cssSelector(".add-event")).click();
    }

    @Then("I successfully proposed the groupdate")
    public void i_successfully_proposed_the_groupdate() {
        WebDriverWait wait = new WebDriverWait(driver,30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#proposeEvent_success")));
        String temp = driver.findElement(By.cssSelector("#proposeEvent_success")).getText();
        assertEquals(temp, "Successfully Propose the GroupDate!");
    }

    @Then("input text is empty")
    public void input_text_is_empty() {
        WebDriverWait wait = new WebDriverWait(driver,30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#errmsg")));
        String temp = driver.findElement(By.cssSelector("#errmsg")).getText();
        assertEquals(temp, "There is input text is empty!");
    }

    @Then("sender users list or groupdate name is empty")
    public void sender_users_list_or_groupdate_name_is_empty() {
        WebDriverWait wait = new WebDriverWait(driver,30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#proposeEvent_errmsg")));
        String temp = driver.findElement(By.cssSelector("#proposeEvent_errmsg")).getText();
        assertEquals(temp, "Sender Users List or Events List or GroupDate Name is Empty!");
    }

    @Then("username already exists in the sender list")
    public void username_already_exists_in_the_sender_list() {
        WebDriverWait wait = new WebDriverWait(driver,30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#username_errmsg")));
        String temp = driver.findElement(By.cssSelector("#username_errmsg")).getText();
        assertEquals(temp, "Username already exists in the sender list!");
    }

    @Given("I am on the user setting page and signed in as root")
    public void i_am_on_the_user_setting_page_and_signed_in_as_root() {
        // Write code here that turns the phrase above into concrete actions

        driver.get(ROOT_URL + "/signup");
        driver.findElement(By.cssSelector("#username")).sendKeys("root1");
        driver.findElement(By.cssSelector("#password")).sendKeys("123");
        driver.findElement(By.cssSelector("#re_password")).sendKeys("123");
        driver.findElement(By.cssSelector("#signup")).click();
        driver.get(ROOT_URL + "/home");
        driver.findElement(By.cssSelector("#username")).sendKeys("root");
        driver.findElement(By.cssSelector("#password")).sendKeys("123");
        driver.findElement(By.cssSelector("#signin")).click();
        driver.get(ROOT_URL + "/setting");
    }

    @When("I enter {string} in the Username Search form")
    public void i_enter_in_the_Username_Search_form(String string) {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.cssSelector("#searchusername")).sendKeys(string);
    }

    @When("{string} is not in the blocked user list")
    public void is_not_in_the_blocked_user_list(String string) {
        // Write code here that turns the phrase above into concrete actions
        String text = driver.findElement(By.cssSelector("#blockedUsers")).getText();
        assertNotSame(string, text);
    }

    @When("I click the Blocked This Username button")
    public void i_click_the_Blocked_This_Username_button() {
        // Write code here that turns the phrase above into concrete actions
        WebDriverWait wait = new WebDriverWait(driver,30);
        driver.findElement(By.cssSelector("#username_add")).click();
    }

    @Then("I should see {string} appear on the Username list")
    public void i_should_see_appear_on_the_Username_list(String string) {
        // Write code here that turns the phrase above into concrete actions
        WebDriverWait wait = new WebDriverWait(driver,30);
        String text = driver.findElement(By.cssSelector("#blockedUsers:first-of-type:first-of-type")).getText();
        assertNotSame("root", text);
    }

    @When("{string} is in the blocked user list")
    public void is_in_the_blocked_user_list(String string) {
        // Write code here that turns the phrase above into concrete actions
        WebDriverWait wait = new WebDriverWait(driver,30);
        String text = driver.findElement(By.cssSelector("#blockedUsers:first-of-type:first-of-type")).getText();
        assertNotSame("root", text);
    }

    @Then("I should see an error message of root1 is already on your blocked list")
    public void i_should_see_an_error_message_of_root1_is_already_on_your_blocked_list() {
        // Write code here that turns the phrase above into concrete actions
        WebDriverWait wait = new WebDriverWait(driver,30);
        String text = driver.findElement(By.cssSelector("#username_errmsg")).getText();
        assertEquals("root1 is already on your blocked list", text);
    }

    @Then("I should see an error message of You cannot block yourself!")
    public void i_should_see_an_error_message_of_You_cannot_block_yourself() {
        // Write code here that turns the phrase above into concrete actions
        String text = driver.findElement(By.cssSelector("#username_errmsg")).getText();
        assertEquals("You cannot block yourself!", text);
    }

    @When("I enter nothing in the Username Search form")
    public void i_enter_nothing_in_the_Username_Search_form() {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("I should see an error message of Input Username is Empty!")
    public void i_should_see_an_error_message_of_Input_Username_is_Empty() {
        // Write code here that turns the phrase above into concrete actions
        String text = driver.findElement(By.cssSelector("#username_errmsg")).getText();
        assertEquals("Input Username is Empty!", text);
    }

    @Given("I am on the user setting page and signed in as {string} and {string} is on my blocked user list")
    public void i_am_on_the_user_setting_page_and_signed_in_as_and_is_on_my_blocked_user_list(String string, String string2) {
        // Write code here that turns the phrase above into concrete actions
        driver.get(ROOT_URL + "/home");
        driver.findElement(By.cssSelector("#username")).sendKeys(string);
        driver.findElement(By.cssSelector("#password")).sendKeys("123");
        driver.findElement(By.cssSelector("#signin")).click();
        driver.get(ROOT_URL + "/setting");
        driver.findElement(By.cssSelector("#searchusername")).sendKeys(string2);
        driver.findElement(By.cssSelector("#username_add")).click();
    }

    @When("I click the remove button of {string}")
    public void i_click_the_remove_button_of(String string) {
        // Write code here that turns the phrase above into concrete actions
        WebDriverWait wait = new WebDriverWait(driver,30);
        driver.findElement(By.cssSelector("button.btn.btn-outline-danger.delete-btn.removeUser")).click();
    }
    @When("I see a warning saying {string}")
    public void i_see_a_warning_saying(String string) {
        // Write code here that turns the phrase above into concrete actions
        String text = driver.switchTo().alert().getText();
        assertEquals(string, text);
    }
    @When("I click OK button")
    public void i_click_OK_button() {
        // Write code here that turns the phrase above into concrete actions
        driver.switchTo().alert().accept();
    }
    @Then("root1 will be deleted from the blocked user list")
    public void root1_will_be_deleted_from_the_blocked_user_list() {
        // Write code here that turns the phrase above into concrete actions
        String text = driver.findElement(By.cssSelector("#blockedUsers:first-of-type:first-of-type")).getText();
        assertNotSame("root1 Remove", text);
    }
    @When("I click cancel button")
    public void i_click_cancel_button() {
        // Write code here that turns the phrase above into concrete actions
        driver.switchTo().alert().dismiss();
    }
    @Then("root1 will still be on the blocked user list")
    public void root1_will_still_be_on_the_blocked_user_list() {
        // Write code here that turns the phrase above into concrete actions
        String text = driver.findElement(By.cssSelector("#blockedUsers:first-of-type:first-of-type")).getText();
        assertEquals("root1 Remove", text);
    }

    @Given("I am on the user settings page and signed in as root")
    public void i_am_on_the_user_settings_page_and_signed_in_as_root() {
        // Write code here that turns the phrase above into concrete actions
        driver.get(ROOT_URL + "/signin");
        driver.findElement(By.cssSelector("#username")).sendKeys("root");
        driver.findElement(By.cssSelector("#password")).sendKeys("123");
        driver.findElement(By.cssSelector("#signin")).click();
        driver.get(ROOT_URL + "/setting");
    }

    @When("I enter nothing for the select start start date")
    public void i_enter_nothing_for_the_select_start_date() {
        // Write code here that turns the phrase above into concrete actions
    }

    @When("I select an end date from the end date calendar")
    public void i_select_an_end_date_from_the_end_date_calendar() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.cssSelector("#end-date")).sendKeys("01232019");
    }

    @When("When I select a start date from the start date calendar")
    public void i_select_a_start_date_from_the_start_date_calendar() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.cssSelector("#start-date")).sendKeys("01232019");
    }

    @When("I enter nothing for the select start end date")
    public void i_enter_nothing_for_the_select_end_date() {
        // Write code here that turns the phrase above into concrete actions
    }

    @When("I click on the Add Date Range button")
    public void i_click_on_the_add_date_range_button() {
        // Write code here that turns the phrase above into concrete actions
        WebDriverWait wait = new WebDriverWait(driver,30);
        driver.findElement(By.cssSelector("#daterange_add")).click();
    }

    @Then("I should see an error message of Start Date or End Date is Empty")
    public void i_should_see_an_error_message_of_start_date_or_end_date_is_empty() {
        // Write code here that turns the phrase above into concrete actions
        String text = driver.findElement(By.cssSelector("#daterange_errmsg")).getText();
        assertEquals("Start Date or End Date is Empty!", text);
    }

    @When("I select a date from the select start date calendar")
    public void i_select_a_date_from_the_select_start_date_calendar() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.cssSelector("#start-date")).sendKeys("01232019");
    }

    @When("I select a date from the select end date calendar")
    public void i_select_a_date_from_the_select_end_date_calendar() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.cssSelector("#end-date")).sendKeys("01232019");
    }

    @When("I click on the add date range button below")
    public void i_click_on_the_add_date_range_button_below() {
        // Write code here that turns the phrase above into concrete actions
        WebDriverWait wait = new WebDriverWait(driver,30);
        driver.findElement(By.cssSelector("#daterange_add")).click();
    }

    @Then("my unavailable date range will appear on the unavailable date range list")
    public void my_unavailable_date_range_will_appear_on_the_unavailable_date_range_list() {
        // Write code here that turns the phrase above into concrete actions
        WebDriverWait wait = new WebDriverWait(driver,30);
        String text = driver.findElement(By.cssSelector("body > div:nth-child(4) > div > div:nth-child(2) > div:nth-child(1) > div > table")).getText();
        assertNotNull(text);
    }

    @When("I click on the remove button of the date range selected")
    public void i_click_on_the_remove_button_of_the_date_range_selected() {
        // Write code here that turns the phrase above into concrete actions
        WebDriverWait wait = new WebDriverWait(driver,30);
        driver.findElement(By.cssSelector("body > div:nth-child(4) > div > div:nth-child(2) > div:nth-child(1) > div > table > tbody > tr > td:nth-child(3) > a")).click();
    }

    @Then("this unavailable date range will be deleted from the unavailable date range list")
    public void my_unavailable_date_range_will_be_deleted_from_the_unavailable_date_range_list(String string) {
        // Write code here that turns the phrase above into concrete actions
        String text = driver.findElement(By.cssSelector("body > div:nth-child(4) > div > div:nth-child(2) > div:nth-child(1) > div > table")).getText();
        assertNotSame(string, text);
    }

    @Given("I am on the sign in page and already signed in as root")
    public void i_am_on_the_sign_in_page_and_already_signed_in_as_root() {
        // Write code here that turns the phrase above into concrete actions
        driver.get(ROOT_URL + "/signin");
        driver.findElement(By.cssSelector("#username")).sendKeys("root");
        driver.findElement(By.cssSelector("#password")).sendKeys("123");
        driver.findElement(By.cssSelector("#signin")).click();
    }

    @When("I stay on the page passively for {int} seconds")
    public void i_stay_on_the_page_passively_for_seconds(Integer int1) throws InterruptedException {
        // Write code here that turns the phrase above into concrete actions
        TimeUnit.SECONDS.sleep(int1);
        throw new io.cucumber.java.PendingException();
    }

    @Then("I am still logged in")
    public void i_am_still_logged_in() {
        // Write code here that turns the phrase above into concrete actions
        driver.get(ROOT_URL + "/setting");
        assertEquals(driver.getCurrentUrl(), ROOT_URL +"/setting");
    }

//    @When("I stay on the page passively for {int} seconds")
//    public void i_stay_on_the_page_passively_for_int_seconds(Integer int2) {
//        // Write code here that turns the phrase above into concrete actions
//        driver.manage().timeouts().implicitlyWait(61, TimeUnit.SECONDS);
//        throw new io.cucumber.java.PendingException();
//    }

    @Then("I am automatically logged out")
    public void i_am_automatically_logged_out() {
        // Write code here that turns the phrase above into concrete actions
        driver.get(ROOT_URL + "/setting");
        assertNotEquals(driver.getCurrentUrl(), ROOT_URL +"/setting");
    }

    @Given("I am on the Sent GroupDates main page")
    public void i_am_on_the_Sent_GroupDates_main_page() {
        // Write code here that turns the phrase above into concrete actions
        driver.get(ROOT_URL + "/signin");
        driver.findElement(By.cssSelector("#username")).sendKeys("root");
        driver.findElement(By.cssSelector("#password")).sendKeys("123");
        driver.findElement(By.cssSelector("#signin")).click();
        driver.get(ROOT_URL + "/list-sent-invite");
    }
    @Then("I should see a list of events with their date and status")
    public void i_should_see_a_list_of_events_with_their_date_and_status() {
        // Write code here that turns the phrase above into concrete actions
        String groupdate_name = driver.findElement(By.cssSelector("body > div.padding > div.container-fluid > div.row > div.col-12.d-flex.justify-content-center > div.center > table.table.table-hover.table-responsive.mt-4 > thead > tr > th:nth-child(1)")).getText();
        String groupdate_date = driver.findElement(By.cssSelector("body > div.padding > div.container-fluid > div.row > div.col-12.d-flex.justify-content-center > div.center > table.table.table-hover.table-responsive.mt-4 > thead > tr > th:nth-child(2)")).getText();
        String groupdate_status = driver.findElement(By.cssSelector("body > div.padding > div.container-fluid > div.row > div.col-12.d-flex.justify-content-center > div.center > table.table.table-hover.table-responsive.mt-4 > thead > tr > th:nth-child(3)")).getText();

        assertEquals("GroupDate Name", groupdate_name);
        assertEquals("Date", groupdate_date);
        assertEquals("Status", groupdate_status);
    }

    @When("I click into one of the event")
    public void i_click_into_one_of_the_event() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.cssSelector("body > div.padding > div.container-fluid > div.row > div.col-12.d-flex.justify-content-center > div.center > table.table.table-hover.table-responsive.mt-4 > tbody > tr > td >a")).click();
    }
    @Then("I should see the preference and availability of the receivers about the events.")
    public void i_should_see_the_preference_and_availability_of_the_receivers_about_the_events() {
        // Write code here that turns the phrase above into concrete actions
        String Receiver = driver.findElement(By.cssSelector("body > div.padding > div.container-fluid > div.row > div.col-12.d-flex.justify-content-center > div.center > table.table.table-hover.table-responsive.mt-4 > thead > tr > th:nth-child(1)")).getText();
        String Event_Name = driver.findElement(By.cssSelector("body > div.padding > div.container-fluid > div.row > div.col-12.d-flex.justify-content-center > div.center > table.table.table-hover.table-responsive.mt-4 > thead > tr > th:nth-child(2)")).getText();
        String Event_Date = driver.findElement(By.cssSelector("body > div.padding > div.container-fluid > div.row > div.col-12.d-flex.justify-content-center > div.center > table.table.table-hover.table-responsive.mt-4 > thead > tr > th:nth-child(3)")).getText();
        String Preference = driver.findElement(By.cssSelector("body > div.padding > div.container-fluid > div.row > div.col-12.d-flex.justify-content-center > div.center > table.table.table-hover.table-responsive.mt-4 > thead > tr > th:nth-child(4)")).getText();
        String Availability = driver.findElement(By.cssSelector("body > div.padding > div.container-fluid > div.row > div.col-12.d-flex.justify-content-center > div.center > table.table.table-hover.table-responsive.mt-4 > thead > tr > th:nth-child(5)")).getText();

        assertEquals("Receiver", Receiver);
        assertEquals("Event Name", Event_Name);
        assertEquals("Event Date", Event_Date);
        assertEquals("Preference", Preference);
        assertEquals("Availability", Availability);
    }

    @When("I click finalize button")
    public void i_click_finalize_button() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.id("finalize")).click();
    }
    @When("I see the stats of the event")
    public void i_see_the_stats_of_the_event() {
        // Write code here that turns the phrase above into concrete actions
        String stats = driver.findElement(By.cssSelector("body > div#shadow > div > div#stats_result > h1")).getText();
        assertEquals("Stats about Events",stats);
    }
    @When("I click yes")
    public void i_click_yes() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement((By.id("finalize_yes"))).click();
    }
    @Then("I should see the finalized event.")
    public void i_should_see_the_finalized_event() {
        // Write code here that turns the phrase above into concrete actions
        String Event_Name = driver.findElement(By.cssSelector("body > div.padding > div.container-fluid > div.row > div.col-12.d-flex.justify-content-center > div.center > table.table.table-hover.table-responsive.mt-4 > thead > tr > th:nth-child(2)")).getText();
        assertEquals("Event Name", Event_Name);
    }

    @When("I click No")
    public void i_click_No() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement((By.id("finalize_no"))).click();
    }

    @Then("I will be direct back to the sent group date page")
    public void i_will_be_direct_back_to_the_sent_group_date_page() {
        // Write code here that turns the phrase above into concrete actions
        String strUrl = driver.getCurrentUrl();
        assertTrue(strUrl.contains(ROOT_URL + "/list-sent-invite-event"));
    }
    @When("I click confirm button")
    public void i_click_confirm_button() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.cssSelector("body > div.padding > div.container-fluid > div.row > div.col-4.mt-3.mb-3:nth-child(2)  > a.btn.btn-info.mr-5.responsive-width.save")).click();
    }

    @When("I see the warning saying Are you sure you want to Confirm this GroupDate?")
    public void i_see_the_warning_saying_Are_you_sure_you_want_to_Confirm_this_GroupDate() {
        // Write code here that turns the phrase above into concrete actions
        String text = driver.switchTo().alert().getText();
        assertEquals("Are you sure you want to Confirm this GroupDate?", text);
    }
    @When("I click OK")
    public void i_click_OK() {
        // Write code here that turns the phrase above into concrete actions
        driver.switchTo().alert().accept();
    }
    @Then("I can see the event pop on the set groupdate page")
    public void i_can_see_the_event_pop_on_the_set_groupdate_page() {
        // Write code here that turns the phrase above into concrete actions
        String strUrl = driver.getCurrentUrl();
        assertTrue(strUrl.contains(ROOT_URL + "/confirm_receive_invite"));
    }
    @When("I click Back button")
    public void i_click_Back_button() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.cssSelector("body > div.padding > div.container-fluid > div.row > div.col-4.mt-3.mb-3:nth-child(1)  > div.float-right > a.btn.btn-primary.mr-5.responsive-width")).click();
    }
    @Then("I can will be direct back to the receive-groupDates page")
    public void i_can_will_be_direct_back_to_the_receive_groupDates_page() {
        // Write code here that turns the phrase above into concrete actions
        String strUrl = driver.getCurrentUrl();
        assertEquals(ROOT_URL + "/list-sent-invite",strUrl);
    }

    @When("I click the x next to the added user")
    public void i_click_the_x_next_to_the_added_user() {
        WebDriverWait wait = new WebDriverWait(driver,2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".remove")));
        driver.findElement(By.cssSelector(".remove")).click();
    }

    @When("I click the x next to the added event")
    public void i_click_the_x_next_to_the_added_event() {
        WebDriverWait wait = new WebDriverWait(driver,2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".remove")));
        driver.findElement(By.cssSelector(".remove")).click();
    }

    @Then("the user list should be empty")
    public void then_the_user_list_should_be_empty() {
        String temp = driver.findElement(By.cssSelector("#add-users-list")).getText();
        assertEquals(temp, "");
    }

    @Then("the event list should be empty")
    public void then_the_event_list_should_be_empty() {
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        String temp = driver.findElement(By.cssSelector("#events_list")).getText();
        assertEquals(temp, "Propose Events:");
    }

    @When("I go to the received group date page")
    public void i_go_to_the_received_group_date_page() {
        // Write code here that turns the phrase above into concrete actions
        driver.get(ROOT_URL + "/home");
        driver.findElement(By.cssSelector("#username")).sendKeys("root");
        driver.findElement(By.cssSelector("#password")).sendKeys("123");
        driver.findElement(By.cssSelector("#signin")).click();
        driver.get(ROOT_URL + "/receive-groupDates");
    }

    @Then("I should see the {string} event with the status new")
    public void i_should_see_the_event_with_the_status_new(String string) {
        String temp = driver.findElement(By.cssSelector("td")).getText();
        assertEquals(temp, string);
    }

    @When("I logout")
    public void i_logout() {
        // Write code here that turns the phrase above into concrete actions
        driver.get(ROOT_URL + "/logout");
    }

    @Given("I am on the new received event page")
    public void i_am_on_the_new_received_event_page() {
        driver.get(ROOT_URL + "/home");
        driver.findElement(By.cssSelector("#username")).sendKeys("root");
        driver.findElement(By.cssSelector("#password")).sendKeys("123");
        driver.findElement(By.cssSelector("#signin")).click();
        driver.get(ROOT_URL + "/receive-groupDates");

        num_events = driver.findElements(By.cssSelector("tr")).size();
        new_events  = 0;
        rejected_events  = 0;
        confirmed_events  = 0;
        for(int i = 0; i < driver.findElements(By.cssSelector("td")).size(); i++) {
            if(driver.findElements(By.cssSelector("td")).get(i).getText().equalsIgnoreCase("New")) {
                new_events++;
            } else if(driver.findElements(By.cssSelector("td")).get(i).getText().equalsIgnoreCase("Rejected")) {
                rejected_events++;
            } else if(driver.findElements(By.cssSelector("td")).get(i).getText().equalsIgnoreCase("Confirmed")) {
                confirmed_events++;
            }
        }
    }


    @When("I login and go to the send proposal page")
    public void i_login_and_go_to_the_send_proposal_page() {
        // Write code here that turns the phrase above into concrete actions
        driver.get(ROOT_URL + "/home");
        driver.findElement(By.cssSelector("#username")).sendKeys("root1");
        driver.findElement(By.cssSelector("#password")).sendKeys("123");
        driver.findElement(By.cssSelector("#signin")).click();
        driver.get(ROOT_URL + "/proposeEvent");
    }

    @Then("the new event should be there")
    public void the_new_event_should_be_there() {
        // Write code here that turns the phrase above into concrete actions
        int temp = driver.findElements(By.cssSelector("tr")).size();
        assertEquals(temp, num_events+1);
    }
//    driver.get(ROOT_URL);



    @When("I click an event")
    public void i_click_an_event() {
        // Write code here that turns the phrase above into concrete actions
        int temp = driver.findElements(By.cssSelector("a")).size();
        driver.findElement(By.partialLinkText("new_event_test")).click();
    }

    @When("I click a event")
    public void i_click_a_event() {
        // Write code here that turns the phrase above into concrete actions
        int temp = driver.findElements(By.cssSelector("a")).size();
        driver.findElements(By.cssSelector("a")).get(7).click();
    }



    @When("I click the accept button")
    public void i_click_the_accept_button() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.partialLinkText("Confirm")).click();
    }

    @When("I click the reject button")
    public void i_click_the_reject_button() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.partialLinkText("Reject")).click();
    }



    @Then("the group date gets accepted")
    public void the_group_date_gets_accepted() {
        // Write code here that turns the phrase above into concrete actions
        driver.get(ROOT_URL + "/receive-groupDates");
        int temp_new_events  = 0;
        int temp_rejected_events  = 0;
        int temp_confirmed_events  = 0;
        for(int i = 0; i < driver.findElements(By.cssSelector("td")).size(); i++) {
            if(driver.findElements(By.cssSelector("td")).get(i).getText().equalsIgnoreCase("New")) {
                temp_new_events++;
            } else if(driver.findElements(By.cssSelector("td")).get(i).getText().equalsIgnoreCase("Rejected")) {
                temp_rejected_events++;
            } else if(driver.findElements(By.cssSelector("td")).get(i).getText().equalsIgnoreCase("Confirmed")) {
                temp_confirmed_events++;
            }
        }
        assertEquals(temp_confirmed_events, confirmed_events+1);
        new_events = temp_new_events;
        rejected_events = temp_rejected_events;
        confirmed_events = temp_confirmed_events;
    }



    @Then("the group date gets rejected")
    public void the_group_date_gets_rejected() {
        // Write code here that turns the phrase above into concrete actions
        driver.get(ROOT_URL + "/receive-groupDates");
        int temp_new_events  = 0;
        int temp_rejected_events  = 0;
        int temp_confirmed_events  = 0;
        for(int i = 0; i < driver.findElements(By.cssSelector("td")).size(); i++) {
            if(driver.findElements(By.cssSelector("td")).get(i).getText().equalsIgnoreCase("New")) {
                temp_new_events++;
            } else if(driver.findElements(By.cssSelector("td")).get(i).getText().equalsIgnoreCase("Rejected")) {
                temp_rejected_events++;
            } else if(driver.findElements(By.cssSelector("td")).get(i).getText().equalsIgnoreCase("Confirmed")) {
                temp_confirmed_events++;
            }
        }
        assertEquals(temp_rejected_events, rejected_events+1);
        new_events = temp_new_events;
        rejected_events = temp_rejected_events;
        confirmed_events = temp_confirmed_events;
    }

    @When("I update my preference")
    public void i_update_my_preference() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.cssSelector(".preference")).sendKeys("2");
    }


    @When("I update my availability")
    public void i_update_my_availability() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.cssSelector(".availability")).sendKeys("maybe");
    }
    @When("I click back")
    public void i_click_back() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.partialLinkText("Back")).click();
    }
    @Then("my preference of the group date should get updated")
    public void my_preference_of_the_group_date_should_get_updated() {
        // Write code here that turns the phrase above into concrete actions
//        WebDriverWait wait = new WebDriverWait(driver,30);
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("")));
        assertEquals(driver.findElement(By.cssSelector(".availability")).getAttribute("value"), "maybe");
        assertEquals(driver.findElement(By.cssSelector(".preference")).getAttribute("value"), "2");
    }
    @When("I clear the Username search field")
    public void i_clear_the_Username_search_field() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.cssSelector("#searchusername")).clear();
    }

    @Given("I am on the user setting page and signed in as {string} and {string} has blocked me")
    public void i_am_on_the_user_setting_page_and_signed_in_as_and_has_blocked_me(String string, String string2) {
        // Write code here that turns the phrase above into concrete actions
        //sign up string
        driver.get(ROOT_URL + "/signup");
        driver.findElement(By.cssSelector("#username")).sendKeys(string);
        driver.findElement(By.cssSelector("#password")).sendKeys("123");
        driver.findElement(By.cssSelector("#re_password")).sendKeys("123");
        driver.findElement(By.cssSelector("#signup")).click();
        //sign up string2
        driver.get(ROOT_URL + "/signup");
        driver.findElement(By.cssSelector("#username")).sendKeys(string2);
        driver.findElement(By.cssSelector("#password")).sendKeys("123");
        driver.findElement(By.cssSelector("#re_password")).sendKeys("123");
        driver.findElement(By.cssSelector("#signup")).click();
       //login string 2
        driver.get(ROOT_URL + "/home");
        driver.findElement(By.cssSelector("#username")).sendKeys(string2);
        driver.findElement(By.cssSelector("#password")).sendKeys("123");
        driver.findElement(By.cssSelector("#signin")).click();
        //block string1 by string2
        driver.get(ROOT_URL + "/setting");
        driver.findElement(By.cssSelector("#searchusername")).sendKeys(string);
        driver.findElement(By.cssSelector("#username_add")).click();
        //log out
        driver.findElement(By.cssSelector("#navbarNav > ul.navbar-nav > li.nav-item:nth-child(6) > a.nav-link")).click();
        //login in as string
        driver.get(ROOT_URL + "/home");
        driver.findElement(By.cssSelector("#username")).sendKeys(string);
        driver.findElement(By.cssSelector("#password")).sendKeys("123");
        driver.findElement(By.cssSelector("#signin")).click();
        //to user setting
        driver.get(ROOT_URL + "/setting");
    }

    @When("I search {string} in the username search form")
    public void i_search_in_the_username_search_form(String string) {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.cssSelector("#searchusername")).sendKeys(string);
    }
    @Then("I can see the fiona has blocked me")
    public void i_can_see_the_fiona_has_blocked_me() {
        // Write code here that turns the phrase above into concrete actions
        String text = driver.findElement(By.cssSelector("#result > ul > li")).getText();
        assertEquals("fiona/ blocked you", text);
    }

    @When("I clear the Keyword field")
    public void i_clear_the_Keyword_field() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.cssSelector("#keyword")).clear();
    }

    @When("I go to the sent groudates page")
    public void i_go_to_the_sent_groudates_page() {
        // Write code here that turns the phrase above into concrete actions
        driver.get(ROOT_URL + "/list-sent-invite");
    }


    @When("I click the {string} proposal")
    public void i_click_the_proposal(String string) {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.partialLinkText(string + String.valueOf(temp_time))).click();
    }

    @When("I delete a user")
    public void i_delete_a_user() {
        // Write code here that turns the phrase above into concrete actions
        num_userevents = driver.findElements(By.partialLinkText("delete")).size();
        driver.findElement(By.partialLinkText("delete")).click();
    }
    @Then("there should be one less user")
    public void there_should_be_one_less_user() {
        // Write code here that turns the phrase above into concrete actions
        driver.navigate().refresh();
        assertEquals(driver.findElements(By.partialLinkText("delete")).size(), num_userevents - 1);
    }


    @When("I delete an event")
    public void i_delete_an_event() {
        // Write code here that turns the phrase above into concrete actions
        num_userevents = driver.findElements(By.partialLinkText("delete")).size();
        driver.findElements(By.partialLinkText("delete")).get(num_userevents-1).click();
    }


    @Then("there should be one less event")
    public void there_should_be_one_less_event() {
        // Write code here that turns the phrase above into concrete actions
        driver.navigate().refresh();
        assertEquals(driver.findElements(By.partialLinkText("delete")).size(), num_userevents - 1);
    }

    @Given("I am on the user setting page and signed in as {string}")
    public void i_am_on_the_user_setting_page_and_signed_in_as(String string) {
        // Write code here that turns the phrase above into concrete actions
        driver.get(ROOT_URL + "/home");
        driver.findElement(By.cssSelector("#username")).sendKeys(string);
        driver.findElement(By.cssSelector("#password")).sendKeys("123");
        driver.findElement(By.cssSelector("#signin")).click();
        //block string1 by string2
        driver.get(ROOT_URL + "/setting");
    }

    @Then("I can see the unavailble date range of {string}")
    public void i_can_see_the_unavailble_date_range_of(String string) {
        // Write code here that turns the phrase above into concrete actions
        String text = driver.findElement(By.cssSelector("#result > ul > li")).getText();
        String true_text = string+"/ No time from ";
        assertTrue(text.contains(true_text));
    }

    @When("I go to the home page")
    public void i_go_to_the_home_page() {
        // Write code here that turns the phrase above into concrete actions
        driver.get(ROOT_URL +"/home");
    }

    @When("I go to the propose event page")
    public void i_go_to_the_propose_event_page() {
        // Write code here that turns the phrase above into concrete actions
        driver.get(ROOT_URL + "/proposeEvent");
    }

    @Then("the added user and event should still be there")
    public void the_added_user_and_event_should_still_be_there() {
        // Write code here that turns the phrase above into concrete actions
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        String user_list = driver.findElement(By.cssSelector("#add-users-list")).getText();

        assertNotEquals(user_list, "");

        String event_list = driver.findElement(By.cssSelector("#events_list")).getText();
        assertNotEquals(event_list, "Propose Events:");
    }

    @When("I sort by name")
    public void i_sort_by_name() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.cssSelector("#invite_sort")).sendKeys("GroupDate Name");
    }

    @Then("the groupdates should be sorted in alphabetical order")
    public void the_groupdates_should_be_sorted_in_alphabetical_order() {
        // Write code here that turns the phrase above into concrete actions
        List<WebElement> events= driver.findElements(By.cssSelector("td a"));
        String prev = events.get(0).getText();
        for(int i=1; i<events.size(); i++) {
            assertTrue(events.get(i).getText().compareTo(prev) >= 0);
            prev = events.get(i).getText();
        }
    }

    @When("I sort by date")
    public void i_sort_by_date() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.cssSelector("#invite_sort")).sendKeys("Date");
    }

    @Then("the groupdates should be sorted in date order")
    public void the_groupdates_should_be_sorted_in_date_order() {
        // Write code here that turns the phrase above into concrete actions
        List<WebElement> events= driver.findElements(By.cssSelector("td"));
        String prev = events.get(1).getText();
        for(int i=4; i<events.size(); i+=3) {
            assertTrue(events.get(i).getText().compareTo(prev) >= 0);
            prev = events.get(i).getText();
        }
    }

    @Given("I am on the sent groupdate page as root1")
    public void i_am_on_the_sent_groupdate_page_as_root1() {
        // Write code here that turns the phrase above into concrete actions
        driver.get(ROOT_URL + "/home");
        driver.findElement(By.cssSelector("#username")).sendKeys("root1");
        driver.findElement(By.cssSelector("#password")).sendKeys("123");
        driver.findElement(By.cssSelector("#signin")).click();
        driver.get(ROOT_URL + "/list-sent-invite");
    }

    @When("I filter by {string}")
    public void i_filter_by(String string) {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.cssSelector("#finalized-filter")).sendKeys((string));
    }


    @Then("I should only see finalized dates")
    public void i_should_only_see_finalized_dates() {
        // Write code here that turns the phrase above into concrete actions
        List<WebElement> events= driver.findElements(By.cssSelector("tr:not(.noshow) td"));
        String prev = events.get(2).getText();
        for(int i=2; i<events.size(); i+=3) {
            String temp = events.get(i).getText();
            assertTrue(temp.compareTo("finalized not responded") == 0 || temp.compareTo("finalized responded") == 0);
        }
    }

    @Then("I should only see not finalized dates")
    public void i_should_only_see_not_finalized_dates() {
        // Write code here that turns the phrase above into concrete actions
        List<WebElement> events= driver.findElements(By.cssSelector("tr:not(.noshow) td"));
        String prev = events.get(2).getText();
        for(int i=2; i<events.size(); i+=3) {
            String temp = events.get(i).getText();
            assertEquals(temp,"not finalized");
        }
    }

    @Then("I should only see not responded dates")
    public void i_should_only_see_not_responded_dates() {
        // Write code here that turns the phrase above into concrete actions
        List<WebElement> events= driver.findElements(By.cssSelector("tr:not(.noshow) td"));
        String prev = events.get(2).getText();
        for(int i=2; i<events.size(); i+=3) {
            String temp = events.get(i).getText();
            assertEquals(temp,"finalized not responded");
        }
    }

    @Then("I should only see responded dates")
    public void i_should_only_see_responded_dates() {
        // Write code here that turns the phrase above into concrete actions
        List<WebElement> events= driver.findElements(By.cssSelector("tr:not(.noshow) td"));
        String prev = events.get(2).getText();
        for(int i=2; i<events.size(); i+=3) {
            String temp = events.get(i).getText();
            assertEquals(temp,"finalized responded");
        }
    }


    @After()
    public void cleanup() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.quit();
    }
}