package com.github.fernthedev.fcommands.spigotclass.ncp;

@SuppressWarnings("unused")
public class Expiration {

    private long expiration = 0;

    /**
     * Set the Expiration using milliseconds
     *
     * @param milliseconds How long the Expiration should take effect (in millis)
     * @return Expiration The Expiration
     */
    public Expiration expireIn(long milliseconds) {
        this.expiration = System.currentTimeMillis() + milliseconds;

        return this;
    }

    /**
     * Set the Expiration using the Duration class
     *
     * @param duration How long the Expiration should take effect
     * @return Expiration The Expiration
     */
    public Expiration expireIn(Duration duration) {
        return expireIn(duration.toMilliseconds());
    }

    /**
     * @return True if the Expiration is expired
     */
    public boolean isExpired() {
        return System.currentTimeMillis() >= this.expiration;
    }

    /**
     * Expire the Expiration now
     */
    public void expireNow() {
        this.expiration = 0;
    }

    /**
     * @return Long The Expiration
     */
    public long getExpiration() {
        return this.expiration;
    }
}
