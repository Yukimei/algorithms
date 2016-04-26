package com.cloudcomputing.samza.pitt_cabs;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Deque;
import java.io.IOException;
import java.util.HashMap;

import org.apache.samza.config.Config;
import org.apache.samza.storage.kv.KeyValueIterator;
import org.apache.samza.storage.kv.KeyValueStore;
import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.system.OutgoingMessageEnvelope;
import org.apache.samza.task.InitableTask;
import org.apache.samza.task.MessageCollector;
import org.apache.samza.task.StreamTask;
import org.apache.samza.task.TaskContext;
import org.apache.samza.task.TaskCoordinator;
import org.apache.samza.task.WindowableTask;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
 * Consumes the stream of driver location updates and rider cab requests.
 * Outputs a stream which joins these 2 streams and gives a stream of rider to
 * driver matches.
 */
public class DriverMatchTask implements StreamTask, InitableTask, WindowableTask {

	/* Define per task state here. (kv stores etc) */
	private KeyValueStore<String, Map<String, Object>> driverLoc;
	private KeyValueStore<String, Map<String, Object>> driverList;
	
	private static final double DISTANCE_WEIGHT = 0.4;
	private static final double GENDER_WEIGHT = 0.2;
	private static final double RATING_WEIGHT = 0.2;
	private static final double SALARY_WEIGHT = 0.2;
	private static final double MAX_DIST = 500.0;

	// data structure for bonus part
	private static Map<String, Deque<String>> blockRequestStore;
	private static Map<String, Integer> availableDriver;

	@Override
	@SuppressWarnings("unchecked")
	public void init(Config config, TaskContext context) throws Exception {
		// Initialize stuff (maybe the kv stores?)
		driverLoc = (KeyValueStore<String, Map<String, Object>>) context.getStore("driver-loc");
		driverList = (KeyValueStore<String, Map<String, Object>>) context.getStore("driver-list");
		
		// innitialize bonus part
		blockRequestStore = new HashMap<String, Deque<String>>();
		availableDriver = new HashMap<String, Integer>();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void process(IncomingMessageEnvelope envelope, MessageCollector collector, TaskCoordinator coordinator) {
		// The main part of your code. Remember that all the messages for a
		// particular partition
		// come here (somewhat like MapReduce). So for task 1 messages for a
		// blockId will arrive
		// at one task only, thereby enabling you to do stateful stream
		// processing.
		String incomingStream = envelope.getSystemStreamPartition().getStream();

		Map<String, Object> message = null;
		message = (HashMap<String, Object>) envelope.getMessage();

		if (message != null && incomingStream.equals(DriverMatchConfig.EVENT_STREAM.getStream())) {
			processDriverEvent(message, collector);
		} else if (message != null && incomingStream.equals(DriverMatchConfig.DRIVER_LOC_STREAM.getStream())) {
			processDriverLocation(message);
		} else {
			throw new IllegalStateException("Unexpected input stream: " + envelope.getSystemStreamPartition());
		}
	}

	@Override
	public void window(MessageCollector collector, TaskCoordinator coordinator) {
		// this function is called at regular intervals, not required for this
		// project
	}

	// bonus part
	private void processDriverEvent(Map<String, Object> message, MessageCollector collector) {

		 String blockId = String.valueOf(message.get("blockId")); 
		 String status = String.valueOf(message.get("status")); 
		 String type = String.valueOf(message.get("type")); 
		
		 if( type != null && type.equalsIgnoreCase("ENTERING_BLOCK") && status.equalsIgnoreCase("AVAILABLE")){
			 String driverId = String.valueOf(message.get("driverId")); 
			 driverLoc.put(driverId, message);
			 
			 //bonus part
			 if (!blockRequestStore.containsKey(blockId)) {
				 blockRequestStore.put(blockId, 1);
			 } else {
				 blockRequestStore.put(blockId, blockRequestStore.get(blockId) + 1);
			 }
			
		 }else if(type != null && (type.equalsIgnoreCase("LEAVING_BLOCK") || status.equalsIgnoreCase("UNAVAILABLE"))){
			 String driverId = String.valueOf(message.get("driverId")); 
			 // null的话会不会出问题， 如果driverLoc里本来没有这个driverId的话？？
			 driverLoc.delete(driverId);
			 
			 //bonus part
			 if (status.equalsIgnoreCase("AVAILABLE")) {
				 blockRequestStore.put(blockId, blockRequestStore.get(blockId) - 1);
			 }
			 
		 } else if (type != null && type.equalsIgnoreCase("RIDE_REQUEST")) {
			 String gender_preference = String.valueOf(message.get("gender_preference")); 
			 String rating = String.valueOf(message.get("rating")); 
			 String salary = String.valueOf(message.get("salary")); 
			 String clientId = String.valueOf(message.get("clientId")); 
			 String latitude = String.valueOf(message.get("latitude")); 
			 String longitude = String.valueOf(message.get("longitude")); 
			 
			 KeyValueStore<String, Map<String, Object>> iter = driverLoc.all();
			 double maxMatchScore = DOUBLE.MIN_VALUE;
			 String matchResultDriverId = null;
			 
			 while (iter.hasNext()) {
				 Map<String, Object> obj = (Map<String, Object>) iter.next().getValue();
				 if (obj == null || obj.size() == 0) {
					 continue;
				 }
				 
				 String curblockId = String.valueOf(obj.get("blockId"));
				 if (!curblockId.equals(blockId)) {
					 continue;
				 }
				 
				 String curlatitude = String.valueOf(obj.get("latitude")); 
				 String curlongitude = String.valueOf(obj.get("longitude")); 
				 
				 double client_driver_distance = Math.sqrt(Math.pow((Double.parseDouble(curlatitude)-Double.parseDouble(latitude)), 2) + 
						  Math.pow((Double.parseDouble(curlongitude)-Double.parseDouble(longitude)), 2) );
				 double distance_score = 1 - client_driver_distance / MAX_DIST;
				 
				 String curgender = String.valueOf(obj.get("gender")); 
				 double gender_score = gender.equals("N") ? 1.0 : curgender.equals(gender) ? 1.0 : 0.0 ;
				 
				 String currating = String.valueOf(message.get("rating")); 
				 double rating_score = Double.parseDouble(currating) / 5.0;
				 
				 String cursalary = String.valueOf(message.get("salary")); 
				 double salary_score = 1 - Double.parseDouble(cursalary) / 100.0;
				 double match_score = distance_score * DISTANCE_WEIGHT 
						 + rating_score * RATING_WEIGHT + salary_score * SALARY_WEIGHT + gender_score * GENDER_WEIGHT;
				 
				 if (match_score > maxMatchScore) {
					 maxMatchScore = match_score;
					 matchResultDriverId =  String.valueOf(obj.get("driverId"))
				 }
			 }
			 //no match, return
			 if (matchResultDriverId == null) {
				 return;
			 }
			 
			  //remove matched driver
			  driverLocation.delete(matchResultDriverId); 
			  
			  //bonus
			  int curAvailableDriverNum = availableDriver.get(blockId);
			  if (!blockRequestStore.containsKey(blockId)) {
				  blockRequestStore.put(blockId, new Element());
			  }
			  blockRequestStore.get(blockId).update(curAvailableDriverNum);
			  Double spf = null;
			  int size = blockRequestStore.getSize();
			  if (size < 5) {
				  spf = 1.0;
			  } else if (blockRequestStore.getA() >= 3.6) {
				  spf = 1.0;
			  } else if (blockRequestStore.getA() < 3.6){
				  spf = 1.0 + (4 * (3.6 - blockRequestStore.getA())/(1.8 - 1));
			  }
			  availableDriver.put(blockId, curAvailableDriverNum - 1);
			  
			  message.clear();
			  message.put("clientId", clientId);
			  message.put("driverId", matchResultDriverId);
			  message.put("priceFactor", String.valueOf(spf));
			  collector.send(new OutgoingMessageEnvelope(DriverMatchConfig.MATCH_STREAM, message));
			  
			  //complete doesnot has event
		 } else if (type != null && type.equalsIgnoreCase("RIDE_COMPLETE")) {
			 String driverId = String.valueOf(message.get("driverId")); 
			 message.put("status", "AVAILABLE");
			 driverLoc.put(driverId, message);
			 
			 //bonus 
			 availableDriver.put(blockId, availableDriver.get(blockId) + 1);
		 }
	}

	private void processDriverLocation(Map<String, Object> message) {
		String driverId = String.valueOf(message.get("driverId"));
		driverLoc.put(driverId, message);
	}

	class Element {
		Deque<Integer> queue;
		int sum;
		Element() {
			queue = new LinkedList<Integer>();
			sum = 0;
		}
		void update(int availableNum) {
			if (queue.size() < 5) {
				queue.offerLast(availableNum);
				sum += availableNum;
			} else {
				int oldest = queue.pollFirst();
				sum = sum - oldest + availableNum;
				queue.offerLast(availableNum);
			}
		}
		int getSize() {
			return queue.size();
		}
		double getA() { 
			return sum / 5.0;
		}
	}
	
	//task1
//	private void processDriverEvent(Map<String, Object> message, MessageCollector collector) {
//		 
//		 String status = String.valueOf(message.get("status")); 
//		 String type = String.valueOf(message.get("type")); 
//		
//		 if( type != null && type.equalsIgnoreCase("ENTERING_BLOCK") && status.equalsIgnoreCase("AVAILABLE")){
//			 String driverId = String.valueOf(message.get("driverId")); 
//			 driverLoc.put(driverId, message);
//		 }else if(type != null && (type.equalsIgnoreCase("LEAVING_BLOCK") || status.equalsIgnoreCase("UNAVAILABLE"))){
//			 String driverId = String.valueOf(message.get("driverId")); 
//			 driverLoc.delete(driverId);
//		 } else if (type != null && type.equalsIgnoreCase("RIDE_REQUEST")) {
//			 String gender_preference = String.valueOf(message.get("gender_preference")); 
//			 String rating = String.valueOf(message.get("rating")); 
//			 String salary = String.valueOf(message.get("salary")); 
//			 String blockId = String.valueOf(message.get("blockId")); 
//			 String clientId = String.valueOf(message.get("clientId")); 
//			 String latitude = String.valueOf(message.get("latitude")); 
//			 String longitude = String.valueOf(message.get("longitude")); 
//			 
//			 KeyValueStore<String, Map<String, Object>> iter = driverLoc.all();
//			 double maxMatchScore = DOUBLE.MIN_VALUE;
//			 String matchResultDriverId = null;
//			 
//			 while (iter.hasNext()) {
//				 Map<String, Object> obj = (Map<String, Object>) iter.next().getValue();
//				 if (obj == null || obj.size() == 0) {
//					 continue;
//				 }
//				 
//				 String curblockId = String.valueOf(obj.get("blockId"));
//				 if (!curblockId.equals(blockId)) {
//					 continue;
//				 }
//				 
//				 String curlatitude = String.valueOf(obj.get("latitude")); 
//				 String curlongitude = String.valueOf(obj.get("longitude")); 
//				 
//				 double client_driver_distance = Math.sqrt(Math.pow((Double.parseDouble(curlatitude)-Double.parseDouble(latitude)), 2) + 
//						  Math.pow((Double.parseDouble(curlongitude)-Double.parseDouble(longitude)), 2) );
//				 double distance_score = 1 - client_driver_distance / MAX_DIST;
//				 
//				 String curgender = String.valueOf(obj.get("gender")); 
//				 double gender_score = gender.equals("N") ? 1.0 : curgender.equals(gender) ? 1.0 : 0.0 ;
//				 
//				 String currating = String.valueOf(message.get("rating")); 
//				 double rating_score = Double.parseDouble(currating) / 5.0;
//				 
//				 String cursalary = String.valueOf(message.get("salary")); 
//				 double salary_score = 1 - Double.parseDouble(cursalary) / 100.0;
//				 double match_score = distance_score * DISTANCE_WEIGHT 
//						 + rating_score * RATING_WEIGHT + salary_score * SALARY_WEIGHT + gender_score * GENDER_WEIGHT;
//				 
//				 if (match_score > maxMatchScore) {
//					 maxMatchScore = match_score;
//					 matchResultDriverId =  String.valueOf(obj.get("driverId"))
//				 }
//			 }
//			 
//			//remove matched driver
//			  driverLocation.delete(matchResultDriverId); 
//			  message.clear();
//			  message.put("clientId", clientId);
//			  message.put("driverId", matchResultDriverId);
//			  collector.send(new OutgoingMessageEnvelope(DriverMatchConfig.MATCH_STREAM, message));
//		 } else if (type != null && type.equalsIgnoreCase("RIDE_COMPLETE")) {
//			 String driverId = String.valueOf(message.get("driverId")); 
//	 		 message.put("status", "AVAILABLE");
//			 driverLoc.put(driverId, message);
//		 }
//	}
}
