package net.klayil;

import org.slf4j.Logger;
import org.slf4j.spi.LoggingEventBuilder;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class FinalInfoLogger {
    public static Logger storedLogger;
    public FinalInfoLogger(Logger storedLoggerParam) {
        storedLogger = storedLoggerParam;
    }

    public static class NumExtensionWithNonNumber extends Number {
        private String superToString;

        final String superToString(Supplier<? extends Number> sup, String percentFormat) {
            superToString = percentFormat.formatted(sup.get());
            return superToString;
        }

        @Override
        public String toString() {
            return superToString;
        }

        private static Long longValue;
        private static Double doubleValue;

        private NumExtensionWithNonNumber(Long valueLong, Double valueDouble) {
            assert valueLong == ((double) valueDouble);

            longValue = valueLong;
            doubleValue = valueDouble;
        }

        public static NumExtensionWithNonNumber of(long value) {
            NumExtensionWithNonNumber res = new NumExtensionWithNonNumber(value, (double) value);
            res.superToString(res::longValue, "%d");

            return res;
        }
        public static NumExtensionWithNonNumber of(double value) {
            NumExtensionWithNonNumber res = new NumExtensionWithNonNumber((long) value, value);
            res.superToString(res::doubleValue, "%f");

            return res;
        }

        public static NumExtensionWithNonNumber of(Integer value) {
            return of((long) value);
        }
        public static NumExtensionWithNonNumber of(Float value) {
            return of((double) value);
        }


//        static ArrayList<Long> val1st = new ArrayList<>(List.of(-1L));
//        static ArrayList<Double> val2nd = new ArrayList<>(List.of(-1D));
//        protected static Number of(Object obj, boolean... STATIC) {
//            Number res = new Number() {
//                @Override
//                public String toString() {
//                    return obj.toString();
//                }
//
//                @Override
//                public int intValue() {
//                    return -1;
//                }
//
//                @Override
//                public long longValue() {
//                    return -1L;
//                }
//
//                @Override
//                public float floatValue() {
//                    return -1f;
//                }
//
//                @Override
//                public double doubleValue() {
//                    return -1d;
//                }
//            };
//
//            if (obj instanceof Number) {
//                val1st.set(0, ((NumExtensionWithNonNumber) obj).longValue()); val2nd.set(0, ((NumExtensionWithNonNumber) obj).doubleValue());
//
//                res = new Number() {
//                    @Override
//                    public String toString() {
//                        return ((NumExtensionWithNonNumber) obj).superToString;
//                    }
//
//                    @Override
//                    public int intValue() {
//                        return (int) (Number) val1st.getFirst();
//                    }
//
//                    @Override
//                    public long longValue() {
//                        return val1st.getFirst();
//                    }
//
//                    @Override
//                    public float floatValue() {
//                        return (float) (Number) val2nd.getFirst();
//                    }
//
//                    @Override
//                    public double doubleValue() {
//                        return val2nd.getFirst();
//                    }
//                };
//
//            }
//
//            return res;
//        }

        public static NumExtensionWithNonNumber of(Object objVal) {
            var clazzTsDispatcher = Map.of(
                    Long.class, (Function<Object, NumExtensionWithNonNumber>) obj -> (NumExtensionWithNonNumber) of((long)obj),
                    Integer.class, obj -> (NumExtensionWithNonNumber) of((Integer)obj),
                    Double.class, obj -> (NumExtensionWithNonNumber) of((double)obj),
                    Float.class, obj -> (NumExtensionWithNonNumber) of((Float)obj)
            );

            if (objVal == null) {
                return new NumExtensionWithNonNumber(-1L, -1D) {
                    @Override
                    public String toString() {
                        return "null";
                    }
                };
            }

            if (!clazzTsDispatcher.containsKey(objVal.getClass())) {
                return new NumExtensionWithNonNumber(-1L, -1D) {
                    @Override
                    public String toString() {
                        return objVal.toString();
                    }
                };
            }

            return ((Supplier<NumExtensionWithNonNumber>) () -> {
                return clazzTsDispatcher.get(objVal.getClass()).apply(objVal);
            }).get();
        }

        @Override
        public int intValue() {
            return ((Integer) (Number) this.longValue());
        }

        @Override
        public long longValue() {
            return longValue;
        }

        @Override
        public float floatValue() {
            return ((Float) (Number) this.doubleValue());
        }

        @Override
        public double doubleValue() {
            return doubleValue;
        }
    }

    private void infoSuperAble(BiConsumer<String, Object[]> superAble, String format, Object[] args) {
        Object[] wrappedArgs = Arrays.stream(args).map(NumExtensionWithNonNumber::of).toArray();

        final String formatted = format.formatted(wrappedArgs);

        superAble.accept(formatted, new Object[]{});
    }


    public void trace(String format, Object... arguments) {
        infoSuperAble(storedLogger::trace, format, arguments);
    }


    public LoggingEventBuilder atTrace() {
        return storedLogger.atTrace();
    }


    public void debug(String format, Object... arguments) {
        infoSuperAble(storedLogger::debug, format, arguments);
    }


    public LoggingEventBuilder atDebug() {
        return storedLogger.atDebug();
    }


    public void info(String format, Object... arguments) {
        infoSuperAble(storedLogger::info, format, arguments);
    }


    public LoggingEventBuilder atInfo() {
        return storedLogger.atInfo();
    }


    public void warn(String format, Object... arguments) {
        infoSuperAble(storedLogger::warn, format, arguments);
    }


    public LoggingEventBuilder atWarn() {
        return storedLogger.atWarn();
    }


    public void error(String format, Object... arguments) {
        infoSuperAble(storedLogger::error, format, arguments);
    }


    public LoggingEventBuilder atError() {
        return storedLogger.atError();
    }
}