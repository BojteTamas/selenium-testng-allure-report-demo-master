package page;


import models.DiscountedItem;
import models.Item;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.*;
import util.ItemType;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;


public class HomePage extends BasePage {

  private final By selectResultPerPageArrow = By.xpath(
      "//li[@data-qa-id='results-per-page-select']//div[@data-clientside-hook='select-replace']");

  private final By sortArrow = By.cssSelector(
      "div[data-qa-id='sorting-options'] div.o-SortByDesktop--select .m-Select__replaced div.m-Select__arrow");

  private final String selectResultPerPageValues =
      "//li[@data-qa-id='results-per-page-select']//div[@data-clientside-hook='select-drop-down']//div[@role='option']/span[text()='%s']";

  private final String
      sortOptions
      = "//div[contains(@class, 'o-SortByDesktop--select')]//div[@role='option']/span[text()='%s']";

  private final By listOfDiscountedItems = By.xpath(
      "//li[@data-qa-id='result-list-entry']//span[@class='a-CircleBadge__inner']/ancestor::li[@data-qa-id='result-list-entry']");

  private final By listOfItems = By.xpath(
      "//li[@data-qa-id='result-list-entry']//p[contains(@class, 'o-SearchProductListItem__prices__retail-price') and not(contains(@class, 'u-color--red'))]/ancestor::li[@data-qa-id='result-list-entry']");


  private final By itemPrice = By.cssSelector("p[data-qa-id='entry-price'] span.a-Price");

  private final By itemName = By.cssSelector(".o-SearchProductListItem__title");

  private final By priceBefore = By.cssSelector(
      "span.u-text-decoration--line-through.o-SearchProductListItem__prices__list-price");

  private final By discountValue = By.cssSelector("span.a-CircleBadge__inner span.u-no-wrap");

  private final By acceptCookiesButton = By.cssSelector("button[data-testid='uc-accept-all-button']");

  private final By shadowHostSelector = By.id("usercentrics-root");


  public HomePage (WebDriver driver) {

    super(driver);
  }


  public void selectResultPerPage (int resultsPerPage) {

    click(selectResultPerPageArrow);
    clickXpathTemplate(String.format(selectResultPerPageValues, resultsPerPage));
  }


  public void selectSortOption (String sortOption) {

    click(sortArrow);
    clickXpathTemplate(format(sortOptions, sortOption));
  }


  public List collectItems (ItemType itemType) {

    switch (itemType) {
      case WITHOUT_DISCOUNT:
        return getItems();

      case WITH_DISCOUNT:
        return getDiscountedItems();

      default:
        throw new NotFoundException("Please use different value");
    }
  }


  public List<DiscountedItem> getDiscountedItems () {

    List<WebElement> elements = getElements(listOfDiscountedItems);
    List<DiscountedItem> discountedItems = new ArrayList<>();
    elements.forEach(element -> {
      DiscountedItem item = DiscountedItem.builder()
          .name(element.findElement(itemName).getText())
          .price(element.findElement(itemPrice).getText())
          .priceBefore(element.findElement(priceBefore).getText())
          .discountValue(element.findElement(discountValue).getText()).build();
      discountedItems.add(item);
    });
    return discountedItems;
  }


  public List<Item> getItems () {

    List<WebElement> elements = getElements(listOfItems);
    List<Item> items = new ArrayList<>();
    elements.forEach(element -> {
      waitUntilElementClickable(element);
      Item item = Item.builder()
          .name(element.findElement(itemName).getText())
          .price(element.findElement(itemPrice).getText())
          .build();
      items.add(item);
    });
    return items;
  }


  public void acceptCookies () {

    WebElement shadowHost = webDriver.findElement(shadowHostSelector);
    SearchContext shadowRoot = shadowHost.getShadowRoot();
    assertHelpers.assertWithPollThirtySeconds(
        webDriver,
        () -> Assertions.assertThat(shadowRoot.findElement(acceptCookiesButton))
            .isNotNull()
    );
    WebElement shadowContent = shadowRoot.findElement(acceptCookiesButton);
    waitUntilElementClickable(shadowContent);
    shadowContent.click();
  }

}
