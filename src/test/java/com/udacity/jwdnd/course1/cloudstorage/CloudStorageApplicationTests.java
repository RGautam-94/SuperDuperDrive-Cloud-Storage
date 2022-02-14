package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();

    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }

    }

    @Order(1)
    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Order(2)
    @Test
    public void getSignupPage() {
        driver.get("http://localhost:" + this.port + "/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());
    }

    //Sign up and login test
    @Order(3)
    @Test
    public void testRedirection() {
        // Create a test account (Sign up)
        doMockSignUp("Redirection", "Test", "RT", "123");
        // Use credentials to login
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
        doLogIn("RT", "123");
        Assertions.assertEquals("Home", driver.getTitle());
    }

    @Order(4)
    @Test
    public void creatingNoteTest() {
        // Create a test account (Sign up)
        doMockSignUp("Redirection", "Test", "QT", "123");
        // Use credentials to login
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
        doLogIn("QT", "123");
        // Create note test
        noteCreate("Note Title", "Test Note");
        // update note test
        updateNote();
        //delete note test
        deleteNote();


    }

    @Order(5)
    @Test
    public void credentialTest() {
        // Create a test account (Sign up)
        doMockSignUp("Redirection", "Test", "CT", "123");
        // Use credentials to login
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
        doLogIn("CT", "123");

        ///Add Credential
        addCredential("Heroku", "rgautam", "1234");

        //Update Credential
        updateCredentialTest();

        //Delete Credential
        deleteCredential();

    }

    @Order(6)
    @Test
    public void testLargeUpload() {
        // Create a test account
        doMockSignUp("Large File", "Test", "LFT", "123");
        doLogIn("LFT", "123");
        // Try to upload an arbitrary large file
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        String fileName = "upload5m.zip";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));
    }

    @Order(7)
    @Test
    public void testBadUrl() throws InterruptedException {
        // Create a test account
        doMockSignUp("URL", "Test", "UT", "123");
        doLogIn("UT", "123");
        // Try to access a random made-up URL.
        driver.get("http://localhost:" + this.port + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
        Thread.sleep(1000);
    }


    private void doMockSignUp(String firstName, String lastName, String userName, String password) {

        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUserName")));
        WebElement inputUsername = driver.findElement(By.id("inputUserName"));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        // Attempt to sign up.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();
        Assertions.assertTrue(driver.findElement(By.id("sign-up")).getText().contains("You successfully signed up!"));
        //To enter login page
        WebElement signUpButton = driver.findElement(By.id("loginButton"));
        signUpButton.click();

    }

    private void doLogIn(String userName, String password) {

        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));

    }


    // note create method
    private void noteCreate(String noteTitle, String noteDescription) {

        WebDriverWait wait = new WebDriverWait(driver, 2);
        WebElement noteTab = driver.findElement(By.id("nav-notes-tab"));
        noteTab.click();
        wait.withTimeout(Duration.ofSeconds(2));
        WebElement newNote = driver.findElement(By.id("btnAddNewNote"));
        wait.until(ExpectedConditions.elementToBeClickable(newNote)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).sendKeys(noteTitle);
        WebElement notedescription = driver.findElement(By.id("note-description"));
        notedescription.sendKeys(noteDescription);
        WebElement savechanges = driver.findElement(By.id("noteSubmit-button"));
        savechanges.click();
        Assertions.assertEquals("Result", driver.getTitle());
        driver.get("http://localhost:" + this.port + "/home");
        WebElement note1Tab = driver.findElement(By.id("nav-notes-tab"));
        note1Tab.click();
    }

    //Update note test method
    private void updateNote() {
        WebDriverWait wait = new WebDriverWait(driver, 2);
        WebElement notesTable = driver.findElement(By.id("userTable"));
        WebElement editButtonElement = driver.findElement(By.id("edit-button"));
        wait.until(ExpectedConditions.elementToBeClickable(editButtonElement)).click();
        WebElement notetitle = driver.findElement(By.id("note-title"));
        wait.until(ExpectedConditions.elementToBeClickable(notetitle));
        notetitle.clear();
        notetitle.sendKeys("Edited Note Title");
        WebElement notedescription = driver.findElement(By.id("note-description"));
        notedescription.clear();
        notedescription.sendKeys("Edited note Description.");
        WebElement savechanges = driver.findElement(By.id("noteSubmit-button"));
        savechanges.click();
        Assertions.assertEquals("Result", driver.getTitle());
    }

    // Delete note test
    private void deleteNote() {
        driver.get("http://localhost:" + this.port + "/home");
        WebElement noteTab = driver.findElement(By.id("nav-notes-tab"));
        noteTab.click();
        WebDriverWait wait = new WebDriverWait(driver, 2);
        WebElement deleteNote = driver.findElement(By.id("deleteButton"));
        wait.until(ExpectedConditions.elementToBeClickable(deleteNote)).click();
        Assertions.assertEquals("Result", driver.getTitle());
        driver.get("http://localhost:" + this.port + "/home");
        WebElement noteTab1 = driver.findElement(By.id("nav-notes-tab"));
        noteTab1.click();
    }


    // Methods for Credentials
    private void addCredential(String url, String userName, String password) {
        driver.get("http://localhost:" + this.port + "/home");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 30);
        WebElement credentialTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialTab.click();
        webDriverWait.withTimeout(Duration.ofSeconds(2));
        WebElement addNewCredentialButton = driver.findElement(By.id("add-credential"));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(addNewCredentialButton)).click();
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url"))).sendKeys(url);
        WebElement newUserName = driver.findElement(By.id("credential-username"));
        newUserName.sendKeys(userName);
        WebElement newPassword = driver.findElement(By.id("credential-password"));
        newPassword.sendKeys(password);
        WebElement saveChange = driver.findElement(By.id("credential-submit"));
        saveChange.click();
        Assertions.assertEquals("Result", driver.getTitle());
        driver.get("http://localhost:" + this.port + "/home");
        WebElement credentialTab1 = driver.findElement(By.id("nav-credentials-tab"));
        credentialTab1.click();
    }

    private void updateCredentialTest() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        WebElement editButtonElement = driver.findElement(By.id("credential-edit"));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(editButtonElement)).click();
        WebElement editUrl = driver.findElement(By.id("credential-url"));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(editUrl));
        editUrl.clear();
        editUrl.sendKeys("EditedUrl.com");
        WebElement userName = driver.findElement(By.id("credential-username"));
        userName.clear();
        userName.sendKeys("EditedUsername");
        WebElement password = driver.findElement(By.id(("credential-password")));
        password.clear();
        password.sendKeys("EditedPassword");
        WebElement saveChages = driver.findElement(By.id("credential-submit"));
        saveChages.click();
        Assertions.assertEquals("Result", driver.getTitle());
        driver.get("http://localhost:" + this.port + "/home");
        WebElement credentialTab1 = driver.findElement(By.id("nav-credentials-tab"));
        credentialTab1.click();
    }

    private void deleteCredential() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        WebElement deleteCredential = driver.findElement(By.id("delete-credential"));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(deleteCredential)).click();
        Assertions.assertEquals("Result", driver.getTitle());
        driver.get("http://localhost:" + this.port + "/home");
        WebElement credentialTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialTab.click();

    }


}
