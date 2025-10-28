/*
 * package com.coint.cointcontrol;
 * public class ServerTaskScheduler {
 * private static final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();
 * public static void schedule(Runnable task) {
 * tasks.add(task);
 * }
 * @SubscribeEvent
 * public void onServerTick(TickEvent.ServerTickEvent event) {
 * if (event.phase == TickEvent.Phase.END) {
 * while (!tasks.isEmpty()) {
 * try {
 * tasks.poll()
 * .run();
 * } catch (Exception e) {
 * e.printStackTrace();
 * }
 * }
 * }
 * }
 * }
 */
