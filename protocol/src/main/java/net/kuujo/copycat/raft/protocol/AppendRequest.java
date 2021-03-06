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
package net.kuujo.copycat.raft.protocol;

import net.kuujo.copycat.util.BuilderPool;
import net.kuujo.copycat.io.BufferInput;
import net.kuujo.copycat.io.BufferOutput;
import net.kuujo.copycat.io.serializer.SerializeWith;
import net.kuujo.copycat.io.serializer.Serializer;
import net.kuujo.copycat.io.storage.Entry;
import net.kuujo.copycat.util.ReferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Protocol append request.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
@SerializeWith(id=256)
public class AppendRequest extends AbstractRequest<AppendRequest> {
  private static final BuilderPool<Builder, AppendRequest> POOL = new BuilderPool<>(Builder::new);

  /**
   * Returns a new append request builder.
   *
   * @return A new append request builder.
   */
  public static Builder builder() {
    return POOL.acquire();
  }

  /**
   * Returns an append request builder for an existing request.
   *
   * @param request The request to build.
   * @return The append request builder.
   */
  public static Builder builder(AppendRequest request) {
    return POOL.acquire(request);
  }

  private long term;
  private int leader;
  private long logIndex;
  private long logTerm;
  private List<Entry> entries = new ArrayList<>(128);
  private long commitIndex;
  private long globalIndex;

  private AppendRequest(ReferenceManager<AppendRequest> referenceManager) {
    super(referenceManager);
  }

  @Override
  public Type type() {
    return Type.APPEND;
  }

  /**
   * Returns the requesting node's current term.
   *
   * @return The requesting node's current term.
   */
  public long term() {
    return term;
  }

  /**
   * Returns the requesting leader address.
   *
   * @return The leader's address.
   */
  public int leader() {
    return leader;
  }

  /**
   * Returns the index of the log entry preceding the new entry.
   *
   * @return The index of the log entry preceding the new entry.
   */
  public long logIndex() {
    return logIndex;
  }

  /**
   * Returns the term of the log entry preceding the new entry.
   *
   * @return The index of the term preceding the new entry.
   */
  public long logTerm() {
    return logTerm;
  }

  /**
   * Returns the log entries to append.
   *
   * @return A list of log entries.
   */
  public List<? extends Entry> entries() {
    return entries;
  }

  /**
   * Returns the leader's commit index.
   *
   * @return The leader commit index.
   */
  public long commitIndex() {
    return commitIndex;
  }

  /**
   * Returns the leader's global index.
   *
   * @return The leader global index.
   */
  public long globalIndex() {
    return globalIndex;
  }

  @Override
  public void writeObject(BufferOutput buffer, Serializer serializer) {
    buffer.writeLong(term)
      .writeInt(leader)
      .writeLong(logIndex)
      .writeLong(logTerm)
      .writeLong(commitIndex)
      .writeLong(globalIndex);

    buffer.writeInt(entries.size());
    for (Entry entry : entries) {
      buffer.writeLong(entry.getIndex());
      serializer.writeObject(entry, buffer);
    }
  }

  @Override
  public void readObject(BufferInput buffer, Serializer serializer) {
    term = buffer.readLong();
    leader = buffer.readInt();
    logIndex = buffer.readLong();
    logTerm = buffer.readLong();
    commitIndex = buffer.readLong();
    globalIndex = buffer.readLong();

    int numEntries = buffer.readInt();
    entries.clear();
    for (int i = 0; i < numEntries; i++) {
      long index = buffer.readLong();
      Entry entry = serializer.readObject(buffer);
      entry.setIndex(index);
      entries.add(entry);
    }
  }

  @Override
  public void close() {
    entries.forEach(Entry::release);
    super.close();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getClass(), term, leader, logIndex, logTerm, entries, commitIndex, globalIndex);
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof AppendRequest) {
      AppendRequest request = (AppendRequest) object;
      return request.term == term
        && request.leader == leader
        && request.logIndex == logIndex
        && request.logTerm == logTerm
        && request.entries.equals(entries)
        && request.commitIndex == commitIndex
        && request.globalIndex == globalIndex;
    }
    return false;
  }

  @Override
  public String toString() {
    return String.format("%s[term=%d, leader=%s, logIndex=%d, logTerm=%d, entries=[%d], commitIndex=%d, globalIndex=%d]", getClass().getSimpleName(), term, leader, logIndex, logTerm, entries.size(), commitIndex, globalIndex);
  }

  /**
   * Append request builder.
   */
  public static class Builder extends AbstractRequest.Builder<Builder, AppendRequest> {

    protected Builder(BuilderPool<Builder, AppendRequest> pool) {
      super(pool, AppendRequest::new);
    }

    @Override
    public void reset() {
      super.reset();
      request.leader = 0;
      request.term = 0;
      request.logIndex = 0;
      request.logTerm = 0;
      request.entries.clear();
      request.commitIndex = 0;
      request.globalIndex = 0;
    }

    /**
     * Sets the request term.
     *
     * @param term The request term.
     * @return The append request builder.
     */
    public Builder withTerm(long term) {
      if (term <= 0)
        throw new IllegalArgumentException("term must be positive");
      request.term = term;
      return this;
    }

    /**
     * Sets the request leader.
     *
     * @param leader The request leader.
     * @return The append request builder.
     */
    public Builder withLeader(int leader) {
      request.leader = leader;
      return this;
    }

    /**
     * Sets the request last log index.
     *
     * @param index The request last log index.
     * @return The append request builder.
     */
    public Builder withLogIndex(long index) {
      if (index < 0)
        throw new IllegalArgumentException("log index must be positive");
      request.logIndex = index;
      return this;
    }

    /**
     * Sets the request last log term.
     *
     * @param term The request last log term.
     * @return The append request builder.
     */
    public Builder withLogTerm(long term) {
      if (term < 0)
        throw new IllegalArgumentException("log term must be positive");
      request.logTerm = term;
      return this;
    }

    /**
     * Sets the request entries.
     *
     * @param entries The request entries.
     * @return The append request builder.
     */
    public Builder withEntries(Entry... entries) {
      return withEntries(Arrays.asList(entries));
    }

    /**
     * Sets the request entries.
     *
     * @param entries The request entries.
     * @return The append request builder.
     */
    @SuppressWarnings("unchecked")
    public Builder withEntries(List<? extends Entry> entries) {
      if (entries == null)
        throw new NullPointerException("entries cannot be null");
      request.entries = (List<Entry>) entries;
      return this;
    }

    /**
     * Sets the request commit index.
     *
     * @param index The request commit index.
     * @return The append request builder.
     */
    public Builder withCommitIndex(long index) {
      if (index < 0)
        throw new IllegalArgumentException("commit index must be positive");
      request.commitIndex = index;
      return this;
    }

    /**
     * Sets the request global index.
     *
     * @param index The global recycle index.
     * @return The append request builder.
     */
    public Builder withGlobalIndex(long index) {
      if (index < 0)
        throw new IllegalArgumentException("global index must be positive");
      request.globalIndex = index;
      return this;
    }

    @Override
    public AppendRequest build() {
      super.build();
      if (request.term <= 0)
        throw new IllegalArgumentException("term must be positive");
      if (request.logIndex < 0)
        throw new IllegalArgumentException("log index must be positive");
      if (request.logTerm < 0)
        throw new IllegalArgumentException("log term must be positive");
      if (request.entries == null)
        throw new NullPointerException("entries cannot be null");
      if (request.commitIndex < 0)
        throw new IllegalArgumentException("commit index must be positive");
      if (request.globalIndex < 0)
        throw new IllegalArgumentException("global index must be positive");

      return request;
    }

    @Override
    public int hashCode() {
      return Objects.hash(request);
    }

    @Override
    public boolean equals(Object object) {
      return object instanceof Builder && ((Builder) object).request.equals(request);
    }

    @Override
    public String toString() {
      return String.format("%s[request=%s]", getClass().getCanonicalName(), request);
    }

  }

}
