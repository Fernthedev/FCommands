package io.github.fernthedev.fcommands.spigotclass.ncp;

@SuppressWarnings("unused")
public class expiration {

    private long expiration = 0;

    /**
     * Set the Expiration using milliseconds
     *
     * @param milliseconds How long the Expiration should take effect (in millis)
     * @return Expiration The Expiration
     */
    public expiration expireIn(long milliseconds) {
        this.expiration = System.currentTimeMillis() + milliseconds;

        return this;
    }

    /**
     * Set the Expiration using the Duration class
     *
     * @param duration How long the Expiration should take effect
     * @return Expiration The Expiration
     */
    public expiration expireIn(duration duration) {
        return expireIn(duration.toMilliseconds());
    }

    /**
     * @return True if the expiration is expired
     */
    public boolean isExpired() {
        return System.currentTimeMillis() >= this.expiration;
    }

    /**
     * Expire the expiration now
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
