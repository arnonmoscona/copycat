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
package net.kuujo.copycat.io.storage;

import java.util.concurrent.CompletableFuture;

/**
 * Log entry filter.
 * <p>
 * The entry filter assist in {@link Compaction} by providing a mechanism through which to define
 * which entries should be retained in the log during compaction. During the log compaction process, the configured
 * {@link Compactor#filter(EntryFilter)} will be called for each entry in the segment or segments
 * being compacted.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
@FunctionalInterface
public interface EntryFilter {

  /**
   * Returns a boolean value indicating whether to keep the given entry in the log.
   * <p>
   * During compaction, the {@link Compaction} will call this method for each {@link Entry}
   * in the segment or segments being compacted. If this method returns {@code true} then the entry will be written to
   * the compacted segment, otherwise it will be <em>permanently discarded from the log</em>. Users should favor accepting
   * unneeded entries over rejecting entries.
   *
   * @param entry The entry to check.
   * @param compaction The compaction context.
   * @return Indicates whether to keep the entry in the log.
   */
  CompletableFuture<Boolean> accept(Entry entry, Compaction compaction);

}
