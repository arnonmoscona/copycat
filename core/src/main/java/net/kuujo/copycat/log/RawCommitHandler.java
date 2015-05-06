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
package net.kuujo.copycat.log;

import net.kuujo.copycat.io.Buffer;

/**
 * Raw commit handler.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
@FunctionalInterface
public interface RawCommitHandler {

  /**
   * Handles a raw commit.
   *
   * @param index The entry index.
   * @param key The raw commit key.
   * @param entry The raw commit entry.
   * @param result The raw result.
   * @return The raw result buffer.
   */
  Buffer commit(long index, Buffer key, Buffer entry, Buffer result);

}