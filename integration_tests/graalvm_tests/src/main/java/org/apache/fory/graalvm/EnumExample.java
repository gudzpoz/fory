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

import org.apache.fory.Fory;
import org.apache.fory.util.Preconditions;

public class EnumExample {
  static Fory fory;

  static {
    fory = Fory.builder().withName(EnumExample.class.getName()).requireClassRegistration(true).build();
    // register and generate serializer code.
    for (ExampleEnum e : ExampleEnum.values()) {
      fory.register(e.getClass());
    }
    fory.register(ExampleEnum.class);
    fory.register(EnumExample.class);
    fory.ensureSerializersCompiled();
  }

  ExampleEnum field = ExampleEnum.ZERO;

  static void test(Fory fory) {
    EnumExample obj = new EnumExample();
    for (int i = 0; i < ExampleEnum.values().length; i++) {
      Preconditions.checkArgument(i == ExampleEnum.values()[i].getNumber());
      obj.field = ExampleEnum.values()[i];
      EnumExample out = (EnumExample) fory.deserialize(fory.serialize(obj));
      Preconditions.checkArgument(out.field == obj.field);
      Preconditions.checkArgument(i == out.field.getNumber());
    }
  }

  public static void main(String[] args) {
    test(fory);
    System.out.println("EnumExample succeed");
  }

  public enum ExampleEnum {
    ZERO {
      @Override
      public int getNumber() {
        return 0;
      }
    }, ONE {
      @Override
      public int getNumber() {
        return 1;
      }
    }, TWO {
      @Override
      public int getNumber() {
        return 2;
      }
    }, THREE {
      @Override
      public int getNumber() {
        return 3;
      }
    };

    public abstract int getNumber();
  }
}
