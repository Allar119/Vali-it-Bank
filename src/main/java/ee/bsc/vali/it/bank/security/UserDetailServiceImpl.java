package ee.bsc.vali.it.bank.security;

import ee.bsc.vali.it.bank.application.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailServiceImpl implements UserDetailsService {


    @Autowired
    private BankRepository bankRepository;

    @Override

    public UserDetails loadUserByUsername(String username) {
        System.out.println("login request");
        System.out.println("pass: " + (bankRepository.loginRequest(username)));
        //return User.withUsername(username);

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new MyUser(username, bankRepository.loginRequest(username), authorities);

//        return User.withUsername(username)
//                .password(accountRepository.loginRequest(username)) //get encoded password form database for comprising
//                .roles("USER").build();
    }
}

