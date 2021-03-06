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
package net.kuujo.copycat.io;

/**
 * Buffer allocator.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public interface BufferAllocator {

  /**
   * Allocates a fixed capacity buffer.
   *
   * @param capacity The buffer capacity.
   * @return The allocated buffer.
   */
  Buffer allocate(long capacity);

  /**
   * Allocates a new buffer.
   *
   * @param initialCapacity The initial buffer capacity.
   * @param maxCapacity The maximum buffer capacity.
   * @return The allocated buffer.
   */
  Buffer allocate(long initialCapacity, long maxCapacity);

}
