package project.dao.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

import static java.util.Collections.singletonList;
import static project.dao.RequestRegistry.lookup;

@Repository
public class UserDao implements UserDetailsService, UserValidatorDao {

    private final DataSource ds;

    private final PasswordEncoder passwordEncoder;

    private NamedParameterJdbcTemplate jdbc;

    private RowMapper<UserDetails> mapper = (rs, rowNum) -> new User(rs.getString("username"), rs.getString("password"), singletonList(new SimpleGrantedAuthority(rs.getString("role"))));

    @Autowired
    public UserDao(DataSource ds, PasswordEncoder passwordEncoder) {
        this.ds = ds;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        jdbc = new NamedParameterJdbcTemplate(ds);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetails> data = jdbc.query(lookup("user/LoadUser"), new MapSqlParameterSource("username", username), mapper);
        if (data.isEmpty() || data.size() != 1) {
            throw new UsernameNotFoundException(username);
        }

        return data.get(0);
    }

    public void register(String username, String password) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("username", username);
        params.addValue("password", passwordEncoder.encode(password));
        params.addValue("date", Timestamp.from(ZonedDateTime.now().toInstant()));

        jdbc.update(lookup("user/CreateUser"), params);
    }

    @Override
    public boolean usernameAlreadyExists(String username) {
        return jdbc.queryForObject(lookup("user/AlreadyExists"), new MapSqlParameterSource("username", username), Boolean.class);
    }

}
