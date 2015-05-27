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
package net.kuujo.copycat.cluster;

import net.kuujo.copycat.io.Buffer;
import net.kuujo.copycat.io.serializer.Serializer;
import net.kuujo.copycat.io.serializer.Writable;

/**
 * Typed member info.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public class TypedMemberInfo implements Writable {
  private Member.Type type;
  private MemberInfo info;

  public TypedMemberInfo() {
  }

  public TypedMemberInfo(Member.Type type, MemberInfo info) {
    this.type = type;
    this.info = info;
  }

  /**
   * Returns the member type.
   *
   * @return The member type.
   */
  public Member.Type type() {
    return type;
  }

  /**
   * Returns the member info.
   *
   * @return The member info.
   */
  public MemberInfo info() {
    return info;
  }

  @Override
  public void writeObject(Buffer buffer, Serializer serializer) {
    buffer.writeByte(type.ordinal());
    serializer.writeObject(info, buffer);
  }

  @Override
  public void readObject(Buffer buffer, Serializer serializer) {
    type = Member.Type.values()[buffer.readByte()];
    info = serializer.readObject(buffer);
  }

}