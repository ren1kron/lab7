package com.ren1kron.server.managers;


import com.ren1kron.common.models.Worker;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Manages collection
 * @author ren1kron
 */

public class CollectionManager implements Iterable<Worker> {
    private int currentId = 1;
    private Map<Integer, Worker> keyMap = new LinkedHashMap<>();
//    private Map<Integer, Worker> idMap = new LinkedHashMap<>();
//    private Map<Organization, Map<Integer, Worker>> OrganizationMap = new LinkedHashMap<>();
//    private Set<Organization> organizations = new HashSet<>();
    private LocalDateTime lastInitTime;
    private DatabaseManager databaseManager;

    public CollectionManager(DatabaseManager databaseManager) {
        this.lastInitTime = null;
        this.databaseManager = databaseManager;
    }

    /**
     * @return map of workers
     */
    public synchronized Map<Integer, Worker> getKeyMap() {
        return keyMap;
    }

    /**
     * @return Worker by their id
     */
    public synchronized Worker byId(int id) {
//        return idMap.get(id);
        for (Worker worker : this) {
            if (worker.getId() == id) return worker;
        }
        return null;
    }

    /**
     * @return Worker by their key
     */
    public synchronized Worker byKey(Integer key) {
        return keyMap.get(key);
    }


    /**
     * Finds free ID
     * @return Free ID
     */
    public synchronized int getFreeId() {
        while (byId(currentId) != null)
            if (++currentId < 0)
                currentId = 1;
        return currentId;
    }


    public synchronized boolean isContain(Worker worker) {
        return worker == null || byKey(worker.getKey()) != null || byId(worker.getId()) != null;
    }


    /**
     * Adds worker to maps
     * @param worker added worker
     */
    public synchronized boolean add(Worker worker) {
        if (isContain(worker)) return false;
        if (databaseManager.addWorker(worker) > 0) {
            keyMap.put(worker.getKey(), worker);
            update();
            return true;
        } else return false;
    }

    /**
     * Removes worker from maps by its key
     * @param key worker's key
     * @return true if worker was successfully removed
     and false if worker was never found
     */
    public synchronized boolean removeByKey(Integer key) {
        var worker = byKey(key);
        if (worker == null) return false;
        if (!databaseManager.removeByKey(key)) return false;
        keyMap.remove(key);
//        idMap.remove(worker.getId());
        update();
        return true;
    }

    /**
     * Removes all elements created by the specified user from keyMap and database
     * @param username the name of user whose elements will be deleted
     */
    public synchronized void removeAllByUser(String username) {
        databaseManager.removeByUser(username);
        for (Worker worker : this) {
            if (worker.getUsername().equals(username)) {
                int key = worker.getKey();
//                databaseManager.removeByKey(key);
                keyMap.remove(key);
            }
        }
    }

//    /**
//     * Removes worker from maps
//     * @param worker worker
//     * @return true if worker was successfully removed
//    and false if worker was never found
//     */
//    public boolean remove(Worker worker) {
//        if (worker == null || !keyMap.containsValue(worker)) return false;
//        keyMap.remove(worker.getKey());
//        idMap.remove(worker.getId());
//        return true;
//    }

    /**
     * Sorts collection
     */
    public synchronized void update() {
        Map<Integer, Worker> sortedMap = keyMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        keyMap.clear();
        keyMap.putAll(sortedMap);
    }

    /**
     * Downloads collection from file
     * @return true if collection was downloaded successfully
     */
    public synchronized boolean init() {
//        idMap.clear();
        keyMap.clear();
//        dumpManager.readCsv(keyMap);
        keyMap = databaseManager.getWorkers();
        lastInitTime = LocalDateTime.now();
//        for (Worker worker : keyMap.values()) {
////            if (byId(e.getId()) != null) {
//            if (!worker.validate()) {
//                keyMap.clear();
////                idMap.clear();
////                throw new RuntimeException();
//                return false;
//            }
////            else {
//////                idMap.put(e.getId(), e);
////                keyMap.put(e.getKey(), e);
////            }
//        }
        update();
        return true;
    }



    public synchronized LocalDateTime getLastInitTime() {
        return lastInitTime;
    }


    @Override
    public synchronized String toString() {
        if (keyMap.isEmpty()) return "Collection is empty";
        StringBuilder info = new StringBuilder();
        for (var worker : keyMap.values()) {
            info.append(worker.toString()).append("\n");
        }
        return info.toString().trim();
    }


    @Override
    public synchronized Iterator<Worker> iterator() {
        return new WorkerIterator(new LinkedHashMap<>(keyMap)); //
        // итератор принимает не основную коллекцию, а её копию. Это сделает итерации потокобезопасными

    }
    private static class WorkerIterator implements Iterator<Worker> {
        private Iterator<Worker> iterator;

        public WorkerIterator(Map<Integer, Worker> keyMap) {
            this.iterator = keyMap.values().iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Worker next() {
            return iterator.next();
        }

    }
}
