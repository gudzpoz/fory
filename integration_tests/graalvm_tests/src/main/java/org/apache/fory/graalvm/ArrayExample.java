/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.fory.graalvm;

import java.nio.charset.StandardCharsets;
import java.util.*;
import org.apache.fory.Fory;
import org.apache.fory.util.Preconditions;

public class ArrayExample {
  private static final Fory FORY = Fory.builder().registerGuavaTypes(false).build();

  static {
    FORY.register(ArrayExample.class);
    FORY.register(ConcreteObject.class);
    FORY.register(AbstractObject[].class);
    FORY.ensureSerializersCompiled();
  }

  byte[] bytes;
  short[] shorts;
  int[] ints;
  long[] longs;
  Object[] objects;
  AbstractObject[] abstractObjects;

  public static void main(String[] args) {
    FORY.reset();
    ArrayExample arrayExample = new ArrayExample();
    arrayExample.bytes = "01234567890".getBytes(StandardCharsets.UTF_8);
    arrayExample.shorts = new short[] {0xF01, 0xF02, 0xF03, 0xF04, 0xF05, 0xF06, 0xF07, 0xF08};
    arrayExample.ints = new int[] {0xF0F0F01, 0xF0F0F02, 0xF0F0F03, 0xF0F0F04};
    arrayExample.longs = new long[] {0x0FFF_0000_FFFF_0001L, 0x0FFF_0000_FFFF_0002L};
    arrayExample.objects = new Object[] {"A", "B"};
    arrayExample.abstractObjects = new AbstractObject[] {new ConcreteObject(42)};

    byte[] bytes = FORY.serialize(arrayExample);
    ArrayExample deserialized = FORY.deserialize(bytes, ArrayExample.class);
    Preconditions.checkArgument(Arrays.equals(arrayExample.bytes, deserialized.bytes));
    Preconditions.checkArgument(Arrays.equals(arrayExample.shorts, deserialized.shorts));
    Preconditions.checkArgument(Arrays.equals(arrayExample.ints, deserialized.ints));
    Preconditions.checkArgument(Arrays.equals(arrayExample.longs, deserialized.longs));
    Preconditions.checkArgument(Arrays.equals(arrayExample.objects, deserialized.objects));
    Preconditions.checkArgument(Arrays.equals(arrayExample.abstractObjects, deserialized.abstractObjects));
    System.out.println("ArrayExample succeed");
  }

  public abstract static class AbstractObject {
  }
  public static class ConcreteObject extends AbstractObject {
    private final int i;
    public ConcreteObject(int i) {
      this.i = i;
    }
    @Override
    public boolean equals(Object obj) {
      return obj instanceof ConcreteObject && ((ConcreteObject) obj).i == i;
    }
  }
}
