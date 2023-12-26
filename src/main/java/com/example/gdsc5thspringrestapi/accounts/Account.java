package com.example.gdsc5thspringrestapi.accounts;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of="id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String email;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER) //ㄱㅏ져올 role이 적은 데다가 거의 매번 account 가져올 때마다 필요한 정보이므로
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;


}
