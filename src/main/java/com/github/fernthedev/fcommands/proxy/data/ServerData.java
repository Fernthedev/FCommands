package com.github.fernthedev.fcommands.proxy.data;

import com.github.fernthedev.fernapi.universal.Universal;
import lombok.*;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *     {
 *     "name": "test2",
 *     "address": "localhost",
 *     "port": 25566,
 *      timeout": 2
 *     }
 */
@Data
@ToString
@RequiredArgsConstructor
public class ServerData implements Serializable {

    @NonNull
    private String name;

//    @NonNull
//    private String address;
//
//    @NonNull
//    private int port;

    @NonNull
    private AddressPortPair addressPortPair;

    @NonNull
    @Getter(AccessLevel.NONE)
    private long timeoutMS;

    @Getter
    private boolean hidden = false;

    private ServerData() {
        if (timeoutMS <= 0) timeoutMS = 1;
    }

    public long getTimeoutMS() {
        if (timeoutMS <= 0) timeoutMS = 1;
        return timeoutMS;
    }

    @Getter
    private transient volatile boolean online = false;

    private static final List<AddressPortPair> addressPingStatus = Collections.synchronizedList(new ArrayList<>());


    private void clearPing(boolean status) {
        addressPingStatus.remove(addressPortPair);
        online = status;
    }

    public synchronized void ping() {
        if(addressPingStatus.contains(addressPortPair)) {
            Universal.debug("Already pinging");
            return; // Already pinging
        }

        try (Socket s = new Socket()) {
            addressPingStatus.add(addressPortPair);
            s.connect(new InetSocketAddress(addressPortPair.getAddress(), addressPortPair.getPort()), (int) getTimeoutMS());
            clearPing(true);
        } catch (SocketTimeoutException e) {
            clearPing(false);
            Universal.debug(e.getMessage() + " Timed out Name: " + name + " Port: " + addressPortPair.port + " Time: " + getTimeoutMS());
        } catch (SocketException e) {
            clearPing(false);
            Universal.debug(e.getMessage() + " Name: " + name + " Port: " + addressPortPair.port + " Time: " + getTimeoutMS());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AllArgsConstructor
    @Data
    public static class AddressPortPair {
        private String address;

        private int port;
    }
}
