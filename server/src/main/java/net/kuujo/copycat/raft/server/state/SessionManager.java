/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.kuujo.copycat.raft.server.state;

import net.kuujo.copycat.raft.Session;
import net.kuujo.copycat.io.transport.Connection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Session manager.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
class SessionManager {
  private final Map<UUID, Connection> connections = new HashMap<>();
  private final Map<Long, ServerSession> sessions = new HashMap<>();

  /**
   * Registers a connection.
   */
  SessionManager registerConnection(Connection connection) {
    for (ServerSession session : sessions.values()) {
      if (session.connection().equals(connection.id())) {
        session.setConnection(connection);
      }
    }
    connections.put(connection.id(), connection);
    return this;
  }

  /**
   * Unregisters a connection.
   */
  SessionManager unregisterConnection(Connection connection) {
    for (ServerSession session : sessions.values()) {
      if (session.connection().equals(connection.id())) {
        session.setConnection(null);
      }
    }
    connections.remove(connection.id());
    return this;
  }

  /**
   * Registers a session.
   */
  ServerSession registerSession(long sessionId, UUID connectionId) {
    ServerSession session = new ServerSession(sessionId, connectionId).setConnection(connections.get(connectionId));
    sessions.put(sessionId, session);
    return session;
  }

  /**
   * Unregisters a session.
   */
  ServerSession unregisterSession(long sessionId) {
    return sessions.remove(sessionId);
  }

  /**
   * Gets a session by session ID.
   *
   * @param sessionId The session ID.
   * @return The session or {@code null} if the session doesn't exist.
   */
  ServerSession getSession(long sessionId) {
    return sessions.get(sessionId);
  }

}
