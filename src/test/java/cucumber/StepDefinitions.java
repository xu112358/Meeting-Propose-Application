package cucumber;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.Assert.*;

/**
 * Step definitions for Cucumber tests.
 */
public class StepDefinitions {
    private static final String ROOT_URL = "http://localhost:8080/";

    private final WebDriver driver = new ChromeDriver();

    @Given("I am on the index page")
    public void i_am_on_the_index_page() {
        driver.get("http://localhost:8080/");
    }

    @When("I click the sign up button")
    public void I_click_the_sign_up_button() {
        driver.findElement(By.xpath("//*[@id=\"signup\"]")).click();
    }

    @Then("I should be on the sign up page")
    public void I_should_be_on_the_sign_up_page() {
        assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/signup"));
    }

    @Given("I am on the Sign Up page")
    public void i_am_on_the_Sign_Up_page() {
        driver.get("http://localhost:8080/signup");
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
        driver.get("http://localhost:8080/signin");
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
        driver.get("http://localhost:8080/SearchEvents");
    }

    @When("I click the targeted keyword field")
    public void i_click_the_targeted_keyword_field() {
        // Write code here that turns the phrase above into concrete actions
        String text = driver.findElement(By.cssSelector("#check_mark")).getText();
//		assertEquals("Add Event", text);
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