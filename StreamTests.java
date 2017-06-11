import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.Test;

public class StreamTests {
    private static final List<String> THE_SORT_SLIST = Arrays.asList("One", "Two", "Three", "Four", "Five");       
    private static final List<Integer> THE_SORT_LIST = Arrays.asList(1, 2, 3, 4, 5);

    /**
     * allMatch: Returns whether all elements of a stream match the provided predicate.
     * 
     * Signature: boolean allMatch(Predicate<? super T> predicate)
     */
    @Test
    public void testAllMatch() {
	// Using a lambda expression
	boolean result = THE_SORT_SLIST.stream().allMatch(s -> s.isEmpty());
	assertFalse(result);
	// Using a method reference
	result = THE_SORT_SLIST.stream().allMatch(String::isEmpty);
	assertFalse(result);
    }
    
    /**
     * anyMatch: Returns whether any elements of a stream match the provided predicate.
     * 
     * Signature: boolean anyMatch(Predicate<? super T> predicate)
     */
    @Test
    public void testAnyMatch() {
	// Using a lambda expression
	boolean result = THE_SORT_SLIST.stream().anyMatch(s -> s.isEmpty());
	assertFalse(result);
	// Using a method reference
	result = THE_SORT_SLIST.stream().anyMatch(String::isEmpty);
	assertFalse(result);
    }
    
    /**
     * Builder: Returns a builder for a Stream.
     * 
     * Signature: static <T> Stream.Builder<T> builder()
     */
    @Test
    public void testBuilder() {
	Stream<String> stream = Stream.<String>builder().add("One").add("Two").add("Three").build();
	assertTrue(stream.count() > 0);
    }
    
    /**
     * Collect (1): Performs a mutable reduction operation on the elements of a stream using a Collector.
     * 
     * Signature: <R,A> R collect(Collector<? super T,A,R> collector) 
     */
    @Test
    public void testCollect() {
	Set<String> set = THE_SORT_SLIST.stream().collect(Collectors.toSet());
	assertEquals(THE_SORT_SLIST.size(), set.size());
    }
    
    /**
     * Collect (2): Performs a mutable reduction operation on the elements of a stream.
     * 
     * Signature: <R> R collect(Supplier<R> supplier, BiConsumer<R,? super T> accumulator, BiConsumer<R,R> combiner) 
     */
    @Test
    public void testCollectSupplier() {
	Set<String> set = THE_SORT_SLIST.stream().collect(HashSet::new, Set::add, Set::addAll);
	assertEquals(THE_SORT_SLIST.size(), set.size());
    }
    
    /**
     * Concat: Creates a lazily concatenated stream whose elements are all the elements of the first stream followed by all the elements of the second stream.
     * 
     * Concat: static <T> Stream<T> concat(Stream<? extends T> a, Stream<? extends T> b) 
     */
    @Test
    public void testConcat() {
	Stream<String> s1 = Stream.of("One", "Three", "Five", "Seven", "Nine");
	Stream<String> s2 = Stream.of("Two", "Four", "Six", "Eight", "Ten");
	String concat = Stream.concat(s1, s2).collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
	assertEquals("OneThreeFiveSevenNineTwoFourSixEightTen", concat);
    }
        
    /**
     * Distinct: Returns a stream consisting of the distinct elements (according to Object.equals(Object)) of a stream.
     * 
     * Signature: Stream<T> distinct() 
     */
    @Test
    public void testDistinct() {
	Stream<Integer> si = Stream.<Integer>builder().add(1).add(1).add(1).add(1).add(2).add(2).add(3).add(4).add(5).add(5).build();
	Stream<Integer> sd = si.distinct();
	assertEquals(5, sd.count());		// 1, 2, 3, 4, 5
    }
    
    /*
     * Filter: Returns a stream consisting of the elements of a stream that match the given predicate.
     * 
     * Signature: Stream<T> filter(Predicate<? super T> predicate) 
     */
    @Test
    public void testFilter() {
	Set<String> set = THE_SORT_SLIST.stream().filter(s -> s.startsWith("T")).collect(Collectors.toSet());
	assertEquals(2, set.size());		// "Two" && "Three"
    }
    
    /*
     * FindAny: Returns an Optional describing some element of the stream, or an empty Optional if the stream is empty.
     * 
     * Signature: Optional<T> findAny()
     */
    @Test
    public void testFindAny() {
	Optional<String> opt = THE_SORT_SLIST.stream().findAny();
	assertTrue(opt.isPresent());
    }

    /*
     * findFirst: Returns an Optional describing the first element of this stream, or an empty Optional if the stream is empty.
     * 
     * Signature: Optional<T> findFirst()
     */
    @Test
    public void testFindFirst() {
	Optional<String> opt = THE_SORT_SLIST.stream().findFirst();
	assertTrue(opt.isPresent());
	assertEquals("One", opt.get());
    }
    
    /*
     * flatMap: Returns a stream consisting of the results of replacing each element of a stream with the contents of a mapped stream produced by applying 
     * the provided mapping function to each element.
     * 
     * Signature: <R> Stream<R> flatMap(Function<? super T,? extends Stream<? extends R>> mapper) 
     */
    @Test
    public void testFlatMap() {
	List<List<String>> theTenList = new ArrayList<>();
	theTenList.add(Arrays.asList("One", "Three", "Five", "Seven", "Nine"));
	theTenList.add(Arrays.asList("Two", "Four", "Six", "Eight", "Ten"));
	Stream<String> ss = theTenList.stream().flatMap(list -> list.stream().filter(s -> s.startsWith("T")));
	assertEquals(3, ss.count());		// "Three", "Two" & "Ten"
    }
    
    /*
     * flatMapToDouble: Returns an DoubleStream consisting of the results of replacing each element of a stream with the contents of a mapped stream produced 
     * by applying the provided mapping function to each element.
     * 
     * Signature: DoubleStream flatMapToDouble(Function<? super T,? extends DoubleStream> mapper) 
     */
    @Test
    public void testFlatMapToDouble() {
	List<List<String>> theTenList = new ArrayList<>();
	theTenList.add(Arrays.asList("One", "Three", "Five", "Seven", "Nine"));
	theTenList.add(Arrays.asList("Two", "Four", "Six", "Eight", "Ten"));
	DoubleStream ds = theTenList.stream().flatMapToDouble(list -> list.stream().mapToDouble(s -> s.length()));
	assertEquals(39d, ds.sum(), 0);		// 39 = 3, 5, 4, 5, 4, 3, 4, 3, 5, 3
    }
    
    /*
     * flatMapToInt: Returns an IntStream consisting of the results of replacing each element of a stream with the contents of a mapped stream produced 
     * by applying the provided mapping function to each element.
     * 
     * Signature: IntStream flatMapToInt(Function<? super T,? extends IntStream> mapper) 
     */
    @Test
    public void testFlatMapToInt() {
	List<List<Integer>> mlist = Arrays.asList(Arrays.asList(1,2,3,4,5), Arrays.asList(6,7,8,9,10));
	IntStream ds = mlist.stream().flatMapToInt(l -> l.stream().mapToInt(i -> i));
	assertEquals(55, ds.sum(), 0);		// 55 = 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 + 10
    }
    
    /*
     * flatMapToLong: Returns an LongStream consisting of the results of replacing each element of a stream with the contents of a mapped stream produced 
     * by applying the provided mapping function to each element.
     * 
     * Signature: LongStream flatMapToLong(Function<? super T,? extends LongStream> mapper) 
     */
    @Test
    public void testFlatMapToLong() {
	List<List<Long>> mlist = Arrays.asList(Arrays.asList(1l,2l,3l,4l,5l), Arrays.asList(6l,7l,8l,9l,10l));
	LongStream ds = mlist.stream().flatMapToLong(l -> l.stream().filter(n -> n % 2 == 0).mapToLong(i -> i));
	assertEquals(30l, ds.sum());		// 30 = 2 + 4 + 6 + 8 + 10
    }
    
    /**
     * ForEach: Performs an action for each element of a stream.
     * 
     * Signature: void forEach(Consumer<? super T> action) 
     */
    @Test
    public void testForEach() {
	StringBuilder sb = new StringBuilder();  
	THE_SORT_SLIST.stream().forEach(s -> sb.append(s));
	assertEquals("OneTwoThreeFourFive", sb.toString());
    }
    
    /**
     * forEachOrdered: Performs an action for each element of a stream, in the encounter order of the stream if the stream has a defined encounter order.
     * 
     * Signature: void forEachOrdered(Consumer<? super T> action) 
     */
    @Test
    public void testForEachOrdered() {
	List<Integer> list = new ArrayList<>();
	for (int i = 0; i < 10000; i++) {
	    list.add(i);
	}
	// Append in order
	StringBuffer sbOrdered = new StringBuffer();  
	list.parallelStream().forEachOrdered(s -> sbOrdered.append(s));
	// Append without order
	StringBuffer sbUnordered = new StringBuffer();
	list.parallelStream().forEach(s -> sbUnordered.append(s));
	// Strings should be different
	assertFalse(sbOrdered.toString().equals(sbUnordered.toString()));
    }

    /**
     * generate: Returns an infinite sequential unordered stream where each element is generated by the provided Supplier.
     * 
     * Signature: static <T> Stream<T> generate(Supplier<T> s) 
     */
    @Test
    public void testGenerate() {
	List<Double> list = Stream.generate(() -> Math.random()).limit(10).collect(Collectors.toList());
	assertEquals(10, list.size());
    }
    
    /**
     * iterate: Returns an infinite sequential ordered stream produced by iterative application of a function f to an initial element seed, producing a stream consisting of seed, f(seed), f(f(seed)), etc.
     * 
     * Signature: static <T> Stream<T> iterate(T seed, UnaryOperator<T> f) 
     */
    @Test
    public void testIterate() {
	// To generate an ordered list from 0 to 9
	List<Integer> list = Stream.iterate(0, n -> n + 1).limit(10).collect(Collectors.toList());
	assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", list.toString());	
	// To generate a list with alternated values of 0 and 1
	list = Stream.iterate(0, n -> n == 0 ? 1 : 0).limit(10).collect(Collectors.toList());
	assertEquals("[0, 1, 0, 1, 0, 1, 0, 1, 0, 1]", list.toString());	
    }
    
    /**
     * limit: Returns a stream consisting of the elements of this stream, truncated to be no longer than maxSize in length.
     * 
     * Signature: Stream<T> limit(long maxSize) 
     */
    @Test
    public void testLimit() {
	List<String> list = THE_SORT_SLIST.stream().limit(3).collect(Collectors.toList());
	assertEquals("One", list.get(0));
	assertEquals("Two", list.get(1));
	assertEquals("Three", list.get(2));
    }
    
    /**
     * Map: Returns a stream consisting of the results of applying a function to the elements of a stream.
     * 
     * Signature: <R> Stream<R> map(Function<? super T,? extends R> mapper) 
     */
    @Test
    public void testMap() {
	// Using a lambda expression
	Set<String> set = THE_SORT_SLIST.stream().map(s -> s.toUpperCase()).collect(Collectors.toSet());
	assertEquals(THE_SORT_SLIST.size(), set.size());
	assertTrue(set.contains("THREE"));
	// Using a method reference
	set = THE_SORT_SLIST.stream().map(String::toUpperCase).collect(Collectors.toSet());
	assertEquals(THE_SORT_SLIST.size(), set.size());
	assertTrue(set.contains("FIVE"));
    }
    
    /**
     * Max: Returns the maximum element of a stream according to the provided Comparator.
     * 
     * Signature: Optional<T> max(Comparator<? super T> comparator) 
     */
    @Test
    public void testMax() {
	Optional<String> op = Stream.of("Zero", "Eleven", "One").max(Comparator.comparing(String::length));
	assertEquals("Eleven", op.get());
    }
    
    /**
     * Min: Returns the minimum element of a stream according to the provided Comparator.
     * 
     * Signature: Optional<T> min(Comparator<? super T> comparator) 
     */
    @Test
    public void testMin() {
	Optional<String> op = Stream.of("Zero", "Eleven", "One").min(Comparator.comparing(String::length));
	assertEquals("One", op.get());
    }
    
    /**
     * NoneMatch: Returns whether no elements of this stream match the provided predicate.
     * 
     * Signature: boolean noneMatch(Predicate<? super T> predicate) 
     */
    @Test
    public void testNoneMatch() {
	boolean result = THE_SORT_SLIST.stream().noneMatch(s -> s.equals("Zero"));
	assertTrue(result);
    }
    
    /**
     * Of: Returns a sequential ordered stream whose elements are the specified values.
     * 
     * Signature: static <T> Stream<T> of(T... values)
     */
    @Test
    public void testOf() {
	Stream<String> ss = Stream.of("Zero", "Eleven", "Twelve");
	assertEquals(3, ss.count());
    }
    
    /**
     * Peek: Returns a stream consisting of the elements of a stream, additionally performing the provided action on each element as elements are
     * consumed from the resulting stream. This method was provided to support debugging and, because it works by means of side-effects, should not be
     * used for any other purpose.
     * 
     * Signature: Stream<T> peek(Consumer<? super T> action)
     */
    @Test
    public void testPeek() {
	StringBuilder sbLowercase = new StringBuilder();  
	StringBuilder sbUppercase = new StringBuilder();
	long count = THE_SORT_SLIST.stream().map(String::toLowerCase).peek(s -> sbLowercase.append(s)).map(String::toUpperCase).peek(s -> sbUppercase.append(s)).count();
	assertEquals(5, count);
	assertEquals("onetwothreefourfive", sbLowercase.toString());
	assertEquals("ONETWOTHREEFOURFIVE", sbUppercase.toString());
    }
	
    /**
     * Reduce (1): Performs a reduction on the elements of a stream, using an associative accumulation function, and returns an Optional describing
     * the reduced value, if any.
     * 
     * Signature: Optional<T> reduce(BinaryOperator<T> accumulator)
     */
    @Test
    public void testReduceAccumulator() {
	// Using sequential stream
	Optional<String> op = THE_SORT_SLIST.stream().reduce((s1, s2) -> {
	    return s1 + "," + s2;
	});
	assertEquals("One,Two,Three,Four,Five", op.get());
	// Using parallel stream
	op = THE_SORT_SLIST.parallelStream().reduce((s1, s2) -> {
	    return s1 + "," + s2;
	});
	assertEquals("One,Two,Three,Four,Five", op.get());
    }
    
    /**
     * Reduce (2): Performs a reduction on the elements of this stream, using the provided identity value and an associative accumulation function,
     * and returns the reduced value.
     * 
     * Signature: T reduce(T identity, BinaryOperator<T> accumulator)
     */
    @Test
    public void testReduceIdentityAccumulator() {
	// NOTE: Below examples are dependent about their sequential or parallel execution. Examples are coded with the intention to show an scenario
	// might happend, however this behaviour is something we should try to avoid.
	//
	// 1) Using sequential stream
	int total = THE_SORT_LIST.stream().reduce(10, (n1, n2) -> {    
	    return n1 + n2;
	});
	assertEquals(10 + 1 + 2 + 3 + 4 + 5, total);
	// 2) Using parallel stream
	total = THE_SORT_LIST.parallelStream().reduce(10, (n1, n2) -> {    
	    return n1 + n2;
	});
	assertEquals(11 + 12 + 13 + 14 + 15, total);
    }
    
    /**
     * Reduce (3): Performs a reduction on the elements of this stream, using the provided identity, accumulation and combining functions.
     * 
     * Signature: <U> U<T> reduce(U identity, BiFunction<U,? super T,U> accumulator, BinaryOperator<U> combiner)
     */
    @Test
    public void testReduceIdentityAccumulatorCombiner() {
	// NOTE: Below examples are dependent about their sequential or parallel execution. Examples are coded with the intention to show an scenario
	// might happend, however this behaviour is something we should try to avoid.
	//
	// 1) Using sequential stream (Identity = 10)
	int total = THE_SORT_LIST.stream().reduce(10, (n1, n2) -> {
	    return n1 + n2;
	}, (n1, n2) -> {
	    return n1 * n2;
	});
	assertEquals(10 + 1 + 2 + 3 + 4 + 5, total);
	// 2) Using parallel stream (Identity = 10)
	total = THE_SORT_LIST.parallelStream().reduce(10, (n1, n2) -> {
	    return n1 + n2;
	}, (n1, n2) -> {
	    return n1 * n2;
	});
	assertEquals( (10 + 1) * (10 + 2) * (10 + 3) * (10 + 4) * (10 + 5), total);
    }

    /**
     * skip: Returns a stream consisting of the remaining elements of this stream after discarding the first n elements of the stream.
     * 
     * Signature: Stream<T> skip(long n) 
     */
    @Test
    public void testSkip() {
	List<String> list = THE_SORT_SLIST.stream().skip(3).collect(Collectors.toList());
	assertEquals("Four", list.get(0));
	assertEquals("Five", list.get(1));
    }
    
    /**
     * sorted (1): Returns a stream consisting of the elements of this stream, sorted according to natural order.
     * 
     * Signature: Stream<T> sorted() 
     */
    @Test
    public void testSorted() {
	List<String> list = THE_SORT_SLIST.stream().sorted().collect(Collectors.toList());
	assertEquals("Five", list.get(0));
	assertEquals("Four", list.get(1));
	assertEquals("One", list.get(2));
	assertEquals("Three", list.get(3));
	assertEquals("Two", list.get(4));
    }

    /**
     * sorted (2): Returns a stream consisting of the elements of this stream, sorted according to the provided Comparator.
     * 
     * Signature: Stream<T> sorted(Comparator<? super T> comparator)  
     */
    @Test
    public void testSortedComparator() {
	List<String> unsortedList = Arrays.asList("ccczz", "eeeee", "bbzzz", "ddddz", "azzzz");
	// Using an anonymous comparator
	List<String> sortedList = unsortedList.stream().sorted((e1, e2) -> { return e1.compareTo(e2); }).collect(Collectors.toList());
	assertEquals("azzzz", sortedList.get(0));
	assertEquals("bbzzz", sortedList.get(1));
	assertEquals("ccczz", sortedList.get(2));
	assertEquals("ddddz", sortedList.get(3));
	assertEquals("eeeee", sortedList.get(4));
	// Using Comparator.comparing
	sortedList = unsortedList.stream().sorted(Comparator.comparing(String::toString)).collect(Collectors.toList());
	assertEquals("azzzz", sortedList.get(0));
	assertEquals("bbzzz", sortedList.get(1));
	assertEquals("ccczz", sortedList.get(2));
	assertEquals("ddddz", sortedList.get(3));
	assertEquals("eeeee", sortedList.get(4));
    }

}
