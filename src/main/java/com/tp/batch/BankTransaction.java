package com.tp.batch;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.*;
//2: @Entity annotation defines that a class can be mapped to a table. And that is it,
// it is just a marker, like for example Serializable interface.
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BankTransaction {
    @Id
    private long id;
    private long accountID;
    private Date transactionDate;
    //2: dans les fichier text les dates sont cree dans un format particuliere alors
    //   pour eviter le formattage de la date je vais declarer le variable strTransactionDate
    //   alors lorsque l'objet itemreader  lire la valeur il va stocker dans strTransactionDate
    //   et on va transformer cette donnee on format date et onva stocker dans la variable transactionDate
    //2:@Transient on utilise cette annotaion pour dire que ce champ va pas apparaitre dans la table de la base de
    //  donnée
    @Transient
    private String strTransactionDate;
    private String transactionType;
    private double amount;
}
