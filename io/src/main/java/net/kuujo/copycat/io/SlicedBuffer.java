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
 * Sliced buffer.
 * <p>
 * The sliced buffer provides a view of a subset of an underlying buffer. This buffer operates directly on the {@link Bytes}
 * underlying the child {@link Buffer} instance.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public class SlicedBuffer extends AbstractBuffer {
  private final Buffer root;

  public SlicedBuffer(Buffer root, Bytes bytes, long offset, long initialCapacity, long maxCapacity) {
    super(bytes, offset, initialCapacity, maxCapacity, null);
    this.root = root;
    root.acquire();
  }

  /**
   * Returns the root buffer.
   *
   * @return The root buffer.
   */
  public Buffer root() {
    return root;
  }

  @Override
  public boolean isDirect() {
    return root.isDirect();
  }

  @Override
  protected void compact(long from, long to, long length) {
    if (root instanceof AbstractBuffer) {
      ((AbstractBuffer) root).compact(from, to, length);
    }
  }

  @Override
  public boolean isFile() {
    return root.isFile();
  }

  @Override
  public boolean isReadOnly() {
    return root.isReadOnly();
  }

  @Override
  public Buffer compact() {
    return null;
  }

  @Override
  public Buffer acquire() {
    root.acquire();
    return this;
  }

  @Override
  public void release() {
    root.release();
  }

  @Override
  public void close() {
    root.release();
  }

}
