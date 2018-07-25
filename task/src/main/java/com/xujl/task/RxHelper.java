package com.xujl.task;

public class RxHelper {
    /**
     * 倒计时任务
     */
    public static abstract class CountDownTask extends Task {
        private long mils;
        private long space;

        /**
         *
         * @param mils 倒计时总时间
         * @param space 倒计时间隔
         */
        public CountDownTask (long mils, long space) {
            this.mils = mils;
            this.space = space;
        }

        public CountDownTask (boolean needCancel, long mils, long space) {
            super(needCancel);
            this.mils = mils;
            this.space = space;
        }

        @Override
        public void run (Emitter emitter) throws Exception {
            long count = mils;
            while (count > 0) {
                Thread.sleep(space);
                count -= space;
                emitter.next(count);
            }
        }

        @Override
        public void onNext (Object object) {
            super.onNext(object);
            count(((Long) object));
        }

        public abstract void count (long time);
    }

    /**
     * 循环任务
     */
    public static abstract class RecycleTask extends Task {
        private long space;
        private int maxCount;

        public RecycleTask (boolean needCancel, long space, int maxCount) {
            super(needCancel);
            this.space = space;
            this.maxCount = maxCount;
        }

        /**
         *
         * @param space  循环间隔
         * @param maxCount 循环总次数，-1表示无限循环
         */
        public RecycleTask (long space, int maxCount) {
            this.space = space;
            this.maxCount = maxCount;
        }

        @Override
        public void run (Emitter emitter) throws Exception {
            int count = 1;
            while ((maxCount == -1 || count <= maxCount) && !isDestroy()) {
                Thread.sleep(space);
                emitter.next(count);
                count++;
            }
        }

        @Override
        public void onNext (Object object) {
            super.onNext(object);
            count(((Integer) object));
        }

        public abstract void count (int count);
    }
}
