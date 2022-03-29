package models;


import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class DiscountedItem {

  String name;

  String price;

  String priceBefore;

  String discountValue;
}
