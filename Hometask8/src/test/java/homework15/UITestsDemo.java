package homework15;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class UITestsDemo {
    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://www.bstackdemo.com/");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    String quantityOfProducts = "2";
    String product1Name = "iPhone 12 Pro Max";
    String xpathExpression1 = String.format("//p[text()='%s']/following-sibling::div[@class='shelf-item__buy-btn']", product1Name);

    double product1Price = 1099.00;
    double product2Price = 649.00;
    String product2Name = "iPhone XS Max";
    String xpathExpression2 = String.format("//p[text()='%s']/following-sibling::div[@class='shelf-item__buy-btn']", product2Name);
    String[] productsNames = {product1Name, product2Name};
    double[] productsPrices = {product1Price, product2Price};

    @Test
    public void checkAddToCart() throws InterruptedException {
        WebElement phone1 = driver.findElement(By.xpath(xpathExpression1));
        Thread.sleep(1000);
        phone1.click();

        WebElement cartCloseBtn = driver.findElement(By.xpath("//div[@class='float-cart__close-btn']"));
        cartCloseBtn.click();

        WebElement phone2 = driver.findElement(By.xpath(xpathExpression2));
        phone2.click();

        WebElement quantity = driver.findElement(By.xpath("//span[@class='bag__quantity']"));
        String quantityProducts = quantity.getText();

        Assert.assertEquals(quantityProducts, quantityOfProducts);

        List<WebElement> products = driver.findElements(By.xpath("//p[@class='title']"));

        List<WebElement> prices = driver.findElements(By.xpath("//div[@class='shelf-item__price']//p"));

        WebElement subTotal = driver.findElement(By.xpath("//p[@class='sub-price__val']"));

        for (int i = 0; i < products.size(); i++) {
            Assert.assertEquals(products.get(i).getText(), productsNames[i]);

            double price = Double.parseDouble(prices.get(i).getText().substring(2));
            Assert.assertEquals(price, productsPrices[i]);
        }

        double priceTotal = Double.parseDouble(subTotal.getText().substring(2));
        Assert.assertEquals(priceTotal, product1Price + product2Price);

        WebElement checkoutBtn = driver.findElement(By.xpath("//div[@class='buy-btn'  and  text()='Checkout']"));
        Assert.assertTrue(checkoutBtn.isDisplayed());
    }

    @Test
    public void checkDeleteFromCart() throws InterruptedException {
        WebElement phone1 = driver.findElement(By.xpath(xpathExpression1));
        phone1.click();

        Thread.sleep(200);

        WebElement deleteItem = driver.findElement(By.xpath("//div[@class='shelf-item']//div[@class='shelf-item__del']"));
        deleteItem.click();

        WebElement shelfEmpty = driver.findElement(By.xpath("//p[@class='shelf-empty' and text()='Add some products in the bag ']"));
        Assert.assertTrue(shelfEmpty.isDisplayed());

        WebElement continueShoppingBtn = driver.findElement(By.xpath("//div[@class='buy-btn'  and  text()='Continue Shopping']"));
        Assert.assertTrue(continueShoppingBtn.isDisplayed());

        WebElement subTotal = driver.findElement(By.xpath("//p[@class='sub-price__val']"));
        double priceTotal = Double.parseDouble(subTotal.getText().substring(2));
        Assert.assertEquals(priceTotal, 0.00);
    }

}
