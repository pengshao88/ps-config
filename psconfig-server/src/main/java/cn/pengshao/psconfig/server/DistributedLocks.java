package cn.pengshao.psconfig.server;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Description: 分布式锁
 *
 * @Author: yezp
 * @date 2024/5/14 22:39
 */
@Slf4j
@Component
public class DistributedLocks {

    @Autowired
    DataSource dataSource;

    Connection connection;

    private AtomicBoolean locked = new AtomicBoolean(false);
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void init() {
        try {
            // 这个连接专门用来获取锁
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        executor.scheduleWithFixedDelay(this::tryLock, 1000, 5000, TimeUnit.MILLISECONDS);
    }

    private void tryLock() {
        try {
            lock();
            locked.set(true);
        } catch (Exception ex) {
            log.info("[PSCONFIG] ===>>>> lock failed...");
            locked.set(false);
        }
    }

    private boolean lock() throws SQLException {
        // 一直不提交，占用锁
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        connection.createStatement().execute("set innodb_lock_wait_timeout=5");
        // lock 5s
        connection.createStatement().execute("select app from locks where id=1 for update");

        if (locked.get()) {
            // 可重入锁
            log.debug("[PSCONFIG] ===>>>> reenter this dist lock.");
        } else {
            log.info("[PSCONFIG] ===>>>> get dist lock.");
            // TODO 如果是切换主，则需要重新加载数据库的数据
        }
        return true;
    }

    @PreDestroy
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
                connection.close();
            }
        } catch (Exception ex) {
            log.info("[PSCONFIG] ignore this close exception");
        }
    }

}
