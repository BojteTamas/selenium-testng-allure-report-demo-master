package models;


import lombok.*;


@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Item {

  String name;

  String price;

}
