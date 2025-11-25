package org.pokeherb.slackservice.domain;

import java.util.List;

public interface MessageSend {
    boolean send(List<String> ids, String message);
}
