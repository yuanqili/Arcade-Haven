package edu.ucsb.g01.core;

/**
 * {@link User} class represents a user entity, recording user-specific
 * information. Its fields are identical to the user table schema in database.
 * But for different usage, some fields may be {@code null}.
 */
public class User {

    /** User id, corresponds to {@code g01.user.uid}. */
    public int uid;

    /** User name, corresponds to {@code g01.user.name}. */
    public String name;

    /** User password, corresponds to {@code g01.user.password}.*/
    public String password;

    /** User email, corresponds to {@code g01.user.email}.*/
    public String email;

    /** User's last login IP, used for validation/security. */
    public String lastLoginIp;
}
