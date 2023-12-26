package com.example.gdsc5thspringrestapi.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService implements UserDetailsService {
    @Autowired
    AccountRepository accountRepository;
    //우리가 사용하는 domain을 우리가 정해놓은 인터페이스로 변환

    @Autowired
    PasswordEncoder passwordEncoder;

    public Account saveAccount(Account account){
        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        return this.accountRepository.save(account); //인코딩해서 저장하도록
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username)); //user에 해당하는 account 객체 없으면 에러
        return new AccountAdaptor(account);
    }

    private Collection<? extends GrantedAuthority> authorities(Set<AccountRole> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .collect(Collectors.toSet());
    }
}
