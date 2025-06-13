package pl.lodz.dormConnect.dorm.scheduler;

import java.util.concurrent.locks.ReentrantLock;

public class RoomAssignmentLock {
    public static final ReentrantLock LOCK = new ReentrantLock();
}
