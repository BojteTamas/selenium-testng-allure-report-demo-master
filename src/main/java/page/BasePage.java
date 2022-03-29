package page;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import util.AssertHelpers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class BasePage {

  static WebDriver webDriver;

  AssertHelpers assertHelpers = new AssertHelpers();


  public BasePage (WebDriver driver) {

    webDriver = driver;
  }


  public void waitUntilElementClickable (By by) {

    assertHelpers.assertWithPollThirtySeconds(webDriver, () -> {
      assertThat(webDriver.findElement(by)).isNotNull();
      assertThat(webDriver.findElement(by).isDisplayed()).isTrue();
      assertThat(webDriver.findElement(by).isEnabled()).isTrue();
    });
  }


  public void waitUntilElementClickable (WebElement element) {

    assertHelpers.assertWithPollThirtySeconds(webDriver, () -> {
      assertThat(element).isNotNull();
      assertThat(element.isDisplayed()).isTrue();
      assertThat(element.isEnabled()).isTrue();
    });
  }


  public void waitUntilElementsClickable (By by) {

    assertHelpers.assertWithPollThirtySeconds(webDriver, () -> {
      webDriver.findElements(by).forEach(element -> {
        assertThat(element).isNotNull();
        assertThat(element.isDisplayed()).isTrue();
        assertThat(element.isEnabled()).isTrue();
      });

    });
  }


  public void click (By by) {

    waitUntilElementClickable(by);
    webDriver.findElement(by).click();
  }


  public void clickXpathTemplate (String template) {

    By by = By.xpath(template);
    waitUntilElementClickable(by);
    webDriver.findElement(by).click();
  }


  public List<WebElement> getElements (By by) {

    waitUntilElementsClickable(by);
    return webDriver.findElements(by);
  }


}
