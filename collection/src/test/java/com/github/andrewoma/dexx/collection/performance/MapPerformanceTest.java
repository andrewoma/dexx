//package com.github.andrewoma.dexx.collection.performance;
//
//import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
//import com.github.andrewoma.dexx.collection.DerivedKeyHashMap;
//import com.github.andrewoma.dexx.collection.HashMap;
//import com.github.andrewoma.dexx.collection.KeyFunction;
//import com.github.andrewoma.dexx.collection.Map;
//import com.github.andrewoma.dexx.collection.TreeMap;
//import org.jetbrains.annotations.NotNull;
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
///**
// *
// */
//public class MapPerformanceTest extends AbstractBenchmark {
//    private static int COUNT = 50000;
//    private static int[] rnd;
//    private static IntClassWithKey[] objects;
//    private static List<Integer> results = new ArrayList<Integer>();
//
//    @BeforeClass
//    public static void prepare() {
//        rnd = new int[COUNT];
//
//        final Random random = new Random();
//        for (int i = 0; i < COUNT; i++) {
//            rnd[i] = Math.abs(random.nextInt());
//        }
//
//        objects = new IntClassWithKey[COUNT];
//        for (int i = 0; i < COUNT; i++)
//            objects[i] = new IntClassWithKey(i, i + 100);
//    }
//
//    @AfterClass
//    public static void compare() {
//        for (Integer result : results) {
//            System.out.println(result);
//        }
//    }
//
//    @Test
//    public void derivedKeyMap() throws Exception {
//        results.add(runTest(new DerivedKeyHashMap<Integer, IntClassWithKey>(new KeyFunction<Integer, IntClassWithKey>() {
//            @NotNull
//            public Integer key(@NotNull IntClassWithKey value) {
//                return value.key;
//            }
//        })));
//    }
//
//    @Test
//    public void hashMap() throws Exception {
//        results.add(runTest(HashMap.<Integer, IntClassWithKey>empty()));
//    }
//
//    @Test
//    public void altHashMap() throws Exception {
//        results.add(runTest(dexx.collection.HashMap.<Integer, IntClassWithKey>empty()));
//    }
//
////    @Test
////    public void scalaMap() throws Exception {
////        results.add(runTest(Map$.MODULE$.< Integer, IntClassWithKey >empty()));
////    }
//
//    @Test
//    public void javaHashMap() throws Exception {
//        results.add(runTest(new HashMap<Integer, IntClassWithKey>()));
//    }
//
//    @Test
//    public void treeMap() throws Exception {
//        results.add(runTest(new TreeMap<Integer, IntClassWithKey>()));
//    }
//
//    @Test
//    public void javaTreeMap() throws Exception {
//        results.add(runTest(new java.util.TreeMap<Integer, IntClassWithKey>()));
//    }
//
//    private int runTest(Map<Integer, IntClassWithKey> map) {
//        int result = 0;
//
//        // First, add a number of objects to the list.
//        for (int i = 0; i < COUNT; i++)
//            map = map.put(i, objects[i]);
//
//        for (int j = 0; j < 5; j++) {
//            for (int i : rnd) {
//                result += map.get(i % COUNT).key;
//            }
//        }
//
//        for (int i : rnd) {
//            map = map.remove(i);
//        }
//
//        return result + map.size();
//    }
//
//    private int runTest(java.util.Map<Integer, IntClassWithKey> map) {
//        int result = 0;
//
//        // First, add a number of objects to the list.
//        for (int i = 0; i < COUNT; i++)
//            map.put(i, objects[i]);
//
//        for (int j = 0; j < 5; j++) {
//            for (int i : rnd) {
//                result += map.get(i % COUNT).key;
//            }
//        }
//
//        for (int i : rnd) {
//            map.remove(i);
//        }
//
//        return result + map.size();
//    }
//
////    private int runTest(scala.collection.immutable.Map<Integer, IntClassWithKey> map) {
////        int result = 0;
////
////        // First, add a number of objects to the list.
////        for (int i = 0; i < COUNT; i++)
////            map = map.updated(i, objects[i]);
////
////        for (int j = 0; j < 5; j++) {
////            for (int i : rnd) {
////                int i1 = i % COUNT;
////                result += map.get(i1).getOrElse().key;
////            }
////        }
////
////        for (int i : rnd) {
////            map = (scala.collection.immutable.Map<Integer, IntClassWithKey>) map.$minus(i);
////        }
////
////        return result + map.size();
////    }
//
//    public static class IntClassWithKey {
//        int key;
//        int value;
//
//        public IntClassWithKey(Integer key, Integer value) {
//            this.key = key;
//            this.value = value;
//        }
//
//        protected final int improve(int hashCode) {
//            int h = hashCode + ~(hashCode << 9);
//            h = h ^ (h >>> 14);
//            h = h + (h << 4);
//            return h ^ (h >>> 10);
//        }
//
//        @Override
//        public String toString() {
//            return "IntClassWithKey{" +
//                    "hash=" + Integer.toBinaryString(improve(new Integer(key).hashCode())) +
//                    ", key=" + key +
//                    ", value=" + value +
//                    '}';
//        }
//    }
//
//}
