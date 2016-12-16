package ru.javajava.mechanics;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import ru.javajava.mechanics.base.UserSnap;
import ru.javajava.mechanics.internal.ClientSnapService;
import ru.javajava.mechanics.internal.ServerSnapService;


import ru.javajava.mechanics.base.UserSnap;
import ru.javajava.mechanics.internal.ClientSnapService;
import ru.javajava.mechanics.internal.GameSessionService;
import ru.javajava.mechanics.internal.ServerSnapService;
import ru.javajava.mechanics.utils.TimeHelper;
import ru.javajava.services.AccountService;
import ru.javajava.websocket.RemotePointService;

import javax.annotation.PostConstruct;
import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import ru.javajava.services.AccountService;
import ru.javajava.websocket.Message;
import ru.javajava.websocket.RemotePointService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


/**
 * Created by ivan on 15.11.16.
 */
@Service
public class MechanicsExecutor {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientSnapService clientSnapshotsService;
    @Autowired
    private ServerSnapService serverSnapshotService;
    @Autowired
    private RemotePointService remotePointService;
    @Autowired
    private GameSessionService gameSessionService;

    private static final long STEP_TIME = 30;
    private static final int THREADS_NUM = 4;

    private final ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("Game-механики")
            .build();

    private final ExecutorService tickExecutors = Executors.newFixedThreadPool(THREADS_NUM, threadFactory);

    @PostConstruct
    public void initAfterStartup() {
        for (int i = 0; i < THREADS_NUM; ++i) {
            final GameMechanics gameMechanics = new GameMechanicsImpl(accountService,
                    serverSnapshotService, remotePointService, gameSessionService, clientSnapshotsService);
            final Worker worker = new Worker(gameMechanics);
            tickExecutors.execute(worker);
        }
    }

    public void addClientSnapshot(long forUser, UserSnap message) {

    public void addUser (long user) {

    }

    public void removeUser(long user) {

    }


    private static class Worker implements Runnable {

        private final GameMechanics gameMechanics;

        Worker(GameMechanics gameMechanics) {
            this.gameMechanics = gameMechanics;
        }

        private final Clock clock = Clock.systemDefaultZone();

        @Override
        public void run() {
            long lastFrameMillis = STEP_TIME;
            while (true) {
                final long before = clock.millis();

                try {
                    gameMechanics.gmStep(lastFrameMillis);
                }
                catch (NullPointerException e) {
                    System.out.println("Autowiring failed :(");
                    continue;
                }

                final long after = clock.millis();
                TimeHelper.sleep(STEP_TIME - (after - before));

                if (Thread.currentThread().isInterrupted()) {
                    gameMechanics.reset();
                    return;
                }
                final long afterSleep = clock.millis();
                lastFrameMillis = afterSleep - before;
            }
        }
    }
}
