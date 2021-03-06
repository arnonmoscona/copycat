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
package net.kuujo.copycat.raft.server.storage;

import net.kuujo.copycat.io.serializer.SerializeWith;
import net.kuujo.copycat.io.storage.Entry;
import net.kuujo.copycat.util.ReferenceManager;

/**
 * No-op entry.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
@SerializeWith(id=300)
public class NoOpEntry extends RaftEntry<NoOpEntry> {

  public NoOpEntry(ReferenceManager<Entry<?>> referenceManager) {
    super(referenceManager);
  }

  @Override
  public String toString() {
    return String.format("%s[index=%d, term=%d]", getClass().getSimpleName(), getIndex(), getTerm());
  }

}
