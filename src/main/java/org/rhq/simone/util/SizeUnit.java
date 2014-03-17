/*
 * Copyright 2014 Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rhq.simone.util;

/**
 * @author Thomas Segismont
 */
public enum SizeUnit {
  BYTES {
    @Override
    public long toBytes(long value) {
      return value;
    }

    @Override
    public long toKiloBytes(long value) {
      return value / O1;
    }

    @Override
    public long toMegaBytes(long value) {
      return value / O2;
    }

    @Override
    public long toGigaBytes(long value) {
      return value / O3;
    }

    @Override
    public long toTeraBytes(long value) {
      return value / O4;
    }

    @Override
    public long convert(long value, SizeUnit source) {
      return source.toBytes(value);
    }
  },
  KILOBYTES {
    @Override
    public long toBytes(long value) {
      return value * O1;
    }

    @Override
    public long toKiloBytes(long value) {
      return value;
    }

    @Override
    public long toMegaBytes(long value) {
      return value / O1;
    }

    @Override
    public long toGigaBytes(long value) {
      return value / O2;
    }

    @Override
    public long toTeraBytes(long value) {
      return value / O3;
    }

    @Override
    public long convert(long value, SizeUnit source) {
      return source.toKiloBytes(value);
    }
  },
  MEGABYTES {
    @Override
    public long toBytes(long value) {
      return value * O2;
    }

    @Override
    public long toKiloBytes(long value) {
      return value * O1;
    }

    @Override
    public long toMegaBytes(long value) {
      return value;
    }

    @Override
    public long toGigaBytes(long value) {
      return value / O1;
    }

    @Override
    public long toTeraBytes(long value) {
      return value / O2;
    }

    @Override
    public long convert(long value, SizeUnit source) {
      return source.toMegaBytes(value);
    }
  },
  GIGABYTES {
    @Override
    public long toBytes(long value) {
      return value * O3;
    }

    @Override
    public long toKiloBytes(long value) {
      return value * O2;
    }

    @Override
    public long toMegaBytes(long value) {
      return value * O1;
    }

    @Override
    public long toGigaBytes(long value) {
      return value;
    }

    @Override
    public long toTeraBytes(long value) {
      return value / O1;
    }

    @Override
    public long convert(long value, SizeUnit source) {
      return source.toGigaBytes(value);
    }
  },
  TERABYTES {
    @Override
    public long toBytes(long value) {
      return value * O4;
    }

    @Override
    public long toKiloBytes(long value) {
      return value * O3;
    }

    @Override
    public long toMegaBytes(long value) {
      return value * O2;
    }

    @Override
    public long toGigaBytes(long value) {
      return value * O1;
    }

    @Override
    public long toTeraBytes(long value) {
      return value;
    }

    @Override
    public long convert(long value, SizeUnit source) {
      return source.toTeraBytes(value);
    }
  };

  public abstract long toBytes(long value);

  public abstract long toKiloBytes(long value);

  public abstract long toMegaBytes(long value);

  public abstract long toGigaBytes(long value);

  public abstract long toTeraBytes(long value);

  public abstract long convert(long value, SizeUnit source);

  private static final long O1 = 1024;
  private static final long O2 = O1 * O1;
  private static final long O3 = O2 * O1;
  private static final long O4 = O3 * O1;
}
