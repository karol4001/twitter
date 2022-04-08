package pl.sda.twitter.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.sda.twitter.repository.JpaUserRepository;

@Service
public class UserAppService implements UserDetailsService {
    private final JpaUserRepository users;

    public UserAppService(JpaUserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.findUserByUsername(username).orElse(null);
    }
}
